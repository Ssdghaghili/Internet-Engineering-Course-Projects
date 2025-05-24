package org.example.dto;

import java.time.LocalDate;

public class AuthorDTO {
    private String name;
    private String penName;
    private String nationality;
    private LocalDate born;
    private LocalDate died;
    private String imageLink;

    public AuthorDTO(String name, String penName, String nationality,
                     LocalDate born, LocalDate died, String imageLink) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPenName() {
        return penName;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBorn() {
        return born;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public LocalDate getDied() {
        return died;
    }

    public void setDied(LocalDate died) {
        this.died = died;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
