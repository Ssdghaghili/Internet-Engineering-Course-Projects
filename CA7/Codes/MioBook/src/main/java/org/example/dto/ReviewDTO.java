package org.example.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private String username;
    private int rate;
    private String comment;
    private LocalDateTime dateTime;

    public ReviewDTO(String username, int rate, String comment, LocalDateTime dateTime) {
        this.username = username;
        this.rate = rate;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
