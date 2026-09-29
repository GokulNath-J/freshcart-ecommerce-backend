package com.example.E_Commerce.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductClass {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "productSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.AUTO)
    private Integer productId;
    private String productName;
    private String brandName;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private LocalDate expireDate;
    private Double discountPercent;
    private LocalDateTime createdAt;
}
