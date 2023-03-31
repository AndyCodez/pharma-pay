package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.services.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/bills")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.created(
                URI.create("/api/v1/bills/" + createdBill.getId())
        ).body(createdBill);
    }
}
