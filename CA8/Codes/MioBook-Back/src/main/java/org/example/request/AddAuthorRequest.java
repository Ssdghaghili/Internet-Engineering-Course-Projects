package org.example.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Date;

public class AddAuthorRequest {
    @NotBlank(message = "Name is missing")
    private String name;

    @NotBlank(message = "PenName is missing")
    private String penName;

    @NotBlank(message = "Nationality is missing")
    private String nationality;

    @NotNull(message = "Born date is missing")
    @Past(message = "Born date must be in the past")
    private LocalDate born;

    private LocalDate died;

    private String imageLink;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPenName() { return penName; }
    public void setPenName(String penName) { this.penName = penName; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public LocalDate getBorn() { return born; }
    public void setBorn(LocalDate born) { this.born = born; }

    public LocalDate getDied() { return died; }
    public void setDied(LocalDate died) { this.died = died; }

    public String getImageLink() { return imageLink; }
    public void setImageLink(String imageLink) { this.imageLink = imageLink; }
}