package com.coscraper.product.models;

import java.util.UUID;

public record ProductUpdateMessage(UUID id, Double price, Double oldPrice) {
}
