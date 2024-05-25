package com.coscraper.customer.controllers;

import com.coscraper.customer.controller.CustomerController;
import com.coscraper.customer.models.CustomerAddRequest;
import com.coscraper.customer.service.CustomerService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testRegisterCustomer() throws Exception {
        CustomerAddRequest request = new CustomerAddRequest("John", "Doe", "john.doe@example.com");

        mockMvc.perform(post("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Mocking the behavior of registerCustomer method
        doNothing().when(customerService).registerCustomer(any(CustomerAddRequest.class));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/e24e03df-c66e-4ddd-b047-943c63477f5f"))
                .andExpect(status().isOk());

        // Mocking the behavior of deleteCustomerById method
        doNothing().when(customerService).deleteCustomerById(UUID.fromString("e24e03df-c66e-4ddd-b047-943c63477f5f"));
    }
}
