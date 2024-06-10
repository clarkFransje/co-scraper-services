package com.coscraper.product.controllers;

import com.coscraper.product.models.Product;
import com.coscraper.product.models.ProductUpdateMessage;
import com.coscraper.product.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    public void testGetProduct() throws Exception {
        Product product = Product.builder()
                .id(UUID.fromString("15b4a4de-51ce-4428-acaa-cb65a5083256"))
                .storeId(UUID.fromString("154ebc21-8538-47ea-a408-efab8c26fc90"))
                .name("Product Name")
                .description("Product Description")  // Add description
                .color("Red")                        // Add color
                .url("url")
                .price(100.00)
                .oldPrice(139.99)
                .imageUrl("imageUrl")
                .build();

        Mockito.when(productService.getProductById(UUID.fromString("15b4a4de-51ce-4428-acaa-cb65a5083256"))).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/15b4a4de-51ce-4428-acaa-cb65a5083256"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("15b4a4de-51ce-4428-acaa-cb65a5083256"))
                .andExpect(jsonPath("$.storeId").value("154ebc21-8538-47ea-a408-efab8c26fc90"))
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.description").value("Product Description"))  // Expect description
                .andExpect(jsonPath("$.color").value("Red"))                        // Expect color
                .andExpect(jsonPath("$.url").value("url"))
                .andExpect(jsonPath("$.price").value(100.00))
                .andExpect(jsonPath("$.oldPrice").value(139.99))
                .andExpect(jsonPath("$.imageUrl").value("imageUrl"));
    }
}
