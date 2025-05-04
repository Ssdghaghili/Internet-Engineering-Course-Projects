package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.Book;
import org.example.model.Customer;
import org.example.model.Review;
import org.example.model.User;

import org.example.repository.BookRepository;
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

    public void addReview(String bookTitle, int rate, String comment, LocalDateTime dateTime)
            throws UnauthorizedException, ForbiddenException, NotFoundException, BadRequestException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (!Objects.equals(user.getRole(), "customer"))
            throw new ForbiddenException("Only customers can add reviews");

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        if (rate < 1 || rate > 5)
            throw new BadRequestException("Rate should be between 1 and 5");

        Customer customer = (Customer) user;

        if (!customer.isBookPurchased(book))
            throw new BadRequestException("Only customers who have purchased the book can add reviews");


        reviewRepository.deleteByBookAndCustomer(book, customer);

        Review newReview = new Review(customer, book, rate, comment, dateTime);
        //book.addReview(newReview);

        reviewRepository.save(newReview);
    }

    public void loadReview(User user, String bookTitle, int rate, String comment, LocalDateTime dateTime)
            throws UnauthorizedException, ForbiddenException, NotFoundException, BadRequestException {

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (!Objects.equals(user.getRole(), "customer"))
            throw new ForbiddenException("Only customers can add reviews");

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new NotFoundException("Book not found");

        if (rate < 1 || rate > 5)
            throw new BadRequestException("Rate should be between 1 and 5");

        Customer customer = (Customer) user;

        reviewRepository.deleteByBookAndCustomer(book, customer);

        Review newReview = new Review(customer, book, rate, comment, dateTime);
        reviewRepository.save(newReview);
    }

    public boolean hasUserReviewedBook(Book book, Customer customer) {
        return reviewRepository.existsByBookAndCustomer(book, customer);
    }
}
