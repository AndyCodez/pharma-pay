package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.services.StockItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class StockItemController {

    private final StockItemService stockItemService;

    public StockItemController(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
    }

    @PostMapping("/stock-items")
    public ResponseEntity<StockItem> createItem(@Valid @RequestBody StockItem stockItem) {
        StockItem createdStockItem = stockItemService.createItem(stockItem);
        return ResponseEntity.created(
                URI.create("/api/v1/stock-items/" + createdStockItem.getId())
        ).body(createdStockItem);
    }
}
