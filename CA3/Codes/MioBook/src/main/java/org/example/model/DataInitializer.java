package org.example.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.*;

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

    private final UserService userService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final ReviewService reviewService;

    private final ObjectMapper objectMapper;

    public DataInitializer(UserService userService, AuthorService authorService, BookService bookService, ReviewService reviewService) {
        this.userService = userService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.reviewService = reviewService;
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
            users.forEach(userService::addUser);
            //System.out.println("Loaded " + users.size() + " users.");
        } catch (Exception e) {
            System.out.println("Failed to fetch users: " + e.getMessage());
        }
    }

    private void fetchAuthors() {
        try {
            String json = getJsonFromApi("Authors");
            List<Author> authors = objectMapper.readValue(json, new TypeReference<List<Author>>() {});
            authors.forEach(authorService::addAuthor);
            //System.out.println("Loaded " + authors.size() + " authors.");
        } catch (Exception e) {
            System.out.println("Failed to fetch authors: " + e.getMessage());
        }
    }

    private void fetchBooks() {
        try {
            String json = getJsonFromApi("Books");
            List<Book> books = objectMapper.readValue(json, new TypeReference<List<Book>>() {});
            books.forEach(bookService::addBook);
            //System.out.println("Loaded " + books.size() + " books.");
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
                try {
                    String username = (String) review.get("username");
                    String bookTitle = (String) review.get("title");
                    int rate = (int) review.get("rate");
                    String comment = (String) review.get("comment");
                    String dateString = (String) review.get("date");
                    LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

                    User user =userService.findUserByUsername(username);
                    reviewService.loadReview(user, bookTitle, rate, comment, dateTime);

                    //System.out.println("Added review for book: " + bookTitle + " by user: " + username);
                } catch (IllegalArgumentException e) {
                    System.out.println("Failed to add review: " + e.getMessage());
                }
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
}