package com.coscraper.store.models.store;

import java.util.UUID;

public record StoreGetProductRequest(UUID storeId, String query) {
}
