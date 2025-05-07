package org.example.dto;

public class CustomerDTO extends UserDTO {
    private int balance;

    public CustomerDTO(String username, String email, String country, String city, String role, int balance) {
        super(username, email, country, city, role);
        this.balance = balance;
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
