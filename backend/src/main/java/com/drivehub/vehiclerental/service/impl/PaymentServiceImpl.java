package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.PaymentRequest;
import com.drivehub.vehiclerental.entity.Booking;
import com.drivehub.vehiclerental.entity.Booking.BookingStatus;
import com.drivehub.vehiclerental.repository.BookingRepository;
import com.drivehub.vehiclerental.service.PaymentService;
import com.drivehub.vehiclerental.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of PaymentService using Strategy Pattern
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    
    private final BookingRepository bookingRepository;
    private final List<PaymentStrategy> paymentStrategies;
    
    @Override
    @Transactional
    public String processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for booking: {}", paymentRequest.getBookingId());
        
        // Get booking
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        
        // Validate booking status
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Booking is not in a valid state for payment");
        }
        
        // Validate payment amount
        if (!paymentRequest.getAmount().equals(booking.getTotalCost())) {
            throw new IllegalArgumentException("Payment amount does not match booking total cost");
        }
        
        // Find payment strategy
        PaymentStrategy paymentStrategy = paymentStrategies.stream()
            .filter(strategy -> strategy.getPaymentMethod().equalsIgnoreCase(paymentRequest.getPaymentMethod()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid payment method"));
        
        // Process payment
        String result = paymentStrategy.processPayment(paymentRequest);
        
        // Update booking status
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentMethod(paymentRequest.getPaymentMethod());
        bookingRepository.save(booking);
        
        log.info("Payment processed successfully for booking: {}", paymentRequest.getBookingId());
        
        return result;
    }
}
