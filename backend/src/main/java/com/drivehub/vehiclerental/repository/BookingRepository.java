package com.drivehub.vehiclerental.repository;

import com.drivehub.vehiclerental.entity.Booking;
import com.drivehub.vehiclerental.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByCustomerId(Long customerId);
    
    List<Booking> findByVehicleId(Long vehicleId);
    
    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
