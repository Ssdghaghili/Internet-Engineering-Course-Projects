package org.example.service;

import org.example.model.User;

public class AuthService {
    private UserService userService;
    private UserSession userSession;

    public AuthService(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    public void login(String username, String password) {
        User user = userService.findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User doesn't exist.");

        if (!user.checkPassword(password))
            throw new IllegalArgumentException("Password is incorrect.");

        userSession.login(user);
    }

    public void logout() {
        if (!userSession.isLoggedIn())
            throw new IllegalArgumentException("User is not logged in.");

        userSession.logout();
    }

    public void validateAdmin() {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new IllegalArgumentException("User is not logged in.");

        if (user.getRole() != User.Role.admin)
            throw new IllegalArgumentException("User is not an admin.");
    }

    public User getCurrentUser() {
        return userSession.getCurrentUser();
    }
}
