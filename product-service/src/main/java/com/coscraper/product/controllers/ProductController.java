package com.coscraper.product.controllers;

import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductUpdateMessage;
import com.coscraper.product.services.ProductService;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1")
public record ProductController(ProductService productService) {

    @GetMapping("/{id}")
    public ResponseEntity<Product> GetProduct(@PathVariable UUID id) {
        Optional<Product> product = productService.getProductById(id);

        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> GetAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
