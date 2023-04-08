package com.andrew.pharmapay.services;

import com.andrew.pharmapay.exceptions.ItemNotInStockException;
import com.andrew.pharmapay.exceptions.LessItemInStockException;
import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.models.SoldItem;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.StockItemRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;

import static com.andrew.pharmapay.models.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BillServiceTest {

    @Autowired
    private BillService billService;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @AfterEach
    void tearDown() {
        stockItemRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        getAuthToken();
    }

    @Test
    void addingItemsToBill_shouldFail_ifItemIsNotInStock() {
        // Ensure no item is in stock
        stockItemRepository.deleteAll();

        Bill bill = new Bill();

        SoldItem item = new SoldItem("Paracetamol", 5);

        assertThrows(ItemNotInStockException.class,
                () -> billService.addItemToBill(bill, item)
        );
    }

    @Test
    void addingItemsToBill_shouldFail_ifItemQtyIsMoreThanInStock() {
        StockItem paracetamolStock = new StockItem("Paracetamol", BigDecimal.valueOf(20), 1);
        stockItemRepository.save(paracetamolStock);

        Bill bill = new Bill();

        SoldItem item = new SoldItem("Paracetamol", 5);

        assertThrows(LessItemInStockException.class,
                () -> billService.addItemToBill(bill, item)
        );
    }

    @Test
    void addingItemsToBill_shouldSucceed_ifItemQtyIsLessThanInStock() throws LessItemInStockException, ItemNotInStockException {
        StockItem paracetamolStock = new StockItem("Paracetamol", BigDecimal.valueOf(20), 5);
        stockItemRepository.save(paracetamolStock);

        Bill bill = new Bill();
        SoldItem item = new SoldItem("Paracetamol", 4);

        billService.addItemToBill(bill, item);

        assertEquals(1, bill.getSoldItems().size());
        assertEquals(BigDecimal.valueOf(20 * 4).setScale(2), bill.getAmount());
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