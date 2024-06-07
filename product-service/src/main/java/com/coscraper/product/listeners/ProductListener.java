package com.coscraper.product.listeners;

import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductCreateMessage;
import com.coscraper.product.models.ProductDeleteMessage;
import com.coscraper.product.models.ProductUpdateMessage;
import com.coscraper.product.services.ProductService;
import com.coscraper.product.utils.EncryptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class ProductListener {

    private final ProductService productService;
    private final Logger log = Logger.getLogger(ProductListener.class.getName());
    private final ObjectMapper objectMapper;
    private final EncryptionUtils encryptionUtils;

    @Autowired
    public ProductListener(ProductService productService, EncryptionUtils encryptionUtils, ObjectMapper objectMapper) {
        this.productService = productService;
        this.encryptionUtils = encryptionUtils;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_product_create}")
    public void handleCreateMessage(Message message) {
        processMessage(message, ProductCreateMessage.class, productCreateMessage -> {
            log.info("Received message to create product " + productCreateMessage.name());
            productService.createProduct(productCreateMessage);
        });
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_product_update}")
    public void handleUpdateMessage(Message message) {
        processMessage(message, ProductUpdateMessage.class, productUpdateMessage -> {
            log.info("Received message to update product " + productUpdateMessage.id());
            Optional<Product> product = productService.getProductById(productUpdateMessage.id());
            if (product.isPresent()) {
                productService.updateProduct(product.get(), productUpdateMessage);
                log.info("Updated product " + productUpdateMessage.id());
            } else {
                log.warning("No product found with id " + productUpdateMessage.id());
            }
        });
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_product_delete}")
    public void handleDeleteMessage(Message message) {
        processMessage(message, ProductDeleteMessage.class, productDeleteMessage -> {
            log.info("Received message to delete product " + productDeleteMessage.productId());
            Optional<Product> product = productService.getProductById(productDeleteMessage.productId());
            if (product.isPresent()) {
                productService.deleteProductById(productDeleteMessage.productId());
                log.info("Deleted product " + productDeleteMessage.productId());
            } else {
                log.warning("No product found with id " + productDeleteMessage.productId());
            }
        });
    }

    private <T> void processMessage(Message message, Class<T> messageType, MessageHandler<T> handler) {
        try {
            // Extract the byte array payload
            byte[] body = message.getBody();
            // Convert the byte array to a string (encrypted message)
            String encryptedMessage = new String(body);
            // Decrypt the message payload
            String jsonMessage = encryptionUtils.decrypt(encryptedMessage);
            // Convert JSON payload to message object
            T msgObject = objectMapper.readValue(jsonMessage, messageType);
            handler.handle(msgObject);
        } catch (Exception e) {
            log.severe("Error decrypting or deserializing message: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface MessageHandler<T> {
        void handle(T message);
    }
}