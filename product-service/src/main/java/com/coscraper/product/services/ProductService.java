package com.coscraper.product.services;


import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductAddRequest;
import com.coscraper.product.models.ProductDeleteMessage;
import com.coscraper.product.models.ProductUpdateRequest;
import com.coscraper.product.repositories.ProductRepository;
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

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductService(RabbitTemplate rabbitTemplate, @Value("${spring.rabbitmq.exchange}") String exchangeName, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public void createProduct(ProductAddRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .description(request.description())
                .build();

        productRepository.save(product);

        log.info("Created product: {}", product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
        log.info("Deleted product: {}", id);

        try {
            // Convert product ID to JSON
            ProductDeleteMessage message = new ProductDeleteMessage(id);
            String jsonMessage = objectMapper.writeValueAsString(message);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");

            // Create a RabbitMQ message with the JSON payload
            Message rabbitMessage = MessageBuilder.withBody(jsonMessage.getBytes()).andProperties(messageProperties)
                    .build();

            // Publish message to RabbitMQ exchange
            rabbitTemplate.send(exchangeName, "product.deleted", rabbitMessage);
        } catch (JsonProcessingException e) {
            // Handle JSON serialization error
            log.error(e.getMessage());
        }
    }

    public void updateProduct(Product product, ProductUpdateRequest request) {
        product.setPrice(request.price());
        productRepository.save(product);

        log.info("Updated product: {}", product);
    }
}
