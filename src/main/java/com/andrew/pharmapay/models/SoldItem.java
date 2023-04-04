package com.andrew.pharmapay.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class SoldItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Column(name = "name")
    private String name;

//    @NotNull(message = "Price is required")
//    @Column(name = "price")
//    @Min(value = 1, message = "Price must be greater than or equal to 1")
//    private BigDecimal price;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    @Column(name = "quantity")
    private int quantity;

    public SoldItem() {
    }

    public SoldItem(String name, int quantity) {
        this.name = name;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
