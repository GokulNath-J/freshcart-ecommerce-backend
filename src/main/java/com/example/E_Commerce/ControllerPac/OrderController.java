package com.example.E_Commerce.ControllerPac;

import com.example.E_Commerce.DTO.OrderRequestDTO;
import com.example.E_Commerce.DTO.OrderResponseDTO;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.ServicePac.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orderRequest")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws UserException {
        return new ResponseEntity<>(orderService.orderRequest(orderRequestDTO), HttpStatus.OK);
    }

//    @PostMapping("/testplaceOrder")
//    public ResponseEntity<OrderDetailsDTO> testplaceOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws UserException {
//        return new ResponseEntity<>(orderService.orderRequest(orderRequestDTO), HttpStatus.OK);
//    }


}
