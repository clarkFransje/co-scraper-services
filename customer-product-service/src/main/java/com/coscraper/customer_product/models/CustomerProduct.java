package com.coscraper.customer_product.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerProductId;
    private UUID customerId;
    private UUID productId;
}
