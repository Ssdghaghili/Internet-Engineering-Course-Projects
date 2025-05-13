package org.example.repository;

import org.example.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByTitle(String title);
    List<Book> findByAuthorName(String authorName);

    @Query("SELECT b, AVG(r.rate) As averageRating " +
            "FROM Book b " +
            "JOIN b.reviews r " +
            "GROUP BY b " +
            "ORDER BY averageRating DESC")
    List<Book> findTopRatedBooks(Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "ORDER BY b.year DESC")
    List<Book> findNewReleasedBooks(Pageable pageable);

    @Query("SELECT b, COUNT(r) AS reviewCount " +
            "FROM Book b " +
            "JOIN b.reviews r " +
            "JOIN b.genres g " +
            "JOIN b.author a " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:authorName IS NULL OR a.name LIKE %:authorName%) " +
            "AND (:genre IS NULL OR g.name = :genre) " +
            "AND (:year IS NULL OR b.year = :year) " +
            "Group By b " +
            "ORDER BY reviewCount DESC")
    List<Book> searchBooksSortedByReviewsDesc(@Param("title") String title,
                                          @Param("authorName") String authorName,
                                          @Param("genre") String genre,
                                          @Param("year") Integer year,
                                          Pageable pageable);

    @Query("SELECT b, COUNT(r) AS reviewCount " +
            "FROM Book b " +
            "JOIN b.reviews r " +
            "JOIN b.genres g " +
            "JOIN b.author a " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:authorName IS NULL OR a.name LIKE %:authorName%) " +
            "AND (:genre IS NULL OR g.name = :genre) " +
            "AND (:year IS NULL OR b.year = :year) " +
            "Group By b " +
            "ORDER BY reviewCount")
    List<Book> searchBooksSortedByReviewsAsc(@Param("title") String title,
                                          @Param("authorName") String authorName,
                                          @Param("genre") String genre,
                                          @Param("year") Integer year,
                                          Pageable pageable);

    @Query("SELECT b, AVG(r.rate) As averageRating " +
            "FROM Book b " +
            "JOIN b.reviews r " +
            "JOIN b.genres g " +
            "JOIN b.author a " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:authorName IS NULL OR a.name LIKE %:authorName%) " +
            "AND (:genre IS NULL OR g.name = :genre) " +
            "AND (:year IS NULL OR b.year = :year) " +
            "Group By b " +
            "ORDER BY averageRating DESC")
    List<Book> searchBooksSortedByRatingDesc(@Param("title") String title,
                                         @Param("authorName") String authorName,
                                         @Param("genre") String genre,
                                         @Param("year") Integer year,
                                         Pageable pageable);

    @Query("SELECT b, AVG(r.rate) As averageRating " +
            "FROM Book b " +
            "JOIN b.reviews r " +
            "JOIN b.genres g " +
            "JOIN b.author a " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:authorName IS NULL OR a.name LIKE %:authorName%) " +
            "AND (:genre IS NULL OR g.name = :genre) " +
            "AND (:year IS NULL OR b.year = :year) " +
            "Group By b " +
            "ORDER BY averageRating")
    List<Book> searchBooksSortedByRatingAsc(@Param("title") String title,
                                         @Param("authorName") String authorName,
                                         @Param("genre") String genre,
                                         @Param("year") Integer year,
                                         Pageable pageable);
}