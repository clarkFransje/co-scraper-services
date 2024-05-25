package com.coscraper.customer_product.models;

import java.util.UUID;

public record CustomerProductAddRequest (UUID customerId, UUID productId) {
}
