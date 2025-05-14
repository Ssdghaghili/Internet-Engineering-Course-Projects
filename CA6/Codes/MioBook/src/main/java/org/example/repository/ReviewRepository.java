package org.example.repository;

import org.example.model.Book;
import org.example.model.Customer;
import org.example.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookAndCustomer(Book book, Customer customer);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.book = :book AND r.customer = :customer")
    void deleteByBookAndCustomer(@Param("book") Book book, @Param("customer") Customer customer);

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN r.book b " +
            "WHERE b.title = :title")
    List<Review> getBookReviews(@Param("title") String title, Pageable pageable);
}
