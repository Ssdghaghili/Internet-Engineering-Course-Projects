package org.example.dto;

import java.util.List;

public class CartDTO {
    private int totalCost;
    private List<CartItemDTO>  items;

    public CartDTO(int totalCost, List<CartItemDTO> items) {
        this.totalCost = totalCost;
        this.items = items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}
