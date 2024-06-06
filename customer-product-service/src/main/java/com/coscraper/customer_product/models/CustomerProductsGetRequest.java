package com.coscraper.customer_product.models;

import java.util.UUID;

public record CustomerProductsGetRequest (UUID customerProductId, UUID productId) {
}
