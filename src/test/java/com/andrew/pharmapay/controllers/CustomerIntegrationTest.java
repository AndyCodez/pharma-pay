package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Customer;
import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.repositories.CustomerRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.andrew.pharmapay.models.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    public void createCustomer_withValidData_shouldReturnCreated() throws Exception {
        Customer customer = new Customer("John", "Doe");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
                        .header("Authorization", "Bearer " + getAuthToken()))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<Customer> allCustomers = customerRepository.findAll();
        assertEquals(1, allCustomers.size());
        assertEquals("John", allCustomers.get(0).getFirstName());
    }

    @Test
    public void createCustomer_withMissingFields_shouldReturnBadRequest() throws Exception {
        Customer customer = new Customer("", "");

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
                        .header("Authorization", "Bearer " + getAuthToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("First name is required"));
        assertTrue(responseBody.contains("Last name is required"));
    }

    private String getAuthToken() {
        UserDetails admin = new Pharmacist("John", "Doe", "johndoe@example.com", "pass123", ADMIN);
        // Set up security context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities())
        );
        return jwtUtil.generateToken(admin);
    }
}