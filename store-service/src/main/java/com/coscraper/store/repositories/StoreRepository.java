package com.coscraper.store.repositories;


import com.coscraper.store.models.store.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    @Transactional
    void deleteById(UUID storeId);
    Optional<Store> findById(UUID storeId);
}