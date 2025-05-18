package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class SessionService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private final Duration sessionTTL = Duration.ofMinutes(20);

    public String createSession(Long userId) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token, Long.toString(userId), sessionTTL);
        return token;
    }

    public boolean isValid(String token) {
        return redisTemplate.hasKey(token);
    }

    public void deleteSession(String token) {
        redisTemplate.delete(token);
    }

    public Long getUserID(String token) {
        String value = redisTemplate.opsForValue().get(token);
        return value != null ? Long.parseLong(value) : null;
    }
}
