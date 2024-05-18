package com.coscraper.customer_product.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerProduct {
    @Id
    @SequenceGenerator(
            name = "customer_product_id_sequence",
            sequenceName = "customer_product_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_product_id_sequence"
    )
    private Integer customerProductId;
    private Integer customerId;
    private Integer productId;
}
