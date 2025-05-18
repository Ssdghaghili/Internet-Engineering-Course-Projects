package org.example.service;

//import org.example.database.Database;
import org.example.exception.*;
import org.example.model.*;

import org.example.repository.BookRepository;
import org.example.repository.GenreRepository;
import org.example.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.example.service.ServiceUtils.MAX_PAGE_SIZE;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthService authService;


    public void addBook(String title, String publisher, int year, int price, String synopsis, String content,
                        List<String> genreNames, String imageLink, String authorName)
            throws NotFoundException, DuplicateEntityException, ForbiddenException, UnauthorizedException {

        Admin admin = authService.validateAndGetAdmin();

        if (bookTitleExists(title))
            throw new DuplicateEntityException("Book already exists");

        Author author = authorService.findAuthorByName(authorName);
        if (author == null)
            throw new NotFoundException("Author not found");

        Set<Genre> genres = new HashSet<>();
        for (String name : genreNames) {
            Genre genre = genreRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException("Genre not found: " + name));
            genres.add(genre);
        }

        Book newBook = new Book(admin, author, title, publisher, year, price, synopsis, content, genres, imageLink);
        author.addBook(newBook);
        admin.addBook(newBook);

        bookRepository.save(newBook);
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

        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        Customer customer = authService.getLoggedInCustomer();

        if (!customer.isBookPurchased(book))
            throw new ForbiddenException("The book is not in your possession");

        Map<String, Object> bookContent = new LinkedHashMap<>();

        bookContent.put("title", book.getTitle());
        bookContent.put("author", book.getAuthor());
        bookContent.put("content", book.getContent());

        return bookContent;
    }

    public List<Review> getBookReviews(String title, Integer page, Integer size) throws NotFoundException {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new NotFoundException("Book not found");

        if (size > MAX_PAGE_SIZE)
            size = MAX_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page - 1, size);

        return reviewRepository.getBookReviews(title, pageable);
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

    public Set<Book> getBooksByAdmin() throws ForbiddenException, UnauthorizedException {
        Admin admin = authService.validateAndGetAdmin();
        return admin.getBooks();
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Book findBookByTitle(String title) {
        return bookRepository.findByTitle(title).orElse(null);
    }

    public boolean bookTitleExists(String title) {
        return bookRepository.findByTitle(title).isPresent();
    }
}