package com.drivehub.vehiclerental.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Bike entity representing motorcycle vehicles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("BIKE")
public class Bike extends Vehicle {
    
    private Integer engineCapacity; // in CC
    private String bikeType; // SPORT, CRUISER, SCOOTER, TOURING
    private Boolean hasHelmet;
}
