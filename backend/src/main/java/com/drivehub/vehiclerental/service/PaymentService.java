package com.drivehub.vehiclerental.service;

import com.drivehub.vehiclerental.dto.PaymentRequest;

/**
 * Service interface for payment operations
 */
public interface PaymentService {
    
    /**
     * Process payment using strategy pattern
     * 
     * @param paymentRequest Payment request
     * @return Payment confirmation message
     */
    String processPayment(PaymentRequest paymentRequest);
}
