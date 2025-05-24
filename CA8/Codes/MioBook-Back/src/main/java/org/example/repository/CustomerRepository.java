package org.example.repository;

import org.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    Customer findByUsername(String username);
    Optional<Customer> findByEmail(String email);
    Customer findByUsernameAndPassword(String username, String password);
}