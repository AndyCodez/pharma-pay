package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class PharmacistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void createPharmacist_withValidData_shouldReturnCreated() throws Exception {
        Pharmacist pharmacist = new Pharmacist("John", "Doe", "johndoe3@example.com", "pass123", ADMIN);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAuthToken())
                .content(objectMapper.writeValueAsString(pharmacist)))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<Pharmacist> allPharmacists = pharmacistRepository.findAll();
        assertEquals(1, allPharmacists.size());
        assertEquals("johndoe3@example.com", allPharmacists.get(0).getEmail());
    }

    @Test
    public void createPharmacist_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        Pharmacist pharmacist = new Pharmacist("John", "Doe", "invalidemail@", "pass123", ADMIN);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAuthToken())
                        .content(objectMapper.writeValueAsString(pharmacist)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createPharmacist_withDuplicateEmail_shouldReturnBadRequest() throws Exception {

        String authToken = getAuthToken();
        Pharmacist pharmacist1 = new Pharmacist("John", "Doe", "johndoe@example.com", "pass123", ADMIN);
        pharmacistRepository.save(pharmacist1);

        Pharmacist pharmacist2 = new Pharmacist("James", "Known", "johndoe@example.com", "pass123", ADMIN);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .content(objectMapper.writeValueAsString(pharmacist2)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("Email address already taken"));
    }

    @Test
    public void createPharmacist_withMissingFields_shouldReturnBadRequest() throws Exception {
        Pharmacist pharmacist = new Pharmacist("", "","", "pass123", ADMIN);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pharmacist))
                .header("Authorization", "Bearer " + getAuthToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("First name is required"));
        assertTrue(responseBody.contains("Last name is required"));
        assertTrue(responseBody.contains("Email address is required"));
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