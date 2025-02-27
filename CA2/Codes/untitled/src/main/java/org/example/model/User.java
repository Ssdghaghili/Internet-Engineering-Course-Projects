package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class User {

    public enum Role {
        customer,
        admin,
    }

    private Role role;
    private String username;
    @JsonProperty("password")
    private String password;
    private String email;
    private Address address;
    private int balance;
    private List<CartItem> cart;
    private List<PurchaseRecord> purchaseHistory;

    public User() {                         //Default constructor
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
    public List<PurchaseRecord> getPurchaseHistory() {return purchaseHistory;}

    public void setBalance(int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

}
