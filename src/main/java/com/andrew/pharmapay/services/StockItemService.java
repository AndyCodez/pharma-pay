package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.repositories.StockItemRepository;
import org.springframework.stereotype.Service;

@Service
public class StockItemService {
    private final StockItemRepository stockItemRepository;

    public StockItemService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public StockItem createItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }
}
