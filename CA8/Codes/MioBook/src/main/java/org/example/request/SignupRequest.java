package org.example.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {
    @NotBlank(message = "Username is missing")
    private String username;

    @NotBlank(message = "Password is missing")
    private String password;

    @NotBlank(message = "Email is missing")
    private String email;

    @NotBlank(message = "Role is missing")
    private String role;

    @Valid
    private AddressRequest address;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public AddressRequest getAddress() { return address; }
    public void setAddress(AddressRequest address) { this.address = address; }
}