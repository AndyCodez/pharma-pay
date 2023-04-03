package com.andrew.pharmapay.services;

import com.andrew.pharmapay.exceptions.ItemNotInStockException;
import com.andrew.pharmapay.exceptions.LessItemInStockException;
import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.models.SoldItem;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.StockItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BillServiceTest {

    @Autowired
    private BillService billService;

    @Autowired
    private StockItemRepository stockItemRepository;

    @AfterEach
    void tearDown() {
        stockItemRepository.deleteAll();
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
}