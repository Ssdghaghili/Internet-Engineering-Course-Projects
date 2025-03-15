package org.example;

import org.example.handler.CommandHandler;
import org.example.model.DataInitializer;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.example.service.ReviewService;
import org.example.service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        AuthorService authorService = new AuthorService();
        BookService bookService = new BookService(authorService);
        UserService userService = new UserService(bookService);
        ReviewService reviewService = new ReviewService(userService, bookService);

        DataInitializer dataInitializer = new DataInitializer(userService, authorService, bookService, reviewService);
        dataInitializer.initializeData();


        CommandHandler commandHandler = new CommandHandler(authorService, bookService, userService, reviewService);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nEnter command: ");
            String commandLine = scanner.nextLine();

            if (commandLine.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            }
            String response = commandHandler.handleCommand(commandLine).toJson();
            System.out.println(response);
        }
    }
}