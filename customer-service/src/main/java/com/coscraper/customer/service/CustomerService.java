package com.coscraper.customer.service;


import com.coscraper.customer.model.Customer;
import com.coscraper.customer.model.CustomerAddRequest;
import com.coscraper.customer.model.CustomerDeleteMessage;
import com.coscraper.customer.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public CustomerService(RabbitTemplate rabbitTemplate, @Value("${spring.rabbitmq.exchange}") String exchangeName, CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    public void registerCustomer(CustomerAddRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // Check if email has been used by user
        Customer existingCustomer = customerRepository.findByEmail(request.email());
        if (existingCustomer != null) {
            log.info("Customer with email {} already exists", existingCustomer.getEmail());
        }
        else {
            customerRepository.save(customer);
        }
    }

    public void deleteCustomerById(int id) {
        customerRepository.deleteById(id);
        log.info("Deleted user: {}", id);

        try {
            // Convert customer ID to JSON
            CustomerDeleteMessage message = new CustomerDeleteMessage(id);
            String jsonMessage = objectMapper.writeValueAsString(message);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");

            // Create a RabbitMQ message with the JSON payload
            Message rabbitMessage = MessageBuilder.withBody(jsonMessage.getBytes()).andProperties(messageProperties)
                    .build();

            // Publish message to RabbitMQ exchange
            rabbitTemplate.send(exchangeName, "customer.deleted", rabbitMessage);
        } catch (JsonProcessingException e) {
            // Handle JSON serialization error
            log.error(e.getMessage());
        }
    }

    public Optional<Customer> findCustomerById(Integer id) {
        return customerRepository.findById(id);
    }
}
