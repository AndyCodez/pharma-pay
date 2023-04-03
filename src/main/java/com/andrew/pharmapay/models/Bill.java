package com.andrew.pharmapay.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "bill_date_time")
    private Date billDateTime;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "bill_item",
            joinColumns = { @JoinColumn(name = "bill_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "sold_item_id", referencedColumnName = "id") }
    )
    private Set<SoldItem> soldItems = new HashSet<>();

    public Bill() {
    }

    public Bill(BigDecimal amount, Date billDateTime, Set<SoldItem> soldItems) {
        this.amount = amount;
        this.billDateTime = billDateTime;
        this.soldItems = soldItems;
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

    public Set<SoldItem> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(Set<SoldItem> soldItems) {
        this.soldItems = soldItems;
    }
}
