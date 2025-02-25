package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private Author author;

    @NotNull(message = "Publisher is required")
    private String publisher;

    @NotNull(message = "Publication year is required")
    private int year;

    @NotNull(message = "Price is required")
    private int price;

    @NotNull(message = "Synopsis is required")
    private String synopsis;

    @NotNull(message = "Content is required")
    private String content;

    @NotNull(message = "Genres are required")
    @Size(min = 1, message = "There must be at least one genre")
    private List<String> genres;


    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public Author getAuthor() {return author;}
    public void setAuthor(Author author) {this.author = author;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public String getSynopsis() {return synopsis;}
    public void setSynopsis(String synopsis) {this.synopsis = synopsis;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public List<String> getGenres() {return genres;}
    public void setGenres(List<String> genres) {this.genres = genres;}

}