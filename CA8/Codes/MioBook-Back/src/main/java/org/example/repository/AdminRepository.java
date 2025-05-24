package org.example.repository;

import org.example.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUsername(String username);
    Admin findByUsername(String username);
    Admin findByEmail(String email);
}