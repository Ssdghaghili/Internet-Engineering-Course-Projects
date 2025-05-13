package org.example.service;

import jakarta.transaction.Transactional;
import org.example.exception.BadRequestException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.Book;
import org.example.model.Customer;
import org.example.model.Review;
import org.example.model.User;

import org.example.repository.BookRepository;
import org.example.repository.CustomerRepository;
import org.example.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserSession  userSession;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void addReview(String bookTitle, int rate, String comment, LocalDateTime dateTime)
            throws UnauthorizedException, ForbiddenException, NotFoundException, BadRequestException {

        User userSess = userSession.getCurrentUser();
        if (userSess == null)
            throw new UnauthorizedException("User is not logged in");

        if (!(userSess instanceof Customer))
            throw new ForbiddenException("Only customers can add reviews");

        Customer customer = customerRepository.findById(userSess.getId())
                .filter(u -> u instanceof Customer)
                .map(u -> (Customer) u)
                .orElseThrow(() -> new UnauthorizedException("Customer not found"));

        Book book = bookRepository.findByTitle(bookTitle)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (book == null)
            throw new NotFoundException("Book not found");

        if (rate < 1 || rate > 5)
            throw new BadRequestException("Rate should be between 1 and 5");

        if (!customer.isBookPurchased(book))
            throw new BadRequestException("Only customers who have purchased the book can add reviews");

        if (reviewRepository.existsByBookAndCustomer(book, customer)) {
            reviewRepository.deleteByBookAndCustomer(book, customer);
        }

        Review newReview = new Review(customer, book, rate, comment, dateTime);
        book.addReview(newReview);

        reviewRepository.save(newReview);
    }
}