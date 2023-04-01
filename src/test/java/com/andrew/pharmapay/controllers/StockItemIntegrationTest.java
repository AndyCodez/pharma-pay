package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.StockItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class StockItemIntegrationTest {

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        stockItemRepository.deleteAll();
    }

    @Test
    public void createItem_withValidData_shouldReturnCreated() throws Exception {
        StockItem stockItem = new StockItem("Paracetamol", new BigDecimal(20), 30);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockItem)))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<StockItem> allStockItems = stockItemRepository.findAll();
        assertEquals(1, allStockItems.size());
        assertEquals("Paracetamol", allStockItems.get(0).getName());
        assertEquals(new BigDecimal(20).setScale(2), allStockItems.get(0).getPrice());
        assertEquals(30, allStockItems.get(0).getQuantity());
    }

    @Test
    public void createItem_withMissingNameField_shouldReturnBadRequest() throws Exception {
        StockItem stockItem = new StockItem("", new BigDecimal(20), 10);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockItem)))
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
                        .content(objectMapper.writeValueAsString(stockItem)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Price is required"));
    }

    @Test
    public void createItem_withPriceOrQuantityOfZero_shouldReturnBadRequest() throws Exception {
        StockItem stockItem = new StockItem("Paracetamol", BigDecimal.ZERO, 0);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockItem)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Price must be greater than or equal to 1"));
        assertTrue(responseBody.contains("Quantity must be greater than or equal to 1"));
    }
}