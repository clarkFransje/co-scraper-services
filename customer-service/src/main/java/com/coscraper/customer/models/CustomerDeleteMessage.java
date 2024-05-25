package com.coscraper.customer.models;

import java.util.UUID;

public record CustomerDeleteMessage(UUID customerId) {
}
