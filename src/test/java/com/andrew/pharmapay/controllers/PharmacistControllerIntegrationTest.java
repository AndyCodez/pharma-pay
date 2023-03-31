package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PharmacistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @AfterEach
    public void tearDown() {
        pharmacistRepository.deleteAll();
    }

    @Test
    public void createPharmacist_withValidData_shouldReturnCreated() throws Exception {
        Pharmacist pharmacist = new Pharmacist("John", "Doe", "johndoe@example.com");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pharmacist)))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<Pharmacist> allPharmacists = pharmacistRepository.findAll();
        assertEquals(1, allPharmacists.size());
        assertEquals("johndoe@example.com", allPharmacists.get(0).getEmail());
    }

    @Test
    public void createPharmacist_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        Pharmacist pharmacist = new Pharmacist("John", "Doe", "invalidemail@");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pharmacist)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createPharmacist_withDuplicateEmail_shouldReturnBadRequest() throws Exception {

        Pharmacist pharmacist1 = new Pharmacist("John", "Doe", "johndoe@example.com");
        pharmacistRepository.save(pharmacist1);

        Pharmacist pharmacist2 = new Pharmacist("James", "Known", "johndoe@example.com");

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pharmacist2)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("Email address already taken"));
    }

    @Test
    public void createPharmacist_withMissingFields_shouldReturnBadRequest() throws Exception {
        Pharmacist pharmacist = new Pharmacist("", "","");

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pharmacist)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("First name is required"));
        assertTrue(responseBody.contains("Last name is required"));
        assertTrue(responseBody.contains("Email address is required"));
    }


}