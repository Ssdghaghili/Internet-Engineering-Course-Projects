package org.example.controller;

import jakarta.validation.Valid;
import org.example.exception.DuplicateEntityException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.Author;
import org.example.request.AddAuthorRequest;
import org.example.response.Response;
import org.example.service.AuthService;
import org.example.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthService authService;

    @PostMapping("/add")
    public Response<Object> addAuthor(@Valid @RequestBody AddAuthorRequest addAuthorRequest)
            throws ForbiddenException, UnauthorizedException, DuplicateEntityException {
        authService.validateAdmin();
        Author newAuthor = new Author(addAuthorRequest.getName(), addAuthorRequest.getPenName(),
                addAuthorRequest.getNationality(), addAuthorRequest.getBorn(), addAuthorRequest.getDied(), addAuthorRequest.getImageLink());
        authorService.addAuthor(newAuthor);
        return Response.ok("Author added successfully");
    }

    @GetMapping("/{name}")
    public Response<Author> getAuthorDetails(@PathVariable String name) throws NotFoundException {
        Author author = authorService.showAuthorDetails(name);
        return Response.ok("Author details retrieved successfully", author);
    }

    @GetMapping("/all")
    public Response<List<Author>> getAuthors() throws ForbiddenException, UnauthorizedException {
        authService.validateAdmin();
        List<Author> authors = authorService.getAllAuthors();
        return Response.ok("Authors retrieved successfully", authors);
    }
}
