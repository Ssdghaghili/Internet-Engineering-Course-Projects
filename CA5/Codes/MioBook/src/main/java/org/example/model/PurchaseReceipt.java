package org.example.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class PurchaseReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_count", nullable = false)
    private int bookCount;

    @Column(name = "total_cost", nullable = false)
    private int totalCost;

    @Column(name = "purchase_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    public PurchaseReceipt() {}

    public PurchaseReceipt(int itemCount, int totalCost, LocalDateTime date) {
        this.bookCount = itemCount;
        this.totalCost = totalCost;
        this.purchaseDate = date;
    }

    public int getBookCount() { return bookCount; }

    public double getTotalCost() { return totalCost; }

    public LocalDateTime getDate() { return purchaseDate; }
}
