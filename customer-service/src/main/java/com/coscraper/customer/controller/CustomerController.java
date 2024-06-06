package com.coscraper.customer.controller;

import com.coscraper.customer.models.Customer;
import com.coscraper.customer.models.CustomerAddRequest;
import com.coscraper.customer.models.CustomerGetRequest;
import com.coscraper.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/v1")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerAddRequest customerAddRequest) {
        Optional<Customer> customer = customerService.registerCustomer(customerAddRequest);

        return customer.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PreAuthorize("hasAuthority('read:stores')")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") UUID id) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PreAuthorize("hasAuthority('read:stores')")
    @PostMapping
    public ResponseEntity<Customer> getCustomerByEmail(@RequestBody CustomerGetRequest customerGetRequest) {
        Optional<Customer> customer = customerService.findCustomerByEmail(customerGetRequest.email());
        return customer.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomerById(id);
    }
}