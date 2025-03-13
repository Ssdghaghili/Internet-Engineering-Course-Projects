package org.example.service;

import org.example.model.Author;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class AuthorService {
    private List<Author> authors;

    public AuthorService() {
        authors = new ArrayList<>();
    }

    public void addAuthor(Author newAuthor) {

        if (authorNameExists(newAuthor.getName()))
            throw new IllegalArgumentException("Author already exists.");

        authors.add(newAuthor);
    }

    public Author showAuthorDetails(String name) {
        Author author = findAuthorByName(name);

        if (author == null)
            throw new IllegalArgumentException("Author not found.");

        return author;
    }

    public Author findAuthorByName(String name) {
        for (Author author : authors) {
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }

    public boolean authorNameExists(String name) {
        return authors.stream().anyMatch(a -> a.getName().equals(name));
    }

    public List<Author> getAuthors() {
        return authors;
    }
}