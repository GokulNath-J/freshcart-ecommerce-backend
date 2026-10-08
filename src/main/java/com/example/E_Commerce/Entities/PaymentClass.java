package com.example.E_Commerce.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        indexes = {
                @Index(name = "payment_idx", columnList = "paymentId")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentClass {

    @Id
//    @SequenceGenerator(name = "seq", sequenceName = "paymentSeq", initialValue = 1, allocationSize = 1)
//    @GeneratedValue(generator = "seq", strategy = GenerationType.AUTO)
    private String paymentId;
    private String userId;
    private Integer walletId;
    private Integer productId;
    private Double amountToPay;
    private Double amountPaid;

    private LocalDateTime paymentRequestDateTime;
    private LocalDateTime paymentDateTime;

    @Enumerated(value = EnumType.STRING)
    private StatusClass status;

    @OneToOne(mappedBy = "paymentClass")
    private OrderClass OrderClass;

    public PaymentClass(String paymentId, Integer walletId, Double amountPaid, LocalDateTime paymentDateTime, StatusClass status) {
        this.paymentId = paymentId;
        this.walletId = walletId;
        this.amountPaid = amountPaid;
        this.paymentDateTime = paymentDateTime;
        this.status = status;
    }

    public PaymentClass(String paymentId, String userId, Integer productId, LocalDateTime paymentRequestDateTime, StatusClass status, Double amountToPay) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentRequestDateTime = paymentRequestDateTime;
        this.status = status;
        this.amountToPay = amountToPay;
        this.productId = productId;
    }
}
