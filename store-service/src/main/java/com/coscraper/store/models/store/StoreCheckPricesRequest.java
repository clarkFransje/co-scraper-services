package com.coscraper.store.models.store;

import com.coscraper.store.models.product.Product;

import java.util.List;
import java.util.UUID;

public record StoreCheckPricesRequest (List<Product> products, UUID storeId) {
}
