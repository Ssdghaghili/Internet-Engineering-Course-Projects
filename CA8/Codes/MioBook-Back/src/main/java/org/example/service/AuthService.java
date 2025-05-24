package org.example.service;

import org.example.exception.*;

import org.example.model.*;
import org.example.repository.CustomerRepository;
import org.example.security.JwtUtil;
import org.example.security.UserContextHolder;
import org.example.security.UserDetailsFromToken;
import org.example.utils.PasswordHasher;

import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public User getLoggedInUser() throws UnauthorizedException {
        UserDetailsFromToken details = UserContextHolder.get();

        if (details == null)
            throw new UnauthorizedException("No authenticated user");

        return userRepository.findById(details.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    public LoginResponse login(String username, String password) throws UnauthorizedException {
        User user = findUserByUsername(username);

        if (user == null || !user.checkPassword(password))
            throw new UnauthorizedException("Invalid username or password");

        String token = JwtUtil.generateToken(
                String.valueOf(user.getId()),
                user.getUsername(),
                user.getEmail()
        );

        return new LoginResponse(token, user.getRole());
    }

    public void signup(String username, String password, String email, String country, String city, String role)
            throws InvalidFormatException, DuplicateEntityException {

        User newUser;
        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword(password, salt);


        if (role.equalsIgnoreCase("customer")) {
            newUser = new Customer(username, hashedPassword, email, new Address(country, city));
        }
        else {
            newUser = new Admin(username, hashedPassword, email, new Address(country, city));
        }

        if (!ServiceUtils.validateUsername(newUser.getUsername()))
            throw new InvalidFormatException("Username is invalid");

        if (!ServiceUtils.validateEmail(newUser.getEmail()))
            throw new InvalidFormatException("Email is invalid");

        if (!ServiceUtils.validatePassword(password))
            throw new InvalidFormatException("Password is invalid");

        if (usernameExists(newUser.getUsername()))
            throw new DuplicateEntityException("Username already exists");

        if (emailExists(newUser.getEmail()))
            throw new DuplicateEntityException("Email already exists");

        newUser.setSalt(salt);
        userRepository.save(newUser);
    }

    public Admin validateAndGetAdmin() throws UnauthorizedException, ForbiddenException {
        User user = getLoggedInUser();

        if (!(user instanceof Admin))
            throw new ForbiddenException("Only admins can perform this action");

        return (Admin) user;
    }

    public Customer getLoggedInCustomer() throws UnauthorizedException, ForbiddenException {
        User user = getLoggedInUser();

        if (!(user instanceof Customer))
            throw new ForbiddenException("Only customers can perform this action");

        return (Customer) user;
    }

    public Customer findOrCreateByGoogleEmail(String email, String name) {

        Optional<User> existing = userRepository.findByEmail(email);

        if (existing.isEmpty()){
            String salt = PasswordHasher.generateSalt();
            String hashedPassword = PasswordHasher.hashPassword("oauth_google_login", salt);

            Customer newCustomer = new Customer(name, hashedPassword, email, new Address("google_oauth", "google_oauth"));
            newCustomer.setRole("customer");
            newCustomer.setSalt(salt);

            customerRepository.save(newCustomer);
            return newCustomer;
        }

        else {
            User user = existing.get();
            if (user instanceof Customer) {
                return (Customer) user;
            } else {
                throw new IllegalStateException("User with email " + email + " exists but is not a customer");
            }
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
