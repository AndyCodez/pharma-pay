package com.andrew.pharmapay.payloads;

import com.andrew.pharmapay.models.Bill;

import java.util.List;

public class BillResponse {
    private int count;
    private List<Bill> bills;

    public BillResponse() {
    }

    public BillResponse(int count, List<Bill> bills) {
        this.count = count;
        this.bills = bills;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
