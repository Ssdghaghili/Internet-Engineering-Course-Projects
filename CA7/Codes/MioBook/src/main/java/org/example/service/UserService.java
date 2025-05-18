package org.example.service;

import jakarta.transaction.Transactional;
import org.example.exception.*;
import org.example.model.*;

import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.model.Customer;
import org.example.repository.UserRepository;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private AuthService authService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    @Transactional
    public void addCart(String bookTitle, String token)
            throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        Customer customer = authService.getLoggedInCustomer(token);

        if (customer.getCart().size() >= 10)
            throw new BadRequestException("Cart cannot have more than 10 items");

        if (customer.hasBookInCart(book))
            throw new BadRequestException("Book is already in the cart");

        if (customer.isBookPurchased(book))
            throw new BadRequestException("Book is already purchased");

        customer.addCart(book);
    }

    @Transactional
    public void removeCart(String bookTitle, String token)
            throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        Customer customer = authService.getLoggedInCustomer(token);

        if (!customer.removeBookFromCart(book))
            throw new BadRequestException("Book is not in the your cart");
    }

    @Transactional
    public Customer addCredit(int amount, String token)
            throws UnauthorizedException, ForbiddenException, BadRequestException {

        Customer customer = authService.getLoggedInCustomer(token);

        if (amount < 100)
            throw new BadRequestException("Minimum deposit amount is 100 cents (1 dollar)");

        customer.increaseAmount(amount);
        userRepository.save(customer);
        return customer;
    }

    @Transactional
    public PurchaseReceipt purchaseCart(String token)
            throws UnauthorizedException, ForbiddenException, BadRequestException {

        Customer customer = authService.getLoggedInCustomer(token);

        if (customer.getCart().isEmpty())
            throw new BadRequestException("Cart cannot be empty");

        if (!customer.hasEnoughCreditForCart())
            throw new BadRequestException("User has not enough credit");

        for (CartItem cartItem : customer.getCart()) {
            Book book = cartItem.getBook();
            book.addBuy();
            bookRepository.save(book);
        }

        return customer.purchaseCart();
    }

    @Transactional
    public void borrowBook(String bookTitle, int days, String token)
            throws UnauthorizedException, NotFoundException, ForbiddenException, BadRequestException {

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        Customer customer = authService.getLoggedInCustomer(token);

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
    public Cart showCart(String token)
            throws UnauthorizedException, ForbiddenException {

        Customer customer = authService.getLoggedInCustomer(token);

        return new Cart(customer.calculateCartCost(), customer.getCart());
    }

    @Transactional
    public List<PurchaseRecord> showPurchaseHistory(String token) throws UnauthorizedException, ForbiddenException {

        Customer customer = authService.getLoggedInCustomer(token);

        return customer.getPurchaseHistory();
    }

    @Transactional
    public List<PurchaseItem> showPurchasedBooks(String token) throws UnauthorizedException, ForbiddenException {

        Customer customer = authService.getLoggedInCustomer(token);

        return customer.getPurchasedBooks();
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
}