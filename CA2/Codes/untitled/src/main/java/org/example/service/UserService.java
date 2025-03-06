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

//        if (purchaseCart().)
//            throw new IllegalArgumentException("Book is borrowed.");


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

    public Map<String, Object> showCart(String username) {
        User user = findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

        if (user.getRole() == User.Role.admin)
            throw new IllegalArgumentException("Admins cannot have a cart.");

        Map<String, Object> userCart = new LinkedHashMap<>();

        userCart.put("username", user.getUsername());
        userCart.put("totalCost", user.calculateCartCost());

        List<Map<String, Object>> cart = new ArrayList<>();

        for (int i = 0; i < user.getCart().size(); i++) {
            Map<String, Object> cartItem = new LinkedHashMap<>();
            cartItem.put("title", user.getCart().get(i).getBook().getTitle());
            cartItem.put("author", user.getCart().get(i).getBook().getAuthor());
            cartItem.put("publisher", user.getCart().get(i).getBook().getPublisher());
            cartItem.put("genres", user.getCart().get(i).getBook().getGenres());
            cartItem.put("year", user.getCart().get(i).getBook().getYear());
            cartItem.put("price", user.getCart().get(i).getBook().getPrice());
            cartItem.put("isBorrowed", user.getCart().get(i).isBorrowed());
            cartItem.put("finalPrice", user.getCart().get(i).getFinalPrice());
            if (user.getCart().get(i).isBorrowed())
                cartItem.put("borrowDays", user.getCart().get(i).getBorrowDays());
            cart.add(cartItem);
        }

        userCart.put("items", cart);

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

        List<Map<String, Object>> records = new ArrayList<>();

        for (int i = 0; i < user.getPurchaseHistory().size(); i++) {
            Map<String, Object> record = new LinkedHashMap<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            record.put("purchaseDate", user.getPurchaseHistory().get(i).getPurchaseDate().format(formatter));

            List<Map<String, Object>> itemsList = new ArrayList<>();

            for (int j = 0; j < user.getPurchaseHistory().get(i).getItems().size(); j++) {
                Map<String, Object> items = new LinkedHashMap<>();
                items.put("title", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getTitle());
                items.put("author", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getAuthor());
                items.put("publisher", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getPublisher());
                items.put("genres", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getGenres());
                items.put("year", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getYear());
                items.put("isBorrowed", user.getPurchaseHistory().get(i).getItems().get(j).isBorrowed());
                if (user.getPurchaseHistory().get(i).getItems().get(j).isBorrowed())
                    items.put("borrowDays", user.getPurchaseHistory().get(i).getItems().get(j).getBorrowDays());
                items.put("price", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getPrice());
                items.put("finalPrice", user.getPurchaseHistory().get(i).getItems().get(j).getFinalPrice());
                itemsList.add(items);
            }

            record.put("items", itemsList);
            record.put("totalCost", user.getPurchaseHistory().get(i).getTotalCost());
            records.add(record);
        }

        purchaseHistory.put("purchaseHistory", records);

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

        List<Map<String, Object>> books = new ArrayList<>();

        for (int i = 0; i < user.getPurchaseHistory().size(); i++) {
            for (int j = 0; j < user.getPurchaseHistory().get(i).getItems().size(); j++) {
                Map<String, Object> book = new LinkedHashMap<>();
                if (!user.getPurchaseHistory().get(i).getItems().get(j).isBorrowed() ||
                        user.getPurchaseHistory().get(i).getItems().get(j).getBorrowDays() > 0) {

                    book.put("title", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getTitle());
                    book.put("author", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getAuthor());
                    book.put("publisher", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getPublisher());
                    book.put("genres", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getGenres());
                    book.put("year", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getYear());
                    book.put("price", user.getPurchaseHistory().get(i).getItems().get(j).getBook().getPrice());
                    book.put("isBorrowed", user.getPurchaseHistory().get(i).getItems().get(j).isBorrowed());
                    if (user.getPurchaseHistory().get(i).getItems().get(j).isBorrowed())
                        book.put("borrowDays", user.getPurchaseHistory().get(i).getItems().get(j).getBorrowDays());
                    book.put("finalPrice", user.getPurchaseHistory().get(i).getItems().get(j).getFinalPrice());

                    books.add(book);

                }
            }
        }

        purchaseBooks.put("books", books);

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