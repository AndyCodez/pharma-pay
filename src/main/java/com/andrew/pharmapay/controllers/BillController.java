package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.exceptions.ItemNotInStockException;
import com.andrew.pharmapay.exceptions.LessItemInStockException;
import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.models.SoldItem;
import com.andrew.pharmapay.payloads.BillResponse;
import com.andrew.pharmapay.services.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/bills")
    public ResponseEntity<Bill> createNewBill(@RequestBody List<SoldItem> soldItems) throws ItemNotInStockException, LessItemInStockException {
        Bill createdBill = billService.createBill(soldItems);
        return ResponseEntity.created(
                URI.create("/api/v1/bills" + createdBill.getId())
        ).body(createdBill);
    }

    @GetMapping("/bills")
    public ResponseEntity<BillResponse> getAllBills() {
        List<Bill> allBills = billService.getAllBills();
        BillResponse billResponse = new BillResponse();
        billResponse.setCount(allBills.size());
        billResponse.setBills(allBills);
        return ResponseEntity.ok(billResponse);
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBill(id));
    }

    @PostMapping("/add-bill-to-customer/customers/{customerId}/bills/{billId}")
    public ResponseEntity<?> addBillToCustomer(@PathVariable Long billId, @PathVariable Long customerId) {
        return ResponseEntity.ok(billService.addBillToCustomer(billId, customerId));
    }

    @PutMapping("/complete-sale/bills/{billId}")
    public ResponseEntity<Bill> completeSale(@PathVariable Long billId) throws LessItemInStockException, ItemNotInStockException {
        return ResponseEntity.ok(billService.completeSale(billId));
    }

    @DeleteMapping("/bills/{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long billId){
        billService.deleteBill(billId);
        return ResponseEntity.ok().build();
    }
}
