package org.example.service;

//import org.example.database.Database;
import org.example.exception.*;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Review;
import org.example.model.User;
import org.example.model.Customer;

import org.example.repository.BookRepository;
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
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private UserSession userSession;

    public void addBook(Book book)
            throws NotFoundException, DuplicateEntityException {
        Author author = authorService.findAuthorByName(book.getAuthor().getName());

        if (author == null)
            throw new NotFoundException("Author not found");

        if (bookTitleExists(book.getTitle()))
            throw new DuplicateEntityException("Book already exists");

        bookRepository.save(book);
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

        if (!(user instanceof Customer customer))
            throw new UnauthorizedException("Only customers can access book content");

        if (!customer.isBookPurchased(book))
            throw new ForbiddenException("The book is not in your possession");

        Map<String, Object> bookContent = new LinkedHashMap<>();

        bookContent.put("title", book.getTitle());
        bookContent.put("author",book.getAuthor());
        bookContent.put("content", book.getContent());

        return bookContent;
    }

    public List<Review> getBookReviews(String title, Integer page, Integer size) throws NotFoundException {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new NotFoundException("Book not found");

        if (size > MAX_PAGE_SIZE)
            size = MAX_PAGE_SIZE;

        return book.getReviews().stream()
                .skip((long) (page-1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Book> getTopRatedBooks(int size) {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .sorted(Comparator.comparingDouble(Book::getAverageRate).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Book> getNewReleases(int size) {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getYear).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }


    public List<Book> searchBooks(String title, String author, String genre, Integer year,
                                  Integer page, Integer size, String sortBy, String order) throws BadRequestException {

        Stream<Book> booksStream = bookRepository.findAll().stream();

        if (title != null) {
            booksStream = booksStream.filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()));
        }

        if (author != null) {
            booksStream = booksStream.filter(b -> b.getAuthor().getName().toLowerCase().contains(author.toLowerCase()));
        }

        if (genre != null) {
            booksStream = booksStream.filter(b -> b.getGenres().contains(genre));
        }

        if (year != null) {
            booksStream = booksStream.filter(b -> b.getYear() == year);
        }

        Comparator<Book> comparator = switch (sortBy) {
            case "rating" -> Comparator.comparingDouble(Book::getAverageRate);
            case "reviews" -> Comparator.comparing(Book::getReviewsCount);
            default -> throw new BadRequestException("Invalid sortBy value");
        };

        if (order.equals("desc")) {
            comparator = comparator.reversed();
        }

        booksStream = booksStream.sorted(comparator);

        if (size > MAX_PAGE_SIZE)
            size = MAX_PAGE_SIZE;

        return booksStream.skip((long) (page-1) * size).limit(size).collect(Collectors.toList());
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public double getBookAverageRating(String title) throws NotFoundException {
        Book book = bookRepository.findByTitle(title)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        return book.getAverageRate();
    }

    public Book findBookByTitle(String title) {
        return bookRepository.findByTitle(title).orElse(null);
    }

    public boolean bookTitleExists(String title) {
        return bookRepository.findByTitle(title).isPresent();
    }
}