package com.example.E_Commerce.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderClass {

    @Id
//    @SequenceGenerator(name = "seq", sequenceName = "orderSeq", initialValue = 1, allocationSize = 1)
//    @GeneratedValue(generator = "seq", strategy = GenerationType.AUTO)
    private String orderId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private Double totalAmount;
    @Enumerated(value = EnumType.STRING)
    private StatusClass status;

    private LocalDateTime createdAt;
    private String userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paymentId", referencedColumnName = "paymentId")
    private PaymentClass paymentClass;


//    public OrderClass(String orderId, String productName, Integer quantity, Double totalAmount, StatusClass status, LocalDateTime createdAt, String userId, PaymentClass paymentClass) {
//        this.orderId = orderId;
//        this.productName = productName;
//        this.quantity = quantity;
//        this.totalAmount = totalAmount;
//        this.status = status;
//        this.createdAt = createdAt;
//        this.userId = userId;
//        this.paymentClass = paymentClass;
//    }
}
