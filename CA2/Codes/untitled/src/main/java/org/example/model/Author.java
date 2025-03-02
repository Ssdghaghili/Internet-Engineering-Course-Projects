package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    private String name;
    private String penName;
    private String nationality;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date born;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date died;

    public Author() {               //Default constructor
    }

    public Author(String name, String penName, String nationality, Date born, Date died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }

    public String getName() { return name; }
    public String getPenName() { return penName; }
    public String getNationality() { return nationality; }
    public Date getBorn() { return born; }
    public Date getDied() { return died; }
}
