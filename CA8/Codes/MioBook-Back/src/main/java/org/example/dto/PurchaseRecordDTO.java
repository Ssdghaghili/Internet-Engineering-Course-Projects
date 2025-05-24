package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseRecordDTO {
    private LocalDateTime purchaseDate;
    private List<ItemDTO> items;
    private int totalCost;

    public PurchaseRecordDTO(LocalDateTime purchaseDate, List<ItemDTO> items, int totalCost) {
        this.purchaseDate = purchaseDate;
        this.items = items;
        this.totalCost = totalCost;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
