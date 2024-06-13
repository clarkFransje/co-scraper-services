package com.coscraper.store.controllers;

import com.coscraper.store.models.product.Product;
import com.coscraper.store.models.store.*;
import com.coscraper.store.services.ShopService;
import com.coscraper.store.services.StoreService;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/v1")
public class StoreController {
    private final StoreService storeService;
    private final ShopService shopService;
    private final static Logger logger = Logger.getLogger(StoreController.class.getName());

    public StoreController(StoreService storeService, ShopService shopService) {
        this.storeService = storeService;
        this.shopService = shopService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStore(@PathVariable UUID id) {
        Optional<Store> store = storeService.findStoreById(id);

        return store.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Store>> getStores() {
        List<Store> stores = storeService.findAllStores();
        return ResponseEntity.ok().body(stores);
    }

    @PostMapping("/search")
    public ResponseEntity<Product> searchProductByQuery(@RequestBody StoreGetProductRequest storeGetProductRequest) throws ExecutionException, InterruptedException {
        Optional<Store> store = storeService.findStoreById(storeGetProductRequest.storeId());

        if (store.isPresent()) {
            Optional<Product> possibleProduct = shopService.findProductByQuery(storeGetProductRequest.query(), store.get());

            if (possibleProduct.isPresent()) {
                logger.info("Adding new product: " + possibleProduct.get().name());
                shopService.sendNewProductToService(possibleProduct.get());
                return ResponseEntity.ok().body(possibleProduct.get());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/check-prices")
    public void checkPrices(@RequestBody StoreCheckPricesRequest storeCheckPricesRequest) {

    }

    @PostMapping
    public void AddStore(@RequestBody StoreAddRequest storeAddRequest) {
        storeService.addNewStore(storeAddRequest);
        logger.info("New store added: " + storeAddRequest.name());
    }

    @PutMapping
    public ResponseEntity<Response> UpdateStore(@RequestBody StoreUpdateRequest storeUpdateRequest) {
        Optional<Store> store = storeService.findStoreById(storeUpdateRequest.storeId());

        if (store.isPresent()) {
            storeService.updateStore(store.get(), storeUpdateRequest);
            logger.info("Updated store with id: " + storeUpdateRequest.storeId());
            return ResponseEntity.ok().body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping()
    public ResponseEntity<Response> DeleteStore(@RequestBody StoreDeleteRequest storeDeleteRequest) {
        Optional<Store> store = storeService.findStoreById(storeDeleteRequest.storeId());

        if (store.isPresent()) {
            storeService.removeStoreById(storeDeleteRequest.storeId());
            logger.info("Removed store with id: " + storeDeleteRequest.storeId());
            return ResponseEntity.ok().body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
