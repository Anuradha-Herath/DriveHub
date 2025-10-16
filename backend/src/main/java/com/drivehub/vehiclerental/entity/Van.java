package com.drivehub.vehiclerental.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Van entity representing van vehicles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("VAN")
public class Van extends Vehicle {
    
    private Integer cargoCapacity; // in cubic meters
    private Integer numberOfSeats;
    private Boolean hasSlidingDoor;
    private String vanType; // PASSENGER, CARGO, MINIBUS
}
