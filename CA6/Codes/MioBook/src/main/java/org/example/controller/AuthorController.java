package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.AuthorDTO;
import org.example.dto.DtoMapper;
import org.example.exception.DuplicateEntityException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.Admin;
import org.example.model.Author;
import org.example.request.AddAuthorRequest;
import org.example.response.Response;
import org.example.service.AuthService;
import org.example.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping("/add")
    public Response<Object> addAuthor(@Valid @RequestBody AddAuthorRequest addAuthorRequest,
                                      @RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException, DuplicateEntityException {

        authorService.addAuthor(addAuthorRequest.getName(), addAuthorRequest.getPenName(), addAuthorRequest.getNationality()
                , addAuthorRequest.getBorn(), addAuthorRequest.getDied(), addAuthorRequest.getImageLink(), token);
        return Response.ok("Author added successfully");
    }

    @GetMapping("/{name}")
    public Response<AuthorDTO> getAuthorDetails(@PathVariable String name) throws NotFoundException {
        Author author = authorService.showAuthorDetails(name);
        return Response.ok("Author details retrieved successfully", DtoMapper.authorToDTO(author));
    }

    @GetMapping("/all")
    public Response<List<AuthorDTO>> getAdminAuthors(@RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException {

        Set<Author> authors = authorService.getAuthorsByAdmin(token);
        return Response.ok("Authors retrieved successfully",
                authors.stream().map(DtoMapper::authorToDTO).collect(Collectors.toList()));
    }
}
