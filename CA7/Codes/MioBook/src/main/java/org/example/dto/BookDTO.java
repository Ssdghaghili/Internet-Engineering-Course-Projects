package org.example.dto;

import org.example.model.Genre;

import java.util.List;

public class BookDTO {
    private String title;
    private String author;
    private String publisher;
    private List<String> genres;
    private int year;
    private int price;
    private String synopsis;
    private double averageRating;
    private int totalBuys;
    private String imageLink;

    public BookDTO(String title, String author, String publisher, List<String> genres, int year, int price,
                   String synopsis, double averageRating, int totalBuys, String imageLink) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genres = genres;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.averageRating = averageRating;
        this.totalBuys = totalBuys;
        this.imageLink = imageLink;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalBuys() {
        return totalBuys;
    }

    public void setTotalBuys(int totalBuys) {
        this.totalBuys = totalBuys;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
