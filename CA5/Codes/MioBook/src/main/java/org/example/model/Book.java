package org.example.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import org.example.model.serializer.BookSerializer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@JsonSerialize(using = BookSerializer.class)
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @NotNull
    @Column(unique = true, nullable = false)
    private String title;

    private String publisher;
    private int year;
    private int price;
    private String synopsis;
    private String content;
    private String imageLink;
    private int totalBuys = 0;

    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();


    public Book() {}

    public Book(Admin admin, Author author, String title, String publisher, int year, int price,
                String synopsis, String content, Set<Genre> genres , String imageLink) {
        this.admin = admin;
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.genres = genres;
        this.content = content;
        this.imageLink = imageLink;
    }

    public Admin getAdmin() { return admin; }
    public Author getAuthor() { return author; }
    public String getTitle() { return title; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public int getPrice() { return price; }
    public String getSynopsis() { return synopsis; }
    public String getContent() { return content; }
    public String getImageLink() { return imageLink; }
    public int getTotalBuys() { return totalBuys; }
    public Set<Genre> getGenres() { return genres; }
    public Set<Review> getReviews() { return reviews; }

    public void setAdmin(Admin admin) { this.admin = admin; }
    public void setAuthor(Author author) { this.author = author; }
    public void setTitle(String title) { this.title = title; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(int price) { this.price = price; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
    public void setContent(String content) { this.content = content; }
    public void setImageLink(String imageLink) { this.imageLink = imageLink; }
    public void setTotalBuys(int totalBuys) { this.totalBuys = totalBuys; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }
    public void setReviews(Set<Review> reviews) { this.reviews = reviews; }


    public void addGenre(Genre genre) {
        genres.add(genre);
    }


    public void addReview(Review newReview) {
        for (Review r : reviews) {
            if (r.getCustomer().equals(newReview.getCustomer())) {
                reviews.remove(r);
                r.setBook(null);
                break;
            }
        }
        newReview.setBook(this);
        reviews.add(newReview);
    }

    public void removeReview(Customer customer) {
        for (Review r : reviews) {
            if (r.getCustomer().equals(customer)) {
                r.setBook(null);
                reviews.remove(r);
                break;
            }
        }
    }

    public double getAverageRate() {
        if (reviews.isEmpty())
            return 0;

        double sum = 0;
        for (Review r : reviews) {
            sum += r.getRate();
        }
        return sum / reviews.size();
    }

    public void addBuy() {
        totalBuys++;
    }

    public void removeBuy() {
        if (totalBuys > 0) {
            totalBuys--;
        }
    }

    public int getReviewsCount() {
        return reviews.size();
    }
}
