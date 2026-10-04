package com.example.E_Commerce.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletClass {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "walletSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.AUTO)
    private Integer walletId;

    private Double balance;

    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private String userId;


}
