package org.example.service;

import org.example.model.User;

public class UserSession {
    private static User loggedInUser = null;

    public static void login(User user) {
        loggedInUser = user;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}