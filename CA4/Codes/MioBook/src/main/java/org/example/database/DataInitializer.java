package org.example.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.model.*;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DataInitializer {

    private static final String BASE_URL = "http://194.60.230.196:8000/";

    private Database db;
    private final ObjectMapper objectMapper;

    public DataInitializer(Database db) {
        this.db = db;
        this.objectMapper = new ObjectMapper();
    }

    public void initializeData() {
        fetchUsers();
        fetchAuthors();
        fetchBooks();
        fetchReviews();
    }

    private void fetchUsers() {
        try {
            String json = getJsonFromApi("Users");
            List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {});
            db.users.addAll(users);
        } catch (Exception e) {
            System.out.println("Failed to fetch users: " + e.getMessage());
        }
    }

    private void fetchAuthors() {
        try {
            String json = getJsonFromApi("Authors");
            List<Author> authors = objectMapper.readValue(json, new TypeReference<List<Author>>() {});
            db.authors.addAll(authors);
        } catch (Exception e) {
            System.out.println("Failed to fetch authors: " + e.getMessage());
        }
    }

    private void fetchBooks() {
        try {
            String json = getJsonFromApi("Books");
            List<Book> books = objectMapper.readValue(json, new TypeReference<List<Book>>() {});
            db.books.addAll(books);
        } catch (Exception e) {
            System.out.println("Failed to fetch books: " + e.getMessage());
        }
    }

    private void fetchReviews() {
        try {
            String json = getJsonFromApi("Reviews");
            List<Map<String, Object>> reviews = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Map<String, Object> review : reviews) {
                String username = (String) review.get("username");
                String bookTitle = (String) review.get("title");
                int rate = (int) review.get("rate");
                String comment = (String) review.get("comment");
                String dateString = (String) review.get("date");
                LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

                User user = findUserByUsername(db.users, username);
                Book book = findBookByTitle(db.books, bookTitle);
                Review newReview = new Review(user, rate, comment, dateTime);
                book.addReview(newReview);
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch reviews: " + e.getMessage());
        }
    }

    private String getJsonFromApi(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP error code: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();
        return content.toString();
    }

    User findUserByUsername(List<User> users, String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    Book findBookByTitle(List<Book> books, String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title))
                return book;
        }
        return null;
    }
}