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


}
