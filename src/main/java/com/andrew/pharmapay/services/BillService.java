package com.andrew.pharmapay.services;

import com.andrew.pharmapay.exceptions.BillAlreadySettledException;
import com.andrew.pharmapay.exceptions.ItemNotInStockException;
import com.andrew.pharmapay.exceptions.LessItemInStockException;
import com.andrew.pharmapay.models.*;
import com.andrew.pharmapay.repositories.BillRepository;
import com.andrew.pharmapay.repositories.CustomerRepository;
import com.andrew.pharmapay.repositories.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BillService {
    Logger logger = LoggerFactory.getLogger(BillService.class);

    private final BillRepository billRepository;
    private final StockItemRepository stockItemRepository;
    private final CustomerRepository customerRepository;

    public BillService(BillRepository billRepository, StockItemRepository stockItemRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.stockItemRepository = stockItemRepository;
        this.customerRepository = customerRepository;
    }

    public Bill createBill(List<SoldItem> soldItems) throws ItemNotInStockException, LessItemInStockException {
        logger.info("Creating a new bill.");

        Bill bill = new Bill();

        for (SoldItem soldItem: soldItems) {
            addItemToBill(bill, soldItem);
        }
        return billRepository.save(bill);
    }

    public Bill addItemToBill(Bill bill, SoldItem item) throws ItemNotInStockException, LessItemInStockException {
        logger.info("Populating bill with items.");

        StockItem stockItem =
                stockItemRepository.findByName(
                        item.getName()).orElseThrow(() -> new ItemNotInStockException(item.getName())
                );

        if (item.getQuantity() > stockItem.getQuantity()) {
            logger.warn("Requested quantity is more than the available stock.");
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
        logger.info("Fectching all bills.");

        return billRepository.findAll();
    }

    public Bill addBillToCustomer(Long billId, Long customerId) {
        logger.info("Assigning bill to customer");

        Customer customer =
                customerRepository.findById(customerId).orElseThrow(
                        () -> {
                            logger.error("Missing customer record");
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer record not found");
                        }
                );
        Bill bill = billRepository.findById(billId).orElseThrow(
                () -> {
                    logger.error("Missing bill record");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill record not found");
                }
        );

        logger.info("Successfully retrieved both bill and customer records");

        customer.getBills().add(bill);
        bill.setCustomer(customer);

        customerRepository.save(customer);
        return bill;
    }

    public Bill getBill(Long id) {
        logger.trace("Fetching bill.");

        return billRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill record not found"));
    }

    public Bill completeSale(Long billId) throws ItemNotInStockException, LessItemInStockException {
        logger.trace("Validating bill.");

        Bill bill =
                billRepository.findById(billId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill record not found")
                );

        if (bill.getStatus().name().equals(Status.PAID.name())) {
            logger.error("Attempting to settle a bill that was already settled.");
            throw new BillAlreadySettledException("Bill Already Settled");
        }

        List<SoldItem> soldItems = bill.getSoldItems();
        for (SoldItem item : soldItems) {
            StockItem stockItem =
                    stockItemRepository.findByName(item.getName())
                            .orElseThrow(() -> new ItemNotInStockException(item.getName()));
            if (item.getQuantity() > stockItem.getQuantity()) {
                logger.warn("Requested quantity is more than the available stock.");
                throw new LessItemInStockException(stockItem);
            }

            int remainingStock = stockItem.getQuantity() - item.getQuantity();
            stockItem.setQuantity(remainingStock);
            stockItemRepository.save(stockItem);
        }

        bill.setBillDateTime(new Date());
        bill.setStatus(Status.PAID);
        return billRepository.save(bill);
    }

    public void deleteBill(Long billId) {
        Bill bill =
                billRepository.findById(billId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill record not found")
                );
        billRepository.delete(bill);
    }
}
