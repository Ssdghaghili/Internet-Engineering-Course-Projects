package org.example.model;

import java.util.List;

public class Cart {
    private int totalCost;
    private List<CartItem> items;

    public Cart(int totalCost, List<CartItem> items) {
        this.totalCost = totalCost;
        this.items = items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
