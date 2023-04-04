package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Customer;
import com.andrew.pharmapay.payloads.CustomersResponse;
import com.andrew.pharmapay.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.created(
                URI.create("/api/v1/customers/" + createdCustomer.getId())
        ).body(createdCustomer);
    }

    @GetMapping("/customers")
    public ResponseEntity<CustomersResponse> fetchAllCustomers() {
        List<Customer> allCustomers = customerService.fetchAllCustomers();
        CustomersResponse response = new CustomersResponse();
        response.setCount(allCustomers.size());
        response.setCustomers(allCustomers);
        return ResponseEntity.ok(response);
    }

}
