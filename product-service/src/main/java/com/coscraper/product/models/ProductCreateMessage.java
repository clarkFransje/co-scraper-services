package com.coscraper.product.models;


import java.util.UUID;

public record ProductCreateMessage(UUID id, UUID storeId, String name, String sku, String url, Double price, Double oldPrice, String imageUrl) {
}
