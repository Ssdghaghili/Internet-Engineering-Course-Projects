package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {
    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "name is required")
    private String penName;

    @NotNull(message = "Born date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date born;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date died;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPenName() { return penName; }
    public void setPenName(String penName) { this.penName = penName; }

    public Date getBorn() { return born; }
    public void setBorn(Date born) { this.born = born; }

    public Date getDied() { return died; }
    public void setDied(Date died) { this.died = died; }

}