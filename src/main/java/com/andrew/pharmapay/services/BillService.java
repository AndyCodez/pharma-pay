package com.andrew.pharmapay.services;

import com.andrew.pharmapay.exceptions.ItemNotInStockException;
import com.andrew.pharmapay.exceptions.LessItemInStockException;
import com.andrew.pharmapay.models.Bill;
import com.andrew.pharmapay.models.Customer;
import com.andrew.pharmapay.models.SoldItem;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.BillRepository;
import com.andrew.pharmapay.repositories.CustomerRepository;
import com.andrew.pharmapay.repositories.StockItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final StockItemRepository stockItemRepository;
    private final CustomerRepository customerRepository;

    public BillService(BillRepository billRepository, StockItemRepository stockItemRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.stockItemRepository = stockItemRepository;
        this.customerRepository = customerRepository;
    }

    public Bill createBill(List<SoldItem> soldItems) throws ItemNotInStockException, LessItemInStockException {
        Bill bill = new Bill();

        for (SoldItem soldItem: soldItems) {
            addItemToBill(bill, soldItem);
        }
        return billRepository.save(bill);
    }

    public Bill addItemToBill(Bill bill, SoldItem item) throws ItemNotInStockException, LessItemInStockException {
        StockItem stockItem =  stockItemRepository.findByName(item.getName()).orElseThrow(() -> new ItemNotInStockException(item.getName()));
        if (item.getQuantity() > stockItem.getQuantity()) {
            throw new LessItemInStockException(stockItem);
        }

        bill.setBillDateTime(new Date());

        List<SoldItem> soldItems = bill.getSoldItems();
        soldItems.add(item);

        BigDecimal itemAmount = stockItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        BigDecimal totalAmount = bill.getAmount().add(itemAmount);
        bill.setAmount(totalAmount);

        return bill;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill addBillToCustomer(Long billId, Long customerId) {
        Customer customer =
                customerRepository.findById(customerId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer record not found")
                );
        Bill bill = billRepository.findById(billId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill record not found")
        );

        customer.getBills().add(bill);
        bill.setCustomer(customer);

        customerRepository.save(customer);
        return bill;
    }

    public Bill getBill(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill record not found"));
    }
}
