package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.StockItemRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

import java.math.BigDecimal;
import java.util.List;

import static com.andrew.pharmapay.models.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class StockItemIntegrationTest {

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void createItem_withValidData_shouldReturnCreated() throws Exception {
        StockItem stockItem = new StockItem("Paracetamol", new BigDecimal(20), 30);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockItem))
                        .header("Authorization", "Bearer " + getAuthToken()))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<StockItem> allStockItems = stockItemRepository.findAll();
        assertEquals(1, allStockItems.size());
        assertEquals("Paracetamol", allStockItems.get(0).getName());
        assertEquals(BigDecimal.valueOf(20), allStockItems.get(0).getPrice());
        assertEquals(30, allStockItems.get(0).getQuantity());
    }

    @Test
    public void createItem_withMissingNameField_shouldReturnBadRequest() throws Exception {
        StockItem stockItem = new StockItem("", new BigDecimal(20), 10);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockItem))
                        .header("Authorization", "Bearer " + getAuthToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Item name is required"));
    }

    @Test
    public void createItem_withMissingPriceField_shouldReturnBadRequest() throws Exception {
        StockItem stockItem = new StockItem("Paracetamol", null, 10);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockItem))
                        .header("Authorization", "Bearer " + getAuthToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Price is required"));
    }

    private String getAuthToken() {
        UserDetails admin = new Pharmacist("Admin", "Doe", "admindoe@example.com", "pass123", ADMIN);
        // Set up security context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities())
        );
        return jwtUtil.generateToken(admin);
    }
}