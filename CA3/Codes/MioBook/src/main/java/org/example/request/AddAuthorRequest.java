package org.example.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class AddAuthorRequest {
    @NotBlank(message = "Name is missing")
    private String name;

    @NotBlank(message = "PenName is missing")
    private String penName;

    @NotBlank(message = "Nationality is missing")
    private String nationality;

    @NotBlank(message = "Born date is missing")
    private Date born;

    private Date died;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPenName() { return penName; }
    public void setPenName(String penName) { this.penName = penName; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Date getBorn() { return born; }
    public void setBorn(Date born) { this.born = born; }

    public Date getDied() { return died; }
    public void setDied(Date died) { this.died = died; }
}