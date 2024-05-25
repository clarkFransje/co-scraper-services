package com.coscraper.customer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.customer_queue}")
    private String queueName;

    @Value("${spring.rabbitmq.customer_exchange}")
    private String exchangeName;

    // Define the exchange for customer events
    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(exchangeName);
    }

    // Define a durable queue for customer events
    @Bean
    public Queue customerQueue() {
        return new Queue(queueName, true);
    }

    // Bind the customer queue to the exchange
    @Bean
    public Binding binding(Queue customerQueue, TopicExchange customerExchange) {
        return BindingBuilder.bind(customerQueue).to(customerExchange).with("customer.deleted");
    }
}
