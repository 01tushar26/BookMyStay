package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Advice;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.AlreadyExistException;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Exceptions.ResourceNotFoundException;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
