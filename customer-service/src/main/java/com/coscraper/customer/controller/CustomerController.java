package com.coscraper.customer.controller;

import com.coscraper.customer.model.Customer;
import com.coscraper.customer.model.CustomerAddRequest;
import com.coscraper.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1")
public record CustomerController(CustomerService customerService) {

    @PostMapping
    public void RegisterCustomer(@RequestBody CustomerAddRequest customerAddRequest) {
        log.info("New customer added {}", customerAddRequest);
        customerService.registerCustomer(customerAddRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Integer id) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{id}")
    public void DeleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
    }
}
