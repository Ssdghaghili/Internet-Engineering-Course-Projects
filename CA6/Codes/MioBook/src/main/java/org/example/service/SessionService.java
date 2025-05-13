package org.example.service;

import java.util.UUID;

public class SessionService {

    public String createSession(int userId) {
        String token = UUID.randomUUID().toString();

        // Save <Token, userId< in redis

        return token;
    }
}
