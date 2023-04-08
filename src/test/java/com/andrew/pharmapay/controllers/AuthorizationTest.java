package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.AuthRequest;
import com.andrew.pharmapay.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.andrew.pharmapay.models.Role.ADMIN;
import static com.andrew.pharmapay.models.Role.NORMAL_PHARMACIST;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void allUsers_shouldBeAbleToAccess_signInEndpoint() throws Exception {
        AuthRequest request = new AuthRequest("", "");
        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletRequestBuilder requestBuilders = MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request));

        MvcResult result = mockMvc.perform(requestBuilders)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(response.contains("Email address is required"));
    }

    @Test
    void onlyAdmins_shouldBeAbleToAccess_createPharmacistEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pharmacists")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void allPharmacists_shouldBeAbleToAccess_createCustomersEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void onlyAdmins_shouldBeAbleToAccess_createStockItemsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stock-items")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void onlyAdmins_shouldBeAbleToAccess_updateStockItemsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/stock-items/1")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/stock-items/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/stock-items/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void onlyAdmins_shouldBeAbleToAccess_deleteStockItemsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/stock-items/1")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/stock-items/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/stock-items/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void allPharmacists_shouldBeAbleToAccess_deleteBillsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bills/1")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bills/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bills/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void onlyAdmins_shouldBeAbleToAccess_listAllBillsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bills")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bills")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bills")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void onlyAdmins_shouldBeAbleToAccess_listAllPharmacistsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pharmacists")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pharmacists")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForNormalPharmacist()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pharmacists")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + getAuthTokenForAdmin()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private String getAuthTokenForAdmin() {
        UserDetails admin = new Pharmacist("John", "Doe", "johndoe@example.com", "pass123", ADMIN);
        when(userDetailsService.loadUserByUsername(admin.getUsername())).thenReturn(admin);
        return jwtUtil.generateToken(admin);
    }

    private String getAuthTokenForNormalPharmacist() {
        UserDetails normalPharmacist = new Pharmacist("Bron", "Doe", "brondoe@example.com", "pass123", NORMAL_PHARMACIST);
        when(userDetailsService.loadUserByUsername(normalPharmacist.getUsername())).thenReturn(normalPharmacist);
        return jwtUtil.generateToken(normalPharmacist);
    }
}
