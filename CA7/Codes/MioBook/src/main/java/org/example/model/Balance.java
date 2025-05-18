package org.example.model;

import jakarta.persistence.*;

@Entity
public class Balance {

    @Id
    @Column(name = "customer_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private int amount;

    public Balance() {
        this.amount = 0;
    }

    public Balance(Customer customer) {
        this.customer = customer;
        this.amount = 0;
    }

    public Customer getCustomer() { return customer; }
    public int getAmount() { return amount; }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increaseAmount(int amount) {
        this.amount += amount;
    }

    public void decreaseAmount(int amount) {
        this.amount -= amount;
    }
}
