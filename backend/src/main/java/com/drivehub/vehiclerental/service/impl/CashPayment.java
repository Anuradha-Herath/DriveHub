package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.PaymentRequest;
import com.drivehub.vehiclerental.service.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Cash payment implementation of PaymentStrategy
 */
@Slf4j
@Component
public class CashPayment implements PaymentStrategy {
    
    @Override
    public String processPayment(PaymentRequest paymentRequest) {
        log.info("Processing cash payment for booking: {}", paymentRequest.getBookingId());
        
        if (!validatePayment(paymentRequest)) {
            throw new IllegalArgumentException("Invalid cash payment details");
        }
        
        // Simulate cash payment processing
        log.info("Cash payment of ${} received successfully", paymentRequest.getAmount());
        
        return "Cash payment of $" + paymentRequest.getAmount() + 
               " received successfully. Please collect receipt at counter.";
    }
    
    @Override
    public boolean validatePayment(PaymentRequest paymentRequest) {
        // For cash payment, we just need to ensure amount is positive
        return paymentRequest.getAmount() != null && 
               paymentRequest.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0;
    }
    
    @Override
    public String getPaymentMethod() {
        return "CASH";
    }
}
