package com.example.E_Commerce.ServicePac;

import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.Entities.ExpiredProducts;
import com.example.E_Commerce.Entities.PaymentClass;
import com.example.E_Commerce.Entities.ProductClass;
import com.example.E_Commerce.GlobalExceptionPac.ProductException;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.Repos.ExpiredProductsRepo;
import com.example.E_Commerce.Repos.ProductRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

//    @Autowired
//    private PaymentService paymentService;

    @Autowired
    private ExpiredProductsRepo expiredProductsRepo;

    @Autowired
    @Lazy
    private UserService userService;

    private static Double commonDiscountPercentage = 2.00;

    private final Logger log = LoggerFactory.getLogger(ProductService.class);


    public List<ProductDTO> addProduct(AddProductDTO addProductDTO) {
        UserDetailsDTO userDetailsDTO = userService.getLoggedInUserDetails();
        for (ProductDTO productDTO : addProductDTO.getProductDTOList()) {
            Integer daysBeforeToAddDiscount = null;
            if (productDTO.getExpireDate() != null) {
                daysBeforeToAddDiscount = 1;
            }
            ProductClass productClass = new ProductClass(userDetailsDTO.getUserId(), productDTO.getProductName()
                    , productDTO.getBrandName(), productDTO.getDescription(), productDTO.getCategory()
                    , productDTO.getQuantity(), productDTO.getPrice(), productDTO.getDiscountPercent(), LocalDateTime.now()
                    , productDTO.getExpireDate(), daysBeforeToAddDiscount);
            productRepo.save(productClass);
        }
        return addProductDTO.getProductDTOList();
    }

    public List<ProductDetailsDTO> searchProduct(String category) throws ProductException {
        Optional<List<ProductClass>> productClass = productRepo.findAllByCategory(category);
//        ProductClass productClass = productRepo.findAllByCategory(category).orElseThrow(() -> new ProductException(category + " Not Found"), PageRequest.of(page,size));
        if (productClass.get().size() == 0) {
            throw new ProductException("Product Not Found");
        }
        List<ProductDetailsDTO> dTOList = new LinkedList<>();
        for (ProductClass aClass : productClass.get()) {
            ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(aClass.getProductId(), aClass.getProductName(), aClass.getBrandName()
                    , aClass.getDescription(), aClass.getCategory(), aClass.getQuantity(), aClass.getPrice(),
                    aClass.getExpireDate(), aClass.getDiscountPercent(), null);
            dTOList.add(productDetailsDTO);
        }
        return dTOList;
    }


    public ProductDetailsDTO orderRequest(OrderRequestDTO orderRequestDTO) throws ProductException, UserException {
        log.info("Inside orderRequest");
        ProductClass productClass = productRepo.findByProductId(orderRequestDTO.getProductId())
                .orElseThrow(() -> new ProductException(orderRequestDTO.getProductId() + " Not Found"));
        if (productClass.getQuantity() >= orderRequestDTO.getQuantity()) {
            Double totalAmount = productClass.getPrice() * orderRequestDTO.getQuantity();
            if (productClass.getDiscountPercent() != null) {
                Double finalPriceAfterAddingDiscount = calculateDiscount(productClass.getDiscountPercent()
                        , orderRequestDTO.getQuantity(), totalAmount);
//                PaymentClass paymentClass = paymentService.generatePaymentID(userService.getLoggedInUserDetails().getUserId()
//                        , finalPriceAfterAddingDiscount, orderRequestDTO.getProductId());
//                orderService.saveOrder(productClass, userService.getLoggedInUserDetails().getUserId(), finalPriceAfterAddingDiscount
//                        , orderRequestDTO.getQuantity());
//                return new ProductAndPaymentDTO(productClass.getProductId(), productClass.getProductName()
//                        , productClass.getQuantity(), productClass.getDiscountPercent(), paymentClass);
//                return new OrderResponseDTO(paymentID, productClass.getProductId(), orderRequestDTO.getQuantity()
//                        , finalPriceAfterAddingDiscount);
                return new ProductDetailsDTO(productClass.getProductId(), productClass.getProductName()
                        , orderRequestDTO.getQuantity(), finalPriceAfterAddingDiscount);
            } else {
//                PaymentClass paymentClass = paymentService.generatePaymentID(userService.getLoggedInUserDetails().getUserId()
//                        , totalAmount, orderRequestDTO.getProductId());
//                return new ProductAndPaymentDTO(productClass.getProductId(), productClass.getProductName()
//                        , productClass.getQuantity(), productClass.getDiscountPercent(), paymentClass);
                return new ProductDetailsDTO(productClass.getProductId(), productClass.getProductName()
                        , orderRequestDTO.getQuantity(), totalAmount);
            }
        } else {
            throw new ProductException("Product Quntatiy is less");
        }

    }

    private Double calculateDiscount(Double discountPercent, Integer quantity, Double totalAmount) {
        Double discount = (discountPercent * quantity);
        Double totalDiscount = (totalAmount * discount) / 100;
        Double finalPrice = totalAmount - totalDiscount;
        return finalPrice;
    }
