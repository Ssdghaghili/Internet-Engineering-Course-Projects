package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.model.Author;
import org.example.model.User;
import org.example.model.Address;
import org.example.ClassesManager.UserManager;
import org.example.ClassesManager.AuthorManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        String jsonInput = """
            {
                "role": "admin",
                "username": "test-name",
                "password": "1234",
                "email": "my.mail@mail.com",
                "address": {
                    "country": "Iran",
                    "city": "Karaj"
                }
            }
        """;

        String jsonInput1 = """
            {
                "username": "test-name",
                "name": "author-name",
                "penName": "abc",
                "born": "1990-01-01"
            }
        """;


        UserManager userManager = new UserManager();
        AuthorManager authorManager = new AuthorManager(userManager);
        ObjectMapper objectMapper = new ObjectMapper();


        try {
            // Convert JSON to User object
            User newUser = objectMapper.readValue(jsonInput, User.class);
            Author newAuthor = objectMapper.readValue(jsonInput1, Author.class);

            // Add user after validation
            userManager.addUser(newUser);


            // Create JSON response
            ObjectNode successResponse = objectMapper.createObjectNode();
            successResponse.put("success", true);
            successResponse.put("message", "User added successfully.");

            // Print JSON response
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse));

            authorManager.addAuthor(newAuthor);

            // Add credit to user
            String input = """
             {
               "username": "test-name",
                  "credit": 8200
             }
             """;

            System.out.println(userManager.addCredit(input));

        } catch (Exception e) {
            // Create JSON error response
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            // Print JSON error response
            System.out.println(errorResponse.toPrettyString());
        }
    }
}