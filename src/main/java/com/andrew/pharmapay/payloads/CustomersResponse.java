package com.andrew.pharmapay.payloads;

import com.andrew.pharmapay.models.Customer;

import java.util.List;

public class CustomersResponse {

    private int count;
    private List<Customer> customers;

    public CustomersResponse() {
    }

    public CustomersResponse(int count, List<Customer> customers) {
        this.count = count;
        this.customers = customers;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
