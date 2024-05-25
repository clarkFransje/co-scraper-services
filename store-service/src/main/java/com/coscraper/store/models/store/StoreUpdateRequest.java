package com.coscraper.store.models.store;

import java.util.UUID;

public record StoreUpdateRequest (UUID storeId, String baseUrl, String imageUrl) {}
