package org.example.service;

import org.example.model.User;

import org.springframework.stereotype.Component;

@Component
public class UserSession {
    private User currentUser;

    public UserSession() {
        currentUser = null;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

