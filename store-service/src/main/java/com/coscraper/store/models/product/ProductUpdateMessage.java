package com.coscraper.store.models.product;

import java.util.UUID;

public record ProductUpdateMessage(UUID id, Double price, Double oldPrice) {
}
