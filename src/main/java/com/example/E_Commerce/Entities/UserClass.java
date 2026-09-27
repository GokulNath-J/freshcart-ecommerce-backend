package com.example.E_Commerce.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_table")
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


}
