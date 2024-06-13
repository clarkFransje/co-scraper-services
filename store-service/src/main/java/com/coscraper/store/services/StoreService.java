package com.coscraper.store.services;

import com.coscraper.store.models.product.Product;
import com.coscraper.store.models.product.ProductUpdateMessage;
import com.coscraper.store.models.store.Store;
import com.coscraper.store.models.store.StoreAddRequest;
import com.coscraper.store.models.store.StoreCheckPricesRequest;
import com.coscraper.store.models.store.StoreUpdateRequest;
import com.coscraper.store.repositories.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class StoreService {
    private static final Logger log = LoggerFactory.getLogger(StoreService.class);
    private StoreRepository storeRepository;
    private ShopService shopService;

    public StoreService (StoreRepository storeRepository, ShopService shopService) {
        this.storeRepository = storeRepository;
        this.shopService = shopService;
    }

    public Optional<Store> findStoreById(UUID storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        return store;
    }

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public void addNewStore(StoreAddRequest request) {
        Store store = Store.builder()
                .name(request.name())
                .baseUrl(request.baseUrl())
                .description(request.description().get())
                .imageUrl(request.imageUrl().get())
                .language(request.language())
                .age(0)
                .build();

        storeRepository.save(store);
    }

    public void removeStoreById(UUID storeId) {
        storeRepository.deleteById(storeId);

        // TODO: Delete products related to the store.
    }

    public void updateStore(Store store, StoreUpdateRequest storeUpdateRequest) {
        store.setBaseUrl(storeUpdateRequest.baseUrl());
        store.setImageUrl(storeUpdateRequest.imageUrl());

        storeRepository.save(store);
    }

    public void checkProductPrices(List<StoreCheckPricesRequest> stores) throws ExecutionException, InterruptedException {
        for (StoreCheckPricesRequest store : stores) {
            Optional<Store> possibleStore = findStoreById(store.storeId());

            if (possibleStore.isPresent()) {
                for (Product product : store.products()) {
                    Optional<Product> scrapedProduct = shopService.findProductByUrl(product.url(), possibleStore.get());
                    if (scrapedProduct.isPresent()) {
                        Product currentProduct = scrapedProduct.get();
                        // Check if product has changed in price
                        if (!currentProduct.price().equals(product.price())) {
                            // Send update if the price has changed
                            log.info("Product {} is in sale from {} to {}", currentProduct.name(), product.price(), scrapedProduct.get());
                            ProductUpdateMessage updateMessage = new ProductUpdateMessage(
                                    product.id(),
                                    currentProduct.price(),
                                    product.price()
                            );

                            // Send about updated product
                            shopService.sendUpdateProductToService(updateMessage);
                        } else {
                            log.info("No changes for product {}", currentProduct.name());
                        }
                    }
                }
            }
        }
    }
}
