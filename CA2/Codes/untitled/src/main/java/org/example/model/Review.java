package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.model.serializer.ReviewSerializer;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = ReviewSerializer.class)
public class Review {
    private User user;
    private int rate;
    private String comment;
    private LocalDateTime dateTime;

    public Review() {}

    public Review(User user, int rate, String comment, LocalDateTime dateTime) {
        this.user = user;
        this.rate = rate;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public User getUser() { return user; }
    public int getRate() { return rate; }
    public String getComment() { return comment; }
    public LocalDateTime getDateTime() { return dateTime; }
}
