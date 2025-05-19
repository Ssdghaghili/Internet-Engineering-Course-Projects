package org.example.exception;

import org.example.response.*;

import jakarta.validation.ConstraintViolationException;
import org.example.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    private Response<Object> handleBadRequestException(BadRequestException ex) {
        return Response.failure(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    private Response<Object> handleDuplicateEntityException(DuplicateEntityException ex) {
        return Response.failure(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    private Response<Object> handleForbiddenException(ForbiddenException ex) {
        return Response.failure(HttpStatus.FORBIDDEN, ex.getMessage(), null);
    }

    @ExceptionHandler(InvalidFormatException.class)
    private Response<Object> handleInvalidFormatException(InvalidFormatException ex) {
        return Response.failure(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(NotFoundException.class)
    private Response<Object> handleNotFoundException(NotFoundException ex) {
        return Response.failure(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    private Response<Object> handleUnauthorized(UnauthorizedException ex) {
        return Response.failure(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Response<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return Response.failure(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private Response<Object> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error ->
                errors.put(error.getPropertyPath().toString(), error.getMessage()));
        return Response.failure(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(GoogleAuthException.class)
    private Response<Object> handleGoogleAuthException(GoogleAuthException ex) {
        return Response.failure(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }
}
