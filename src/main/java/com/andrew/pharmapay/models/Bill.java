package com.andrew.pharmapay.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "bill_date_time")
    private Date billDateTime;

    public Bill() {
    }

    public Bill(BigDecimal amount, Date billDateTime) {
        this.amount = amount;
        this.billDateTime = billDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getBillDateTime() {
        return billDateTime;
    }

    public void setBillDateTime(Date billDateTime) {
        this.billDateTime = billDateTime;
    }
}
