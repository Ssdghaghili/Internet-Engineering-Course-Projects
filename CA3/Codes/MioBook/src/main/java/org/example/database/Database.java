package org.example.database;

import org.example.model.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Database {
    public List<User> users;
    public List<Book> books;
    public List<Author> authors;

    public Database() {
        users = new ArrayList<>();
        books = new ArrayList<>();
        authors = new ArrayList<>();
        new DataInitializer(this).initializeData();
    }
}
