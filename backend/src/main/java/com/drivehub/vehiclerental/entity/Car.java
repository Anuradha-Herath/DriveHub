package com.drivehub.vehiclerental.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Car entity representing car vehicles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {
    
    private Integer numberOfSeats;
    private String fuelType; // PETROL, DIESEL, ELECTRIC, HYBRID
    private String transmissionType; // MANUAL, AUTOMATIC
    private Boolean hasAirConditioning;
}
