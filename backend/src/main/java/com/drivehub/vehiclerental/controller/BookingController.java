package com.drivehub.vehiclerental.controller;

import com.drivehub.vehiclerental.dto.BookingRequest;
import com.drivehub.vehiclerental.dto.BookingResponse;
import com.drivehub.vehiclerental.entity.Booking.BookingStatus;
import com.drivehub.vehiclerental.entity.User;
import com.drivehub.vehiclerental.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for booking management endpoints
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    @Operation(summary = "Create a new booking", description = "Create a vehicle rental booking")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        BookingResponse response = bookingService.createBooking(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Retrieve booking details by ID")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings", description = "Admin only - Retrieve all bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/my-bookings")
    @Operation(summary = "Get current user bookings", description = "Retrieve bookings for the authenticated user")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<BookingResponse> bookings = bookingService.getBookingsByCustomer(user.getId());
        return ResponseEntity.ok(bookings);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update booking status", description = "Admin only - Update booking status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status
    ) {
        BookingResponse response = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel booking", description = "Cancel a booking")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) {
        BookingResponse response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(response);
    }
}
