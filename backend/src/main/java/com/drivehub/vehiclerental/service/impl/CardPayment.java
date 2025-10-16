package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.PaymentRequest;
import com.drivehub.vehiclerental.service.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Card payment implementation of PaymentStrategy
 */
@Slf4j
@Component
public class CardPayment implements PaymentStrategy {
    
    @Override
    public String processPayment(PaymentRequest paymentRequest) {
        log.info("Processing card payment for booking: {}", paymentRequest.getBookingId());
        
        // Simulate card payment processing
        if (!validatePayment(paymentRequest)) {
            throw new IllegalArgumentException("Invalid card payment details");
        }
        
        // In real implementation, integrate with payment gateway (Stripe, PayPal, etc.)
        String maskedCardNumber = maskCardNumber(paymentRequest.getCardNumber());
        
        log.info("Card payment processed successfully. Card: {}", maskedCardNumber);
        return "Payment of $" + paymentRequest.getAmount() + 
               " processed successfully via Card (" + maskedCardNumber + ")";
    }
    
    @Override
    public boolean validatePayment(PaymentRequest paymentRequest) {
        // Validate card details
        if (paymentRequest.getCardNumber() == null || paymentRequest.getCardNumber().length() < 13) {
            return false;
        }
        if (paymentRequest.getCardHolderName() == null || paymentRequest.getCardHolderName().isEmpty()) {
            return false;
        }
        if (paymentRequest.getExpiryDate() == null || paymentRequest.getExpiryDate().isEmpty()) {
            return false;
        }
        if (paymentRequest.getCvv() == null || paymentRequest.getCvv().length() != 3) {
            return false;
        }
        return true;
    }
    
    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
