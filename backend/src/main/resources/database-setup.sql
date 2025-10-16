-- DriveHub Database Setup Script
-- This script creates the database and adds sample data

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS drivehub_db;
USE drivehub_db;

-- Note: Tables will be auto-created by Hibernate on application startup

-- Sample data insertion script (Run after application starts for the first time)
-- The following are example SQL statements for reference

-- Sample Admin User (password: admin123)
-- INSERT INTO users (user_type, name, email, phone, password, role, enabled, department, employee_id)
-- VALUES ('ADMIN', 'System Admin', 'admin@drivehub.com', '+1234567890', 
--         '$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW', 'ADMIN', true, 
--         'Management', 'EMP001');

-- Sample Customer User (password: customer123)
-- INSERT INTO users (user_type, name, email, phone, password, role, enabled, driving_license_number, address)
-- VALUES ('CUSTOMER', 'John Doe', 'john@example.com', '+1987654321',
--         '$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW', 'CUSTOMER', true,
--         'DL123456789', '123 Main Street, City, Country');

-- Sample Cars
-- INSERT INTO vehicles (vehicle_type, brand, model, rental_price_per_day, availability, 
--                       registration_number, year_of_manufacture, description, 
--                       number_of_seats, fuel_type, transmission_type, has_air_conditioning)
-- VALUES 
-- ('CAR', 'Toyota', 'Camry', 50.00, true, 'ABC-1234', 2022, 
--  'Comfortable sedan perfect for city driving', 5, 'PETROL', 'AUTOMATIC', true),
-- ('CAR', 'Honda', 'Accord', 55.00, true, 'XYZ-5678', 2023,
--  'Luxury sedan with premium features', 5, 'HYBRID', 'AUTOMATIC', true),
-- ('CAR', 'Tesla', 'Model 3', 80.00, true, 'EV-9012', 2023,
--  'Electric vehicle with autopilot', 5, 'ELECTRIC', 'AUTOMATIC', true);

-- Sample Bikes
-- INSERT INTO vehicles (vehicle_type, brand, model, rental_price_per_day, availability,
--                       registration_number, year_of_manufacture, description,
--                       engine_capacity, bike_type, has_helmet)
-- VALUES
-- ('BIKE', 'Yamaha', 'R15', 25.00, true, 'BIKE-001', 2022,
--  'Sport bike for thrill seekers', 150, 'SPORT', true),
-- ('BIKE', 'Honda', 'Activa', 15.00, true, 'BIKE-002', 2023,
--  'Comfortable scooter for daily commute', 110, 'SCOOTER', true);

-- Sample Vans
-- INSERT INTO vehicles (vehicle_type, brand, model, rental_price_per_day, availability,
--                       registration_number, year_of_manufacture, description,
--                       cargo_capacity, number_of_seats, has_sliding_door, van_type)
-- VALUES
-- ('VAN', 'Mercedes', 'Sprinter', 100.00, true, 'VAN-001', 2022,
--  'Large cargo van for moving', 15, 3, true, 'CARGO'),
-- ('VAN', 'Toyota', 'Hiace', 75.00, true, 'VAN-002', 2023,
--  'Passenger van for groups', 8, 12, true, 'PASSENGER');

-- Query to verify data
-- SELECT * FROM users;
-- SELECT * FROM vehicles;

-- Additional useful queries
-- Get all available vehicles
-- SELECT * FROM vehicles WHERE availability = true;

-- Get all bookings with customer and vehicle details
-- SELECT b.*, u.name as customer_name, v.brand, v.model 
-- FROM bookings b 
-- JOIN users u ON b.customer_id = u.id 
-- JOIN vehicles v ON b.vehicle_id = v.id;
