package org.example.service;

import org.example.model.Book;
import org.example.model.PurchaseReceipt;
import org.example.model.User;

import java.time.format.DateTimeFormatter;
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

        if (user.hasBookInCart(book))
            throw new IllegalArgumentException("Book is already in the cart.");

        if (user.isBookPurchased(book))
            throw new IllegalArgumentException("Book is already purchased.");

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

        if (user.hasBookInCart(book))
            throw new IllegalArgumentException("Book is already in the cart.");

        if (user.hasBookInCart(book))
            throw new IllegalArgumentException("Book is already in the cart.");

        if (user.isBookPurchased(book))
            throw new IllegalArgumentException("Book is already purchased.");

        if (days < 1 || days > 9)
            throw new IllegalArgumentException("Borrow days should be between 1 and 9.");

        user.borrowBook(book, days);
    }

    public User showUserDetails(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        return user;
    }

    public Map<String, Object> showBookContent(String username, String title) {
        User user = findUserByUsername(username);
        Book book = bookService.findBookByTitle(title);
        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (book == null)
            throw new IllegalArgumentException("Book not found.");

        if (!user.isBookPurchased(book))
            throw new IllegalArgumentException("The book is not in your possession.");

        Map<String, Object> bookContent = new LinkedHashMap<>();

        bookContent.put("title", book.getTitle());
        bookContent.put("content", book.getContent());

        return bookContent;
    }

    public Map<String, Object> showCart(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("Admins cannot have a cart.");

        Map<String, Object> userCart = new LinkedHashMap<>();

        userCart.put("username", user.getUsername());
        userCart.put("totalCost", user.calculateCartCost());
        userCart.put("items", user.getCart());

        return userCart;
    }

    public Map<String, Object> showPurchaseHistory(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("Admins cannot have a purchase history.");

        Map<String, Object> purchaseHistory = new LinkedHashMap<>();
        purchaseHistory.put("username", user.getUsername());
        purchaseHistory.put("purchaseHistory", user.getPurchaseHistory());

        return purchaseHistory;
    }

    public Map<String, Object> showPurchaseBooks(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("Admins cannot have a purchase history.");

        Map<String, Object> purchaseBooks = new LinkedHashMap<>();

        purchaseBooks.put("username", user.getUsername());
        purchaseBooks.put("books", user.getPurchasedBooks());

        return purchaseBooks;
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