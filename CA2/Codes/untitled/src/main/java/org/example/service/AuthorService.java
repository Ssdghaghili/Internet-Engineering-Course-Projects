package org.example.service;

import org.example.model.Author;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class AuthorService {
    private List<Author> authors = new ArrayList<>();
    private UserService userService;

    public AuthorService(UserService userService) {
        this.userService = userService;
    }

    public void addAuthor(Author newAuthor) {
        for (Author author : authors) {
            if (author.getName().equalsIgnoreCase(newAuthor.getName())) {
                throw new IllegalArgumentException("Author already exists!");
            }
        }
        if (ServiceUtils.validateUsername(newAuthor.getName()) && validateAdder(newAuthor.getAdderUserName())) {
            authors.add(newAuthor);
        }
    }

    private boolean validateAdder(String username) {
        User user = userService.findUserByUsername(username);
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

    public List<Author> getAuthors() {
        return authors;
    }
}