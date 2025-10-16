# 🚗 DriveHub - Vehicle Rental Management System

A modern, full-stack vehicle rental management system built with Spring Boot 3.2 and React. DriveHub provides a comprehensive solution for managing vehicles, bookings, and payments with a secure JWT-based authentication system.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Backend Setup](#-backend-setup)
- [Frontend Setup](#-frontend-setup)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Key Components](#-key-components)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

### Authentication & Authorization
- ✅ JWT-based authentication
- ✅ Role-based access control (ADMIN, CUSTOMER)
- ✅ Secure password encryption with BCrypt
- ✅ User registration and login

### Vehicle Management
- ✅ Support for multiple vehicle types (Car, Bike, Van)
- ✅ Complete CRUD operations
- ✅ Real-time availability tracking
- ✅ Type-specific properties and pricing

### Booking System
- ✅ Easy booking creation and management
- ✅ Automated cost calculation based on rental period
- ✅ Date validation and conflict prevention
- ✅ Booking status management (PENDING, CONFIRMED, COMPLETED, CANCELLED)
- ✅ Automatic vehicle availability updates

### Payment Processing
- ✅ Strategy Pattern implementation for flexible payment methods
- ✅ Multiple payment options (Card, Cash)
- ✅ Payment validation and tracking
- ✅ Integration with booking system

### API Documentation
- ✅ Swagger/OpenAPI 3.0 specification
- ✅ Interactive API testing interface
- ✅ Comprehensive endpoint documentation

### User Interface
- ✅ Modern React-based frontend
- ✅ Responsive design with Tailwind CSS
- ✅ Protected routes for authenticated users
- ✅ Real-time vehicle availability display
- ✅ Booking management dashboard

---

## 🛠 Tech Stack

### Backend
| Component | Technology |
|-----------|------------|
| **Framework** | Spring Boot 3.2.0 |
| **Language** | Java 17 |
| **Build Tool** | Maven 3.6+ |
| **Database** | MySQL 8.0+ |
| **Authentication** | JWT (JJWT 0.12.3) |
| **API Documentation** | Springdoc OpenAPI 2.3.0 |
| **ORM** | Spring Data JPA |
| **Security** | Spring Security 6.x |
| **Validation** | Bean Validation (Jakarta) |
| **Lombok** | 1.18+ (Boilerplate reduction) |

### Frontend
| Component | Technology |
|-----------|------------|
| **Framework** | React 19.1.1 |
| **Language** | TypeScript 5.9.3 |
| **Build Tool** | Vite 7.1.7 |
| **Styling** | Tailwind CSS 4.1.14 |
| **Routing** | React Router DOM 7.9.4 |
| **HTTP Client** | Axios 1.12.2 |
| **Linting** | ESLint 9.36.0 |

---

## 📁 Project Structure

```
DriveHub/
├── backend/                          # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/drivehub/vehiclerental/
│   │   │   │   ├── DriveHubApplication.java
│   │   │   │   ├── config/              # Security & JWT Configuration
│   │   │   │   ├── controller/          # REST Controllers (4 files)
│   │   │   │   ├── service/             # Business Logic Layer
│   │   │   │   ├── repository/          # Data Access Layer
│   │   │   │   ├── entity/              # Domain Models (8 files)
│   │   │   │   ├── dto/                 # Data Transfer Objects (8 files)
│   │   │   │   ├── exception/           # Exception Handling
│   │   │   │   └── util/                # Utility Classes
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── database-setup.sql
│   │   └── test/                    # Unit & Integration Tests
│   ├── pom.xml
│   ├── README.md
│   ├── QUICKSTART.md
│   ├── POSTMAN_GUIDE.md
│   └── PROJECT_SUMMARY.md
│
├── frontend/                         # React TypeScript Application
│   ├── src/
│   │   ├── components/               # Reusable React Components
│   │   ├── pages/                    # Page Components
│   │   ├── services/                 # API Communication Services
│   │   ├── context/                  # React Context (Auth)
│   │   ├── hooks/                    # Custom React Hooks
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── tailwind.config.js
│   ├── README.md
│   ├── QUICKSTART.md
│   └── .env.example
│
└── README.md                         # This file
```

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 17+** - [Download](https://www.oracle.com/java/technologies/downloads/#java17)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
- **Git** - [Download](https://git-scm.com/)

### Clone Repository

```bash
git clone https://github.com/Anuradha-Herath/DriveHub.git
cd DriveHub
```

---

## 🔧 Backend Setup

### 1. Database Setup

Create a MySQL database:

```bash
mysql -u root -p

# In MySQL shell:
CREATE DATABASE drivehub_db;
```

Or run the provided script:

```bash
mysql -u root -p drivehub_db < backend/src/main/resources/database-setup.sql
```

### 2. Configure Backend

Navigate to the backend directory:

```bash
cd backend
```

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/drivehub_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your_super_secret_jwt_key_min_256_chars_xxxxxxxxxxxxxxxxxxxxxxxx
jwt.expiration=86400000

# Application Configuration
server.port=8080
spring.application.name=drivehub

# Swagger/OpenAPI
springdoc.swagger-ui.enabled=true
```

### 3. Install Dependencies & Build

```bash
# Install Maven dependencies
mvn clean install

# Build the project
mvn clean package
```

---

## 🎨 Frontend Setup

### 1. Install Dependencies

Navigate to the frontend directory:

```bash
cd frontend
npm install
```

### 2. Configure Environment

Create `.env` file from template:

```bash
cp .env.example .env
```

Update `.env`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 3. Verify Configuration

Check `src/services/axiosInstance.js` is configured correctly:

```javascript
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
```

---

## ▶️ Running the Application

### Start Backend

```bash
cd backend
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

**Swagger UI**: http://localhost:8080/swagger-ui.html

### Start Frontend (New Terminal)

```bash
cd frontend
npm run dev
```

The frontend will start on `http://localhost:5173`

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}

Response: 201 Created
{
  "message": "User registered successfully"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "email": "john@example.com",
    "firstName": "John",
    "role": "CUSTOMER"
  }
}
```

### Vehicle Endpoints

#### Get All Vehicles
```http
GET /api/vehicles
```

#### Get Available Vehicles
```http
GET /api/vehicles/available
```

#### Get Vehicle by ID
```http
GET /api/vehicles/{id}
```

#### Create Vehicle (Admin Only)
```http
POST /api/vehicles
Authorization: Bearer {token}
Content-Type: application/json

{
  "brand": "Toyota",
  "model": "Camry",
  "licensePlate": "ABC-1234",
  "pricePerDay": 50,
  "type": "CAR",
  "capacity": 5,
  "transmission": "AUTOMATIC"
}
```

### Booking Endpoints

#### Create Booking
```http
POST /api/bookings
Authorization: Bearer {token}
Content-Type: application/json

{
  "vehicleId": 1,
  "startDate": "2025-01-20",
  "endDate": "2025-01-25"
}

Response: 201 Created
{
  "id": 1,
  "vehicleId": 1,
  "customerId": 5,
  "startDate": "2025-01-20",
  "endDate": "2025-01-25",
  "totalCost": 250,
  "status": "PENDING"
}
```

#### Get My Bookings
```http
GET /api/bookings/my-bookings
Authorization: Bearer {token}
```

#### Cancel Booking
```http
DELETE /api/bookings/{id}
Authorization: Bearer {token}
```

### Payment Endpoints

#### Process Payment
```http
POST /api/payments/process
Authorization: Bearer {token}
Content-Type: application/json

{
  "bookingId": 1,
  "amount": 250,
  "paymentMethod": "CARD"
}
```

For complete API documentation with all endpoints and request/response examples, visit the **Swagger UI** after running the backend.

---

## 💾 Database Schema

### Key Tables

#### Users
- `id` (PK)
- `email` (UNIQUE)
- `password` (encrypted)
- `first_name`
- `last_name`
- `role` (ADMIN/CUSTOMER)
- `created_at`

#### Vehicles
- `id` (PK)
- `brand`
- `model`
- `license_plate` (UNIQUE)
- `price_per_day`
- `type` (CAR/BIKE/VAN)
- `is_available`
- `capacity`
- `transmission`

#### Bookings
- `id` (PK)
- `customer_id` (FK to Users)
- `vehicle_id` (FK to Vehicles)
- `start_date`
- `end_date`
- `total_cost`
- `status` (PENDING/CONFIRMED/COMPLETED/CANCELLED)
- `created_at`

#### Payments
- `id` (PK)
- `booking_id` (FK to Bookings)
- `amount`
- `payment_method` (CARD/CASH)
- `payment_status` (SUCCESS/PENDING/FAILED)
- `created_at`

---

## 🏗 Key Components

### Architecture Pattern
- **MVC Architecture** with clear separation of concerns
- **Repository Pattern** for data access
- **Service Layer** for business logic
- **DTO Pattern** for data transfer
- **Strategy Pattern** for payment processing

### Security Features
- **JWT Authentication** - Stateless, token-based authentication
- **Role-Based Access Control** - ADMIN and CUSTOMER roles
- **Password Encryption** - BCrypt for secure password storage
- **CORS Configuration** - Configured for frontend communication

### Design Patterns
1. **Strategy Pattern** - PaymentStrategy interface with CardPayment and CashPayment implementations
2. **Repository Pattern** - Spring Data JPA repositories
3. **DTO Pattern** - Data transfer objects for API
4. **Inheritance** - Vehicle and User class hierarchies

---

## 🧪 Testing

### Backend Tests

Run unit and integration tests:

```bash
cd backend
mvn test
```

Test files location: `src/test/java/com/drivehub/vehiclerental/`

Available tests:
- `VehicleServiceImplTest.java` - Service layer tests with Mockito
- `VehicleControllerTest.java` - Controller integration tests
- `DriveHubApplicationTests.java` - Application context tests

### Frontend Testing

```bash
cd frontend
npm run lint
```

---

## 📝 Sample Credentials

For testing purposes, use these credentials:

**Admin User:**
- Email: `admin@drivehub.com`
- Password: `admin123`
- Role: ADMIN

**Customer User:**
- Email: `john@example.com`
- Password: `password123`
- Role: CUSTOMER

*Note: Register these users via the `/api/auth/register` endpoint first*

---

## 📖 Additional Documentation

- [Backend README](./backend/README.md) - Backend-specific documentation
- [Backend QUICKSTART](./backend/QUICKSTART.md) - Quick start guide for backend
- [Backend POSTMAN Guide](./backend/POSTMAN_GUIDE.md) - API testing guide
- [Backend PROJECT SUMMARY](./backend/PROJECT_SUMMARY.md) - Detailed project summary
- [Frontend README](./frontend/README.md) - Frontend-specific documentation
- [Frontend QUICKSTART](./frontend/QUICKSTART.md) - Quick start guide for frontend

---

## 🐛 Troubleshooting

### Backend Issues

**Issue: Database connection error**
```
Solution: Verify MySQL is running and credentials in application.properties are correct
```

**Issue: JWT token invalid**
```
Solution: Ensure jwt.secret in application.properties has sufficient length (256+ chars)
```

**Issue: Port 8080 already in use**
```
Solution: Change server.port in application.properties or kill the process using the port
```

### Frontend Issues

**Issue: API connection fails**
```
Solution: Verify VITE_API_BASE_URL in .env file matches backend URL
```

**Issue: CORS errors**
```
Solution: Ensure backend has CORS configured for http://localhost:5173
```

**Issue: Page refresh returns 404**
```
Solution: Vite is configured for SPA routing - this should work automatically
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📋 Project Status

- ✅ Backend API - Complete
- ✅ Frontend UI - Complete
- ✅ Authentication System - Complete
- ✅ Vehicle Management - Complete
- ✅ Booking System - Complete
- ✅ Payment Processing - Complete
- ✅ API Documentation - Complete
- ✅ Testing - In Progress
- ⏳ CI/CD Pipeline - Planned
- ⏳ Deployment - Planned

---

## 📞 Contact & Support

For questions or issues, please:
- Open an GitHub issue
- Check existing documentation in the project
- Review API documentation in Swagger UI

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

---

## 🎯 Quick Links

| Resource | Link |
|----------|------|
| Backend Folder | `./backend/` |
| Frontend Folder | `./frontend/` |
| Backend Docs | `./backend/README.md` |
| Frontend Docs | `./frontend/README.md` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| React Dev Server | `http://localhost:5173` |

---

## 🎉 Getting Started Checklist

- [ ] Clone the repository
- [ ] Install Java JDK 17+
- [ ] Install Node.js 18+
- [ ] Create MySQL database
- [ ] Configure backend `application.properties`
- [ ] Configure frontend `.env`
- [ ] Run backend: `mvn spring-boot:run`
- [ ] Run frontend: `npm run dev`
- [ ] Access frontend: `http://localhost:5173`
- [ ] Access Swagger API docs: `http://localhost:8080/swagger-ui.html`

---

**Happy Coding! 🚗💨**

*Last Updated: October 16, 2025*
