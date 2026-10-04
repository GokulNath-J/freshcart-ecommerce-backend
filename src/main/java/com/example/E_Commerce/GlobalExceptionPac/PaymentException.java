package com.example.E_Commerce.GlobalExceptionPac;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
