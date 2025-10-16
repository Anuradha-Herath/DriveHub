# 🚗 DriveHub - Vehicle Rental Management System

A comprehensive Spring Boot backend application for managing vehicle rentals with JWT-based authentication, role-based access control, and RESTful APIs.

## 📋 Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Setup](#database-setup)
- [Authentication](#authentication)
- [Testing](#testing)

## ✨ Features

- **User Management**
  - Customer and Admin roles
  - JWT-based authentication
  - Secure password encryption

- **Vehicle Management**
  - Support for multiple vehicle types (Car, Bike, Van)
  - CRUD operations for vehicles
  - Availability tracking

- **Booking System**
  - Create and manage bookings
  - Automatic cost calculation
  - Booking status tracking (Pending, Confirmed, Completed, Cancelled)

- **Payment Processing**
  - Strategy Pattern implementation
  - Support for Card and Cash payments
  - Payment validation

- **API Documentation**
  - Interactive Swagger UI
  - OpenAPI 3.0 specification

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **ORM**: Spring Data JPA / Hibernate
- **Testing**: JUnit 5, Mockito
- **Utilities**: Lombok

## 📁 Project Structure

```
src/main/java/com/drivehub/vehiclerental/
├── config/              # Security, Swagger, CORS configurations
│   ├── JwtAuthenticationFilter.java
│   ├── OpenAPIConfig.java
│   └── SecurityConfig.java
├── controller/          # REST API controllers
│   ├── AuthController.java
│   ├── BookingController.java
│   ├── PaymentController.java
│   └── VehicleController.java
├── dto/                 # Data Transfer Objects
│   ├── AuthResponse.java
│   ├── BookingRequest.java
│   ├── BookingResponse.java
│   ├── LoginRequest.java
│   ├── PaymentRequest.java
│   ├── RegisterRequest.java
│   ├── VehicleRequest.java
│   └── VehicleResponse.java
├── entity/              # JPA entities
│   ├── Admin.java
│   ├── Bike.java
│   ├── Booking.java
│   ├── Car.java
│   ├── Customer.java
│   ├── User.java
│   ├── Van.java
│   └── Vehicle.java
├── exception/           # Exception handling
│   └── GlobalExceptionHandler.java
├── repository/          # Spring Data JPA repositories
│   ├── BookingRepository.java
│   ├── UserRepository.java
│   └── VehicleRepository.java
├── service/             # Service interfaces
│   ├── AuthService.java
│   ├── BookingService.java
│   ├── PaymentService.java
│   ├── PaymentStrategy.java
│   └── VehicleService.java
├── service/impl/        # Service implementations
│   ├── AuthServiceImpl.java
│   ├── BookingServiceImpl.java
│   ├── CardPayment.java
│   ├── CashPayment.java
│   ├── PaymentServiceImpl.java
│   └── VehicleServiceImpl.java
├── util/                # Utility classes
│   └── JwtUtil.java
└── DriveHubApplication.java
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/drivehub.git
   cd drivehub
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE drivehub_db;
   ```

3. **Configure database connection**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/drivehub_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Access the interactive Swagger UI documentation at:

**http://localhost:8080/swagger-ui.html**

### Key Endpoints

#### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token
- `GET /api/auth/me` - Get current user details

#### Vehicles
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/available` - Get available vehicles
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `POST /api/vehicles` - Create vehicle (Admin only)
- `PUT /api/vehicles/{id}` - Update vehicle (Admin only)
- `DELETE /api/vehicles/{id}` - Delete vehicle (Admin only)

#### Bookings
- `POST /api/bookings` - Create booking
- `GET /api/bookings` - Get all bookings (Admin only)
- `GET /api/bookings/my-bookings` - Get user's bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `PATCH /api/bookings/{id}/status` - Update booking status (Admin only)
- `DELETE /api/bookings/{id}` - Cancel booking

#### Payments
- `POST /api/payments/process` - Process payment

## 🗄 Database Setup

The application uses Hibernate with `ddl-auto=update`, so tables will be created automatically on first run.

### Sample Data

You can insert sample data using SQL or the API endpoints after registration.

## 🔐 Authentication

The application uses JWT (JSON Web Token) for authentication.

### Registration Example

```json
POST /api/auth/register
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "password": "password123",
  "userType": "CUSTOMER",
  "drivingLicenseNumber": "DL123456",
  "address": "123 Main St"
}
```

### Login Example

```json
POST /api/auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```

### Using JWT Token

Add the token to the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=VehicleServiceImplTest
```

Generate test coverage report:
```bash
mvn clean test jacoco:report
```

## 🏗 Design Patterns

- **Strategy Pattern**: Payment processing (CardPayment, CashPayment)
- **Repository Pattern**: Data access layer
- **DTO Pattern**: Data transfer between layers
- **Factory Pattern**: Entity creation based on type
- **Dependency Injection**: Spring IoC container

## 📝 Configuration

Key configuration properties in `application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/drivehub_db
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=<your-secret-key>
jwt.expiration=86400000

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## 👥 Team

- **DriveHub Development Team**
- Contact: support@drivehub.com

## 📞 Support

For support, email support@drivehub.com or create an issue in the repository.

---

**Made with ❤️ by DriveHub Team**
