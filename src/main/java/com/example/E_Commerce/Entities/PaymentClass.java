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
public class PaymentClass {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "paymentSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.AUTO)
    private Integer paymentId;
    private Integer walletId;
    private Double amount;
    private LocalDateTime paymentDateTime;
    private StatusClass status;

    @OneToOne(mappedBy = "paymentClass")
    private OrderClass OrderClass;
}
