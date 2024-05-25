package com.coscraper.customer.repository;

import com.coscraper.customer.models.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Transactional
    void deleteById(UUID customerId);
    Optional<Customer> findById(UUID customerId);
    Customer findByEmail(String email);
}
