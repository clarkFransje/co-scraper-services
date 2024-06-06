package com.coscraper.product.services;


import com.coscraper.product.messaging.MessageSender;
import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductCreateMessage;
import com.coscraper.product.models.ProductDeleteMessage;
import com.coscraper.product.models.ProductUpdateMessage;
import com.coscraper.product.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final MessageSender messageSender;

    public ProductService(ProductRepository productRepository, MessageSender messageSender) {
        this.productRepository = productRepository;
        this.messageSender = messageSender;
    }

    public void createProduct(ProductCreateMessage request) {
        Product product = Product.builder()
                .id(request.id())
                .name(request.name())
                .storeId(request.storeId())
                .sku(request.sku())
                .url(request.url())
                .price(request.price())
                .oldPrice(request.oldPrice())
                .imageUrl(request.imageUrl())
                .build();

        productRepository.save(product);
        log.info("Created product: {}, {}", product.getName(), product.getId());
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductsById(List<UUID> ids) {
        return productRepository.findAllById(ids);
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
        log.info("Deleted user: {}", id);

        ProductDeleteMessage productDeleteMessage = new ProductDeleteMessage(id);
        messageSender.sendProductDeleteMessage(productDeleteMessage);
    }

    public void updateProduct(Product product, ProductUpdateMessage updatedProduct) {
        product.setPrice(updatedProduct.price());
        product.setOldPrice(updatedProduct.price());
        productRepository.save(product);

        log.info("Updated product: {}", product);
    }
}
