package org.example.service;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Review;
import org.example.model.User;

import java.util.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> showBookDetails(String title) {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new IllegalArgumentException("Book not found.");

        Map<String, Object> bookDetails = new LinkedHashMap<>();

        bookDetails.put("title", book.getTitle());
        bookDetails.put("author", book.getAuthor());
        bookDetails.put("publisher", book.getPublisher());
        bookDetails.put("genres", book.getGenres());
        bookDetails.put("year", book.getYear());
        bookDetails.put("price", book.getPrice());
        bookDetails.put("synopsis", book.getSynopsis());
        bookDetails.put("averageRating", book.getAverageRate());

        return bookDetails;
    }

    public  Map<String, Object> showBookReviews(String title, Map<String, String> queryParams) {
        Book book = findBookByTitle(title);

        if (book == null)
            throw new IllegalArgumentException("Book not found.");

        Map<String, Object> bookReviews = new LinkedHashMap<>();
        bookReviews.put("title", book.getTitle());

        List<Map<String, Object>> reviews = paginateReviews(book.getReviews(), queryParams);
        bookReviews.put("reviews", reviews);
        bookReviews.put("averageRating", book.getAverageRate());

        return bookReviews;
    }

    private List<Map<String, Object>> paginateReviews(List<Review> allReviews, Map<String, String> queryParams) {
        int page = queryParams.containsKey("page") ? Integer.parseInt(queryParams.get("page")) : 1;
        int limit = queryParams.containsKey("limit") ? Integer.parseInt(queryParams.get("limit")) : 10;
        limit = Math.min(limit, 50);

        int totalReviews = allReviews.size();
        int fromIndex = Math.max((page - 1) * limit, 0);
        int toIndex = Math.min(fromIndex + limit, totalReviews);

        List<Map<String, Object>> reviews = new ArrayList<>();
        if (fromIndex < totalReviews) {
            List<Review> paginatedReviews = allReviews.subList(fromIndex, toIndex);
            for (Review rev : paginatedReviews) {
                Map<String, Object> review = new LinkedHashMap<>();
                review.put("username", rev.getUser().getUsername());
                review.put("rate", rev.getRate());
                review.put("comment", rev.getComment());
                reviews.add(review);
            }
        }

        return reviews;
    }

    public Map<String, Object> searchBooksByTitle(String title) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().contains(title)) {
                foundBooks.add(book);
            }
        }

        if (foundBooks.isEmpty())
            throw new IllegalArgumentException("No books found.");

        return buildSearchResult(title, foundBooks);
    }

    public Map<String, Object> searchBooksByAuthor(String author) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().contains(author)) {
                foundBooks.add(book);
            }
        }

        if (foundBooks.isEmpty())
            throw new IllegalArgumentException("No books found.");

        return buildSearchResult(author, foundBooks);
    }

    public Map<String, Object> searchBooksByGenre(String genre) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getGenres().contains(genre)) {
                foundBooks.add(book);
            }
        }

        if (foundBooks.isEmpty())
            throw new IllegalArgumentException("No books found.");

        return buildSearchResult(genre, foundBooks);
    }

    public Map<String, Object> searchBooksByYear(int start, int end) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getYear() >= start && book.getYear() <= end) {
                foundBooks.add(book);
            }
        }

        if (foundBooks.isEmpty())
            throw new IllegalArgumentException("No books found.");

        return buildSearchResult(start + "-" + end, foundBooks);
    }

    private Map<String, Object> buildSearchResult(String searchKey, List<Book> foundBooks) {
        Map<String, Object> searchResults = new LinkedHashMap<>();
        searchResults.put("search", searchKey);

        List<Map<String, Object>> booksList = new ArrayList<>();
        for (Book book : foundBooks) {
            Map<String, Object> bookDetails = new LinkedHashMap<>();
            bookDetails.put("title", book.getTitle());
            bookDetails.put("author", book.getAuthor());
            bookDetails.put("publisher", book.getPublisher());
            bookDetails.put("genres", book.getGenres());
            bookDetails.put("year", book.getYear());
            bookDetails.put("price", book.getPrice());
            bookDetails.put("synopsis", book.getSynopsis());
            booksList.add(bookDetails);
        }

        searchResults.put("books", booksList);
        return searchResults;
    }

    public Map<String, Object> searchBooks(Map<String, String> queryParams) {
        List<Book> filteredBooks = filterBooks(queryParams);
        sortBooks(filteredBooks, queryParams);
        List<Book> paginatedBooks = paginateBooks(filteredBooks, queryParams);
        return buildSearchResult(paginatedBooks, queryParams, filteredBooks.size());
    }

    private List<Book> filterBooks(Map<String, String> queryParams) {
        List<Book> filteredBooks = new ArrayList<>(books);

        if (queryParams.containsKey("title")) {
            String title = queryParams.get("title");
            filteredBooks.removeIf(book -> !book.getTitle().contains(title));
        }

        if (queryParams.containsKey("author")) {
            String author = queryParams.get("author");
            filteredBooks.removeIf(book -> !book.getAuthor().contains(author));
        }

        if (queryParams.containsKey("genre")) {
            String genre = queryParams.get("genre");
            filteredBooks.removeIf(book -> !book.getGenres().contains(genre));
        }

        if (queryParams.containsKey("year")) {
            int year = Integer.parseInt(queryParams.get("year"));
            filteredBooks.removeIf(book -> book.getYear() != year);
        } else if (queryParams.containsKey("from") && queryParams.containsKey("to")) {
            int fromYear = Integer.parseInt(queryParams.get("from"));
            int toYear = Integer.parseInt(queryParams.get("to"));
            filteredBooks.removeIf(book -> book.getYear() < fromYear || book.getYear() > toYear);
        }

        if (filteredBooks.isEmpty())
            throw new IllegalArgumentException("No books found.");

        return filteredBooks;
    }

    private void sortBooks(List<Book> books, Map<String, String> queryParams) {
        String sortBy = queryParams.getOrDefault("sortBy", "rating");
        String order = queryParams.getOrDefault("order", "desc");

        Comparator<Book> comparator;

        switch (sortBy) {
            case "rating":
                comparator = Comparator.comparingDouble(Book::getAverageRate);
                break;
            case "reviews":
                comparator = Comparator.comparingInt(Book::getReviewsCount);
                break;
            default:
                return;
        }

        if (order.equals("desc")) {
            comparator = comparator.reversed();
        }

        books.sort(comparator);
    }

    private List<Book> paginateBooks(List<Book> books, Map<String, String> queryParams) {
        int page = queryParams.containsKey("page") ? Integer.parseInt(queryParams.get("page")) : 1;
        int limit = queryParams.containsKey("limit") ? Integer.parseInt(queryParams.get("limit")) : 10;
        limit = Math.min(limit, 50);

        int fromIndex = Math.max((page - 1) * limit, 0);
        int toIndex = Math.min(fromIndex + limit, books.size());

        if (fromIndex >= books.size()) {
            return Collections.emptyList();
        }

        return books.subList(fromIndex, toIndex);
    }

    private Map<String, Object> buildSearchResult(List<Book> books, Map<String, String> queryParams, int totalItems) {
        Map<String, Object> searchResults = new LinkedHashMap<>();
        searchResults.put("search", queryParams);

        List<Map<String, Object>> bookDetailsList = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> bookDetails = new LinkedHashMap<>();
            bookDetails.put("title", book.getTitle());
            bookDetails.put("author", book.getAuthor());
            bookDetails.put("publisher", book.getPublisher());
            bookDetails.put("genres", book.getGenres());
            bookDetails.put("year", book.getYear());
            bookDetails.put("price", book.getPrice());
            bookDetails.put("synopsis", book.getSynopsis());
            bookDetails.put("averageRating", book.getAverageRate());
            bookDetails.put("reviewCount", book.getReviewsCount());
            bookDetailsList.add(bookDetails);
        }
        searchResults.put("books", bookDetailsList);

        int page = queryParams.containsKey("page") ? Integer.parseInt(queryParams.get("page")) : 1;
        int limit = queryParams.containsKey("limit") ? Integer.parseInt(queryParams.get("limit")) : 10;
        int totalPages = (int) Math.ceil((double) totalItems / limit);

        searchResults.put("page", page);
        searchResults.put("limit", limit);
        searchResults.put("totalItems", totalItems);
        searchResults.put("totalPages", totalPages);

        return searchResults;
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
