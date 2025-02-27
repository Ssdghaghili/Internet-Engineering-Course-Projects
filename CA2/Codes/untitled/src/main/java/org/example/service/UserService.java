package org.example.service;

import org.example.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();

    public void addUser(User newUser) {

        if (!ServiceUtils.validateUsername(newUser.getUsername()))
            throw new IllegalArgumentException("Username is invalid.");

        if (!ServiceUtils.validateEmail(newUser.getEmail()))
            throw new IllegalArgumentException("Email is invalid.");

        if (!ServiceUtils.validatePassword(newUser.getPassword()))
            throw new IllegalArgumentException("Password is invalid.");

        if (usernameExists(newUser.getUsername()))
            throw new IllegalArgumentException("Username already exists.");

        if (emailExists(newUser.getEmail()))
            throw new IllegalArgumentException("Email already exists.");

        users.add(newUser);
    }

    public void addCredit(String username, int credit) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (user.getRole() != User.Role.customer)
            throw new IllegalArgumentException("Only customers can add credit.");

        if (credit < 100)
            throw new IllegalArgumentException("Minimum deposit amount is 100 cents (1 dollar).");

        user.setBalance(user.getBalance() + credit);
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public List<User> getUsers() {
        return users;
    }
}