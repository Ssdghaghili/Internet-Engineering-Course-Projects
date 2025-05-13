package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @OneToMany(mappedBy="record", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseItem> items = new HashSet<>();

    private LocalDateTime purchaseDate;
    private int totalCost;

    public PurchaseRecord() {}

    public PurchaseRecord(Customer customer, LocalDateTime purchaseDate, int totalCost) {
        this.customer = customer;
        this.purchaseDate = purchaseDate;
        this.totalCost = totalCost;
    }

    public Set<PurchaseItem> getItems() { return items; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public int getTotalCost() { return totalCost; }

    public void addItem(PurchaseItem purchaseItem) {
        items.add(purchaseItem);
    }
}
