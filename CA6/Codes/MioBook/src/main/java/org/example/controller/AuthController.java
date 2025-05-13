package org.example.controller;

import jakarta.validation.Valid;

import org.example.dto.DtoMapper;
import org.example.dto.UserDTO;
import org.example.exception.*;
import org.example.model.Address;
import org.example.model.Admin;
import org.example.model.Customer;
import org.example.model.User;
import org.example.request.LoginRequest;
import org.example.request.SignupRequest;
import org.example.response.Response;
import org.example.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/user")
    public Response<UserDTO> user() throws UnauthorizedException, ForbiddenException {
        User user = authService.showUserDetails();
        return Response.ok("User details retrieved successfully",
                DtoMapper.userToDTO(user));
    }

    @PostMapping("/login")
    public Response<Object> login(@Valid @RequestBody LoginRequest loginRequest)
            throws UnauthorizedException, ForbiddenException {

        authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return Response.ok("User logged in successfully", user());
    }

    @PostMapping("/signup")
    public Response<Object> signup(@Valid @RequestBody SignupRequest signupRequest)
            throws InvalidFormatException, DuplicateEntityException {

        Address address = new Address(
                signupRequest.getAddress().getCountry(),
                signupRequest.getAddress().getCity()
        );

        User newUser;
        if ("admin".equalsIgnoreCase(signupRequest.getRole())) {
            newUser = new Admin(
                    signupRequest.getUsername(),
                    signupRequest.getPassword(),
                    signupRequest.getEmail(),
                    address
            );
        } else {
            newUser = new Customer(
                    signupRequest.getUsername(),
                    signupRequest.getPassword(),
                    signupRequest.getEmail(),
                    address
            );
        }

        authService.signup(newUser);
        return Response.ok("signup successful", null);
    }

    @PostMapping("/logout")
    public Response<Object> logout() throws UnauthorizedException {
        authService.logout();
        return Response.ok("User logged out successfully.");
    }
}
