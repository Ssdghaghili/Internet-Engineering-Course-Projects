package org.example.service;

import org.example.model.Book;
import org.example.model.PurchaseReceipt;
import org.example.model.User;

import java.util.*;

public class UserService {
    private List<User> users;
    private BookService bookService;

    public UserService(BookService bookService) {
        users = new ArrayList<>();
        this.bookService = bookService;
    }

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

    public void addCart(String username, String bookTitle) {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new IllegalArgumentException("Book doesn't exist");

        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User doesn't exist");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("admins cannot add to cart.");

        if (user.getCart().size() >= 10)
            throw new IllegalArgumentException("Cart cannot have more than 10 items.");

        user.addCart(book);
    }

    public void removeCart(String username, String bookTitle) {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new IllegalArgumentException("Book doesn't exist");

        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User doesn't exist");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("Admins cannot remove from cart.");

        if (!user.removeBookFromCart(book))
            throw new IllegalArgumentException("Book is not in the user's cart.");
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

    public PurchaseReceipt purchaseCart(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (user.getRole() != User.Role.customer)
            throw new IllegalArgumentException("Only customers can purchase cart.");

        if (user.getCart().isEmpty())
            throw new IllegalArgumentException("Cart cannot be empty.");

        if (!user.hasEnoughCreditForCart())
            throw new IllegalArgumentException("User has not enough credit.");

        return user.purchaseCart();
    }

    public void borrowBook(String username, String bookTitle, int days) {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new IllegalArgumentException("Book doesn't exist");

        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User doesn't exist");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("admins cannot add to cart.");

        if (user.getCart().size() >= 10)
            throw new IllegalArgumentException("Cart cannot have more than 10 items.");

        if (days < 1 || days > 9)
            throw new IllegalArgumentException("Borrow days should be between 1 and 9.");

        user.borrowBook(book, days);
    }

    public Map<String, Object> showUserDetails(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        Map<String, Object> userDetails = new LinkedHashMap<>();
        userDetails.put("username", user.getUsername());
        userDetails.put("role", user.getRole());
        userDetails.put("email", user.getEmail());
        userDetails.put("address", user.getAddress());

        if (user.getRole() != User.Role.admin) {
            userDetails.put("balance", user.getBalance());
        }

        return userDetails;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void validateAdmin(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");;

        if (user.getRole() != User.Role.admin)
            throw new IllegalArgumentException("User is not an admin.");
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