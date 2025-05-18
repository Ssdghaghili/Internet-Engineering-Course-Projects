package org.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "MyVerySecretKey1234567890123456A";
    private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000; // 1 day

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(String userId, String username, String email) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setIssuer("MioBook")
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME_MS))
                .claim("username", username)
                .claim("email", email)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static String extractUsername(String token) {
        return validateToken(token).getBody().get("username", String.class);
    }

    public static String extractUserId(String token) {
        return validateToken(token).getBody().getSubject();
    }

    public static boolean isTokenExpired(String token) {
        Date expiration = validateToken(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}