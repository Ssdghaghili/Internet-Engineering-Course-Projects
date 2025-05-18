package org.example.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import org.example.model.*;

import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.example.utils.PasswordHasher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.database.utils.BASE_URL;
import static org.example.database.utils.genreList;

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
    @Autowired
    private GenreRepository genreRepository;

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    @PostConstruct
    public void initializeData() {
//        fetchGenres();
        fetchUsers();
//        fetchAuthors();
//        fetchBooks();
//        fetchReviews();
    }

    private void fetchGenres() {
        for (String genreName : genreList) {
            Genre genre = genreRepository.findByName(genreName).orElse(null);
            if (genre == null) {
                Genre newGenre = new Genre(genreName);
                genreRepository.save(newGenre);
            }
        }
    }

    private void fetchUsers() {
        try {
            String json = getJsonFromApi("Users");
            JsonNode root = objectMapper.readTree(json);

            for (JsonNode userNode : root) {
                String rawPassword = userNode.get("password").asText();
                String salt = PasswordHasher.generateSalt();
                String hashedPassword = PasswordHasher.hashPassword(rawPassword, salt);

                String username = userNode.get("username").asText();
                String email = userNode.get("email").asText();
                String country = userNode.get("address").get("country").asText();
                String city = userNode.get("address").get("city").asText();

                Address address = new Address(country, city);

                if (userNode.get("role").asText().equals("customer")) {
                    Customer customer =customerRepository.findByUsername(username);
                    customer.setPassword(hashedPassword);
                    customer.setSalt(salt);

                    //Customer newCustomer = new Customer(username, hashedPassword, email, address);
                    customerRepository.save(customer);
                } else {
                    Admin admin = adminRepository.findByUsername(username);
                    admin.setPassword(hashedPassword);
                    admin.setSalt(salt);

                    //Admin newAdmin = new Admin(username, hashedPassword, email, address);
                    adminRepository.save(admin);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch users: " + e.getMessage());
        }
    }

    private void fetchAuthors() {
        try {
            String json = getJsonFromApi("Authors");
            List<Author> authors = new ArrayList<>();
            JsonNode root = objectMapper.readTree(json);

            for (JsonNode authorNode : root) {
                String adminUsername = authorNode.get("username").asText();
                Author author = new Author();

                author.setName(authorNode.get("name").asText());
                author.setPenName(authorNode.get("penName").asText());
                author.setBorn(LocalDate.parse(authorNode.get("born").asText()));
                author.setNationality(authorNode.get("nationality").asText());

                LocalDate died = null;
                if (authorNode.has("died")) {
                    died = LocalDate.parse(authorNode.get("died").asText());
                }
                author.setDied(died);

                Admin admin = adminRepository.findByUsername(adminUsername);
                if (admin == null) {
                    throw new IllegalStateException("Admin with username '" + adminUsername + "' not found!");
                }
                author.setAdmin(admin);
                authors.add(author);
            }

            authorRepository.saveAll(authors);
        } catch (Exception e) {
            System.out.println("Failed to fetch authors: " + e.getMessage());
        }
    }

    private void fetchBooks() {
        try {
            String json = getJsonFromApi("Books");
            JsonNode root = objectMapper.readTree(json);
            List<Book> books = new ArrayList<>();

            for (JsonNode bookNode : root) {
                Book book = new Book();

                book.setTitle(bookNode.get("title").asText());
                book.setPublisher(bookNode.get("publisher").asText());
                book.setYear(bookNode.get("year").asInt());
                book.setPrice(bookNode.get("price").asInt());
                book.setSynopsis(bookNode.get("synopsis").asText());
                book.setContent(bookNode.get("content").asText());

                JsonNode genres = bookNode.get("genres");
                for (JsonNode g : genres) {
                    Genre genre = genreRepository.findByName(g.asText()).orElse(null);
                    if (genre == null) {
                        genre = new Genre(g.asText());
                        genreRepository.save(genre);
                    }
                    book.addGenre(genre);
                }

                String authorName = bookNode.get("author").asText();
                Author author = authorRepository.findByName(authorName).orElse(null);
                book.setAuthor(author);

                String adminUsername = bookNode.get("username").asText();
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

                    if (!reviewRepository.existsByBookAndCustomer(book, (Customer) user)) {
                        reviewRepository.save(newReview);
                    }

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