package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.models.SoldItem;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.BillRepository;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import com.andrew.pharmapay.repositories.StockItemRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.andrew.pharmapay.models.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BillIntegrationTest {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void createBill_withValidData_shouldReturnCreated() throws Exception {
        String authToken = getAuthTokenForAdmin();

        StockItem paracetamolStock = new StockItem("Paracetamol", BigDecimal.valueOf(20), 5);
        stockItemRepository.save(paracetamolStock);

        SoldItem soldItem = new SoldItem("Paracetamol", 3);
        List<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(soldItem);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldItems))
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        List<Bill> allBills = billRepository.findAll();
        assertEquals(1, allBills.size());
        assertEquals(BigDecimal.valueOf(20 * 3), allBills.get(0).getAmount());
    }

    @Test
    public void createBill_withItemThatIsNotInStock_shouldReturnNotFound() throws Exception {
        String authToken = getAuthTokenForAdmin();

        SoldItem soldItem = new SoldItem("Paracetamol", 3);

        List<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(soldItem);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldItems))
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Paracetamol is not in stock"));
    }

    @Test
    public void createBill_withItemQtyMoreThanAvailableQty_shouldReturnBadRequest() throws Exception {
        String authToken = getAuthTokenForAdmin();

        StockItem paracetamolStock = new StockItem("Paracetamol", BigDecimal.valueOf(20), 1);
        stockItemRepository.save(paracetamolStock);

        SoldItem soldItem = new SoldItem("Paracetamol", 3);

        List<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(soldItem);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldItems))
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Only " + 1 + " " + "Paracetamol" + " left"));
    }

    private String getAuthTokenForAdmin() {
        UserDetails admin = new Pharmacist("John", "Doe", "johndoe@example.com", "pass123", ADMIN);
        when(userDetailsService.loadUserByUsername(admin.getUsername())).thenReturn(admin);
        // Set up security context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities())
        );
        return jwtUtil.generateToken(admin);
    }

}