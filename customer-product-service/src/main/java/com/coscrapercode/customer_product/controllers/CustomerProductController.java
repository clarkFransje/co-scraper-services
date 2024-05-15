package com.coscrapercode.customer_product.controllers;

import com.coscrapercode.customer_product.models.CustomerProductAddRequest;
import com.coscrapercode.customer_product.models.CustomerProductDeleteRequest;
import com.coscrapercode.customer_product.services.CustomerProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/customer-product")
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
}
