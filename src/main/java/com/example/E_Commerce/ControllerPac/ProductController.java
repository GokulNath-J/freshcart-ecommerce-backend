package com.example.E_Commerce.ControllerPac;

import com.example.E_Commerce.DTO.AddProductDTO;
import com.example.E_Commerce.DTO.ProductDTO;
import com.example.E_Commerce.DTO.ProductDetailsDTO;
import com.example.E_Commerce.ServicePac.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {


    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/searchProductBySeller")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDetailsDTO>> searchProductBySeller() {
        List<ProductDetailsDTO> result = productService.searchProductBySeller();
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDTO>> addProduct(@RequestBody AddProductDTO addProductDTO) {
        List<ProductDTO> result = productService.addProduct(addProductDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/addDiscountToOneProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> addDiscountToOneProduct(@RequestParam Integer productId, @RequestParam Double discount) {
        String result = productService.addDiscountToOneProduct(productId, discount);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/removeDiscount")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> removeDiscount(@RequestParam Integer productId) {
        String result = productService.removeDiscount(productId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/removeProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> removeProduct(@RequestParam Integer productId) {
        String result = productService.removeProduct(productId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


}
