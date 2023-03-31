package com.andrew.pharmapay.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Price is required")
    @Column(name = "price")
    @Min(value = 1, message = "Price must be greater than or equal to 1")
    private BigDecimal price;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    private Set<Bill> bills;

    public Item() {
    }

    public Item(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
