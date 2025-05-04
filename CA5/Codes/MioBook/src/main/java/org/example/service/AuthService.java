package org.example.service;

import org.example.exception.*;

import org.example.model.Admin;
import org.example.model.User;

import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserSession userSession;
    @Autowired
    private UserRepository userRepository;

    public User showUserDetails() throws UnauthorizedException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        return user;
    }

    public void login(String username, String password) throws UnauthorizedException {
        User user = userService.findUserByUsername(username);

        if (user == null)
            throw new UnauthorizedException("Invalid username or password");

        if (!user.checkPassword(password))
            throw new UnauthorizedException("Invalid username or password");

        userSession.login(user);
    }

    public void logout() throws UnauthorizedException {
        if (!userSession.isLoggedIn())
            throw new UnauthorizedException("User is not logged in");

        userSession.logout();
    }

    public void signup(User newUser) throws InvalidFormatException, DuplicateEntityException {

        if (!ServiceUtils.validateUsername(newUser.getUsername()))
            throw new InvalidFormatException("Username is invalid");

        if (!ServiceUtils.validateEmail(newUser.getEmail()))
            throw new InvalidFormatException("Email is invalid");

        if (!ServiceUtils.validatePassword(newUser.getPassword()))
            throw new InvalidFormatException("Password is invalid");

        if (userService.usernameExists(newUser.getUsername()))
            throw new DuplicateEntityException("Username already exists");

        if (userService.emailExists(newUser.getEmail()))
            throw new DuplicateEntityException("Email already exists");

        userRepository.save(newUser);

    }


    public void validateAdmin() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User is not logged in");

        if (!Objects.equals(user.getRole(), "admin"))
            throw new ForbiddenException("User is not an admin");
    }

    public Admin validateAndGetAdmin() throws UnauthorizedException, ForbiddenException {
        User user = userSession.getCurrentUser();

        if (user == null)
            throw new UnauthorizedException("User not logged in");

        if (!(user instanceof Admin))
            throw new ForbiddenException("Only admins can perform this action");
        return (Admin) user;
    }
}
