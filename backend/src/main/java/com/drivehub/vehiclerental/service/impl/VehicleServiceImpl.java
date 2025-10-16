package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.VehicleRequest;
import com.drivehub.vehiclerental.dto.VehicleResponse;
import com.drivehub.vehiclerental.entity.*;
import com.drivehub.vehiclerental.repository.VehicleRepository;
import com.drivehub.vehiclerental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of VehicleService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    
    private final VehicleRepository vehicleRepository;
    
    @Override
    @Transactional
    public VehicleResponse createVehicle(VehicleRequest request) {
        log.info("Creating new vehicle: {} {}", request.getBrand(), request.getModel());
        
        // Create vehicle based on type
        Vehicle vehicle;
        switch (request.getVehicleType().toUpperCase()) {
            case "CAR":
                Car car = new Car();
                car.setNumberOfSeats(request.getNumberOfSeats());
                car.setFuelType(request.getFuelType());
                car.setTransmissionType(request.getTransmissionType());
                car.setHasAirConditioning(request.getHasAirConditioning());
                vehicle = car;
                break;
            case "BIKE":
                Bike bike = new Bike();
                bike.setEngineCapacity(request.getEngineCapacity());
                bike.setBikeType(request.getBikeType());
                bike.setHasHelmet(request.getHasHelmet());
                vehicle = bike;
                break;
            case "VAN":
                Van van = new Van();
                van.setCargoCapacity(request.getCargoCapacity());
                van.setNumberOfSeats(request.getNumberOfSeats());
                van.setHasSlidingDoor(request.getHasSlidingDoor());
                van.setVanType(request.getVanType());
                vehicle = van;
                break;
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
        
        // Set common fields
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setRentalPricePerDay(request.getRentalPricePerDay());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setYearOfManufacture(request.getYearOfManufacture());
        vehicle.setDescription(request.getDescription());
        vehicle.setAvailability(request.getAvailability());
        
        // Save vehicle
        vehicle = vehicleRepository.save(vehicle);
        
        log.info("Vehicle created successfully with ID: {}", vehicle.getId());
        
        return mapToResponse(vehicle);
    }
    
    @Override
    public VehicleResponse getVehicleById(Long id) {
        log.info("Fetching vehicle by ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
        return mapToResponse(vehicle);
    }
    
    @Override
    public List<VehicleResponse> getAllVehicles() {
        log.info("Fetching all vehicles");
        return vehicleRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<VehicleResponse> getAvailableVehicles() {
        log.info("Fetching available vehicles");
        return vehicleRepository.findByAvailability(true).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        log.info("Updating vehicle with ID: {}", id);
        
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
        
        // Update common fields
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setRentalPricePerDay(request.getRentalPricePerDay());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setYearOfManufacture(request.getYearOfManufacture());
        vehicle.setDescription(request.getDescription());
        vehicle.setAvailability(request.getAvailability());
        
        // Update specific fields based on type
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            car.setNumberOfSeats(request.getNumberOfSeats());
            car.setFuelType(request.getFuelType());
            car.setTransmissionType(request.getTransmissionType());
            car.setHasAirConditioning(request.getHasAirConditioning());
        } else if (vehicle instanceof Bike) {
            Bike bike = (Bike) vehicle;
            bike.setEngineCapacity(request.getEngineCapacity());
            bike.setBikeType(request.getBikeType());
            bike.setHasHelmet(request.getHasHelmet());
        } else if (vehicle instanceof Van) {
            Van van = (Van) vehicle;
            van.setCargoCapacity(request.getCargoCapacity());
            van.setNumberOfSeats(request.getNumberOfSeats());
            van.setHasSlidingDoor(request.getHasSlidingDoor());
            van.setVanType(request.getVanType());
        }
        
        vehicle = vehicleRepository.save(vehicle);
        
        log.info("Vehicle updated successfully: {}", id);
        
        return mapToResponse(vehicle);
    }
    
    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        log.info("Deleting vehicle with ID: {}", id);
        
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle not found with ID: " + id);
        }
        
        vehicleRepository.deleteById(id);
        log.info("Vehicle deleted successfully: {}", id);
    }
    
    @Override
    @Transactional
    public VehicleResponse updateAvailability(Long id, Boolean availability) {
        log.info("Updating availability for vehicle ID: {} to {}", id, availability);
        
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
        
        vehicle.setAvailability(availability);
        vehicle = vehicleRepository.save(vehicle);
        
        return mapToResponse(vehicle);
    }
    
    private VehicleResponse mapToResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setBrand(vehicle.getBrand());
        response.setModel(vehicle.getModel());
        response.setRentalPricePerDay(vehicle.getRentalPricePerDay());
        response.setRegistrationNumber(vehicle.getRegistrationNumber());
        response.setYearOfManufacture(vehicle.getYearOfManufacture());
        response.setDescription(vehicle.getDescription());
        response.setAvailability(vehicle.getAvailability());
        
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            response.setVehicleType("CAR");
            response.setNumberOfSeats(car.getNumberOfSeats());
            response.setFuelType(car.getFuelType());
            response.setTransmissionType(car.getTransmissionType());
            response.setHasAirConditioning(car.getHasAirConditioning());
        } else if (vehicle instanceof Bike) {
            Bike bike = (Bike) vehicle;
            response.setVehicleType("BIKE");
            response.setEngineCapacity(bike.getEngineCapacity());
            response.setBikeType(bike.getBikeType());
            response.setHasHelmet(bike.getHasHelmet());
        } else if (vehicle instanceof Van) {
            Van van = (Van) vehicle;
            response.setVehicleType("VAN");
            response.setCargoCapacity(van.getCargoCapacity());
            response.setNumberOfSeats(van.getNumberOfSeats());
            response.setHasSlidingDoor(van.getHasSlidingDoor());
            response.setVanType(van.getVanType());
        }
        
        return response;
    }
}
