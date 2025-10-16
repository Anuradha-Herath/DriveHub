package com.drivehub.vehiclerental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating/updating vehicles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    
    @NotBlank(message = "Vehicle type is required")
    private String vehicleType; // CAR, BIKE, VAN
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Rental price is required")
    @Positive(message = "Rental price must be positive")
    private BigDecimal rentalPricePerDay;
    
    private String registrationNumber;
    
    private Integer yearOfManufacture;
    
    private String description;
    
    private Boolean availability = true;
    
    // Car specific fields
    private Integer numberOfSeats;
    private String fuelType;
    private String transmissionType;
    private Boolean hasAirConditioning;
    
    // Bike specific fields
    private Integer engineCapacity;
    private String bikeType;
    private Boolean hasHelmet;
    
    // Van specific fields
    private Integer cargoCapacity;
    private Boolean hasSlidingDoor;
    private String vanType;
}
