package com.coscraper.product.models;

import java.util.List;
import java.util.UUID;

public record ProductScopeGetRequest (List<UUID> productIds) { }
