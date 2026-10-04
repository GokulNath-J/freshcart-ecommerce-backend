package com.example.E_Commerce.DTO;


import com.example.E_Commerce.Entities.StatusClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {


    private String orderId;
    private String productName;
    private Integer quantity;
    private Double totalAmount;
    private StatusClass status;
    private LocalDateTime createdAt;

}
