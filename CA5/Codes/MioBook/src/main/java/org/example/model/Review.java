package org.example.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.model.serializer.ReviewSerializer;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@JsonSerialize(using = ReviewSerializer.class)
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int rate;
    private String comment;
    private LocalDateTime dateTime;

    public Review() {}

    public Review(Customer customer, Book book, int rate, String comment, LocalDateTime dateTime) {
        this.customer = customer;
        this.book = book;
        this.rate = rate;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public Customer getCustomer() { return customer; }
    public Book getBook() { return book; }
    public int getRate() { return rate; }
    public String getComment() { return comment; }
    public LocalDateTime getDateTime() { return dateTime; }

    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setBook(Book book) { this.book = book; }
}
