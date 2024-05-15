package com.coscrapercode.customer_product.repositories;

import com.coscrapercode.customer_product.models.CustomerProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerProduct cp WHERE cp.productId = :productId")
    void deleteByProductId(int productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerProduct cp WHERE cp.customerId = :customerId")
    void deleteByCustomerId(int customerId);
}

