package com.coscraper.store.shops;

import com.coscraper.store.models.shop.ShopResult;
import com.coscraper.store.models.product.Product;

import java.util.concurrent.Future;

public interface Shop {
    Future<ShopResult> findProductByQuery(String query);
    Future<Product> scrapeProductFromUrl(String url);
}




