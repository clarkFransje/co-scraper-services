package com.coscraper.store.models.shop;


import com.coscraper.store.enums.ProductScrapingStatus;
import com.coscraper.store.models.product.Product;

import java.util.Optional;

// Define the ShopResult class
public record ShopResult(Optional<Product> possibleProduct, ProductScrapingStatus scrapingStatus) { }
