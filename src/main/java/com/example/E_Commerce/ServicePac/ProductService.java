package com.example.E_Commerce.ServicePac;

import com.example.E_Commerce.DTO.AddProductDTO;
import com.example.E_Commerce.DTO.ProductDTO;
import com.example.E_Commerce.DTO.ProductDetailsDTO;
import com.example.E_Commerce.Entities.ProductClass;
import com.example.E_Commerce.GlobalExceptionPac.ProductException;
import com.example.E_Commerce.Repos.ProductRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepo productRepo;

    public List<ProductDTO> addOneProduct(AddProductDTO addProductDTO) {
        for (ProductDTO productDTO : addProductDTO.getProductDTOList()) {
            ProductClass productClass = new ProductClass();
            productClass.setCategory(productDTO.getCategory());
            productClass.setCreatedAt(LocalDateTime.now());
            productClass.setDescription(productDTO.getDescription());
            productClass.setDiscountPercent(productDTO.getDiscountPercent());
            productClass.setExpireDate(productDTO.getExpireDate());
            productClass.setPrice(productDTO.getPrice());
            productClass.setProductName(productDTO.getProductName());
            productClass.setBrandName(productDTO.getProductName());
            productClass.setQuantity(productDTO.getQuantity());
            productRepo.save(productClass);
        }
        return addProductDTO.getProductDTOList();
    }

    public List<ProductDetailsDTO> searchProduct(String productName) throws ProductException {
        Optional<List<ProductClass>> productClass = productRepo.findByProductName(productName);
        if (productClass.get().size() == 0) {
            throw new ProductException("Product Not Found");
        }
        List<ProductDetailsDTO> dTOList = new LinkedList<>();
        for (ProductClass aClass : productClass.get()) {
            ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
            productDetailsDTO.setCategory(aClass.getCategory());
            productDetailsDTO.setDescription(aClass.getDescription());
            productDetailsDTO.setDiscountPercent(aClass.getDiscountPercent());
            productDetailsDTO.setExpireDate(aClass.getExpireDate());
            productDetailsDTO.setPrice(aClass.getPrice());
            productDetailsDTO.setProductName(aClass.getProductName());
            productDetailsDTO.setBrandName(aClass.getProductName());
            productDetailsDTO.setQuantity(aClass.getQuantity());
            dTOList.add(productDetailsDTO);
        }
        return dTOList;
    }
}
