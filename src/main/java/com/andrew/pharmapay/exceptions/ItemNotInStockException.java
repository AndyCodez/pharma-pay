package com.andrew.pharmapay.exceptions;

public class ItemNotInStockException extends Exception{
    public ItemNotInStockException(String message) {
        super(message + " is not in stock");
    }
}
