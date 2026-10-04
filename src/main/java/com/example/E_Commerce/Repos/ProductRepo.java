package com.example.E_Commerce.Repos;

import com.example.E_Commerce.Entities.ProductClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<ProductClass, Integer> {
    Optional<List<ProductClass>> findAllByCategory(String category);

    Optional<ProductClass> findByProductId(Integer productName);

    Optional<List<ProductClass>> findAllBySellerId(String userId);


    @Query(value = "select * from product_class where expire_date = ?1",nativeQuery = true)
    Optional<List<ProductClass>> findExpiringpProducts(LocalDate now);

//    Page<ProductClass> findByCategoryPaging(String category);

    Page<ProductClass> findByCategory(String category, PageRequest of);
}
