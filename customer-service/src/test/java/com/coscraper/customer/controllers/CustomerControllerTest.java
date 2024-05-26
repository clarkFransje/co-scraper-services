package com.coscraper.customer.controllers;

import com.coscraper.customer.controller.CustomerController;
import com.coscraper.customer.models.Customer;
import com.coscraper.customer.models.CustomerAddRequest;
import com.coscraper.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
        // Create a CustomerAddRequest object for testing
        CustomerAddRequest request = new CustomerAddRequest("John", "Doe", "john.doe@example.com");

        // Mocking the behavior of registerCustomer method to return Optional of Customer
        when(customerService.registerCustomer(any(CustomerAddRequest.class)))
                .thenReturn(Optional.of(new Customer()));

        // Perform a POST request to the specified endpoint with the request content
        mockMvc.perform(post("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Verify that the status code is 200 (OK)
                .andExpect(status().isOk());

        // Verify that the registerCustomer method is invoked with the correct argument
        verify(customerService, times(1)).registerCustomer(eq(request));
    }



    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/e24e03df-c66e-4ddd-b047-943c63477f5f"))
                .andExpect(status().isOk());

        // Mocking the behavior of deleteCustomerById method
        doNothing().when(customerService).deleteCustomerById(UUID.fromString("e24e03df-c66e-4ddd-b047-943c63477f5f"));
    }
}
