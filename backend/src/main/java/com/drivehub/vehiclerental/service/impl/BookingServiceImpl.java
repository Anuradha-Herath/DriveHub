package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.BookingRequest;
import com.drivehub.vehiclerental.dto.BookingResponse;
import com.drivehub.vehiclerental.entity.Booking;
import com.drivehub.vehiclerental.entity.Booking.BookingStatus;
import com.drivehub.vehiclerental.entity.Customer;
import com.drivehub.vehiclerental.entity.Vehicle;
import com.drivehub.vehiclerental.repository.BookingRepository;
import com.drivehub.vehiclerental.repository.UserRepository;
import com.drivehub.vehiclerental.repository.VehicleRepository;
import com.drivehub.vehiclerental.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of BookingService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    
    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long customerId) {
        log.info("Creating booking for customer: {} and vehicle: {}", customerId, request.getVehicleId());
        
        // Validate dates
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        
        // Get vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        
        if (!vehicle.getAvailability()) {
            throw new IllegalArgumentException("Vehicle is not available for booking");
        }
        
        // Get customer
        Customer customer = (Customer) userRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        // Calculate total cost
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days == 0) days = 1; // Minimum 1 day rental
        BigDecimal totalCost = vehicle.getRentalPricePerDay().multiply(BigDecimal.valueOf(days));
        
        // Create booking
        Booking booking = new Booking();
        booking.setVehicle(vehicle);
        booking.setCustomer(customer);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalCost(totalCost);
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentMethod(request.getPaymentMethod());
        booking.setNotes(request.getNotes());
        
        booking = bookingRepository.save(booking);
        
        // Mark vehicle as unavailable
        vehicle.setAvailability(false);
        vehicleRepository.save(vehicle);
        
        log.info("Booking created successfully with ID: {}", booking.getId());
        
        return mapToResponse(booking);
    }
    
    @Override
    public BookingResponse getBookingById(Long id) {
        log.info("Fetching booking by ID: {}", id);
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
        return mapToResponse(booking);
    }
    
    @Override
    public List<BookingResponse> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingResponse> getBookingsByCustomer(Long customerId) {
        log.info("Fetching bookings for customer: {}", customerId);
        return bookingRepository.findByCustomerId(customerId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public BookingResponse updateBookingStatus(Long id, BookingStatus status) {
        log.info("Updating booking status for ID: {} to {}", id, status);
        
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
        
        booking.setStatus(status);
        
        // If booking is completed or cancelled, make vehicle available again
        if (status == BookingStatus.COMPLETED || status == BookingStatus.CANCELLED) {
            Vehicle vehicle = booking.getVehicle();
            vehicle.setAvailability(true);
            vehicleRepository.save(vehicle);
        }
        
        booking = bookingRepository.save(booking);
        
        return mapToResponse(booking);
    }
    
    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id) {
        log.info("Cancelling booking with ID: {}", id);
        return updateBookingStatus(id, BookingStatus.CANCELLED);
    }
    
    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setVehicleId(booking.getVehicle().getId());
        response.setVehicleBrand(booking.getVehicle().getBrand());
        response.setVehicleModel(booking.getVehicle().getModel());
        response.setCustomerId(booking.getCustomer().getId());
        response.setCustomerName(booking.getCustomer().getName());
        response.setStartDate(booking.getStartDate());
        response.setEndDate(booking.getEndDate());
        response.setTotalCost(booking.getTotalCost());
        response.setStatus(booking.getStatus().name());
        response.setPaymentMethod(booking.getPaymentMethod());
        response.setCreatedAt(booking.getCreatedAt());
        response.setNotes(booking.getNotes());
        return response;
    }
}
