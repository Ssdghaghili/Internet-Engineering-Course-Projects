package org.example.service;

import org.example.database.Database;
import org.example.exception.*;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Review;
import org.example.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.service.ServiceUtils.MAX_PAGE_SIZE;

@Service
public class BookService {
    @Autowired
    private Database db;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private UserSession userSession;

    public void addBook(Book book)
            throws NotFoundException, DuplicateEntityException {
        Author author = authorService.findAuthorByName(book.getAuthor());

        if (author == null)
            throw new NotFoundException("Author not found");

        if (bookTitleExists(book.getTitle()))
            throw new DuplicateEntityException("Book already exists");

        db.books.add(book);
    }

    public Book showBookDetails(String title) throws NotFoundException {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new NotFoundException("Book not found");

        return book;
    }

    public Map<String, Object> showBookContent(String title)
            throws NotFoundException, UnauthorizedException, ForbiddenException {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new NotFoundException("Book not found");

        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (!user.isBookPurchased(book))
            throw new ForbiddenException("The book is not in your possession");

        Map<String, Object> bookContent = new LinkedHashMap<>();

        bookContent.put("title", book.getTitle());
        bookContent.put("author",book.getAuthor());
        bookContent.put("content", book.getContent());

        return bookContent;
    }

    public List<Review> getBookReviews(String title, int page, int size) throws NotFoundException {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new NotFoundException("Book not found");

        if (size > MAX_PAGE_SIZE)
            size = MAX_PAGE_SIZE;

        return book.getReviews().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String title, String author, String genre, Integer startYear, Integer endYear,
                                  int page, int size, String sortBy, String order) throws BadRequestException {

        Stream<Book> booksStream = db.books.stream();

        if (title != null) {
            booksStream = booksStream.filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()));
        }

        if (author != null) {
            booksStream = booksStream.filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()));
        }

        if (genre != null) {
            booksStream = booksStream.filter(b -> b.getGenres().contains(genre));
        }

        if (startYear != null) {
            booksStream = booksStream.filter(b -> b.getYear() >= startYear);
        }

        if (endYear != null) {
            booksStream = booksStream.filter(b -> b.getYear() <= endYear);
        }

        Comparator<Book> comparator = switch (sortBy) {
            case "averageRating" -> Comparator.comparingDouble(Book::getAverageRate);
            case "ReviewsCount" -> Comparator.comparing(Book::getReviewsCount);
            default -> throw new BadRequestException("Invalid sortBy value");
        };

        if (order.equals("desc")) {
            comparator = comparator.reversed();
        }

        booksStream = booksStream.sorted(comparator);

        if (size > MAX_PAGE_SIZE)
            size = MAX_PAGE_SIZE;

        return booksStream.skip((long) page * size).limit(size).collect(Collectors.toList());
    }

    public double getBookAverageRating(String title) throws NotFoundException {
        Book book = findBookByTitle(title);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        return book.getAverageRate();
    }


    public Book findBookByTitle(String title) {
        for (Book book : db.books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public boolean bookTitleExists(String title) {
        return db.books.stream().anyMatch(b -> b.getTitle().equals(title));
    }
}
