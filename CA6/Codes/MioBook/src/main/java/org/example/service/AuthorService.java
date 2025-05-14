package org.example.service;

import org.example.exception.*;
import org.example.model.Admin;
import org.example.model.Author;

import org.example.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;


@Service
public class AuthorService {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthorRepository authorRepository;

    public void addAuthor(String name, String penName, String nationality, LocalDate born, LocalDate died,
                          String imageLink, String token) throws
            DuplicateEntityException, ForbiddenException, UnauthorizedException {

        if (authorNameExists(name))
            throw new DuplicateEntityException("Author already exists");

        Admin admin = authService.validateAndGetAdmin(token);

        Author newAuthor = new Author(name, penName, nationality, born, died, imageLink);
        admin.addAuthor(newAuthor);

        authorRepository.save(newAuthor);
    }

    public Author showAuthorDetails(String name) throws NotFoundException {
        Author author = findAuthorByName(name);

        if (author == null)
            throw new NotFoundException("Author not found.");

        return author;
    }

    public Set<Author> getAuthorsByAdmin(String token) throws ForbiddenException, UnauthorizedException {
        Admin admin = authService.validateAndGetAdmin(token);
        return admin.getAuthors();
    }

    public Author findAuthorByName(String name) {
        return authorRepository.findByName(name).orElse(null);
    }

    public boolean authorNameExists(String name) {
        return authorRepository.findByName(name).isPresent();
    }
}