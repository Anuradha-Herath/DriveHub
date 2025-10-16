package com.drivehub.vehiclerental.controller;

import com.drivehub.vehiclerental.dto.VehicleRequest;
import com.drivehub.vehiclerental.dto.VehicleResponse;
import com.drivehub.vehiclerental.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for VehicleController
 */
@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private VehicleService vehicleService;
    
    private VehicleRequest vehicleRequest;
    private VehicleResponse vehicleResponse;
    
    @BeforeEach
    void setUp() {
        vehicleRequest = new VehicleRequest();
        vehicleRequest.setVehicleType("CAR");
        vehicleRequest.setBrand("Toyota");
        vehicleRequest.setModel("Camry");
        vehicleRequest.setRentalPricePerDay(BigDecimal.valueOf(50.0));
        vehicleRequest.setNumberOfSeats(5);
        vehicleRequest.setFuelType("PETROL");
        vehicleRequest.setTransmissionType("AUTOMATIC");
        
        vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(1L);
        vehicleResponse.setVehicleType("CAR");
        vehicleResponse.setBrand("Toyota");
        vehicleResponse.setModel("Camry");
        vehicleResponse.setRentalPricePerDay(BigDecimal.valueOf(50.0));
        vehicleResponse.setAvailability(true);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateVehicle_Success() throws Exception {
        // Arrange
        when(vehicleService.createVehicle(any(VehicleRequest.class))).thenReturn(vehicleResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Camry"));
    }
    
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testGetAllVehicles_Success() throws Exception {
        // Arrange
        when(vehicleService.getAllVehicles()).thenReturn(Arrays.asList(vehicleResponse));
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[0].model").value("Camry"));
    }
    
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testGetVehicleById_Success() throws Exception {
        // Arrange
        when(vehicleService.getVehicleById(1L)).thenReturn(vehicleResponse);
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }
}
