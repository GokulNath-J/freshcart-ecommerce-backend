package com.example.E_Commerce.ServicePac;

import com.example.E_Commerce.DTO.OrderResponseDTO;
import com.example.E_Commerce.DTO.PaymentRequestDTO;
import com.example.E_Commerce.Entities.PaymentClass;
import com.example.E_Commerce.Entities.StatusClass;
import com.example.E_Commerce.Entities.WalletClass;
import com.example.E_Commerce.GlobalExceptionPac.PaymentException;
import com.example.E_Commerce.GlobalExceptionPac.UserException;
import com.example.E_Commerce.Repos.PaymentRepo;
import com.example.E_Commerce.Repos.WalletRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {


    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private OrderService orderService;

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Transactional(rollbackOn = Exception.class)
    public OrderResponseDTO paymentRequest(PaymentRequestDTO paymentRequestDTO) throws UserException {
        PaymentClass payment = paymentRepo.findByPaymentId(paymentRequestDTO.getPaymentId())
                .orElseThrow(() -> new PaymentException("PaymentId Not Found"));
        WalletClass walletClass = walletRepo.findByWalletId(userService.getLoggedInUserDetails()
                        .getWalletClass().getWalletId())
                .orElseThrow(() -> new UserException("Wallet Not Found"));
        if (walletClass.getBalance() >= paymentRequestDTO.getAmount()) {
            log.info("Amount {} AmountToPay {}", paymentRequestDTO.getAmount(), payment.getAmountToPay());
            if (paymentRequestDTO.getAmount().equals(payment.getAmountToPay())) {
                log.info("Amount {} AmountToPay {}", paymentRequestDTO.getAmount(), payment.getAmountToPay());
                walletClass.setBalance(walletClass.getBalance() - paymentRequestDTO.getAmount());
                payment.setAmountPaid(paymentRequestDTO.getAmount());
                payment.setPaymentDateTime(LocalDateTime.now());
                payment.setStatus(StatusClass.SUCCESS);
                payment.setWalletId(walletClass.getWalletId());
                paymentRepo.save(payment);
                return orderService.paymentToOrder(payment, paymentRequestDTO.getPaymentId());
            } else {
                throw new UserException("Total amount is Not equal!");
            }
        } else {
            throw new UserException("Wallet ID : " + walletClass.getWalletId() + " : Money is Not Enough");
        }
    }

    public PaymentClass generatePaymentID(String userId, Double finalPriceAfterAddingDiscount, Integer productId) {
        log.info("Inside generatePaymentID");
        String paymentId = UUID.randomUUID().toString().replace("-", "");
        PaymentClass paymentClass = new PaymentClass(paymentId, userId, productId, LocalDateTime.now(), StatusClass.PENDING, finalPriceAfterAddingDiscount);
        paymentRepo.save(paymentClass);
        return paymentClass;
    }
}
