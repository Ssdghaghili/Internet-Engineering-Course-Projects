package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseRecord {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;
    private List<CartItem> items;
    private int totalCost;

    public PurchaseRecord(LocalDateTime purchaseDate, List<CartItem> items, int totalCost) {
        this.purchaseDate = purchaseDate;
        this.items = items;
        this.totalCost = totalCost;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalCost() { return totalCost; }
}
