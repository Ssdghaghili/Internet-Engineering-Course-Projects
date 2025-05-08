package org.example.service;

//import org.example.database.Database;
import org.example.exception.*;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Review;
import org.example.model.User;
import org.example.model.Customer;

import org.example.repository.BookRepository;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private CustomerRepository customerRepository;

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
        User userSess = userSession.getCurrentUser();

        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        if (userSess == null)
            throw new UnauthorizedException("User is not logged in");

        if (!(userSess instanceof Customer))
            throw new ForbiddenException("Only customers can access book content");

        Customer customer = customerRepository.findById(userSess.getId())
                .filter(u -> u instanceof Customer)
                .map(u -> (Customer) u)
                .orElseThrow(() -> new UnauthorizedException("Customer not found"));

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
        Pageable pageable = PageRequest.of(0, size);
        return bookRepository.findTopRatedBooks(pageable);
    }

    public List<Book> getNewReleases(int size) {
        Pageable pageable = PageRequest.of(0, size);
        return bookRepository.findNewReleasedBooks(pageable);
    }

    public List<Book> searchBooks(String title, String author, String genre, Integer year,
                                  Integer page, Integer size, String sortBy, String order) throws BadRequestException {
        if (size > MAX_PAGE_SIZE)
            size = MAX_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page - 1, size);


        if (sortBy.equals("rating")) {
            if (order.equals("desc"))
                return bookRepository.searchBooksSortedByRatingDesc(title, author, genre, year, pageable);
            else {
                return bookRepository.searchBooksSortedByRatingAsc(title, author, genre, year, pageable);
            }
        }
        else if (sortBy.equals("reviews")) {
            if (order.equals("desc"))
                return bookRepository.searchBooksSortedByReviewsDesc(title, author, genre, year, pageable);
            else {
                return bookRepository.searchBooksSortedByReviewsAsc(title, author, genre, year, pageable);
            }
        }
        else {
            throw new BadRequestException("SortBy value is invalid");
        }
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