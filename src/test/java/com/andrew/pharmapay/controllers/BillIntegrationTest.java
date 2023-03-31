package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.repositories.BillRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class BillIntegrationTest {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        billRepository.deleteAll();
    }

    @Test
    public void createBill_withValidData_shouldReturnCreated() throws Exception {
        Date billDate = new Date();

        Bill bill = new Bill(new BigDecimal(20), billDate, new HashSet<>());

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bill)))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<Bill> allBills = billRepository.findAll();
        assertEquals(1, allBills.size());
        assertEquals(billDate, allBills.get(0).getBillDateTime());
        assertEquals(new BigDecimal(20).setScale(2), allBills.get(0).getAmount());
    }

}