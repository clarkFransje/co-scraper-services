package com.coscraper.customer.service;
import com.coscraper.customer.messaging.MessageSender;
import com.coscraper.customer.models.Customer;
import com.coscraper.customer.models.CustomerAddRequest;
import com.coscraper.customer.models.CustomerDeleteMessage;
import com.coscraper.customer.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final MessageSender messageSender;
    private final RestTemplate restTemplate;

    @Value("${okta.oauth2.issuer}")
    private String auth0Domain;

    @Value("${jwt.token}")
    private String jwtToken;

    public CustomerService(CustomerRepository customerRepository, MessageSender messageSender, RestTemplate restTemplate) {
        this.customerRepository = customerRepository;
        this.messageSender = messageSender;
        this.restTemplate = restTemplate;
    }

    public Optional<Customer> registerCustomer(CustomerAddRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // Check if email has been used by user
        Optional<Customer> existingCustomer = findCustomerByEmail(request.email());
        if (existingCustomer.isPresent()) {
            log.info("Customer with email {} already exists", existingCustomer.get().getEmail());
            return Optional.empty();
        } else {
            customerRepository.save(customer);
            log.info("New customer added {}", customer.getId());
            return Optional.of(customer);
        }
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        Customer existingCustomer = customerRepository.findByEmail(email);
        if (existingCustomer != null) {
            return Optional.of(existingCustomer);
        }

        return Optional.empty();
    }

    public void deleteCustomerById(UUID id) {
        Optional<Customer> possibleCustomer = findCustomerById(id);
        if (possibleCustomer.isPresent()) {
            customerRepository.deleteById(id);
            log.info("Deleted user: {}", id);

            CustomerDeleteMessage customerDeleteMessage = new CustomerDeleteMessage(id);
            messageSender.sendCustomerDeletedMessage(customerDeleteMessage);

            // Delete Customer information from AUTH0 GDPR
            deleteCustomerFromAuth0(possibleCustomer.get().getEmail(), jwtToken);
        }
    }

    private void deleteCustomerFromAuth0(String email, String accessToken) {
        String url = auth0Domain + "/api/v2/users-by-email?email=" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);

        if (response.getBody() != null && response.getBody().length > 0) {
            String userId = (String) response.getBody()[0].get("user_id");
            if (userId != null) {
                String deleteUrl = auth0Domain + "/api/v2/users/" + userId;
                restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, Void.class);
                log.info("Deleted user from Auth0: {}", userId);
            }
        }
    }

    public Optional<Customer> findCustomerById(UUID id) {
        return customerRepository.findById(id);
    }
}