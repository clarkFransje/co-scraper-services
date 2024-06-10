package com.coscraper.product.models;


import java.util.UUID;

public record ProductCreateMessage(UUID id, UUID storeId, String name, String description, String color, String url, Double price, Double oldPrice, String imageUrl) {
}
