package com.coscraper.customer_product.controllers;

import com.coscraper.customer_product.models.CustomerProductAddRequest;
import com.coscraper.customer_product.models.CustomerProductDeleteRequest;
import com.coscraper.customer_product.models.CustomerProductsGetRequest;
import com.coscraper.customer_product.services.CustomerProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/v1")
public class CustomerProductController {
    private final CustomerProductService customerProductService;

    public CustomerProductController(CustomerProductService customerProductService) {
        this.customerProductService = customerProductService;
    }

    @PostMapping
    public void AddProductToCustomer(@RequestBody CustomerProductAddRequest customerProductAddRequest) {
        customerProductService.addProductToCustomer(customerProductAddRequest);
        log.info("New product added to customer {}", customerProductAddRequest);
    }

    @DeleteMapping
    public void RemoveProductFromCustomer(@RequestBody CustomerProductDeleteRequest customerProductDeleteRequest) {
        customerProductService.deleteProductFromCustomer(customerProductDeleteRequest);
        log.info("Removing product from customer {}", customerProductDeleteRequest);
    }

    @GetMapping("/{id}")
    public List<CustomerProductsGetRequest> getAllCustomerProducts(@PathVariable("id") UUID id) {
        return customerProductService.getProductsById(id);
    }
}
