package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Customer;
import com.andrew.pharmapay.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> fetchAllCustomers() {
        return customerRepository.findAll();
    }
}
