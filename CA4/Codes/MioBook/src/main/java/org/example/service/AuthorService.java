package org.example.service;

import org.example.database.Database;
import org.example.exception.*;
import org.example.model.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private Database db;

    public void addAuthor(Author newAuthor) throws DuplicateEntityException {

        if (authorNameExists(newAuthor.getName()))
            throw new DuplicateEntityException("Author already exists");

        db.authors.add(newAuthor);
    }

    public Author showAuthorDetails(String name) throws NotFoundException {
        Author author = findAuthorByName(name);

        if (author == null)
            throw new NotFoundException("Author not found.");

        return author;
    }

    public Author findAuthorByName(String name) {
        for (Author author : db.authors) {
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }

    public boolean authorNameExists(String name) {
        return db.authors.stream().anyMatch(a -> a.getName().equals(name));
    }
}