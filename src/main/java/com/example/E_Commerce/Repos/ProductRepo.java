package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.ProductClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<ProductClass,Integer> {
    Optional<List<ProductClass>> findByProductName(String productName);
}
