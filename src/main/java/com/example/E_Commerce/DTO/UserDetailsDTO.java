package com.example.E_Commerce.DTO;

import com.example.E_Commerce.Entities.OrderClass;
import com.example.E_Commerce.Entities.WalletClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private String userId;

    private String userName;

    private WalletClass walletClass;

    private List<OrderClass> orderClass;
}
