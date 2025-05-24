package org.example.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AddReviewRequest {
    @NotBlank(message = "Title is missing")
    private String title;

    @NotNull(message = "Rate is missing")
    private int rate;

    @NotBlank(message = "Comment is missing")
    private String comment;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getRate() { return rate; }
    public void setRate(int rate) { this.rate = rate; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}