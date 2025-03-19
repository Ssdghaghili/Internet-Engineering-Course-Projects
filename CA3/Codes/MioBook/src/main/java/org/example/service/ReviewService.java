package org.example.service;

import org.example.model.Book;
import org.example.model.Review;
import org.example.model.User;

import java.time.LocalDateTime;

public class ReviewService {
    private BookService bookService;
    private UserSession  userSession;

    public ReviewService(BookService bookService, UserSession  userSession) {
        this.bookService = bookService;
        this.userSession = userSession;
    }

    public void addReview(String bookTitle, int rate, String comment, LocalDateTime dateTime) {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new IllegalArgumentException("User is not logged in.");

        if (user.getRole() != User.Role.customer)
            throw new IllegalArgumentException("Only customers can add reviews.");

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new IllegalArgumentException("Book doesn't exist");

        if (rate < 1 || rate > 5)
            throw new IllegalArgumentException("Rate should be between 1 and 5.");

        if (!user.isBookPurchased(book))
            throw new IllegalArgumentException("Only customers who have purchased the book can add reviews.");

        if (hasUserReviewedBook(book, user))
            book.removeReview(user);

        Review newReview = new Review(user, rate, comment, dateTime);
        book.addReview(newReview);
    }

    public void loadReview(User user, String bookTitle, int rate, String comment, LocalDateTime dateTime) {

        if (user == null)
            throw new IllegalArgumentException("User does not exist.");

        if (user.getRole() != User.Role.customer)
            throw new IllegalArgumentException("Only customers can add reviews.");

        Book book = bookService.findBookByTitle(bookTitle);

        if (book == null)
            throw new IllegalArgumentException("Book doesn't exist");

        if (rate < 1 || rate > 5)
            throw new IllegalArgumentException("Rate should be between 1 and 5.");

        if (hasUserReviewedBook(book, user))
            book.removeReview(user);

        Review newReview = new Review(user, rate, comment, dateTime);
        book.addReview(newReview);
    }

    public boolean hasUserReviewedBook(Book book, User user) {
        return book.getReviews().stream()
                .anyMatch(r -> r.getUser().getUsername().equals(user.getUsername()));
    }
}
