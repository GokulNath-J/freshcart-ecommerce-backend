package com.example.E_Commerce.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String productName;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private LocalDate expireDate;
    private Double discountPercent;
    private LocalDateTime createdAt;
}
