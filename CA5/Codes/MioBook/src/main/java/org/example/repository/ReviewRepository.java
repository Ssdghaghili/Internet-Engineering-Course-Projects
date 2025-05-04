package org.example.repository;

import org.example.model.Book;
import org.example.model.Customer;
import org.example.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookAndCustomer(Book book, Customer customer);
    void deleteByBookAndCustomer(Book book, Customer customer);
}
