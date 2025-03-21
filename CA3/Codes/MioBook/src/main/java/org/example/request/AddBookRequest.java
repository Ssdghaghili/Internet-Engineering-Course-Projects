package org.example.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class AddBookRequest {
    @NotBlank(message = "Title is missing")
    private String title;

    @NotBlank(message = "Author is missing")
    private String author;

    @NotBlank(message = "Publisher is missing")
    private String publisher;

    @NotBlank(message = "Year is missing")
    private int year;

    @NotBlank(message = "Price is missing")
    private int price;

    @NotBlank(message = "Synopsis is missing")
    private String synopsis;

    @NotBlank(message = "Content is missing")
    private String content;

    @NotBlank(message = "Genres is missing")
    private List<String> genres;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
}