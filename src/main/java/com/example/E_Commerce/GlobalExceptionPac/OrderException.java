package com.example.E_Commerce.GlobalExceptionPac;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
