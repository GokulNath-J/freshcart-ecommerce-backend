package com.example.E_Commerce.ServicePac;


import com.example.E_Commerce.DTO.NewAdminDTO;
import com.example.E_Commerce.DTO.NewUserDTO;
import com.example.E_Commerce.Entities.UserClass;
import com.example.E_Commerce.Repos.UserRepo;
import com.example.E_Commerce.SecurityPac.JwtClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class Serviceclass {

    @Autowired
    private UserRepo userRepo;

//    @Autowired
//    private OrderClassRepo orderClassRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtClass jwtClass;

//    @Autowired
//    private RolesRepo rolesRepo;


   /* @Autowired
    private ProductRepo productRepo;*/

    public ResponseEntity<List<UserClass>> getAllCustomer() {
        List<UserClass> userClassList = userRepo.findAll();
        return ResponseEntity.ok(userClassList);
    }


    public String createNewUser(NewUserDTO newUserDTO) {
        UserClass userClass = new UserClass();
        String userId = newUserDTO.getUserName().concat(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
        userClass.setUserName(newUserDTO.getUserName());
        userClass.setUserId(userId);
        userClass.setRole("USER");
        userClass.setCreated_at(LocalDateTime.now());
        userClass.setPassword(encoder.encode(newUserDTO.getPassword()));
        userRepo.save(userClass);
        return "USER CREATED : " + userId;
    }

    public ResponseEntity<String> login(String userID, String password) {

        UserClass userClass = userRepo.findByUserID(userID).orElseThrow(() -> new RuntimeException("Exception"));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(userClass.getUserName(), password);

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentication.isAuthenticated()) {
            List authorityList = (List) authentication.getAuthorities();
            String role = authorityList.get(0).toString();
            String token = jwtClass.generateToken(userClass.getUserName(), role);
            System.out.println(token);
            return ResponseEntity.ok("Token : " + token);
        } else {
            return ResponseEntity.ofNullable("User Not found");
        }

    }

    public ResponseEntity<UserClass> test() {
        UserClass userClass = userRepo.findByUserName("Isac.Hessel58");
        return new ResponseEntity<>(userClass, HttpStatus.FOUND);
    }

//    public String addOneAdmin(AdminClass adminClass) {
//        adminClass.setRole("ADMIN");
//        adminRepo.save(adminClass);
//        return "Admin CREATED";
//    }

    public String createNewAdmin(NewAdminDTO newAdminDTO) {
        UserClass userClass = new UserClass();
        String userId = newAdminDTO.getUserName().concat(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
        userClass.setUserName("ADMIN ".concat(newAdminDTO.getUserName()));
        userClass.setUserId(userId);
        userClass.setRole("ADMIN");
        userClass.setCreated_at(LocalDateTime.now());
        userClass.setPassword(encoder.encode(newAdminDTO.getPassword()));
        userRepo.save(userClass);
        return "ADMIN CREATED : " + userId;

    }

   /* public ResponseEntity<String> orderProduct(int custId, int prodId) {
        ProductWrapper productWrapper = productFeign.getproduct(prodId).getBody();
        OrderedProduct orderedProduct = new OrderedProduct();
        orderedProduct.setProdID(productWrapper.getProdID());
        orderedProduct.setProdName(productWrapper.getProdName());
        orderedProduct.setProdPrice(productWrapper.getProdPrice());
        List<OrderedProduct> orderedProductList = List.of(orderedProduct);
        OrderClass orderClass = new OrderClass();
        orderClass.setCustomer_id(custId);
        orderClass.setOrderedProductList(orderedProductList);
        if (productWrapper == null) {
            return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
        }
        orderClassRepo.save(orderClass);
        return ResponseEntity.ok("Order Placed Successfully");
    }*/
}

   /* public ResponseEntity<String> addproducts(OrderedProduct product) {
        productRepo.save(product);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }*/

