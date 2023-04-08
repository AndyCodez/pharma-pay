package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.StockItem;
import com.andrew.pharmapay.payloads.StockItemResponse;
import com.andrew.pharmapay.services.StockItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
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

    @GetMapping("/stock-items")
    public ResponseEntity<List<StockItemResponse>> getAllStockItems() {
        List<StockItemResponse> stockItemResponses = new ArrayList<>();
        List<StockItem> stockItemList = stockItemService.getAllStockItems();
        for (StockItem item: stockItemList) {
            StockItemResponse response = new StockItemResponse(
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getCreatedBy(),
                    item.getCreatedDate(),
                    item.getLastModifiedBy(),
                    item.getLastModifiedDate()
            );
            stockItemResponses.add(response);
        }
        return ResponseEntity.ok(stockItemResponses);
    }

    @DeleteMapping("/stock-items/{id}")
    public ResponseEntity deleteStockItem(@PathVariable Long id) {
        stockItemService.deleteStockItem(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/stock-items/{stockItemId}")
    public ResponseEntity<StockItem> updateItem(@PathVariable Long stockItemId, @Valid @RequestBody StockItem stockItem) {
        return ResponseEntity.ok(stockItemService.updateItem(stockItemId, stockItem));
    }
}
