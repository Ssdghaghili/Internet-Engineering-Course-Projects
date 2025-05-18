package org.example.service;

import org.example.exception.*;

import org.example.model.*;
import org.example.utils.PasswordHasher;

import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserRepository userRepository;

    public User getLoggedInUser(String token) throws UnauthorizedException {

        Long userId = sessionService.getUserID(token);

        if (userId == null)
            throw new UnauthorizedException("User is not logged in");

        return userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    public LoginResponse login(String username, String password) throws UnauthorizedException {
        User user = findUserByUsername(username);

        if (user == null)
            throw new UnauthorizedException("Invalid username or password");

        if (!user.checkPassword(password))
            throw new UnauthorizedException("Invalid username or password");

        String token = sessionService.createSession(user.getId());

        return new LoginResponse(token, user.getRole());
    }

    public void logout(String token) throws UnauthorizedException {
        if (!sessionService.isValid(token))
            throw new UnauthorizedException("User is not logged in");

        sessionService.deleteSession(token);
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

    public Admin validateAndGetAdmin(String token) throws UnauthorizedException, ForbiddenException {
        User user = getLoggedInUser(token);

        if (!(user instanceof Admin))
            throw new ForbiddenException("Only admins can perform this action");

        return (Admin) user;
    }

    public Customer getLoggedInCustomer(String token) throws UnauthorizedException, ForbiddenException {
        User user = getLoggedInUser(token);

        if (!(user instanceof Customer))
            throw new ForbiddenException("Only customers can perform this action");

        return (Customer) user;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

}
