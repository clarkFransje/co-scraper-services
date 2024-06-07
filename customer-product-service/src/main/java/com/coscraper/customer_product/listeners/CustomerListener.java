package com.coscraper.customer_product.listeners;

import com.coscraper.customer_product.utils.EncryptionUtils;
import com.coscraper.customer_product.models.CustomerDeleteMessage;
import com.coscraper.customer_product.services.CustomerProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CustomerListener {

    @Autowired
    private CustomerProductService customerProductService;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EncryptionUtils encryptionUtils;

    @Autowired
    public CustomerListener(EncryptionUtils encryptionUtils) {
        this.encryptionUtils = encryptionUtils;
    }

    @RabbitListener(queues = "${spring.rabbitmq.customer_delete_queue}")
    public void handleMessage(String encryptedMessage) {
        try {
            // Decrypt the message payload
            String jsonMessage = encryptionUtils.decrypt(encryptedMessage);

            // Convert JSON payload to message object
            CustomerDeleteMessage customerDeleteMessage = objectMapper.readValue(jsonMessage, CustomerDeleteMessage.class);

            log.info("Received Message: " + customerDeleteMessage.toString());
            customerProductService.deleteCustomerById(customerDeleteMessage.customerId());
        } catch (Exception e) {
            log.severe("Error decrypting or deserializing message: " + e.getMessage());
        }
    }
}