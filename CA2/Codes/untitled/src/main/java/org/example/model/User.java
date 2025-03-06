package org.example.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class User {

    public enum Role {
        customer,
        admin,
    }

    private Role role;
    private String username;
    private String password;
    private String email;
    private Address address;
    private int balance;
    private List<CartItem> cart;
    private List<PurchaseRecord> purchaseHistory;

    public User() {
        this.cart = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
        this.balance = 0;
    }

    public User(Role role, String username, String password, String email, Address address) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.balance = 0;
        this.cart = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

    public Role getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Address getAddress() { return address;}
    public int getBalance() { return balance; }
    public List<CartItem> getCart() { return cart; }
    public List<PurchaseRecord> getPurchaseHistory() { return purchaseHistory; }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addCart(Book book) {
        this.cart.add(new CartItem(book, false, 0));
    }

    public void borrowBook(Book book, int days) {
        this.cart.add(new CartItem(book, true, days));
    }

    public boolean removeBookFromCart(Book book) {
        return cart.removeIf(c -> c.getBook().equals(book));
    }

    public PurchaseReceipt purchaseCart() {
        PurchaseReceipt purchaseReceipt = new PurchaseReceipt(this.cart.size(), calculateCartCost(), LocalDateTime.now());
        purchaseHistory.add(new PurchaseRecord(LocalDateTime.now(), this.cart, calculateCartCost()));
        this.balance -= calculateCartCost();
        this.cart = new ArrayList<>();
        return purchaseReceipt;
    }

    public int calculateCartCost() {
        int totalCost = 0;
        for (CartItem cartItem : this.cart) {
            totalCost += cartItem.getFinalPrice();
        }
        return totalCost;
    }

    public boolean hasBookInCart(Book book) {
        return this.cart.stream().anyMatch(c -> c.getBook().equals(book));
    }

    public boolean hasBookInPurchaseHistory(Book book) {
        return this.purchaseHistory.stream().anyMatch(p -> p.getItems().stream().anyMatch(c -> c.getBook().equals(book)));
    }

    public boolean hasBorrowBookValid(Book book) {
        boolean hasNonBorrowed = this.purchaseHistory.stream()
                .flatMap(purchase -> purchase.getItems().stream())
                .anyMatch(cartItem -> cartItem.getBook().equals(book) && !cartItem.isBorrowed());

        if (hasNonBorrowed)
            return true; // If any non-borrowed book is found, return false immediately

        return this.purchaseHistory.stream()
                .flatMap(purchase -> purchase.getItems().stream())
                .filter(cartItem -> cartItem.getBook().equals(book))
                .anyMatch(cartItem -> cartItem.isBorrowed() && cartItem.getBorrowDays() > 0);
    }

    public boolean hasEnoughCreditForCart() {
        return this.balance >= calculateCartCost();
    }

}
