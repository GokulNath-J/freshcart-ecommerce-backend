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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepo productRepo;

    private ExpiredProductsRepo expiredProductsRepo;

    @Autowired
    @Lazy
    private UserService userService;

    public ProductService(ProductRepo productRepo, ExpiredProductsRepo expiredProductsRepo) {
        this.productRepo = productRepo;
        this.expiredProductsRepo = expiredProductsRepo;
    }

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
                return new ProductDetailsDTO(productClass.getProductId(), productClass.getProductName()
                        , orderRequestDTO.getQuantity(), finalPriceAfterAddingDiscount);
            } else {
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

    @Transactional
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
    @CachePut(cacheNames = "Product", key = "#productId")
    public void orderToProduct(Integer productId, Integer quantity) throws ProductException{
        try {
            ProductClass productClass = productRepo.findByProductId(productId)
                    .orElseThrow(() -> new ProductException(productId + " Not Found"));
            if (productClass.getQuantity() >= quantity) {
                try {
                    log.info("Inside Thread 5000");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                productClass.setQuantity(productClass.getQuantity() - quantity);
            } else {
                throw new ProductException("Product Quntatiy is less");
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ProductException("Stock just changed, please try again");
        }
    }

    @Transactional
    @CachePut(cacheNames = "Product", key = "#productId")
    public String addDiscountToOneProduct(Integer productId, Double discount) throws UserException {
        ProductClass productClass = productRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductException(productId + " Not Found"));
        if (productClass.getSellerId().equals(userService.getLoggedInUserDetails().getUserId())) {
            productClass.setDiscountPercent(discount);
        } else {
            throw new UserException("Different User");
        }
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
    @CacheEvict(cacheNames = "Product", key = "#productId")
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
        Page<ProductClass> productClasses = productRepo.findByCategory(category, PageRequest.of(page, size, Sort.by("category").ascending()));
        return productClasses;
    }

    @Cacheable(cacheNames = "Product", key = "#productId")
    public ProductDetailsDTO searchProductId(Integer productId) {
        log.info("Inside searchProductByName(String productName)");
        ProductClass aClass = productRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductException(productId + " Not Found"));
        return new ProductDetailsDTO(aClass.getProductId(), aClass.getProductName(), aClass.getBrandName()
                , aClass.getDescription(), aClass.getCategory(), aClass.getQuantity(), aClass.getPrice(),
                aClass.getExpireDate(), aClass.getDiscountPercent(), null);
    }
}
