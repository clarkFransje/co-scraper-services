package com.coscraper.customer_product.controllers;

import com.coscraper.customer_product.models.CustomerProductAddRequest;
import com.coscraper.customer_product.models.CustomerProductDeleteRequest;
import com.coscraper.customer_product.services.CustomerProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerProductController.class)
public class CustomerProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerProductService customerProductService; // Mocked service

    @Test
    public void testAddProductToCustomer() throws Exception {
        CustomerProductAddRequest request = new CustomerProductAddRequest(UUID.fromString("0851d075-e483-477f-a0fc-c5f8779efb02"), UUID.fromString("297b5734-2c51-4f6f-9775-96ce3f5c9eab");

        mockMvc.perform(post("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveProductFromCustomer() throws Exception {
        CustomerProductDeleteRequest request = new CustomerProductDeleteRequest(UUID.fromString("0851d075-e483-477f-a0fc-c5f8779efb02"));

        mockMvc.perform(delete("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
