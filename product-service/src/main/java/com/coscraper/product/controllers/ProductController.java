package com.coscraper.product.controllers;

import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductAddRequest;
import com.coscraper.product.models.ProductUpdateRequest;
import com.coscraper.product.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
public record ProductController(ProductService productService) {

    @PostMapping
    public void AddProduct(@RequestBody ProductAddRequest productAddRequest) {
        productService.createProduct(productAddRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> GetProduct(@PathVariable Integer id) {
        Optional<Product> product = productService.getProductById(id);

        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping
    public ResponseEntity<Response> UpdateProduct(@RequestBody ProductUpdateRequest productUpdateRequest) {
        Optional<Product> product = productService.getProductById(productUpdateRequest.id());

        if (product.isPresent()) {
            productService.updateProduct(product.get(), productUpdateRequest);
            return ResponseEntity.ok().body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void DeleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
    }
}
