package org.example.exception;

import org.example.response.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    private Response<Object> handleBadRequestException(BadRequestException ex) {
        return Response.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    private Response<Object> handleDuplicateEntityException(DuplicateEntityException ex) {
        return Response.failure(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    private Response<Object> handleForbiddenException(ForbiddenException ex) {
        return Response.failure(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    private Response<Object> handleInvalidFormatException(InvalidFormatException ex) {
        return Response.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    private Response<Object> handleNotFoundException(NotFoundException ex) {
        return Response.failure(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    private Response<Object> handleUnauthorized(UnauthorizedException ex) {
        return Response.failure(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
}
