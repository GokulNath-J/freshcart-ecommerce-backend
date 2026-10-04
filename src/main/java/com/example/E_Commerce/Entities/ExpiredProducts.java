package com.example.E_Commerce.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpiredProducts {

    @Id
    private Integer productId;

    private String sellerId;

    private String productName;
    private String brandName;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private LocalDate expireDate;
    private Double discountPercent;
    private LocalDateTime createdAt;


    private Integer daysBeforeToAddDiscount;
}
