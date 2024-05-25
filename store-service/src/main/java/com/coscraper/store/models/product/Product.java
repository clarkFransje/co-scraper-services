package com.coscraper.store.models.product;

import java.util.UUID;

public record Product (UUID id, UUID storeId, String name, String sku, String url, Double price, Double oldPrice, String imageUrl) {}

