package com.coscraper.customer.controller;

import com.coscraper.customer.model.CustomerAddRequest;
import com.coscraper.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
public record CustomerController(CustomerService customerService) {

    @PostMapping
    public void RegisterCustomer(@RequestBody CustomerAddRequest customerAddRequest) {
        log.info("New customer added {}", customerAddRequest);
        customerService.registerCustomer(customerAddRequest);
    }

    @DeleteMapping("/{id}")
    public void DeleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
    }
}
