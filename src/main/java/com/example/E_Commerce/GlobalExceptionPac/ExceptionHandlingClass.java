package com.example.E_Commerce.GlobalExceptionPac;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice
public class ExceptionHandlingClass {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<HashMap<String, String>> productException(ProductException exception, HttpServletRequest request) {
        HashMap<String, String> response = new HashMap<>();
        response.put("Path", request.getRequestURI());
        response.put("Exception", exception.getMessage());
        response.put("Date AND Time", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<HashMap<String, String>> userException(UserException exception, HttpServletRequest request) {
        HashMap<String, String> response = new HashMap<>();
        response.put("Path", request.getRequestURI());
        response.put("Exception", exception.getMessage());
        response.put("Date AND Time", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
