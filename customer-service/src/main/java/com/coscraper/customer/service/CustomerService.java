package com.coscraper.customer.service;


import com.coscraper.customer.messaging.MessageSender;
import com.coscraper.customer.models.Customer;
import com.coscraper.customer.models.CustomerAddRequest;
import com.coscraper.customer.models.CustomerDeleteMessage;
import com.coscraper.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final MessageSender messageSender;

    public CustomerService(CustomerRepository customerRepository, MessageSender messageSender) {
        this.customerRepository = customerRepository;
        this.messageSender = messageSender;
    }

    public Optional<Customer> registerCustomer(CustomerAddRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // Check if email has been used by user
        Customer existingCustomer = customerRepository.findByEmail(request.email());
        if (existingCustomer != null) {
            log.info("Customer with email {} already exists", existingCustomer.getEmail());
            return Optional.empty();
        }
        else {
            customerRepository.save(customer);
            log.info("New customer added {}", customer.getId());
            return Optional.of(customer);
        }
    }

    public void deleteCustomerById(UUID id) {
        customerRepository.deleteById(id);
        log.info("Deleted user: {}", id);

        CustomerDeleteMessage customerDeleteMessage = new CustomerDeleteMessage(id);
        messageSender.sendCustomerDeletedMessage(customerDeleteMessage);
    }

    public Optional<Customer> findCustomerById(UUID id) {
        return customerRepository.findById(id);
    }
}
