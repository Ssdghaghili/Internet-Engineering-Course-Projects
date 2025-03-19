package org.example;

import org.example.handler.CommandHandler;
import org.example.model.DataInitializer;
import org.example.service.*;

import java.util.Scanner;

public class MioBookCLI {
    public static void main(String[] args) {
        UserSession userSession = new UserSession();
        AuthorService authorService = new AuthorService();
        BookService bookService = new BookService(authorService);
        UserService userService = new UserService(bookService, userSession);
        ReviewService reviewService = new ReviewService(bookService, userSession);
        AuthService authService = new AuthService(userService, userSession);

        DataInitializer dataInitializer = new DataInitializer(userService, authorService, bookService, reviewService);
        dataInitializer.initializeData();

        CommandHandler commandHandler = new CommandHandler(userSession, authorService, bookService,
                userService, reviewService, authService);

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