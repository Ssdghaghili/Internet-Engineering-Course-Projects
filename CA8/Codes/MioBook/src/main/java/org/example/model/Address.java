package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    private String city;

    public Address() {}

    public Address(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public Long getId() { return id; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
}
