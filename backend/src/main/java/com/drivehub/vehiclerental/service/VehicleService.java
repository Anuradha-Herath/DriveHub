package com.drivehub.vehiclerental.service;

import com.drivehub.vehiclerental.dto.VehicleRequest;
import com.drivehub.vehiclerental.dto.VehicleResponse;

import java.util.List;

/**
 * Service interface for vehicle operations
 */
public interface VehicleService {
    
    /**
     * Create a new vehicle
     * 
     * @param request Vehicle request
     * @return Created vehicle response
     */
    VehicleResponse createVehicle(VehicleRequest request);
    
    /**
     * Get vehicle by ID
     * 
     * @param id Vehicle ID
     * @return Vehicle response
     */
    VehicleResponse getVehicleById(Long id);
    
    /**
     * Get all vehicles
     * 
     * @return List of vehicle responses
     */
    List<VehicleResponse> getAllVehicles();
    
    /**
     * Get available vehicles
     * 
     * @return List of available vehicle responses
     */
    List<VehicleResponse> getAvailableVehicles();
    
    /**
     * Update vehicle
     * 
     * @param id Vehicle ID
     * @param request Vehicle request
     * @return Updated vehicle response
     */
    VehicleResponse updateVehicle(Long id, VehicleRequest request);
    
    /**
     * Delete vehicle
     * 
     * @param id Vehicle ID
     */
    void deleteVehicle(Long id);
    
    /**
     * Update vehicle availability
     * 
     * @param id Vehicle ID
     * @param availability Availability status
     * @return Updated vehicle response
     */
    VehicleResponse updateAvailability(Long id, Boolean availability);
}
