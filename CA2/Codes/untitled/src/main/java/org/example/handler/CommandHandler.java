package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.model.Author;
import org.example.model.User;
import org.example.response.Response;
import org.example.service.AuthorService;
import org.example.service.UserService;

public class CommandHandler {
    private AuthorService authorService;
    private UserService userService;
    private ObjectMapper objectMapper;

    public CommandHandler() {
        userService = new UserService();
        authorService = new AuthorService(userService);
        objectMapper = new ObjectMapper();
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
            case "add_credit":
                return handleAddCredit(inputJson);
            default:
                return Response.failure("Command is invalid.");
        }
    }

    public static Response createFailureResponse(Exception e) {
        String message;

        if (e instanceof JsonProcessingException) {
            message = "JSON format is invalid.";
        } else {
            message = e.getMessage();
        }

        return Response.failure(message);
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
            Author newAuthor = objectMapper.readValue(jsonInput, Author.class);
            authorService.addAuthor(newAuthor);
            return new Response(true, "Author added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }

    public Response handleAddCredit(String jsonInput) {
        try {
            String username = objectMapper.readTree(jsonInput).get("username").asText();
            int credit = objectMapper.readTree(jsonInput).get("credit").asInt();
            userService.addCredit(username, credit);
            return new Response(true, "Author added successfully.");
        }
        catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.failure(e.getMessage());
        }
    }
}
