package org.example.dto;

import java.util.List;

public class ItemDTO {
    private String title;
    private String author;
    private String publisher;
    private List<String> genres;
    private int year;
    private int price;
    private boolean isBorrowed;
    private int finalPrice;
    private int borrowDays;
    private String bookImage;

    public ItemDTO(String title, String author, String publisher, List<String> genres, int year, int price,
                   boolean isBorrowed, int finalPrice, int borrowDays, String bookImage) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genres = genres;
        this.year = year;
        this.price = price;
        this.isBorrowed = isBorrowed;
        this.finalPrice = finalPrice;
        this.borrowDays = borrowDays;
        this.bookImage = bookImage;
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

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getBorrowDays() {
        return borrowDays;
    }

    public void setBorrowDays(int borrowDays) {
        this.borrowDays = borrowDays;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
}
