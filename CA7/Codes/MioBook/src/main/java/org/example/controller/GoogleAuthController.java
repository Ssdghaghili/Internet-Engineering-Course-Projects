package org.example.controller;

import org.example.model.Customer;
import org.example.model.LoginResponse;
import org.example.request.GoogleAuthRequest;
import org.example.response.Response;
import org.example.security.JwtUtil;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/google")
public class GoogleAuthController {

    @Autowired
    private AuthService authService;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/callback")
    public ResponseEntity<Response<LoginResponse>> googleCallback(@RequestBody GoogleAuthRequest request) {
        String code = request.getCode();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> tokenResponse;
        try {
            tokenResponse = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token",
                    tokenRequest,
                    Map.class
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Response.failure(HttpStatus.BAD_REQUEST, "Error fetching access token from Google.", null)
            );
        }

        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null || !tokenResponse.getBody().containsKey("access_token")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Response.failure(HttpStatus.BAD_REQUEST,"Failed to retrieve access token.", null)
            );
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse;
        try {
            userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    userInfoRequest,
                    Map.class
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Response.failure(HttpStatus.BAD_REQUEST,"Error fetching user info from Google.", null)
            );
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();
        if (!userInfoResponse.getStatusCode().is2xxSuccessful() || userInfo == null || !userInfo.containsKey("email")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Response.failure(HttpStatus.BAD_REQUEST,"Failed to retrieve user info.", null)
            );
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");


        Customer customer = authService.findOrCreateByGoogleEmail(email, name);
        
        String token = JwtUtil.generateToken(
                String.valueOf(customer.getId()),
                customer.getUsername(),
                customer.getEmail()
        );

        LoginResponse loginResponse = new LoginResponse(token, "customer");

        return ResponseEntity.ok(Response.ok("User logged in successfully", loginResponse));
    }
}