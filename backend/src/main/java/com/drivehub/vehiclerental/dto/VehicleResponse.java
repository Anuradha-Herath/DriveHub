package com.drivehub.vehiclerental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for vehicle response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    
    private Long id;
    private String vehicleType;
    private String brand;
    private String model;
    private BigDecimal rentalPricePerDay;
    private String registrationNumber;
    private Integer yearOfManufacture;
    private String description;
    private Boolean availability;
    
    // Additional fields based on vehicle type
    private Integer numberOfSeats;
    private String fuelType;
    private String transmissionType;
    private Boolean hasAirConditioning;
    private Integer engineCapacity;
    private String bikeType;
    private Boolean hasHelmet;
    private Integer cargoCapacity;
    private Boolean hasSlidingDoor;
    private String vanType;
}
