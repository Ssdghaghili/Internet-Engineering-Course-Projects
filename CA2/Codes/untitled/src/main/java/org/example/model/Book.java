package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.model.serializer.BookSerializer;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = BookSerializer.class)
public class Book {
    private String title;
    private String author;
    private String publisher;
    private int year;
    private int price;
    private String synopsis;
    private String content;
    private List<String> genres;
    private List<Review> reviews;

    public Book() {
        this.reviews = new ArrayList<>();
    }

    public Book(String title, String author, String publisher, int year, int price, String synopsis, String content, List<String> genres) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
        this.genres = genres;
        this.reviews = new ArrayList<>();
    }

    public void addReview(Review newReview) {
        for (Review r : reviews) {
            if (r.getUser().equals(newReview.getUser())) {
                reviews.remove(r);
                break;
            }
        }
        reviews.add(newReview);
    }

    public double getAverageRate() {
        if (reviews.isEmpty())
            return 0;

        double sum = 0;
        for (Review r : reviews) {
            sum += r.getRate();
        }
        return sum / reviews.size();
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public int getPrice() { return price; }
    public String getSynopsis() { return synopsis; }
    public String getContent() { return content; }
    public List<String> getGenres() {return genres;}
    public List<Review> getReviews() { return reviews; }
}
