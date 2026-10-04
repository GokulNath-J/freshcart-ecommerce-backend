package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.OrderClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<OrderClass,String> {
//    Optional<OrderClass> findByPaymentId(String paymentId);

    Optional<OrderClass> findByPaymentClassPaymentId(String paymentId);

}
