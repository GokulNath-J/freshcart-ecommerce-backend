package com.example.E_Commerce.ControllerPac;


import com.example.E_Commerce.DTO.OrderRequestDTO;
import com.example.E_Commerce.DTO.OrderResponseDTO;
import com.example.E_Commerce.DTO.PaymentRequestDTO;
import com.example.E_Commerce.GlobalExceptionPac.ProductException;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.ServicePac.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {


    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/paymentRequest")
    public ResponseEntity<OrderResponseDTO> paymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO) throws UserException, ProductException {
        return new ResponseEntity<>(paymentService.paymentRequest(paymentRequestDTO), HttpStatus.OK);
    }
}
