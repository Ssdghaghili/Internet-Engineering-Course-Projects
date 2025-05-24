package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @NotNull
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "author")
    private Set<Book> books = new HashSet<>();

    private String penName;
    private String nationality;
    private LocalDate born;
    private LocalDate died;
    private String imageLink;

    public Author() {
    }

    public Author(String name, String penName, String nationality,
                  LocalDate born, LocalDate died, String imageLink) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
        this.imageLink = imageLink;
    }

    public String getName() { return name; }
    public String getPenName() { return penName; }
    public String getNationality() { return nationality; }
    public LocalDate getBorn() { return born; }
    public LocalDate getDied() { return died; }
    public String getImageLink() { return imageLink; }

    public void setName(String name) { this.name = name; }
    public void setPenName(String penName) { this.penName = penName; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setBorn(LocalDate born) { this.born = born; }
    public void setDied(LocalDate died) { this.died = died; }
    public void setImageLink(String imageLink) { this.imageLink = imageLink; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public void addBook(Book book) {
        books.add(book);
    }
}
