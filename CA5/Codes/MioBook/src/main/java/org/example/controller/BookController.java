package org.example.controller;

import jakarta.validation.Valid;

import org.example.dto.BookDTO;
import org.example.dto.DtoMapper;
import org.example.dto.ReviewDTO;
import org.example.exception.*;
import org.example.model.*;
import org.example.repository.GenreRepository;
import org.example.request.AddBookRequest;
import org.example.response.Response;
import org.example.service.AuthService;
import org.example.repository.AuthorRepository;

import org.example.service.BookService;
import org.example.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserSession userSession;

    @PostMapping("/add")
    public Response<Object> addBook(@Valid @RequestBody AddBookRequest addBookRequest)
            throws ForbiddenException, UnauthorizedException, NotFoundException, DuplicateEntityException {

        Admin admin = authService.validateAndGetAdmin();
        Author author = authorRepository.findByName(addBookRequest.getAuthor())
                .orElseThrow(() -> new NotFoundException("Author not found"));

        Set<Genre> genres = new HashSet<>();
        for (String name : addBookRequest.getGenres()) {
            Genre genre = genreRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException("Genre not found: " + name));
            genres.add(genre);
        }

        Book newBook = new Book(admin, author, addBookRequest.getTitle(), addBookRequest.getPublisher(),
                addBookRequest.getYear(), addBookRequest.getPrice(), addBookRequest.getSynopsis(),
                addBookRequest.getContent(), genres, addBookRequest.getImageLink());

        bookService.addBook(newBook);
        return Response.ok("Book added successfully.");
    }

    @GetMapping("/{title}")
    public Response<BookDTO> getBookDetails(@PathVariable String title) throws NotFoundException {
        Book book = bookService.showBookDetails(title);
        return Response.ok("Book details retrieved successfully.", DtoMapper.bookToDTO(book));
    }

    @GetMapping("/book/{title}/content")
    public Response<Object> getBookContent(@PathVariable String title)
            throws NotFoundException, ForbiddenException, UnauthorizedException {
        Map<String, Object> bookContent = bookService.showBookContent(title);
        return Response.ok("Book content retrieved successfully.", bookContent);
    }

    @GetMapping("/book/{title}/reviews")
    public Response<List<ReviewDTO>> getBookReviews(
            @PathVariable String title,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ) throws NotFoundException {
        List<Review> reviews = bookService.getBookReviews(title, page, size);
        return Response.ok("Book reviews retrieved successfully.",
                reviews.stream().map(DtoMapper::reviewToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/book/top-rated")
    public Response<List<BookDTO>> getTopRatedBooks(
            @RequestParam(required = false, defaultValue = "5") int size
    ) throws NotFoundException {
        List<Book> topRatedBooks = bookService.getTopRatedBooks(size);
        return Response.ok("Top-rated books retrieved successfully.",
                topRatedBooks.stream().map(DtoMapper::bookToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/book/new-releases")
    public Response<List<BookDTO>> getNewReleases(
            @RequestParam(required = false, defaultValue = "5") int size
    ) throws NotFoundException {
        List<Book> newReleases = bookService.getNewReleases(size);
        return Response.ok("New releases retrieved successfully.",
                newReleases.stream().map(DtoMapper::bookToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public Response<List<BookDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) throws BadRequestException {
        List<Book> books = bookService.searchBooks(title, author, genre, year, page, size, sortBy, order);
        return Response.ok("Search results retrieved successfully",
                books.stream().map(DtoMapper::bookToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/all")
    public Response<List<BookDTO>> getAllBooks() throws ForbiddenException, UnauthorizedException {
        authService.validateAdmin();
        List<Book> books = bookService.getAllBooks();
        return Response.ok("Books retrieved successfully",
                books.stream().map(DtoMapper::bookToDTO).collect(Collectors.toList()));
    }
}
