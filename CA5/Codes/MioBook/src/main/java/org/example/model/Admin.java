package org.example.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {

    @OneToMany(mappedBy = "admin")
    private Set<Book> books = new HashSet<>();;

    @OneToMany(mappedBy = "admin")
    private Set<Author> authors = new HashSet<>();

    public Admin() {}

    public Admin(String username, String password, String email, Address address) {
        super(username, password, email, address);
    }

    public Set<Book> getBooks() { return books; }
    public Set<Author> getAuthors() { return authors; }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }
}
