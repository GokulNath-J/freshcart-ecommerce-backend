package com.example.E_Commerce.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        indexes = {
                @Index(name = "user_idx", columnList = "userId")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClass {

    @Id
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    private String role;

    private LocalDateTime created_at;

    @OneToOne(cascade = CascadeType.ALL)
    private WalletClass walletClass;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private List<OrderClass> orderClass = new LinkedList<>();


//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
//    private ProductClass productClass;
}
