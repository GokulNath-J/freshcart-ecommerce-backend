package com.example.E_Commerce.Scheduling;

import com.example.E_Commerce.ServicePac.ProductService;
import com.example.E_Commerce.ServicePac.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Data
@Component
public class ScheduledClass {

    private final Logger log = LoggerFactory.getLogger(ScheduledClass.class);

    @Autowired
    private ProductService productService;

    @Scheduled(cron = "0 0 12 * * *")
    public void addingDiscountsToExpiringProducts() {
        log.info("Inside addingDiscountsToExpiringProducts");
        productService.addingDiscountsToExpiringProducts();
    }

    @Scheduled(cron = "0 58 11 * * *")
    public void removingExpiredProducts() {
        log.info("Inside removingExpiredProducts");
        productService.removingExpiredProducts();
    }


}
