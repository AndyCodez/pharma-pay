package com.andrew.pharmapay.exceptions;

import com.andrew.pharmapay.payloads.FailedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<FailedResponse> handleResponseStatusException(ResponseStatusException ex) {
        FailedResponse failedResponse = new FailedResponse(Arrays.asList(ex.getReason()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failedResponse);
    }
}
