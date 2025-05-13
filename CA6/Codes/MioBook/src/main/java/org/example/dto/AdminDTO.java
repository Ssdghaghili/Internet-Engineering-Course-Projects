package org.example.dto;

public class AdminDTO extends UserDTO {
    public AdminDTO(String username, String email, String country, String city, String role) {
        super(username, email, country, city, role);
    }
}
