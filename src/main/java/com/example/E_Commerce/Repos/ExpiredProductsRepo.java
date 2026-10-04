package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.ExpiredProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredProductsRepo extends JpaRepository<ExpiredProducts, Integer> {
}
