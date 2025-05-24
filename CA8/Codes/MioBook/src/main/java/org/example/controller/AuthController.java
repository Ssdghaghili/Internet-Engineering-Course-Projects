package org.example.controller;

import jakarta.validation.Valid;

import org.example.dto.DtoMapper;
import org.example.dto.UserDTO;
import org.example.exception.*;
import org.example.model.*;
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
        User user = authService.getLoggedInUser();
        return Response.ok("User details retrieved successfully",
                DtoMapper.userToDTO(user));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest)
            throws UnauthorizedException {

        LoginResponse loginResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return Response.ok("User logged in successfully", loginResponse);
    }

    @PostMapping("/signup")
    public Response<Object> signup(@Valid @RequestBody SignupRequest signupRequest)
            throws InvalidFormatException, DuplicateEntityException {

        authService.signup(signupRequest.getUsername(), signupRequest.getPassword(),  signupRequest.getEmail(),
                signupRequest.getAddress().getCountry(), signupRequest.getAddress().getCity(), signupRequest.getRole());
        return Response.ok("signup successful", null);
    }

    @PostMapping("/logout")
    public Response<Object> logout(@RequestHeader("Authorization") String token) throws UnauthorizedException {
        return Response.ok("User logged out successfully");
    }
}
