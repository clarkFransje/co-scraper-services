package com.coscraper.customer_product.repositories;

import com.coscraper.customer_product.models.CustomerProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, UUID> {
    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerProduct cp WHERE cp.productId = :productId")
    void deleteByProductId(UUID productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerProduct cp WHERE cp.customerId = :customerId")
    void deleteByCustomerId(UUID customerId);
}

