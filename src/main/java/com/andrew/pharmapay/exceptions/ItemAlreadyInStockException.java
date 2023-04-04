package com.andrew.pharmapay.exceptions;

public class ItemAlreadyInStockException extends RuntimeException {
    public ItemAlreadyInStockException(String itemName) {
        super(itemName + " is already in stock");
    }
}
