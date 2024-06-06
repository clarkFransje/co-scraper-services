package com.coscraper.product.models;

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
public class Product {
    @Id
    private UUID id;
    private UUID storeId;
    private String name;
    private String sku;
    private String url;
    private Double price;
    private Double oldPrice;
    private String imageUrl;
}
