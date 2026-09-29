package com.example.E_Commerce.ControllerPac;

import com.example.E_Commerce.DTO.AddProductDTO;
import com.example.E_Commerce.DTO.ProductDTO;
import com.example.E_Commerce.ServicePac.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDTO>> addOneProduct(@RequestBody AddProductDTO addProductDTO) {
        List<ProductDTO> result = productService.addOneProduct(addProductDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


}
