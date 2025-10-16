package com.drivehub.vehiclerental.repository;

import com.drivehub.vehiclerental.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Vehicle entity
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByAvailability(Boolean availability);
    
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    
    List<Vehicle> findByBrandContainingIgnoreCase(String brand);
    
    List<Vehicle> findByModelContainingIgnoreCase(String model);
}
