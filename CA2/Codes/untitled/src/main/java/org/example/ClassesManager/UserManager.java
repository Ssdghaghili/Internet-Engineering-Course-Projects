package org.example.ClassesManager;

import org.example.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserManager {
    private List<User> users = new ArrayList<>();

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public void addUser(User newUser) {
        for (User user: users) {
            if (user.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                throw new IllegalArgumentException("Username already exists!");
            }
            if (user.getEmail().equalsIgnoreCase(newUser.getEmail())) {
                throw new IllegalArgumentException("Email already exists!");
            }
        }
        if (validateUsername(newUser.getUsername()) && validateEmail(newUser.getEmail()) && validateRole(newUser.getRole()) &&
                validatePassword(newUser.getPassword())) {
            users.add(newUser);
//            System.out.println("User added successfully.");
        }
    }

    public List<User> getUsers() {
        return users;
    }

    private boolean validateRole(User.Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        } else if (role != User.Role.customer && role != User.Role.admin) {
            throw new IllegalArgumentException("Role must be either customer or admin");
        }
        else { return true; }
    }

    private boolean validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        } else if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Username must contain only letters, numbers, underscores, and hyphens");
        }
        else { return true; }
    }

    private boolean validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        } else if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }
        else { return true; }
    }

    private boolean validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email must be in right format");
        }
        else { return true; }
    }
}