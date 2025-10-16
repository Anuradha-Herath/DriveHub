package com.drivehub.vehiclerental.service;

import com.drivehub.vehiclerental.dto.BookingRequest;
import com.drivehub.vehiclerental.dto.BookingResponse;
import com.drivehub.vehiclerental.entity.Booking.BookingStatus;

import java.util.List;

/**
 * Service interface for booking operations
 */
public interface BookingService {
    
    /**
     * Create a new booking
     * 
     * @param request Booking request
     * @param customerId Customer ID
     * @return Created booking response
     */
    BookingResponse createBooking(BookingRequest request, Long customerId);
    
    /**
     * Get booking by ID
     * 
     * @param id Booking ID
     * @return Booking response
     */
    BookingResponse getBookingById(Long id);
    
    /**
     * Get all bookings
     * 
     * @return List of booking responses
     */
    List<BookingResponse> getAllBookings();
    
    /**
     * Get bookings by customer
     * 
     * @param customerId Customer ID
     * @return List of booking responses
     */
    List<BookingResponse> getBookingsByCustomer(Long customerId);
    
    /**
     * Update booking status
     * 
     * @param id Booking ID
     * @param status New status
     * @return Updated booking response
     */
    BookingResponse updateBookingStatus(Long id, BookingStatus status);
    
    /**
     * Cancel booking
     * 
     * @param id Booking ID
     * @return Cancelled booking response
     */
    BookingResponse cancelBooking(Long id);
}
