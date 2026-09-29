package com.example.E_Commerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {

    private String productName;
    private String brandName;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private LocalDate expireDate;
    private Double discountPercent;
    
}
