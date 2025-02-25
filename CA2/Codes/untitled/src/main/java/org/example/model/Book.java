package org.example.model;

import java.util.List;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private int year;
    private int price;
    private String synopsis;
    private String content;
    private List<String> genres;

    public Book(String title, String author, String publisher, int year, int price, String synopsis, String content, List<String> genres) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
        this.genres = genres;
    }

    public int getPrice() {
        return price;
    }
}
