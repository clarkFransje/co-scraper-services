package com.coscraper.store.models.store;

import com.coscraper.store.models.product.Product;

import java.util.List;

public record StoreCheckPricesRequest (List<Product> productsToCheck) {
}
