package com.coscraper.customer_product.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.product_delete_queue}")
    private String productDeleteQueue;

    @Value("${spring.rabbitmq.customer_delete_queue}")
    private String customerDeleteQueue;

    @Bean
    public Queue productQueue() {
        return new Queue(productDeleteQueue);
    }

    @Bean
    public Queue customerQueue() {
        return new Queue(customerDeleteQueue);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
