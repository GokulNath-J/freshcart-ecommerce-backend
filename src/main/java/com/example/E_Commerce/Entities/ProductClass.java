package com.example.E_Commerce.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        indexes = {
                @Index(name = "product_idx", columnList = "productId")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductClass implements Serializable {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "productSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.AUTO)
    private Integer productId;
    private String sellerId;
    private String productName;
    private String brandName;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private Double discountPercent;
    private LocalDateTime createdAt;
    private LocalDate expireDate;
    private Integer daysBeforeToAddDiscount;

    @Version
    private Long version;

    public ProductClass(String sellerId, String productName, String brandName, String description, String category, Integer quantity, Double price, Double discountPercent, LocalDateTime createdAt, LocalDate expireDate, Integer daysBeforeToAddDiscount) {
        this.sellerId = sellerId;
        this.productName = productName;
        this.brandName = brandName;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.discountPercent = discountPercent;
        this.createdAt = createdAt;
        this.expireDate = expireDate;
        this.daysBeforeToAddDiscount = daysBeforeToAddDiscount;
    }
}
