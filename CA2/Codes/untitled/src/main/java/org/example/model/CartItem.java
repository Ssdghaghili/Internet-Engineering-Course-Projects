package org.example.model;

public class CartItem {
    private Book book;
    private boolean isBorrowed;
    private int borrowDays;
    private int finalPrice;

    public CartItem(Book book, boolean isBorrowed, int borrowDays) {
        this.book = book;
        this.isBorrowed = isBorrowed;
        this.borrowDays = borrowDays;
        this.finalPrice = isBorrowed ? ((borrowDays * book.getPrice()) / 10) : book.getPrice();
    }
}
