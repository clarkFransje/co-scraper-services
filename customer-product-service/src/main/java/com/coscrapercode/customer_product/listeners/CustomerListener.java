package com.coscrapercode.customer_product.listeners;

import com.coscrapercode.customer_product.models.CustomerDeleteMessage;
import com.coscrapercode.customer_product.services.CustomerProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CustomerListener {

    @Autowired
    private CustomerProductService customerProductService;
    private final Logger log = Logger.getLogger(CustomerListener.class.getName());

    @RabbitListener(queues = "${spring.rabbitmq.customer_queue}")
    public void handleMessage(CustomerDeleteMessage customerDeleteMessage) {
        log.info("Received Message: " + customerDeleteMessage.toString());
        customerProductService.deleteCustomerById(customerDeleteMessage.customerId());
    }
}
