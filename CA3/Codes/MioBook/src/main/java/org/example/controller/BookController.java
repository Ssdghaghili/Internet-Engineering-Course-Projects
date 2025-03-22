package org.example.controller;

import jakarta.validation.Valid;

import org.example.exception.*;
import org.example.model.Book;
import org.example.model.Review;
import org.example.request.AddBookRequest;
import org.example.response.Response;
import org.example.service.AuthService;

import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private AuthService authService;

    @PostMapping("/add")
    public Response<Object> addBook(@Valid @RequestBody AddBookRequest addBookRequest)
            throws ForbiddenException, UnauthorizedException, NotFoundException, DuplicateEntityException {
        authService.validateAdmin();
        Book newBook = new Book(addBookRequest.getTitle(), addBookRequest.getAuthor(), addBookRequest.getPublisher(),
                addBookRequest.getYear(), addBookRequest.getPrice(), addBookRequest.getSynopsis(),
                addBookRequest.getContent(), addBookRequest.getGenres());
        bookService.addBook(newBook);
        return Response.ok("Book added successfully.");
    }

    @GetMapping("/{title}")
    public Response<Book> getBookDetails(@PathVariable String title) throws NotFoundException {
        Book book = bookService.showBookDetails(title);
        return Response.ok("Book details retrieved successfully.", book);
    }

    @GetMapping("/book/{title}/content")
    public Response<Object> getBookContent(@PathVariable String title)
            throws NotFoundException, ForbiddenException, UnauthorizedException {
        Map<String, Object> bookContent = bookService.showBookContent(title);
        return Response.ok("Book content retrieved successfully.", bookContent);
    }

    @GetMapping("/book/{title}/reviews")
    public Response<List<Review>> getBookReviews(
            @PathVariable String title,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) throws NotFoundException, ForbiddenException, UnauthorizedException {
        List<Review> reviews = bookService.getBookReviews(title, page, size);
        return Response.ok("Book reviews retrieved successfully.", reviews);
    }

    @GetMapping("/search")
    public Response<List<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(defaultValue = "averageScore") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) throws BadRequestException {
        List<Book> books = bookService.searchBooks(title, author, genre, startYear, endYear, page, size, sortBy, order);
        return Response.ok("Search results retrieved successfully", books);
    }
}
