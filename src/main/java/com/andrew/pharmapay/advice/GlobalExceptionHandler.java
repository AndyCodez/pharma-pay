package com.andrew.pharmapay.advice;

import com.andrew.pharmapay.exceptions.ItemAlreadyInStockException;
import com.andrew.pharmapay.exceptions.ItemNotInStockException;
import com.andrew.pharmapay.exceptions.LessItemInStockException;
import com.andrew.pharmapay.payloads.FailedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<FailedResponse> handleResponseStatusException(ResponseStatusException ex) {
        FailedResponse failedResponse = new FailedResponse(Arrays.asList(ex.getReason()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failedResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailedResponse> handleInvalidFieldsException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(fieldError.getDefaultMessage()));
        FailedResponse failedResponse = new FailedResponse(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failedResponse);
    }

    @ExceptionHandler(ItemNotInStockException.class)
    public ResponseEntity<FailedResponse> handleItemNotInStockException(ItemNotInStockException ex) {
        FailedResponse failedResponse = new FailedResponse(Arrays.asList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failedResponse);
    }

    @ExceptionHandler(LessItemInStockException.class)
    public ResponseEntity<FailedResponse> handleLessItemInStockException(LessItemInStockException ex) {
        FailedResponse failedResponse = new FailedResponse(Arrays.asList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failedResponse);
    }

    @ExceptionHandler(ItemAlreadyInStockException.class)
    public ResponseEntity<FailedResponse> handleItemAlreadyInStockException(ItemAlreadyInStockException ex) {
        FailedResponse failedResponse = new FailedResponse(Arrays.asList(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failedResponse);
    }
}
