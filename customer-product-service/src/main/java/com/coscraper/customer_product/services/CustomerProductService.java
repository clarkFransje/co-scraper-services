package com.coscraper.customer_product.services;

import com.coscraper.customer_product.models.CustomerProduct;
import com.coscraper.customer_product.models.CustomerProductAddRequest;
import com.coscraper.customer_product.models.CustomerProductDeleteRequest;
import com.coscraper.customer_product.repositories.CustomerProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record CustomerProductService(CustomerProductRepository customerProductRepository) {
    public void addProductToCustomer(CustomerProductAddRequest request) {
        CustomerProduct customerProduct = CustomerProduct.builder()
                .customerId(request.customerId())
                .productId(request.productId())
                .build();

        customerProductRepository.save(customerProduct);
    }

    public void deleteProductFromCustomer(CustomerProductDeleteRequest customerProductDeleteRequest) {
        customerProductRepository.deleteById(customerProductDeleteRequest.customerProductId());
    }

    public void deleteProductById(UUID productId) {
        try {
            customerProductRepository.deleteByProductId(productId);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Failed to delete product with ID: " + productId, e);
        }
    }

    public void deleteCustomerById(UUID customerId) {
        try {
            customerProductRepository.deleteByCustomerId(customerId);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Failed to delete customer with ID: " + customerId, e);
        }
    }
}
