package com.drivehub.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for DriveHub - Vehicle Rental Management System
 * 
 * @author DriveHub Team
 * @version 1.0.0
 */
@SpringBootApplication
public class DriveHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriveHubApplication.class, args);
        System.out.println("🚗 DriveHub Application Started Successfully!");
        System.out.println("📘 Swagger UI: http://localhost:8080/swagger-ui.html");
    }
}
