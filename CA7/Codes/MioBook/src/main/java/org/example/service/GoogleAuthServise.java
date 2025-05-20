package org.example.service;

import org.example.exception.GoogleAuthException;
import org.example.model.Customer;
import org.example.model.GoogleUserInfo;
import org.example.model.LoginResponse;
import org.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleAuthServise {

    @Autowired
    private AuthService authService;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public LoginResponse exchangeCodeForUserInfo(String code) {
        String accessToken = fetchAccessToken(code);
        GoogleUserInfo userInfo = fetchUserInfo(accessToken);

        Customer customer = authService.findOrCreateByGoogleEmail(userInfo.getEmail(), userInfo.getName());

        String jwtToken = JwtUtil.generateToken(
                String.valueOf(customer.getId()),
                customer.getUsername(),
                customer.getEmail()
        );

        return new LoginResponse(jwtToken, "customer");
    }

    private String fetchAccessToken(String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                request,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new GoogleAuthException("Failed to retrieve access token from Google.");
        }

        Object token = response.getBody().get("access_token");
        if (token == null) {
            throw new GoogleAuthException("Access token is missing in Google response.");
        }

        return token.toString();
    }

    private GoogleUserInfo fetchUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                request,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new GoogleAuthException("Failed to retrieve user info from Google.");
        }

        Map<String, Object> userInfo = response.getBody();
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        if (email == null || name == null) {
            throw new GoogleAuthException("Missing email or name in user info.");
        }

        return new GoogleUserInfo(email, name);
    }
}