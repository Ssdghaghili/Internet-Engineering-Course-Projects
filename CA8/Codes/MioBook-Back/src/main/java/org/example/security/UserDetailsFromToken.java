package org.example.security;

public class UserDetailsFromToken {
    private final Long userId;
    private final String username;
    private final String email;

    public UserDetailsFromToken(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}