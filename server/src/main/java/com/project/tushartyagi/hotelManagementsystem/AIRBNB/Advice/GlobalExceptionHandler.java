package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Advice;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.AlreadyExistException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex){

        ApiError er= ApiError.builder().message(ex.getLocalizedMessage()).status(HttpStatus.NOT_FOUND).timestamp(Timestamp.valueOf(LocalDateTime.now())).build();
        ApiResponse<?> res = ApiResponse.builder().error(er).time(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ApiResponse<?>> alreadyExist(AlreadyExistException ex){

        ApiError er= ApiError.builder().message(ex.getLocalizedMessage()).status(HttpStatus.NOT_ACCEPTABLE).timestamp(Timestamp.valueOf(LocalDateTime.now())).build();
        ApiResponse<?> res = ApiResponse.builder().error(er).time(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> internalServerEroor(Exception ex){

        ApiError er= ApiError.builder().message(ex.getLocalizedMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).timestamp(Timestamp.valueOf(LocalDateTime.now())).build();
        ApiResponse<?> res = ApiResponse.builder().error(er).time(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        ApiResponse<?> res = ApiResponse.builder().error(apiError).time(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .message(ex.getMessage())
                .build();
        ApiResponse<?> res = ApiResponse.builder().error(apiError).time(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .message(ex.getMessage())
                .build();
        ApiResponse<?> res = ApiResponse.builder().error(apiError).time(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

}
