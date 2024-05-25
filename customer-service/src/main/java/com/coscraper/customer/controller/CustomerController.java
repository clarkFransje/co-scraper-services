package com.coscraper.customer.controller;

import com.coscraper.customer.models.Customer;
import com.coscraper.customer.models.CustomerAddRequest;
import com.coscraper.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1")
public record CustomerController(CustomerService customerService) {

    @PostMapping
    public ResponseEntity<Customer> RegisterCustomer(@RequestBody CustomerAddRequest customerAddRequest) {
        Optional<Customer> customer = customerService.registerCustomer(customerAddRequest);

        return customer.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") UUID id) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{id}")
    public void DeleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomerById(id);
    }
}
