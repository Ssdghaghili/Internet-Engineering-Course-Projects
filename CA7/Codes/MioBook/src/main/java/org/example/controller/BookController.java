package org.example.controller;

import jakarta.validation.Valid;

import org.example.dto.BookDTO;
import org.example.dto.DtoMapper;
import org.example.dto.ReviewDTO;
import org.example.exception.*;
import org.example.model.*;
import org.example.request.AddBookRequest;
import org.example.response.Response;

import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;


    @PostMapping("/add")
    public Response<Object> addBook(@Valid @RequestBody AddBookRequest addBookRequest)
            throws ForbiddenException, UnauthorizedException, NotFoundException, DuplicateEntityException {

        bookService.addBook(addBookRequest.getTitle(), addBookRequest.getPublisher(), addBookRequest.getYear(),
                addBookRequest.getPrice(), addBookRequest.getSynopsis(), addBookRequest.getContent(),
                addBookRequest.getGenres(), addBookRequest.getImageLink(), addBookRequest.getAuthor());
        return Response.ok("Book added successfully");
    }

    @GetMapping("/{title}")
    public Response<BookDTO> getBookDetails(@PathVariable String title) throws NotFoundException {
        Book book = bookService.showBookDetails(title);
        return Response.ok("Book details retrieved successfully", DtoMapper.bookToDTO(book));
    }

    @GetMapping("/book/{title}/content")
    public Response<Object> getBookContent(@PathVariable String title)
            throws NotFoundException, ForbiddenException, UnauthorizedException {

        Map<String, Object> bookContent = bookService.showBookContent(title);
        return Response.ok("Book content retrieved successfully", bookContent);
    }

    @GetMapping("/book/{title}/reviews")
    public Response<List<ReviewDTO>> getBookReviews(
            @PathVariable String title,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ) throws NotFoundException {

        List<Review> reviews = bookService.getBookReviews(title, page, size);
        return Response.ok("Book reviews retrieved successfully",
                reviews.stream().map(DtoMapper::reviewToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/book/top-rated")
    public Response<List<BookDTO>> getTopRatedBooks(
            @RequestParam(required = false, defaultValue = "5") int size
    ) {

        List<Book> topRatedBooks = bookService.getTopRatedBooks(size);
        return Response.ok("Top-rated books retrieved successfully",
                topRatedBooks.stream().map(DtoMapper::bookToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/book/new-releases")
    public Response<List<BookDTO>> getNewReleases(
            @RequestParam(required = false, defaultValue = "5") int size
    ) {

        List<Book> newReleases = bookService.getNewReleases(size);
        return Response.ok("New releases retrieved successfully",
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
    public Response<List<BookDTO>> getAllAdminBooks()
            throws ForbiddenException, UnauthorizedException {

        Set<Book> books = bookService.getBooksByAdmin();
        return Response.ok("Books retrieved successfully",
                books.stream().map(DtoMapper::bookToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/genres")
    public Response<List<String>> getAllGenreNames() {
        List<Genre> genres = bookService.getAllGenres();
        return Response.ok("Genres retrieved successfully",
                genres.stream().map(Genre::getName).collect(Collectors.toList()));
    }
}
