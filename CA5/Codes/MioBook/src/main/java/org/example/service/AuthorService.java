package org.example.service;

import org.example.exception.*;
import org.example.model.Author;

import org.example.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public void addAuthor(Author newAuthor) throws DuplicateEntityException {
        if (authorNameExists(newAuthor.getName()))
            throw new DuplicateEntityException("Author already exists");

        authorRepository.save(newAuthor);
    }

    public Author showAuthorDetails(String name) throws NotFoundException {
        Author author = findAuthorByName(name);

        if (author == null)
            throw new NotFoundException("Author not found.");

        return author;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author findAuthorByName(String name) {
        return authorRepository.findByName(name).orElse(null);
    }

    public boolean authorNameExists(String name) {
        return authorRepository.findByName(name).isPresent();
    }
}