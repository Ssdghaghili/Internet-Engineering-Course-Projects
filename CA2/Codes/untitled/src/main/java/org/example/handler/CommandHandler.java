package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.model.*;
import org.example.response.Response;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.example.service.ReviewService;
import org.example.service.UserService;

import java.util.Map;

public class CommandHandler {
    private AuthorService authorService;
    private BookService bookService;
    private UserService userService;
    private ReviewService reviewService;
    private ObjectMapper objectMapper;

    public CommandHandler() {
        authorService = new AuthorService();
        bookService = new BookService(authorService);
        userService = new UserService(bookService);
        reviewService = new ReviewService(userService, bookService);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
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
                return handlePurchaseCart(inputJson);
            case "borrow_book":
                return handleBorrowBook(inputJson);
            case "add_review":
                return handleAddReview(inputJson);
            case "show_user_details":
                return handelShowUserDetails(inputJson);
            case "show_author_details":
                return handelShowAuthorDetails(inputJson);
            case "show_book_details":
                return handleShowBookDetails(inputJson);
            case "show_book_content":
                return handleShowBookContent(inputJson);
            case "show_book_reviews":
                return handelShowBookReviews(inputJson);
            case "show_cart":
                return handelShowCart(inputJson);
            case "show_purchase_history":
                return handleShowPurchaseHistory(inputJson);
            case "show_purchase_books":
                return handleShowPurchaseBooks(inputJson);
            case "search_books_by_title":
                return handleSearchBooksByTitle(inputJson);
            case "search_books_by_author":
                return handleSearchBooksByAuthor(inputJson);
            case "search_books_by_genre":
                return handleSearchBooksByGenre(inputJson);
            case "search_books_by_year":
                return handleSearchBooksByYear(inputJson);
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
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            Author newAuthor = objectMapper.readValue(jsonInput, Author.class);
            userService.validateAdmin(username);
            authorService.addAuthor(newAuthor);
            return new Response(true, "Author added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleAddBook(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            Book newBook = objectMapper.readValue(jsonInput, Book.class);
            userService.validateAdmin(username);
            bookService.addBook(newBook);
            return new Response(true, "Book added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    private Response handleAddCart(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String username = jsonNode.get("username").asText();
            String bookTitle = jsonNode.get("title").asText();
            userService.addCart(username, bookTitle);
            return new Response(true, "Added book to cart.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    private Response handleRemoveCart(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String username = jsonNode.get("username").asText();
            String bookTitle = jsonNode.get("title").asText();
            userService.removeCart(username, bookTitle);
            return new Response(true, "Removed book from cart.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleAddCredit(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String username = jsonNode.get("username").asText();
            int credit = jsonNode.get("credit").asInt();
            userService.addCredit(username, credit);
            return new Response(true, "Credit added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handlePurchaseCart(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            PurchaseReceipt purchaseReceipt = userService.purchaseCart(username);
            return new Response(true, "Purchase completed successfully.", purchaseReceipt);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleBorrowBook(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String username = jsonNode.get("username").asText();
            String bookTitle = jsonNode.get("title").asText();
            int days = jsonNode.get("days").asInt();
            userService.borrowBook(username, bookTitle, days);
            return new Response(true, "Added borrowed book to cart.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleAddReview(String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            String username = jsonNode.get("username").asText();
            String bookTitle = jsonNode.get("title").asText();
            int rate = jsonNode.get("rate").asInt();;
            String comment = jsonNode.get("comment").asText();
            reviewService.addReview(username, bookTitle, rate, comment);
            return new Response(true, "Review added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
                return Response.failure(e.getMessage());
        }
    }

    public Response handelShowUserDetails(String jsonInput) {
        try{
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            Map<String, Object> userDetails = userService.showUserDetails(username);
            return new Response(true, "User details retrieved successfully.", userDetails);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handelShowAuthorDetails(String jsonInput) {
        try {
            String authorName = objectMapper.readTree(jsonInput).get("username").asText();
            Author author = authorService.showAuthorDetails(authorName);
            return new Response(true, "Author details retrieved successfully.", author);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleShowBookDetails(String jsonInput) {
        try {
            String bookTitle = objectMapper.readTree(jsonInput).get("title").asText();
            Map<String, Object> bookDetails = bookService.showBookDetails(bookTitle);
            return new Response(true, "Book details retrieved successfully.", bookDetails);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleShowBookContent(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            String bookTitle = objectMapper.readTree(jsonInput).get("title").asText();
            Map<String, Object> bookContent = bookService.showBookContent(username,bookTitle);
            return new Response(true, "Book content retrieved successfully.", bookContent);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handelShowBookReviews(String jsonInput) {
        try {
            String bookTitle = objectMapper.readTree(jsonInput).get("title").asText();
            Map<String, Object> bookReviews = bookService.showBookReviews(bookTitle);
            return new Response(true, "Book reviews retrieved successfully.", bookReviews);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handelShowCart(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            Map<String, Object> cart = userService.showCart(username);
            return new Response(true, "Buy cart retrieved successfully.", cart);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleShowPurchaseHistory(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            Map<String, Object> purchaseHistory = userService.showPurchaseHistory(username);
            return new Response(true, "Purchase history retrieved successfully.", purchaseHistory);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleShowPurchaseBooks(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            Map<String, Object> purchaseBooks = userService.showPurchaseBooks(username);
            return new Response(true, "Purchase books retrieved successfully.", purchaseBooks);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleSearchBooksByTitle (String jsonInput) {
        try {
            String title = objectMapper.readTree(jsonInput).get("title").asText();
            Map<String, Object> searchResults = bookService.searchBooksByTitle(title);
            return new Response(true, "Books containing " + "'" + title + "'" + " in their title:", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleSearchBooksByAuthor(String jsonInput) {
        try {
            String author = objectMapper.readTree(jsonInput).get("name").asText();
            Map<String, Object> searchResults = bookService.searchBooksByAuthor(author);
            return new Response(true, "Books by " + "'" + author + "'" + ":", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleSearchBooksByGenre(String jsonInput) {
        try {
            String genre = objectMapper.readTree(jsonInput).get("genre").asText();
            Map<String, Object> searchResults = bookService.searchBooksByGenre(genre);
            return new Response(true, "Book in the " + "'" + genre + "'" + " genre:", searchResults);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
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
            return Response.failure(e.getMessage());
        }
    }

}
