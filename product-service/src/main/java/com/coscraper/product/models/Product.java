package com.coscraper.product.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
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
    private String description;
    private String color;
    private String url;
    private Double price;
    private Double oldPrice;
    private String imageUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date created;

    @UpdateTimestamp
    private Date updated;
}
