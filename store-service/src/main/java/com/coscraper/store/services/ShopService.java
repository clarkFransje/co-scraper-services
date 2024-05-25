package com.coscraper.store.services;

import com.coscraper.store.enums.ProductScrapingStatus;
import com.coscraper.store.messaging.MessageSender;
import com.coscraper.store.models.product.ProductDeleteMessage;
import com.coscraper.store.models.product.ProductUpdateMessage;
import com.coscraper.store.models.shop.ShopResult;
import com.coscraper.store.models.product.Product;
import com.coscraper.store.shops.ArteAntwerp;
import com.coscraper.store.shops.Shop;
import com.coscraper.store.models.store.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

@Service
public class ShopService {
    private static final Map<String, Function<Store, Shop>> storeCreators = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ShopService.class);
    private final MessageSender messageSender;

    static {
        storeCreators.put("ArteAntwerp", store -> new ArteAntwerp(
                store.getId(), store.getName(), store.getBaseUrl()));
    }

    public ShopService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public static Shop createShopInstance(Store store) {
        Function<Store, Shop> creator = storeCreators.get(store.getName());

        return creator != null ? creator.apply(store) : null;
    }

    public Optional<Product> findProductByUrl(String url, Store store) throws ExecutionException, InterruptedException {
        log.info("Creating {} instance for given url: {}", store.getName(), url);
        Shop shop = createShopInstance(store);

        // Check if the shop has been created
        if (shop != null) {
            log.info("Successfully created {} instance, performing url search now...", store.getName());
            Future<Product> product = shop.scrapeProductFromUrl(url);
            return Optional.of(product.get());
        }

        return Optional.empty();
    }

    public Optional<Product> findProductByQuery(String query, Store store) throws ExecutionException, InterruptedException {
        log.info("Creating {} instance for given query: {}", store.getName(), query);
        Shop shop = createShopInstance(store);

        // Check if the shop has been created
        if (shop != null) {
            log.info("Successfully created {} instance, performing query search now...", store.getName());
            Future<ShopResult> scrapingResultFuture = shop.findProductByQuery(query);

            // Wait for the result to be available
            ShopResult scrapingResult = scrapingResultFuture.get();

            // Check if the scraping result indicates a found product
            if (scrapingResult.scrapingStatus() == ProductScrapingStatus.PRODUCT_FOUND) {
                Product product = scrapingResult.possibleProduct().get();
                return Optional.of(product);
            } else {
                log.info("Product not found or there was an error in scraping.");
                return Optional.empty();
            }
        } else {
            log.error("Failed to create {} instance for store", store.getName());
            return Optional.empty();
        }
    }

    public void sendNewProductToService(Product product) {
        messageSender.sendProductCreateMessage(product);
    }

    public void sendUpdateProductToService(ProductUpdateMessage updatedProduct) {
        messageSender.sendProductUpdateMessage(updatedProduct);
    }

    public void sendDeleteProductToService(ProductDeleteMessage deleteMessage) {
        messageSender.sendProductDeleteMessage(deleteMessage);
    }
}
