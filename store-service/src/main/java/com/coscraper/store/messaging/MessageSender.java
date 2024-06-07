package com.coscraper.store.messaging;

import com.coscraper.store.models.product.Product;
import com.coscraper.store.models.product.ProductDeleteMessage;
import com.coscraper.store.models.product.ProductUpdateMessage;
import com.coscraper.store.utils.EncryptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final EncryptionUtils encryptionUtils;

    @Value("${spring.rabbitmq.product_exchange}")
    private String productExchange;

    public MessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, EncryptionUtils encryptionUtils) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.encryptionUtils = encryptionUtils;
    }

    private void sendMessage(Object message, String routingKey, String exchangeName) {
        try {
            // Convert message to JSON
            String jsonMessage = objectMapper.writeValueAsString(message);

            // Encrypt the JSON payload
            String encryptedMessage = encryptionUtils.encrypt(jsonMessage);

            // Create RabbitMQ message with encrypted payload
            Message rabbitMessage = MessageBuilder
                    .withBody(encryptedMessage.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                    .build();

            // Publish message to RabbitMQ exchange
            rabbitTemplate.send(exchangeName, routingKey, rabbitMessage);
            log.info("Sent encrypted message to RabbitMQ: " + encryptedMessage);
        } catch (JsonProcessingException e) {
            log.severe("Error serializing message: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Error encrypting message: " + e.getMessage());
        }
    }

    public void sendProductDeleteMessage(ProductDeleteMessage message) {
        sendMessage(message, "product.deleted", productExchange);
    }

    public void sendProductCreateMessage(Product message) {
        sendMessage(message, "product.created", productExchange);
    }

    public void sendProductUpdateMessage(ProductUpdateMessage message) {
        sendMessage(message, "product.updated", productExchange);
    }
}