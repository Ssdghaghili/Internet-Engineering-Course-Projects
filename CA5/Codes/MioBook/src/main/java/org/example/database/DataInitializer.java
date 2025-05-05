package org.example.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import org.example.model.*;

import org.example.repository.UserRepository;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.CustomerRepository;
import org.example.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DataInitializer {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdminRepository adminRepository;


    private static final String BASE_URL = "http://194.60.231.242:8000/";

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    @PostConstruct
    public void initializeData() {
//        fetchUsers();
//        fetchAuthors();
//        fetchBooks();
//        fetchReviews();
    }

    private void fetchUsers() {
        try {
            String json = getJsonFromApi("Users");
            List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {});

            for (User user : users) {
                if (user instanceof Admin a) {
                    if (!adminRepository.existsByUsername(a.getUsername())) {
                        adminRepository.save(a);
                    }
                } else if (user instanceof Customer c) {
                    if (!customerRepository.existsByUsername(c.getUsername())) {
                        customerRepository.save(c);
                    }
                }
            }
            System.out.println("Fetched " + users.size() + " users");
        } catch (Exception e) {
            System.out.println("Failed to fetch users: " + e.getMessage());
        }
    }

    private void fetchAuthors() {
        try {
            String json = getJsonFromApi("Authors");
            List<Author> authors = objectMapper.readValue(json, new TypeReference<List<Author>>() {});

            for (Author author : authors) {
                Admin admin = adminRepository.findByUsername(author.getUsername());
                if (admin == null) {
                    throw new IllegalStateException("Admin with username '" + author.getUsername() + "' not found!");
                }
                author.setAdmin(admin);
            }

            authorRepository.saveAll(authors);
        } catch (Exception e) {
            System.out.println("Failed to fetch authors: " + e.getMessage());
        }
    }

    private void fetchBooks() {
        try {
            String json = getJsonFromApi("Books");

            List<Map<String, Object>> rawBooks = objectMapper.readValue(json, new TypeReference<>() {});

            List<Book> books = new ArrayList<>();

            for (Map<String, Object> rawBook : rawBooks) {
                Book book = new Book();

                book.setTitle((String) rawBook.get("title"));
                book.setPublisher((String) rawBook.get("publisher"));
                book.setYear((int) rawBook.get("year"));
                book.setPrice((int) rawBook.get("price"));
                book.setSynopsis((String) rawBook.get("synopsis"));
                book.setContent((String) rawBook.get("content"));


                String authorName = (String) rawBook.get("author");
                Author author = authorRepository.findByName(authorName).orElse(null);
                book.setAuthor(author);

                String adminUsername = (String) rawBook.get("username");
                Admin admin = adminRepository.findByUsername(adminUsername);
                book.setAdmin(admin);

                books.add(book);
            }
            bookRepository.saveAll(books);
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

                Optional<User> optionalUser = userRepository.findByUsername(username);
                Optional<Book> optionalBook = bookRepository.findByTitle(bookTitle);

                if (optionalUser.isPresent() && optionalBook.isPresent()) {
                    User user = optionalUser.get();
                    Book book = optionalBook.get();

                    Review newReview = new Review((Customer) user, book, rate, comment, dateTime);
                    reviewRepository.save(newReview);
                } else {
                    System.out.println("User or Book not found for review: " + username + ", " + bookTitle);
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