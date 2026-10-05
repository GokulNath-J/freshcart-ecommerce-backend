package com.example.E_Commerce.ServicePac;


import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.Entities.ProductClass;
import com.example.E_Commerce.Entities.UserClass;
import com.example.E_Commerce.Entities.WalletClass;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.Repos.UserRepo;
import com.example.E_Commerce.Repos.WalletRepo;
import com.example.E_Commerce.SecurityPac.JwtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private UserRepo userRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    private AuthenticationManager authenticationManager;

    private JwtClass jwtClass;

    private WalletRepo walletRepo;

    private ProductService productService;

    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, JwtClass jwtClass, WalletRepo walletRepo, ProductService productService) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtClass = jwtClass;
        this.walletRepo = walletRepo;
        this.productService = productService;
    }

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public ResponseEntity<List<UserClass>> getAllUser() {
        List<UserClass> userClassList = userRepo.findAll();
        return ResponseEntity.ok(userClassList);
    }


    public String createNewUser(RegisterUserDTO registerUserDTO) {
        UserClass userClass = new UserClass();
        String userId = registerUserDTO.getUserName().concat(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
        userClass.setUserName(registerUserDTO.getUserName());
        userClass.setUserId(userId);
        userClass.setRole("USER");
        userClass.setCreated_at(LocalDateTime.now());
        userClass.setPassword(encoder.encode(registerUserDTO.getPassword()));
        WalletClass walletClass = new WalletClass();
        walletClass.setBalance(0.00);
        walletClass.setUserId(userId);
        userClass.setWalletClass(walletClass);
        userRepo.save(userClass);
        return "USER CREATED : " + userId;
    }

    public ResponseEntity<String> login(String userID, String password) throws UserException {

        log.info("Inside login");

        UserClass userClass = userRepo.findByUserId(userID).orElseThrow(() -> new UserException("Exception"));

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


    public String createNewAdmin(RegisterAdminDTO registerAdminDTO) {
        UserClass userClass = new UserClass();
        String userId = registerAdminDTO.getUserName().concat(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
        userClass.setUserName("ADMIN ".concat(registerAdminDTO.getUserName()));
        userClass.setUserId(userId);
        userClass.setRole("ADMIN");
        userClass.setCreated_at(LocalDateTime.now());
        userClass.setPassword(encoder.encode(registerAdminDTO.getPassword()));
        WalletClass walletClass = new WalletClass();
        walletClass.setBalance(0.00);
        walletClass.setUserId(userId);
        userClass.setWalletClass(walletClass);
        userRepo.save(userClass);
        return "ADMIN CREATED : " + userId;

    }

    public String addMoneyToWallet(WalletDTO walletDTO) {
        WalletClass walletClass = walletRepo.findByUserId(walletDTO.getUserId()).orElseThrow(() -> new RuntimeException("Exception"));
        Double totalAmount = walletClass.getBalance() + walletDTO.getAmount();
        walletClass.setBalance(totalAmount);
        walletRepo.save(walletClass);
        return "Amount Added : ".concat(totalAmount.toString());
    }

    public String addSeller(AddSellerDTO addSellerDTO) {
        UserClass userClass = new UserClass();
        String userId = addSellerDTO.getUserName().concat(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
        userClass.setUserName("SELLER ".concat(addSellerDTO.getUserName()));
        userClass.setUserId(userId);
        userClass.setRole("SELLER");
        userClass.setCreated_at(LocalDateTime.now());
        userClass.setPassword(encoder.encode(addSellerDTO.getPassword()));
        WalletClass walletClass = new WalletClass();
        walletClass.setBalance(0.00);
        walletClass.setUserId(userId);
        userClass.setWalletClass(walletClass);
        userRepo.save(userClass);
        return "SELLER CREATED : " + userId;
    }

    public List<ProductDetailsDTO> searchProductBycCategory(String category) {
        return productService.searchProduct(category);
    }

    public UserDetailsDTO getLoggedInUserDetails() {
        UserClass userClass = userRepo.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        UserDetailsDTO dto = new UserDetailsDTO(userClass.getUserId(), userClass.getUserName(), userClass.getWalletClass(),
                userClass.getOrderClass());
        return dto;
    }

    public Page<ProductClass> searchProductByCategoryAndPaging(String category, int page, int size) {
        return productService.searchProductByCategoryAndPaging(category, page, size);
    }

    public ProductDetailsDTO searchProductByName(Integer productName) {
        return productService.searchProductId(productName);
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

