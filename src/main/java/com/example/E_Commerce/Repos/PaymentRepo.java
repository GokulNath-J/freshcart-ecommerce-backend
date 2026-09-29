package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.PaymentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentClass, Integer> {
}
