package com.coscraper.store.models.store;

import java.util.Optional;

public record StoreAddRequest(String name, Optional<String> description, String baseUrl, Optional<String> imageUrl, String language) {
}
