package com.coscraper.product.controllers;

import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductAddRequest;
import com.coscraper.product.models.ProductUpdateRequest;
import com.coscraper.product.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddProduct() throws Exception {
        ProductAddRequest request = new ProductAddRequest("Product Name", "Product Description", 100.0);

        mockMvc.perform(post("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = new Product(1, "Product Name", "Product Description", 100.0);
        Mockito.when(productService.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.description").value("Product Description"))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product(1, "Product Name", "Product Description", 100.0);
        ProductUpdateRequest request = new ProductUpdateRequest(1, 150.0);
        Mockito.when(productService.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(put("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/1"))
                .andExpect(status().isOk());
    }
}
