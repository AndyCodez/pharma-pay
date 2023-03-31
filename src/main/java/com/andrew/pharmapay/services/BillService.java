package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.repositories.BillRepository;
import org.springframework.stereotype.Service;

@Service
public class BillService {
    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }
}
