package org.example.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import org.example.model.serializer.CartItemSerializer;

@JsonSerialize(using = CartItemSerializer.class)
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    private boolean isBorrowed;
    private int borrowDays;
    private int finalPrice;

    public CartItem() {}

    public CartItem(Book book, boolean isBorrowed, int borrowDays) {
        this.book = book;
        this.isBorrowed = isBorrowed;
        this.borrowDays = borrowDays;
        this.finalPrice = isBorrowed ? ((borrowDays * book.getPrice()) / 10) : book.getPrice();
    }

    public Book getBook() { return book; }
    public boolean isBorrowed() { return isBorrowed; }
    public int getBorrowDays() { return borrowDays; }
    public int getFinalPrice() { return finalPrice; }
}
