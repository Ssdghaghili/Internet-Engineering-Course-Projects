package org.example.controller;

import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.Customer;
import org.example.model.GoogleUserInfo;
import org.example.model.LoginResponse;
import org.example.request.GoogleAuthRequest;
import org.example.response.Response;
import org.example.security.JwtUtil;
import org.example.service.AuthService;
import org.example.service.GoogleAuthServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/google")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthServise googleAuthServise;


    @PostMapping("/callback")
    public Response<LoginResponse> googleCallback(@RequestBody GoogleAuthRequest request) {

        LoginResponse loginResponse = googleAuthServise.exchangeCodeForUserInfo(request.getCode());
        return Response.ok("User logged in successfully", loginResponse);
    }
}