package com.drivehub.vehiclerental.service;

import com.drivehub.vehiclerental.dto.PaymentRequest;

import java.math.BigDecimal;

/**
 * Payment Strategy interface for implementing different payment methods
 */
public interface PaymentStrategy {
    
    /**
     * Process payment using specific payment method
     * 
     * @param paymentRequest Payment details
     * @return Payment confirmation message
     */
    String processPayment(PaymentRequest paymentRequest);
    
    /**
     * Validate payment details
     * 
     * @param paymentRequest Payment details
     * @return true if valid, false otherwise
     */
    boolean validatePayment(PaymentRequest paymentRequest);
    
    /**
     * Get payment method name
     * 
     * @return Payment method name
     */
    String getPaymentMethod();
}
