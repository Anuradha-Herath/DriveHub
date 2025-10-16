package com.drivehub.vehiclerental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Abstract base class for all vehicle types
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false)
    private String model;
    
    @Column(name = "rental_price_per_day", nullable = false)
    private BigDecimal rentalPricePerDay;
    
    @Column(nullable = false)
    private Boolean availability = true;
    
    @Column(name = "registration_number", unique = true)
    private String registrationNumber;
    
    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;
    
    @Column(length = 500)
    private String description;
}
