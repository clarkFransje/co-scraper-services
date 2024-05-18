package com.coscraper.customer_product.listeners;

import com.coscraper.customer_product.models.ProductDeleteMessage;
import com.coscraper.customer_product.services.CustomerProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ProductListener {

    @Autowired
    private CustomerProductService customerProductService;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @RabbitListener(queues = "${spring.rabbitmq.product_queue}")
    public void handleMessage(ProductDeleteMessage productDeleteMessage) {
        log.info("Received Message: " + productDeleteMessage.toString());
        customerProductService.deleteProductById(productDeleteMessage.productId());
    }

}
