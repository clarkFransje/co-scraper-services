package com.coscraper.store.models.product;

import java.util.UUID;

public record Product (UUID id, UUID storeId, String name, String description, String color, String url, Double price, Double oldPrice, String imageUrl) {}

