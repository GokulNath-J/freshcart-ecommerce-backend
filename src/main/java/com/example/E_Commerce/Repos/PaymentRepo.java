package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.PaymentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentClass, String> {
    Optional<PaymentClass> findByPaymentId(String paymentId);
}
