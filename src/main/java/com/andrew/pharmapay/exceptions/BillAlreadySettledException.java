package com.andrew.pharmapay.exceptions;

public class BillAlreadySettledException extends RuntimeException {
    public BillAlreadySettledException(String message) {
        super(message);
    }
}
