package org.example.ClassesManager;

import org.example.model.Author;
import org.example.model.User;
import org.example.ClassesManager.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class AuthorManager {
    private List<Author> authors = new ArrayList<>();
    private UserManager userManager;  // Instance of UserManager


    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public AuthorManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void addAuthor(Author newAuthor) {
        for (Author author : authors) {
            if (author.getName().equalsIgnoreCase(newAuthor.getName())) {
                throw new IllegalArgumentException("Author already exists!");
            }
        }
        if (validateName(newAuthor.getName()) && validateAdder(newAuthor.getAdderUserName())) {
            authors.add(newAuthor);
        }
    }

    public List<Author> getUsers() {
        return authors;
    }

    private boolean validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else if (!USERNAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must contain only letters, numbers, underscores, and hyphens");
        } else {
            return true;
        }
    }

    private boolean validateAdder(String username) {
        User user = userManager.findUserByUsername(username);
        System.out.println(user);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        if (user.getRole() == User.Role.admin) {
            return true;
        } else {
            throw new IllegalArgumentException("Only Admin can add authors!");
        }
    }
}