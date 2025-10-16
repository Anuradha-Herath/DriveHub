package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.VehicleRequest;
import com.drivehub.vehiclerental.dto.VehicleResponse;
import com.drivehub.vehiclerental.entity.Car;
import com.drivehub.vehiclerental.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for VehicleServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {
    
    @Mock
    private VehicleRepository vehicleRepository;
    
    @InjectMocks
    private VehicleServiceImpl vehicleService;
    
    private Car testCar;
    private VehicleRequest vehicleRequest;
    
    @BeforeEach
    void setUp() {
        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Camry");
        testCar.setRentalPricePerDay(BigDecimal.valueOf(50.0));
        testCar.setAvailability(true);
        testCar.setNumberOfSeats(5);
        testCar.setFuelType("PETROL");
        testCar.setTransmissionType("AUTOMATIC");
        testCar.setHasAirConditioning(true);
        
        vehicleRequest = new VehicleRequest();
        vehicleRequest.setVehicleType("CAR");
        vehicleRequest.setBrand("Toyota");
        vehicleRequest.setModel("Camry");
        vehicleRequest.setRentalPricePerDay(BigDecimal.valueOf(50.0));
        vehicleRequest.setNumberOfSeats(5);
        vehicleRequest.setFuelType("PETROL");
        vehicleRequest.setTransmissionType("AUTOMATIC");
        vehicleRequest.setHasAirConditioning(true);
    }
    
    @Test
    void testCreateVehicle_Success() {
        // Arrange
        when(vehicleRepository.save(any(Car.class))).thenReturn(testCar);
        
        // Act
        VehicleResponse response = vehicleService.createVehicle(vehicleRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("Toyota", response.getBrand());
        assertEquals("Camry", response.getModel());
        verify(vehicleRepository, times(1)).save(any(Car.class));
    }
    
    @Test
    void testGetVehicleById_Success() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testCar));
        
        // Act
        VehicleResponse response = vehicleService.getVehicleById(1L);
        
        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Toyota", response.getBrand());
    }
    
    @Test
    void testGetVehicleById_NotFound() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            vehicleService.getVehicleById(999L);
        });
    }
    
    @Test
    void testGetAllVehicles_Success() {
        // Arrange
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(testCar));
        
        // Act
        List<VehicleResponse> vehicles = vehicleService.getAllVehicles();
        
        // Assert
        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals("Toyota", vehicles.get(0).getBrand());
    }
    
    @Test
    void testGetAvailableVehicles_Success() {
        // Arrange
        when(vehicleRepository.findByAvailability(true)).thenReturn(Arrays.asList(testCar));
        
        // Act
        List<VehicleResponse> vehicles = vehicleService.getAvailableVehicles();
        
        // Assert
        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.get(0).getAvailability());
    }
    
    @Test
    void testDeleteVehicle_Success() {
        // Arrange
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(1L);
        
        // Act
        vehicleService.deleteVehicle(1L);
        
        // Assert
        verify(vehicleRepository, times(1)).deleteById(1L);
    }
}
