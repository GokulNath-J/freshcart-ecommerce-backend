package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.OrderClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderClass,Integer> {
}
