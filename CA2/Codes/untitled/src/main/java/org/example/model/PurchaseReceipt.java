package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PurchaseReceipt {
    private int bookCount;
    private int totalCost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    public PurchaseReceipt() {}

    public PurchaseReceipt(int itemCount, int totalCost, LocalDateTime date) {
        this.bookCount = itemCount;
        this.totalCost = totalCost;
        this.date = date;
    }

    public int getBookCount() {return bookCount;}

    public double getTotalCost() {return totalCost;}

    public LocalDateTime getDate() {return date;}
}
