package org.example.service;

//import org.example.database.Database;
import jakarta.transaction.Transactional;
import org.example.exception.*;
import org.example.model.*;

import org.example.repository.BookRepository;
import org.example.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.model.Customer;
import org.example.model.Admin;
import org.example.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserSession userSession;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    private Customer getCurrentCustomer() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();
        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (!(user instanceof Customer))
            throw new ForbiddenException("Only customers can perform this action");

        return (Customer) user;
    }

    @Transactional
    public void addCart(String bookTitle)
            throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {

        Book book = bookRepository.findByTitle(bookTitle).orElse(null);
        Customer customer = getCurrentCustomer();

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot add to cart");

        if (customer.getCart().size() >= 10)
            throw new BadRequestException("Cart cannot have more than 10 items");

        if (customer.hasBookInCart(book))
            throw new BadRequestException("Book is already in the cart");

        if (customer.isBookPurchased(book))
            throw new BadRequestException("Book is already purchased");

        customer.addCart(book);
        book.addBuy();
        bookRepository.save(book);
        cartItemRepository.save(new CartItem());
    }

    public void removeCart(String bookTitle)
            throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {
        Book book = bookService.findBookByTitle(bookTitle);
        Customer customer = getCurrentCustomer();

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot remove from cart");

        if (!customer.removeBookFromCart(book))
            throw new BadRequestException("Book is not in the user's cart");
    }

    public void addCredit(int amount)
            throws UnauthorizedException, ForbiddenException, BadRequestException {
        User user = userSession.getCurrentUser();
        Customer customer = getCurrentCustomer();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot add credit");

        if (amount < 100)
            throw new BadRequestException("Minimum deposit amount is 100 cents (1 dollar)");

        customer.setBalance(customer.getBalance() + amount);
        userRepository.save(customer);
    }

    public PurchaseReceipt purchaseCart()
            throws UnauthorizedException, ForbiddenException, BadRequestException {
        User user = userSession.getCurrentUser();
        Customer customer = getCurrentCustomer();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (!Objects.equals(user.getRole(), "customer"))
            throw new ForbiddenException("Only customers can purchase");

        if (customer.getCart().isEmpty())
            throw new BadRequestException("Cart cannot be empty");

        if (!customer.hasEnoughCreditForCart())
            throw new BadRequestException("User has not enough credit");

        return customer.purchaseCart();
    }

    @Transactional
    public void borrowBook(String bookTitle, int days)
            throws UnauthorizedException, NotFoundException, ForbiddenException, BadRequestException {
        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();
        Customer customer = getCurrentCustomer();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot add to cart");

        if (customer.getCart().size() >= 10)
            throw new BadRequestException("Cart cannot have more than 10 items");

        if (customer.hasBookInCart(book))
            throw new BadRequestException("Book is already in the cart");

        if (customer.isBookPurchased(book))
            throw new BadRequestException("Book is already purchased");

        if (days < 1 || days > 9)
            throw new BadRequestException("Borrow days should be between 1 and 9");

        customer.borrowBook(book, days);
    }

    @Transactional
    public Map<String, Object> showCart()
            throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();
        Customer customer = getCurrentCustomer();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot have a cart");

        Map<String, Object> userCart = new LinkedHashMap<>();

        userCart.put("totalCost", customer.calculateCartCost());
        userCart.put("items", customer.getCart());

        return userCart;
    }

    @Transactional
    public List<PurchaseRecord> showPurchaseHistory() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();
        Customer customer = getCurrentCustomer();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot have a purchase history");

        return customer.getPurchaseHistory();
    }

    @Transactional
    public List<CartItem> showPurchasedBooks() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();
        Customer customer = getCurrentCustomer();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("Admins cannot have purchased books");

        return customer.getCart();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}