//    public PaymentAndProductDTO orderRequestToProduct(PlaceOrderDTO placeOrderDTO, Integer walletId) throws ProductException, UserException {
//        ProductClass productClass = productRepo.findByProductId(placeOrderDTO.getProductId())
//                .orElseThrow(() -> new ProductException(placeOrderDTO.getProductId() + " Not Found"));
//        LocalDate tomorrowDate = LocalDate.now().plusDays(1);
//        if (productClass.getQuantity() >= placeOrderDTO.getQuantity()) {
//            Double totalAmount = productClass.getPrice() * placeOrderDTO.getQuantity();
//            if (productClass.getDiscountPercent() != null &&
//                    (productClass.getExpireDate().equals(LocalDate.now()) || productClass.getExpireDate().equals(tomorrowDate))) {
//                Double discount = (productClass.getDiscountPercent() * placeOrderDTO.getQuantity());
//                Double totalDiscount = (totalAmount * discount) / 100;
//                Double finalPrice = totalAmount - totalDiscount;
//                PaymentClass paymentClass = paymentService.paymentRequest(walletId, placeOrderDTO.getAmount(), finalPrice);
//                productClass.setQuantity(productClass.getQuantity() - placeOrderDTO.getQuantity());
//                return sendPaymentAndProductDTO(productClass, placeOrderDTO, paymentClass, finalPrice);
//            } else {
//                PaymentClass paymentClass = paymentService.paymentRequest(walletId, placeOrderDTO.getAmount(), totalAmount);
//                productClass.setQuantity(productClass.getQuantity() - placeOrderDTO.getQuantity());
//                return sendPaymentAndProductDTO(productClass, placeOrderDTO, paymentClass, totalAmount);
//            }
//        } else {
//            throw new ProductException("Product Quntatiy is less");
//        }
//    }

    private PaymentAndProductDTO sendPaymentAndProductDTO(ProductClass productClass, OrderRequestDTO orderRequestDTO,
                                                          PaymentClass paymentClass, Double finalPrice) {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setProductName(productClass.getProductName());
        productDetailsDTO.setQuantity(orderRequestDTO.getQuantity());
        productDetailsDTO.setPrice(finalPrice);
        PaymentAndProductDTO paymentAndProductDTO = new PaymentAndProductDTO(productDetailsDTO, paymentClass);
        return paymentAndProductDTO;
    }

    public List<ProductDetailsDTO> searchProductBySeller() {
        UserDetailsDTO userDetailsDTO = userService.getLoggedInUserDetails();
        List<ProductClass> productClassList = productRepo.findAllBySellerId(userDetailsDTO.getUserId()).orElseThrow(() -> new ProductException("SellerId Not Found"));
        List<ProductDetailsDTO> dtoList = new LinkedList<>();
        for (ProductClass aClass : productClassList) {
            ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(aClass.getProductId(), aClass.getProductName(), aClass.getBrandName()
                    , aClass.getDescription(), aClass.getCategory(), aClass.getQuantity(), aClass.getPrice(),
                    aClass.getExpireDate(), aClass.getDiscountPercent(), null);
            dtoList.add(productDetailsDTO);
        }
        return dtoList;
    }

    @Transactional
    public void addingDiscountsToExpiringProducts() throws ProductException {
        List<ProductClass> productList = productRepo.findExpiringpProducts(LocalDate.now()).orElseThrow(
                () -> new ProductException("No Expiring Products"));
        System.out.println(productList);
        for (ProductClass productClass : productList) {
            productClass.setDiscountPercent(commonDiscountPercentage);
        }
    }

    public void removingExpiredProducts() {
        List<ProductClass> productList = productRepo.findExpiringpProducts(LocalDate.now()).orElseThrow(
                () -> new ProductException("No Expiring Products"));
        System.out.println(productList);
        List<ExpiredProducts> expiredProductslist = new LinkedList<>();
        for (ProductClass aClass : productList) {
            ExpiredProducts expiredProducts = new ExpiredProducts(aClass.getProductId(), aClass.getSellerId()
                    , aClass.getProductName(), aClass.getBrandName(), aClass.getDescription(), aClass.getCategory()
                    , aClass.getQuantity(), aClass.getPrice(), aClass.getExpireDate(), aClass.getDiscountPercent()
                    , aClass.getCreatedAt(), null);
            expiredProductslist.add(expiredProducts);
        }
        expiredProductsRepo.saveAll(expiredProductslist);
        productRepo.deleteAll(productList);
    }


    @Transactional
    public void orderToProduct(Integer productId, Integer quantity) {
        ProductClass productClass = productRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductException(productId + " Not Found"));
        if (productClass.getQuantity() >= quantity) {
            productClass.setQuantity(productClass.getQuantity() - quantity);
        } else {
            throw new ProductException("Product Quntatiy is less");
        }
    }

    @Transactional
    public String addDiscountToOneProduct(Integer productId, Double discount) {
        ProductClass productClass = productRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductException(productId + " Not Found"));
        productClass.setDiscountPercent(discount);
        return "Discount Added".concat(discount.toString());
    }

    @Transactional
    public String removeDiscount(Integer productId) {
        ProductClass productClass = productRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductException(productId + " Not Found"));
        productClass.setDiscountPercent(null);
        return "Discount Removed";
    }

    @Transactional
    public String removeProduct(Integer productId) {
        ProductClass productClass = productRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductException(productId + " Not Found"));
        if (productClass.getSellerId().equals(userService.getLoggedInUserDetails().getUserId())) {
            productRepo.delete(productClass);
            return "Product Removed";
        }
        return "You Cant remove other Seller Products";
    }

    public Page<ProductClass> searchProductByCategoryAndPaging(String category, int page, int size) {
        Page<ProductClass> productClasses = productRepo.findByCategory(category,PageRequest.of(page,size,Sort.by("category").ascending()));
        return productClasses;
    }
}
