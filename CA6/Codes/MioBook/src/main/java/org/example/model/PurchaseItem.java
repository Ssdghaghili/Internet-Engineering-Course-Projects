package org.example.model;

import jakarta.persistence.*;

@Entity
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="record_id")
    private PurchaseRecord record;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean isBorrowed;
    private int borrowDays;
    private int finalPrice;

    public PurchaseItem() {}

    public PurchaseItem(PurchaseRecord record, CartItem cartItem) {
        this.record = record;
        this.book = cartItem.getBook();
        this.isBorrowed = cartItem.isBorrowed();
        this.borrowDays = cartItem.getBorrowDays();
        this.finalPrice = cartItem.getFinalPrice();
    }

    public Book getBook() { return book; }
    public boolean isBorrowed() { return isBorrowed; }
    public int getBorrowDays() { return borrowDays; }
    public int getFinalPrice() { return finalPrice; }
}
