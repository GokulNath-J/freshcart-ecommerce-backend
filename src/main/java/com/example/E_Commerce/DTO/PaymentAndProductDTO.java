package com.example.E_Commerce.DTO;


import com.example.E_Commerce.Entities.PaymentClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAndProductDTO {

    private ProductDetailsDTO productDetailsDTO;

    private PaymentClass paymentClass;
}
