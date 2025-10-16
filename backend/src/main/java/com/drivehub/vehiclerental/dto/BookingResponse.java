package com.drivehub.vehiclerental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for booking response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    
    private Long id;
    private Long vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private Long customerId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private String status;
    private String paymentMethod;
    private LocalDate createdAt;
    private String notes;
}
