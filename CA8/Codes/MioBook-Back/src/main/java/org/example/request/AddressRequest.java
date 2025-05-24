package org.example.request;

import jakarta.validation.constraints.NotBlank;

public class AddressRequest {
    @NotBlank(message = "Country is missing")
    private String country;

    @NotBlank(message = "City is missing")
    private String city;

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}