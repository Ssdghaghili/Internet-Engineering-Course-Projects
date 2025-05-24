package org.example.dto;

import java.util.List;

public class CartDTO {
    private int totalCost;
    private List<ItemDTO>  items;

    public CartDTO(int totalCost, List<ItemDTO> items) {
        this.totalCost = totalCost;
        this.items = items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
