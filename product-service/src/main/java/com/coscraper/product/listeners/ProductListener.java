package com.coscraper.product.listeners;

import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductCreateMessage;
import com.coscraper.product.models.ProductDeleteMessage;
import com.coscraper.product.models.ProductUpdateMessage;
import com.coscraper.product.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class ProductListener {

    @Autowired
    private ProductService productService;
    private final Logger log = Logger.getLogger(ProductListener.class.getName());

    @RabbitListener(queues = "${spring.rabbitmq.queue_product_create}")
    public void handleCreateMessage(ProductCreateMessage productCreateMessage) {
        log.info("Received message to create product " + productCreateMessage.name());

        productService.createProduct(productCreateMessage);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_product_update}")
    public void handleUpdateMessage(ProductUpdateMessage productUpdateMessage) {
        log.info("Received message to update product " + productUpdateMessage.id());
        Optional<Product> product = productService.getProductById(productUpdateMessage.id());

        if (product.isPresent()) {
            productService.updateProduct(product.get(), productUpdateMessage);
            log.info("Updated product " + productUpdateMessage.id());
        } else {
            log.warning("No product found with id " + productUpdateMessage.id());
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_product_delete}")
    public void handleDeleteMessage(ProductDeleteMessage productDeleteMessage) {
        log.info("Received message to delete product " + productDeleteMessage.productId());
        Optional<Product> product = productService.getProductById(productDeleteMessage.productId());

        if (product.isPresent()) {
            productService.deleteProductById(productDeleteMessage.productId());
            log.info("Deleted product " + productDeleteMessage.productId());
        } else {
            log.warning("No product found with id " + productDeleteMessage.productId());
        }
    }
}