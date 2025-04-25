package org.example.service;

import org.example.database.Database;
import org.example.exception.*;
import org.example.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private Database db;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserSession userSession;

    public void addCart(String bookTitle)
            throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() == User.Role.admin)
            throw new ForbiddenException("Admins cannot add to cart");

        if (user.getCart().size() >= 10)
            throw new BadRequestException("Cart cannot have more than 10 items");

        if (user.hasBookInCart(book))
            throw new BadRequestException("Book is already in the cart");

        if (user.isBookPurchased(book))
            throw new BadRequestException("Book is already purchased");

        user.addCart(book);
        book.addBuy();
    }

    public void removeCart(String bookTitle)
            throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() == User.Role.admin)
            throw new ForbiddenException("Admins cannot remove from cart");

        if (!user.removeBookFromCart(book))
            throw new BadRequestException("Book is not in the user's cart");
    }

    public void addCredit(int amount)
            throws UnauthorizedException, ForbiddenException, BadRequestException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() != User.Role.customer)
            throw new ForbiddenException("Admins cannot add credit");

        if (amount < 100)
            throw new BadRequestException("Minimum deposit amount is 100 cents (1 dollar)");

        user.setBalance(user.getBalance() + amount);
    }

    public PurchaseReceipt purchaseCart()
            throws UnauthorizedException, ForbiddenException, BadRequestException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() != User.Role.customer)
            throw new ForbiddenException("Only customers can purchase");

        if (user.getCart().isEmpty())
            throw new BadRequestException("Cart cannot be empty");

        if (!user.hasEnoughCreditForCart())
            throw new BadRequestException("User has not enough credit");

        return user.purchaseCart();
    }

    public void borrowBook(String bookTitle, int days)
            throws UnauthorizedException, NotFoundException, ForbiddenException, BadRequestException {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() == User.Role.admin)
            throw new ForbiddenException("Admins cannot add to cart");

        if (user.getCart().size() >= 10)
            throw new BadRequestException("Cart cannot have more than 10 items");

        if (user.hasBookInCart(book))
            throw new BadRequestException("Book is already in the cart");

        if (user.isBookPurchased(book))
            throw new BadRequestException("Book is already purchased");

        if (days < 1 || days > 9)
            throw new BadRequestException("Borrow days should be between 1 and 9");

        user.borrowBook(book, days);
    }

    public Map<String, Object> showCart()
            throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() == User.Role.admin)
            throw new ForbiddenException("Admins cannot have a cart");

        Map<String, Object> userCart = new LinkedHashMap<>();

        userCart.put("totalCost", user.calculateCartCost());
        userCart.put("items", user.getCart());

        return userCart;
    }

    public List<PurchaseRecord> showPurchaseHistory() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() == User.Role.admin)
            throw new ForbiddenException("Admins cannot have a purchase history");

        return user.getPurchaseHistory();
    }

    public List<CartItem> showPurchasedBooks() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (user.getRole() == User.Role.admin)
            throw new ForbiddenException("Admins cannot have purchased books");

        return user.getPurchasedBooks();
    }

    public User findUserByUsername(String username) {
        for (User user : db.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        return db.users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean emailExists(String email) {
        return db.users.stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public List<User> getUsers() {
        return db.users;
    }
}