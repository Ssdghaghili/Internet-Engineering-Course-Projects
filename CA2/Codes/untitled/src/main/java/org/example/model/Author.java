package org.example.model;

import java.time.LocalDate;

public class Author {
    private String name;
    private String penName;
    private String nationality;
    private LocalDate born;
    private LocalDate died;

    public Author(String name, String penName, String nationality, LocalDate born, LocalDate died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }
}
