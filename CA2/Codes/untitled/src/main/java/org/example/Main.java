package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.model.User;
import org.example.ClassesManager.UserManager;

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

//        String jsonInput1 = """
//            {
//                "role": "customer",
//                "username": "test-name",
//                "password": "1234",
//                "email": "my.mail1@mail.com",
//                "address": {
//                    "country": "Iran",
//                    "city": "Karaj"
//                }
//            }
//        """;

        UserManager userManager = new UserManager();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert JSON to User object
            User newUser = objectMapper.readValue(jsonInput, User.class);

            // Add user after validation
            userManager.addUser(newUser);

            // Create JSON response
            ObjectNode successResponse = objectMapper.createObjectNode();
            successResponse.put("success", true);
            successResponse.put("message", "User added successfully.");



            // Print JSON response
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse));

//            User newUser1 = objectMapper.readValue(jsonInput1, User.class);
//            userManager.addUser(newUser1);

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