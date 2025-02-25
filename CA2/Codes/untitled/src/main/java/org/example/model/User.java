package org.example.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @NotNull(message = "Role is required")
    @Pattern(regexp = "^(customer|admin)$", message = "Role must be 'customer' or 'admin'")
    private String role;

    @NotNull(message = "Username is required")
    @Pattern(regexp = "^[A-Za-z0-9-_]+$", message = "Username can only contain letters and numbers")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Address is required")
    private Map<String, String> address;

    private Double credit = 0.0;


    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Map<String, String> getAddress() { return address; }
    public void setAddress(Map<String, String> address) { this.address = address; }

    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }
}
