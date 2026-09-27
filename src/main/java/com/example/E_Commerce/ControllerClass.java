package com.example.E_Commerce;


import com.example.E_Commerce.DTO.NewAdminDTO;
import com.example.E_Commerce.DTO.NewUserDTO;
import com.example.E_Commerce.Entities.UserClass;
import com.example.E_Commerce.ServicePac.Serviceclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cus")
public class ControllerClass {

    @Autowired
    private Serviceclass serviceclass;

  /*  @Autowired
    private OrderService orderService;*/

    @GetMapping("/welcome")
    @PreAuthorize("hasRole('USER')")
    //@PostAuthorize("returnObject.username == authentication.name")
    public String welcomeMessage() {
        return "Welcome to the shopping";
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

    //
    @GetMapping("/getAllCustomer")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<UserClass>> getAllCustomer() {
        return serviceclass.getAllCustomer();
    }

   /* @PostMapping("/addproducts")
    public ResponseEntity<String> addproducts(@RequestBody OrderedProduct product){
        return serviceclass.addproducts(product);
    }*/


   /* @PostMapping("/placeorder/{name}/{amount}")
    public ResponseEntity<String> placeOrder1(@PathVariable String name,@PathVariable int amount){
       return orderService.placeOrder1(name,amount);
    }*/

/*    @GetMapping("/geterror")
    public void geterror() throws UnknownException {
        throw new UnknownException("Custome ExceptionHandler");
    }
    @GetMapping("/geterror1")
    public void geterror1() throws ArithmeticException {
        throw new ArithmeticException("Arithmetic ExceptionHan-dler");
    }
    @GetMapping("/geterror3")
    public void geterror3() throws UnknownException {
        throw new UnknownException("Custome ExceptionHandler");
    }*/

    /*@PostMapping("/order/{custId}/{prodId}")
    public ResponseEntity<String> orderProduct(@PathVariable int custId,@PathVariable int prodId){
        return serviceclass.orderProduct(custId,prodId);
    }*/

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userID, @RequestParam String password) {
        return serviceclass.login(userID, password);
    }

    @GetMapping("/test")
    @PostAuthorize("returnObject.body.customerName == authentication.name")
    public ResponseEntity<UserClass> testing() {
        return serviceclass.test();
    }

//    @PostMapping("/addAdmin")
//    public ResponseEntity<String> addOneAdmin(@RequestBody AdminClass adminClass) {
//        String result = serviceclass.addOneAdmin(adminClass);
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
}
