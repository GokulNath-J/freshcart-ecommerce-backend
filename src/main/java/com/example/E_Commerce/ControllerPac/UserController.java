package com.example.E_Commerce.ControllerPac;


import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.Entities.UserClass;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.ServicePac.Serviceclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Serviceclass serviceclass;

    @GetMapping("/welcome")
    @PreAuthorize("hasRole('USER')")
    public String welcomeMessage() {
        return "Welcome to the shopping";
    }

    @GetMapping("/adminTest")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "Admin LoggedIn";
    }

    @PostMapping("/createNewuser")
    public ResponseEntity<String> createNewUser(@RequestBody NewUserDTO newUserDTO) {
        String result = serviceclass.createNewUser(newUserDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/createNewAdmin")
    public ResponseEntity<String> createNewAdmin(@RequestBody NewAdminDTO newAdminDTO) {
        String result = serviceclass.createNewAdmin(newAdminDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @GetMapping("/getAllCustomer")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<UserClass>> getAllCustomer() {
        return serviceclass.getAllCustomer();
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userID, @RequestParam String password) throws UserException {
        return serviceclass.login(userID, password);
    }

    @GetMapping("/test")
    @PostAuthorize("returnObject.body.customerName == authentication.name")
    public ResponseEntity<UserClass> testing() {
        return serviceclass.test();
    }


    @PostMapping("/addMoneyToWallet")
    public ResponseEntity<String> addMoneyToWallet(@RequestBody WalletDTO walletDTO) {
        String result = serviceclass.addMoneyToWallet(walletDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addSeller")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addSeller(@RequestBody AddSellerDTO addSellerDTO) {
        String result = serviceclass.addSeller(addSellerDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/searchProduct")
    public ResponseEntity<List<ProductDetailsDTO>> searchProduct(@RequestParam String productName) {
        return new ResponseEntity<>(serviceclass.searchProduct(productName), HttpStatus.FOUND);
    }

//    @PostMapping("/addAdmin")
//    public ResponseEntity<String> addOneAdmin(@RequestBody AdminClass adminClass) {
//        String result = serviceclass.addOneAdmin(adminClass);
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
}
