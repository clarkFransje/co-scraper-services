package com.coscrapercode.customer_product.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.product_queue}")
    private String productQueueName;

    @Value("${spring.rabbitmq.customer_queue}")
    private String customerQueueName;

    @Bean
    public Queue productQueue() {
        return new Queue(productQueueName);
    }

    @Bean
    public Queue customerQueue() {
        return new Queue(customerQueueName);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
