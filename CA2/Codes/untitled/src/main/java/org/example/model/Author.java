package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    @JsonIgnore
    private String AdderUserName;
    private String name;
    private String penName;
    private String nationality;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date born;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date died;

    public Author() {               //Default constructor
    }

    public Author(String AdderUserName, String name, String penName, String nationality, Date born, Date died) {
        this.AdderUserName = AdderUserName;
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }

    public String getAdderUserName() { return AdderUserName; }
    public String getName() { return name; }
    public String getPenName() { return penName; }
    public String getNationality() { return nationality; }
    public Date getBorn() { return born; }
    public Date getDied() { return died; }
}
