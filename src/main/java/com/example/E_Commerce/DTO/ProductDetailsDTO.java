package com.example.E_Commerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO implements Serializable {

    private Integer productId;
    private String productName;
    private String brandName;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private LocalDate expireDate;
    private Double discountPercent;
    private Double totalPriceAfterDiscount;


    public ProductDetailsDTO(Integer productId, String productName, Integer quantity, Double totalPriceAfterDiscount) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }


}
