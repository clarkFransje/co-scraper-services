package com.coscraper.customer.messaging;

import com.coscraper.customer.utils.EncryptionUtils;
import com.coscraper.customer.models.CustomerDeleteMessage;
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

    @Value("${spring.rabbitmq.customer_exchange}")
    private String customerExchange;

    public MessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, EncryptionUtils encryptionUtils) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.encryptionUtils = encryptionUtils;
    }

    public void sendCustomerDeletedMessage(CustomerDeleteMessage message) {
        try {
            Message rabbitMessage = convertJsonMessage(message);
            // Publish message to RabbitMQ exchange
            rabbitTemplate.send(customerExchange, "customer.deleted", rabbitMessage);
            log.info("Sent message to RabbitMQ: " + rabbitMessage);
        } catch (JsonProcessingException e) {
            // Handle JSON serialization error
            log.severe("Error serializing message: " + e.getMessage());
        } catch (Exception e) {
            // Handle encryption error
            log.severe("Error encrypting message: " + e.getMessage());
        }
    }

    public Message convertJsonMessage(Object message) throws Exception {
        // Convert message object to JSON string using the ObjectMapper
        String jsonMessage = objectMapper.writeValueAsString(message);

        // Encrypt the JSON message
        String encryptedMessage = encryptionUtils.encrypt(jsonMessage);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");

        // Create a RabbitMQ message with the encrypted JSON payload
        return MessageBuilder.withBody(encryptedMessage.getBytes()).andProperties(messageProperties).build();
    }
}