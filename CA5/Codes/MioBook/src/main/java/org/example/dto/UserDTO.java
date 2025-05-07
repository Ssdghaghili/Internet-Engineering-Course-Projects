package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

public class UserDTO {
    private String username;
    private String email;
    private final Map<String, String> address = new HashMap<>();
    private String role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer balance;

    public UserDTO(String username, String email, String country, String city, String role, Integer balance) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.address.put("country", country);
        this.address.put("city", city);
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
