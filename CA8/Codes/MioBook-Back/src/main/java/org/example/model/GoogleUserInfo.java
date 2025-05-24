package org.example.model;

public class GoogleUserInfo {
    private String email;
    private String name;

    public GoogleUserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}