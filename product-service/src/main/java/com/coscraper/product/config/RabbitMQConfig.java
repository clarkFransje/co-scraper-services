package com.coscraper.product.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;


    // Define the exchange for product events
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(exchangeName);
    }

    // Define a durable queue for product events
    @Bean
    public Queue ProductQueue() {
        return new Queue(queueName, true);
    }

    // Bind the product queue to the exchange
    @Bean
    public Binding binding(Queue productQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productQueue).to(productExchange).with("product.deleted");
    }
}
