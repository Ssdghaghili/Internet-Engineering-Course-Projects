package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.model.*;
import org.example.response.Response;
import org.example.service.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private UserSession userSession;
    private AuthorService authorService;
    private BookService bookService;
    private UserService userService;
    private ReviewService reviewService;
    private AuthService authService;
    private ObjectMapper objectMapper;

    public CommandHandler(UserSession userSession, AuthorService authorService, BookService bookService,
                          UserService userService, ReviewService reviewService, AuthService authService) {
        this.userSession = userSession;
        this.authorService = authorService;
        this.bookService = bookService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.authService = authService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    public Response handleCommand(String commandInput) {
        if (commandInput.isBlank())
            return Response.failure("Input cannot be empty.");
        String[] commandParts = commandInput.split(" ", 2);

        String command = commandParts[0];
        String inputJson = (commandParts.length > 1) ? commandParts[1] : null;

        switch (command) {
            case "add_user":
                return handleAddUser(inputJson);
            case "add_author":
                return handleAddAuthor(inputJson);
            case "add_book":
                return handleAddBook(inputJson);
            case "add_cart":
                return handleAddCart(inputJson);
            case "remove_cart":
                return handleRemoveCart(inputJson);
            case "add_credit":
                return handleAddCredit(inputJson);
            case "purchase_cart":
                return handlePurchaseCart();
            case "borrow_book":
                return handleBorrowBook(inputJson);
            case "add_review":
                return handleAddReview(inputJson);
            case "show_user_details":
                return handelShowUserDetails();
            case "show_author_details":
                return handelShowAuthorDetails(inputJson);
            case "show_book_details":
                return handleShowBookDetails(inputJson);
            case "show_book_content":
                return handleShowBookContent(inputJson);
            case "show_book_reviews":
                return handelShowBookReviews(inputJson);
            case "show_cart":
                return handelShowCart();
            case "show_purchase_history":
                return handleShowPurchaseHistory();
            case "show_purchased_books":
                return handleShowPurchasedBooks();
            case "search_books_by_title":
                return handleSearchBooksByTitle(inputJson);
            case "search_books_by_author":
                return handleSearchBooksByAuthor(inputJson);
            case "search_books_by_genre":
                return handleSearchBooksByGenre(inputJson);
            case "search_books_by_year":
                return handleSearchBooksByYear(inputJson);
            case "search_books":
                return handleSearchBooks(inputJson);
            case "login":
                return handleLogin(inputJson);
            case "logout":
                return handleLogout();
            default:
                return Response.failure("Command is invalid.");
        }
    }

    public static Response createFailureResponse(Exception e) {

        if (e instanceof JsonProcessingException) {
            return new Response(false, "JSON format is invalid.", e.getMessage());
        }

        return Response.failure(e.getMessage());
    }

    public Response handleAddUser(String jsonInput) {
        try {
            User newUser = objectMapper.readValue(jsonInput, User.class);
            userService.addUser(newUser);
            return new Response(true, "User added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleAddAuthor(String jsonInput) {
        try {
            authService.validateAdmin();
            Author newAuthor = objectMapper.readValue(jsonInput, Author.class);
            authorService.addAuthor(newAuthor);
            return new Response(true, "Author added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleLogin(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            String password = objectMapper.readTree(jsonInput).get("password").asText();
            authService.login(username, password);
            return new Response(true, "User logged in successfully.");
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleLogout() {
        try {
            authService.logout();
            return new Response(true, "User logged out successfully.");
        } catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleAddBook(String jsonInput) {
        try {
            Book newBook = objectMapper.readValue(jsonInput, Book.class);
            authService.validateAdmin();
            bookService.addBook(newBook);
            return new Response(true, "Book added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    private Response handleAddCart(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String bookTitle = jsonNode.get("title").asText();
            userService.addCart(bookTitle);
            return new Response(true, "Added book to cart.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    private Response handleRemoveCart(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String bookTitle = jsonNode.get("title").asText();
            userService.removeCart(bookTitle);
            return new Response(true, "Removed book from cart.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleAddCredit(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            int credit = jsonNode.get("credit").asInt();
            userService.addCredit(credit);
            return new Response(true, "Credit added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handlePurchaseCart() {
        try {
            PurchaseReceipt purchaseReceipt = userService.purchaseCart();
            return new Response(true, "Purchase completed successfully.", purchaseReceipt);
        }
        catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleBorrowBook(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String bookTitle = jsonNode.get("title").asText();
            int days = jsonNode.get("days").asInt();
            userService.borrowBook(bookTitle, days);
            return new Response(true, "Added borrowed book to cart.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleAddReview(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String bookTitle = jsonNode.get("title").asText();
            int rate = jsonNode.get("rate").asInt();;
            String comment = jsonNode.get("comment").asText();
            reviewService.addReview(bookTitle, rate, comment, LocalDateTime.now());
            return new Response(true, "Review added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
                return createFailureResponse(e);
        }
    }

    public Response handelShowUserDetails() {
        try{
            User user = userService.showUserDetails();
            return new Response(true, "User details retrieved successfully.", user);
        }
        catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handelShowAuthorDetails(String jsonInput) {
        try {
            String authorName = objectMapper.readTree(jsonInput).get("username").asText();
            Author author = authorService.showAuthorDetails(authorName);
            return new Response(true, "Author details retrieved successfully.", author);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleShowBookDetails(String jsonInput) {
        try {
            String bookTitle = objectMapper.readTree(jsonInput).get("title").asText();
            Book book = bookService.showBookDetails(bookTitle);
            return new Response(true, "Book details retrieved successfully.", book);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleShowBookContent(String jsonInput) {
        try {
            String bookTitle = objectMapper.readTree(jsonInput).get("title").asText();
            Map<String, Object> bookContent = userService.showBookContent(bookTitle);
            return new Response(true, "Book content retrieved successfully.", bookContent);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handelShowBookReviews(String query) {
        try {
            Map<String, String> queryParams = parseQueryParams(query);
            String bookTitle = queryParams.get("title");

            if (bookTitle == null || bookTitle.isEmpty())
                throw new IllegalArgumentException("Book title is required.");

            Map<String, Object> bookReviews = bookService.showBookReviews(bookTitle, queryParams);
            return new Response(true, "Book reviews retrieved successfully.", bookReviews);
        } catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handelShowCart() {
        try {
            Map<String, Object> cart = userService.showCart();
            return new Response(true, "Buy cart retrieved successfully.", cart);
        }
        catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleShowPurchaseHistory() {
        try {
            Map<String, Object> purchaseHistory = userService.showPurchaseHistory();
            return new Response(true, "Purchase history retrieved successfully.", purchaseHistory);
        }
        catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleShowPurchasedBooks() {
        try {
            Map<String, Object> purchaseBooks = userService.showPurchasedBooks();
            return new Response(true, "Purchase books retrieved successfully.", purchaseBooks);
        }
        catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleSearchBooksByTitle (String jsonInput) {
        try {
            String title = objectMapper.readTree(jsonInput).get("title").asText();
            Map<String, Object> searchResults = bookService.searchBooksByTitle(title);
            return new Response(true, "Books containing " + "'" + title + "'" + " in their title:", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleSearchBooksByAuthor(String jsonInput) {
        try {
            String author = objectMapper.readTree(jsonInput).get("name").asText();
            Map<String, Object> searchResults = bookService.searchBooksByAuthor(author);
            return new Response(true, "Books by " + "'" + author + "'" + ":", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleSearchBooksByGenre(String jsonInput) {
        try {
            String genre = objectMapper.readTree(jsonInput).get("genre").asText();
            Map<String, Object> searchResults = bookService.searchBooksByGenre(genre);
            return new Response(true, "Books in the " + "'" + genre + "'" + " genre:", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleSearchBooksByYear(String jsonInput) {
        try {
            int start = objectMapper.readTree(jsonInput).get("from").asInt();
            int end = objectMapper.readTree(jsonInput).get("to").asInt();
            Map<String, Object> searchResults = bookService.searchBooksByYear(start, end);
            return new Response(true, "Books published from " + start + " to " + end + ":", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return createFailureResponse(e);
        }
    }

    public Response handleSearchBooks(String query) {
        try {
            Map<String, String> queryParams = parseQueryParams(query);
            Map<String, Object> searchResults = bookService.searchBooks(queryParams);
            return new Response(true, "Search results:", searchResults);
        }
        catch (IllegalArgumentException e) {
            return createFailureResponse(e);
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query == null || query.trim().isEmpty()) return queryParams;

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }

}
