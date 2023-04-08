package com.andrew.pharmapay.services;

import com.andrew.pharmapay.exceptions.ItemAlreadyInStockException;
import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.StockItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StockItemService {
    private final StockItemRepository stockItemRepository;

    public StockItemService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public StockItem createItem(StockItem stockItem) {
        stockItemRepository.findByName(stockItem.getName()).ifPresent(stockItem1 -> {
            throw new ItemAlreadyInStockException(stockItem1.getName());
        });
        return stockItemRepository.save(stockItem);
    }

    public List<StockItem> getAllStockItems() {
        return stockItemRepository.findAll();
    }

    public void deleteStockItem(Long id) {
        StockItem item =
                stockItemRepository.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "StockItem not found")
                );
        stockItemRepository.delete(item);
    }

    public StockItem updateItem(Long id, StockItem stockItem) {
        StockItem stockItem1 =
                stockItemRepository.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock Item record not found")
                );

        stockItem1.setName(stockItem.getName());
        stockItem1.setQuantity(stockItem.getQuantity());
        stockItem1.setPrice(stockItem.getPrice());
        return stockItemRepository.save(stockItem1);
    }

    public StockItem updateCount(Long id, StockItem stockItem) {
        StockItem stockItem1 =
                stockItemRepository.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock Item record not found")
                );

        stockItem1.setQuantity(stockItem.getQuantity());
        return stockItemRepository.save(stockItem1);
    }
}
