package com.coscraper.product.repositories;

import com.coscraper.product.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Transactional
    void deleteById(UUID productId);
    Optional<Product> findById(UUID productId);
    List<Product> findAllById(Iterable<UUID> ids);
}
