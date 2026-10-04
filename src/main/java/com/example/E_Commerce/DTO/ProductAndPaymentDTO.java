package com.example.E_Commerce.DTO;

import com.example.E_Commerce.Entities.PaymentClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAndPaymentDTO {

    private Integer productId;
    private String productName;
    private Integer quantity;
    private Double totalPriceAfterDiscount;
    private PaymentClass paymentClass;
}
