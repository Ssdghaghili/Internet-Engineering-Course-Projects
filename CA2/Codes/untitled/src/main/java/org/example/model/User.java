package org.example.model;

import java.util.ArrayList;
import java.util.List;

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

    public User(Role role, String username, String password, String email, Address address, int balance) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.balance = balance;
        this.cart = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public int getBalance() {
        return balance;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public List<PurchaseRecord> getPurchaseHistory() {
        return purchaseHistory;
    }
}
