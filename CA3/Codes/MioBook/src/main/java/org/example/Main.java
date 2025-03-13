package org.example;

import org.example.handler.CommandHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();

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