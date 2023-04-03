package com.andrew.pharmapay.exceptions;

import com.andrew.pharmapay.models.StockItem;

public class LessItemInStockException extends Exception{
    private StockItem stockItem;
    public LessItemInStockException(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    @Override
    public String getMessage() {
        return "Only " + stockItem.getQuantity() + " " + stockItem + " left";
    }
}
