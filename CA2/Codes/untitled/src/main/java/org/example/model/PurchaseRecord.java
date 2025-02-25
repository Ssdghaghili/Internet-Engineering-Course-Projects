package org.example.model;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseRecord {
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

    public int getTotalCost() {
        return totalCost;
    }
}
