package com.example.E_Commerce.ControllerPac;


import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.Entities.ProductClass;
import com.example.E_Commerce.Entities.UserClass;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.ServicePac.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userID, @RequestParam String password) throws UserException {
        return userService.login(userID, password);
    }

    @PostMapping("/createNewuser")
    public ResponseEntity<String> createNewUser(@RequestBody RegisterUserDTO registerUserDTO) {
        String result = userService.createNewUser(registerUserDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/createNewAdmin")
    public ResponseEntity<String> createNewAdmin(@RequestBody RegisterAdminDTO registerAdminDTO) {
        String result = userService.createNewAdmin(registerAdminDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/addMoneyToWallet")
    public ResponseEntity<String> addMoneyToWallet(@RequestBody WalletDTO walletDTO) {
        String result = userService.addMoneyToWallet(walletDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addSeller")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addSeller(@RequestBody AddSellerDTO addSellerDTO) {
        String result = userService.addSeller(addSellerDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/searchProductByCategory")
    public ResponseEntity<List<ProductDetailsDTO>> searchProductBycCategory(@RequestParam String category) {
        return new ResponseEntity<>(userService.searchProductBycCategory(category), HttpStatus.FOUND);
    }

    @PostMapping("/productCategory")
    public ResponseEntity<Page<ProductClass>> searchProductByCategoryAndPaging(@RequestParam String category
            , @RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(userService.searchProductByCategoryAndPaging(category, page, size), HttpStatus.FOUND);
    }

    @PostMapping("/searchProductByName")
    public ResponseEntity<ProductDetailsDTO> searchProductByName(@RequestParam Integer productId) {
        return new ResponseEntity<>(userService.searchProductByName(productId), HttpStatus.FOUND);
    }


    @GetMapping("/getLoggedInUserDetails")
    public ResponseEntity<UserDetailsDTO> getLoggedInUserDetails() {
        UserDetailsDTO result = userService.getLoggedInUserDetails();
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

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

    @GetMapping("/getAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserClass>> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/test")
    @PostAuthorize("returnObject.body.customerName == authentication.name")
    public ResponseEntity<UserClass> testing() {
        return userService.test();
    }


//    @PostMapping("/addAdmin")
//    public ResponseEntity<String> addOneAdmin(@RequestBody AdminClass adminClass) {
//        String result = serviceclass.addOneAdmin(adminClass);
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
}
