package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.models.SoldItem;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.BillRepository;
import com.andrew.pharmapay.repositories.StockItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class BillIntegrationTest {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        stockItemRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        billRepository.deleteAll();
    }

    @Test
    public void createBill_withValidData_shouldReturnCreated() throws Exception {
        StockItem paracetamolStock = new StockItem("Paracetamol", BigDecimal.valueOf(20), 5);
        stockItemRepository.save(paracetamolStock);

        SoldItem soldItem = new SoldItem("Paracetamol", 3);
        List<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(soldItem);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldItems)))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<Bill> allBills = billRepository.findAll();
        assertEquals(1, allBills.size());
        assertEquals(new BigDecimal(20 * 3).setScale(2), allBills.get(0).getAmount());
    }

    @Test
    public void createBill_withItemThatIsNotInStock_shouldReturnNotFound() throws Exception {
        SoldItem soldItem = new SoldItem("Paracetamol", 3);

        List<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(soldItem);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldItems)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Paracetamol is not in stock"));
    }

    @Test
    public void createBill_withItemQtyMoreThanAvailableQty_shouldReturnBadRequest() throws Exception {
        StockItem paracetamolStock = new StockItem("Paracetamol", BigDecimal.valueOf(20), 1);
        stockItemRepository.save(paracetamolStock);

        SoldItem soldItem = new SoldItem("Paracetamol", 3);

        List<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(soldItem);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldItems)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Only " + 1 + " " + "Paracetamol" + " left"));
    }

}