package org.example.service;

import org.example.model.Book;
import org.example.model.Review;
import org.example.model.User;

import java.time.LocalDateTime;

public class ReviewService {
    private UserService userService;
    private BookService bookService;

    public ReviewService(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    public void addReview(String username, String bookTitle, int rate, String comment) {
        User user = userService.findUserByUsername(username);

        if (user == null)
            throw new IllegalArgumentException("User not found.");

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

        Review newReview = new Review(user, rate, comment, LocalDateTime.now());
        book.addReview(newReview);
    }

    public boolean hasUserReviewedBook(Book book, User user) {
        return book.getReviews().stream()
                .anyMatch(r -> r.getUser().getUsername().equals(user.getUsername()));
    }
}
