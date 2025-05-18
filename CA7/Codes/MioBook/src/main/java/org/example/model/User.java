package org.example.model;

import org.example.utils.PasswordHasher;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="role", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Formula("role")
    private String role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    public User() {}

    public User(String username, String password, String email, Address address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    public Long getId() { return id; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Address getAddress() { return address;}

    public boolean checkPassword(String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        return this.password.equals(hashedPassword);
    }

    public void serPassword(String password) {
        this.password = password;
    }
}
