package org.example.service;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.User;

import java.util.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookService {
    private List<Book> books;
    private AuthorService authorService;

    public BookService(AuthorService authorService) {
        books = new ArrayList<>();
        this.authorService = authorService;
    }


    public void addBook(Book book) {
        Author author = authorService.findAuthorByName(book.getAuthor());

        if (author == null)
            throw new IllegalArgumentException("Author doesn't exist.");

        if (bookTitleExists(book.getTitle()))
            throw new IllegalArgumentException("Book already exists.");

        books.add(book);
    }

    public Map<String, Object> showBookDetails(String title) {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new IllegalArgumentException("Book not found.");

        Map<String, Object> bookDetails = new LinkedHashMap<>();

        bookDetails.put("title", book.getTitle());
        bookDetails.put("author", book.getAuthor());
        bookDetails.put("publisher", book.getPublisher());
        bookDetails.put("genres", book.getGenres());
        bookDetails.put("year", book.getYear());
        bookDetails.put("price", book.getPrice());
        bookDetails.put("synopsis", book.getSynopsis());
        bookDetails.put("averageRating", book.getAverageRate());

        return bookDetails;
    }

    public Map<String, Object> showBookContent(String username, String bookTitle){
        Book book = findBookByTitle(bookTitle);
        if (book == null)
            throw new IllegalArgumentException("Book not found.");

//        User user = userService.findUserByUsername(username);
//        if (!user.hasBookInCart(book))   //book is not in user's possession
//            throw new IllegalArgumentException("The book is not in your possession.");

        Map<String, Object> bookContent = new LinkedHashMap<>();

        bookContent.put("title", book.getTitle());
        bookContent.put("content", book.getContent());

        return bookContent;
    }

    public  Map<String, Object> showBookReviews(String title) {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new IllegalArgumentException("Book not found.");

        Map<String, Object> bookReviews = new LinkedHashMap<>();

        bookReviews.put("title", book.getTitle());

        List<Map<String, Object>> reviews = new ArrayList<>();
        for (int i = 0; i < book.getReviews().size(); i++) {
            Map<String, Object> review = new LinkedHashMap<>();
            review.put("username", book.getReviews().get(i).getUser().getUsername());
            review.put("rate", book.getReviews().get(i).getRate());
            review.put("comment", book.getReviews().get(i).getComment());
            reviews.add(review);
        }

        bookReviews.put("reviews", reviews);
        bookReviews.put("averageRating", book.getAverageRate());

        return bookReviews;
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public boolean bookTitleExists(String title) {
        return books.stream().anyMatch(b -> b.getTitle().equals(title));
    }

    public List<Book> getBooks() {
        return books;
    }
}
