package org.example.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Response<T> {
    private boolean success;
    private HttpStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public Response(boolean success, HttpStatus status, String message, T data, LocalDateTime timestamp) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static <T> Response<T> ok(String message, T data) {
        return new Response<T>(true, HttpStatus.OK, message, data, LocalDateTime.now());
    }

    public static <T> Response<T> ok(String message) {
        return new Response<T>(true, HttpStatus.OK, message, null, LocalDateTime.now());
    }

    public static <T> Response<T> failure(HttpStatus status, String message, T data) {
        return new Response<T>(false, status, message, data, LocalDateTime.now());
    }

}
