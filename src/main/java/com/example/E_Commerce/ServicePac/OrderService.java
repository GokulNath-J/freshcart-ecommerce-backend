package com.example.E_Commerce.ServicePac;

import com.example.E_Commerce.DTO.*;
import com.example.E_Commerce.Entities.OrderClass;
import com.example.E_Commerce.Entities.PaymentClass;
import com.example.E_Commerce.Entities.ProductClass;
import com.example.E_Commerce.Entities.StatusClass;
import com.example.E_Commerce.GlobalExceptionPac.OrderException;
import com.example.E_Commerce.GlobalExceptionPac.ProductException;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.Repos.OrderRepo;
import com.example.E_Commerce.Repos.PaymentRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    private ProductService productService;

    private OrderRepo orderRepo;

    private UserService userService;

    private PaymentService paymentService;

    public OrderService(ProductService productService, OrderRepo orderRepo, UserService userService, PaymentService paymentService) {
        this.productService = productService;
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    private final Logger log = LoggerFactory.getLogger(OrderService.class);


    public OrderResponseDTO orderRequest(OrderRequestDTO orderRequestDTO) throws ProductException, UserException {
        log.info("Inside");
//        ProductAndPaymentDTO productAndPaymentDTO = productService.orderRequest(orderRequestDTO);
        ProductDetailsDTO dto = productService.orderRequest(orderRequestDTO);
        PaymentClass paymentClass = paymentService.generatePaymentID(userService.getLoggedInUserDetails().getUserId()
                , dto.getTotalPriceAfterDiscount(), orderRequestDTO.getProductId());
        String orderId = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime localDateTime = LocalDateTime.now();
        OrderClass orderClass = new OrderClass(orderId, dto.getProductId()
                , dto.getProductName(), dto.getQuantity()
                , dto.getTotalPriceAfterDiscount(), StatusClass.PLACED, localDateTime
                , userService.getLoggedInUserDetails().getUserId()
                , paymentClass);
        orderRepo.save(orderClass);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(orderId, paymentClass.getPaymentId()
                , dto.getProductId(), dto.getQuantity()
                , dto.getTotalPriceAfterDiscount(), StatusClass.PLACED, localDateTime);
        return orderResponseDTO;
    }

    @Transactional
    public void saveOrder(ProductClass productClass, String userId, Double finalPriceAfterAddingDiscount, Integer quantity) {
        String orderId = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime localDateTime = LocalDateTime.now();
        OrderClass orderClass = new OrderClass(orderId, productClass.getProductId(), productClass.getProductName(), quantity
                , finalPriceAfterAddingDiscount, StatusClass.PLACED, localDateTime, userId
                , null);
        orderRepo.save(orderClass);
    }

    @Transactional
    public OrderResponseDTO paymentToOrder(PaymentClass payment, String paymentId) throws ProductException {
        OrderClass orderClass = orderRepo.findByPaymentClassPaymentId(paymentId)
                .orElseThrow(() -> new OrderException("PaymentId Not Found In Orders"));
        orderClass.setStatus(StatusClass.CONFIRMED);
        LocalDateTime now = LocalDateTime.now();
        orderClass.setCreatedAt(LocalDateTime.now());
        productService.orderToProduct(orderClass.getProductId(), orderClass.getQuantity());
        return new OrderResponseDTO(orderClass.getOrderId(), orderClass.getPaymentClass().getPaymentId()
                , orderClass.getProductId(), orderClass.getQuantity(), orderClass.getTotalAmount(), StatusClass.CONFIRMED
                , now);
    }
//    @Transactional
//    public OrderDetailsDTO placeOrder(OrderRequestDTO orderRequestDTO) throws ProductException, UserException {
//        UserDetailsDTO dto = userService.getLoggedInUserDetails();
//
//        PaymentAndProductDTO paymentAndProductDTO = productService.orderRequest(orderRequestDTO, dto.getWalletClass().getWalletId());
//
//        ProductDetailsDTO productDetailsDTO = paymentAndProductDTO.getProductDetailsDTO();
//
//        String orderId = UUID.randomUUID().toString().replace("-", "");
//        LocalDateTime localDateTime = LocalDateTime.now();
//        OrderClass orderClass = new OrderClass(orderId, productDetailsDTO.getProductName(), productDetailsDTO.getQuantity()
//                , productDetailsDTO.getPrice(), StatusClass.CONFIRMED, localDateTime, dto.getUserId()
//                , paymentAndProductDTO.getPaymentClass());
//        orderRepo.save(orderClass);
//        OrderDetailsDTO dto2 = new OrderDetailsDTO(orderId, productDetailsDTO.getProductName()
//                , productDetailsDTO.getQuantity(), productDetailsDTO.getPrice(), StatusClass.CONFIRMED, localDateTime);
//        return dto2;
//
//    }
}
