package com.coscraper.product.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue_product_update}")
    private String productUpdateQueue;

    @Value("${spring.rabbitmq.queue_product_create}")
    private String productCreateQueue;

    @Value("${spring.rabbitmq.queue_product_delete}")
    private String productDeleteQueue;

    @Value("${spring.rabbitmq.product_exchange}")
    private String productExchange;

    // Define the exchange for product events
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(productExchange);
    }

    // Define a durable queue for product creation events
    @Bean
    public Queue productCreateQueue() {
        return new Queue(productCreateQueue, true);
    }

    // Define a durable queue for product update events
    @Bean
    public Queue productUpdateQueue() {
        return new Queue(productUpdateQueue, true);
    }

    // Define a durable queue for product deletion events
    @Bean
    public Queue productDeleteQueue() {
        return new Queue(productDeleteQueue, true);
    }

    // Bind the product creation queue to the exchange
    @Bean
    public Binding bindingProductCreateQueue(@Qualifier("productCreateQueue") Queue productCreateQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productCreateQueue).to(productExchange).with("product.created");
    }

    // Bind the product update queue to the exchange
    @Bean
    public Binding bindingProductUpdateQueue(@Qualifier("productUpdateQueue") Queue productUpdateQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productUpdateQueue).to(productExchange).with("product.updated");
    }

    // Bind the product deletion queue to the exchange
    @Bean
    public Binding bindingProductDeleteQueue(@Qualifier("productDeleteQueue") Queue productDeleteQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productDeleteQueue).to(productExchange).with("product.deleted");
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
