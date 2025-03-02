package org.example.service;

import org.example.model.Author;
import org.example.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private List<Book> books;
    private AuthorService authorService;

    public BookService(AuthorService authorService) {
        books = new ArrayList<>();
        this.authorService = authorService;
    }

    public void addBook(Book book) {
        Author author = authorService.findAuthorByName(book.getAuthor());

        if (author == null)
            throw new IllegalArgumentException("Author doesn't exist.");

        if (bookTitleExists(book.getTitle()))
            throw new IllegalArgumentException("Book already exists.");

        books.add(book);
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public boolean bookTitleExists(String title) {
        return books.stream().anyMatch(b -> b.getTitle().equals(title));
    }

    public List<Book> getBooks() {
        return books;
    }
}
