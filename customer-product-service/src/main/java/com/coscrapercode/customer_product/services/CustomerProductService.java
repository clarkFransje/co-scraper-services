package com.coscrapercode.customer_product.services;

import com.coscrapercode.customer_product.models.CustomerProduct;
import com.coscrapercode.customer_product.models.CustomerProductAddRequest;
import com.coscrapercode.customer_product.models.CustomerProductDeleteRequest;
import com.coscrapercode.customer_product.repositories.CustomerProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

    public void deleteProductById(int productId) {
        try {
            customerProductRepository.deleteByProductId(productId);
        } catch (DataIntegrityViolationException e) {
            // Log the exception or handle it as needed
            // For example, you can rethrow it or perform alternative actions
            // depending on your application's requirements
            throw new RuntimeException("Failed to delete product with ID: " + productId, e);
        }
    }

    public void deleteCustomerById(int customerId) {
        try {
            customerProductRepository.deleteByCustomerId(customerId);
        } catch (DataIntegrityViolationException e) {
            // Log the exception or handle it as needed
            // For example, you can rethrow it or perform alternative actions
            // depending on your application's requirements
            throw new RuntimeException("Failed to delete customer with ID: " + customerId, e);
        }
    }
}
