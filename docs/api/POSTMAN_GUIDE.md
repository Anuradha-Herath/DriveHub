# DriveHub API - Postman Collection Guide

## 🔧 Setup Instructions

1. Import the collection into Postman
2. Set up environment variables:
   - `base_url`: http://localhost:8080
   - `jwt_token`: (will be set automatically after login)

## 📋 API Endpoints

### 1. Authentication

#### Register Customer
```
POST {{base_url}}/api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "password": "password123",
  "userType": "CUSTOMER",
  "drivingLicenseNumber": "DL123456",
  "address": "123 Main St, City"
}
```

#### Register Admin
```
POST {{base_url}}/api/auth/register
Content-Type: application/json

{
  "name": "Admin User",
  "email": "admin@drivehub.com",
  "phone": "+1234567890",
  "password": "admin123",
  "userType": "ADMIN",
  "department": "Management",
  "employeeId": "EMP001"
}
```

#### Login
```
POST {{base_url}}/api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "CUSTOMER"
}
```

#### Get Current User
```
GET {{base_url}}/api/auth/me
Authorization: Bearer {{jwt_token}}
```

---

### 2. Vehicles

#### Create Vehicle (Admin Only)
```
POST {{base_url}}/api/vehicles
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

// Car Example
{
  "vehicleType": "CAR",
  "brand": "Toyota",
  "model": "Camry",
  "rentalPricePerDay": 50.00,
  "registrationNumber": "ABC-1234",
  "yearOfManufacture": 2022,
  "description": "Comfortable sedan",
  "availability": true,
  "numberOfSeats": 5,
  "fuelType": "PETROL",
  "transmissionType": "AUTOMATIC",
  "hasAirConditioning": true
}

// Bike Example
{
  "vehicleType": "BIKE",
  "brand": "Yamaha",
  "model": "R15",
  "rentalPricePerDay": 25.00,
  "registrationNumber": "BIKE-001",
  "yearOfManufacture": 2023,
  "description": "Sport bike",
  "availability": true,
  "engineCapacity": 150,
  "bikeType": "SPORT",
  "hasHelmet": true
}

// Van Example
{
  "vehicleType": "VAN",
  "brand": "Mercedes",
  "model": "Sprinter",
  "rentalPricePerDay": 100.00,
  "registrationNumber": "VAN-001",
  "yearOfManufacture": 2022,
  "description": "Cargo van",
  "availability": true,
  "cargoCapacity": 15,
  "numberOfSeats": 3,
  "hasSlidingDoor": true,
  "vanType": "CARGO"
}
```

#### Get All Vehicles
```
GET {{base_url}}/api/vehicles
Authorization: Bearer {{jwt_token}}
```

#### Get Available Vehicles
```
GET {{base_url}}/api/vehicles/available
Authorization: Bearer {{jwt_token}}
```

#### Get Vehicle by ID
```
GET {{base_url}}/api/vehicles/1
Authorization: Bearer {{jwt_token}}
```

#### Update Vehicle (Admin Only)
```
PUT {{base_url}}/api/vehicles/1
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

{
  "vehicleType": "CAR",
  "brand": "Toyota",
  "model": "Camry 2024",
  "rentalPricePerDay": 55.00,
  "registrationNumber": "ABC-1234",
  "yearOfManufacture": 2024,
  "description": "Updated comfortable sedan",
  "availability": true,
  "numberOfSeats": 5,
  "fuelType": "HYBRID",
  "transmissionType": "AUTOMATIC",
  "hasAirConditioning": true
}
```

#### Update Vehicle Availability (Admin Only)
```
PATCH {{base_url}}/api/vehicles/1/availability?availability=false
Authorization: Bearer {{jwt_token}}
```

#### Delete Vehicle (Admin Only)
```
DELETE {{base_url}}/api/vehicles/1
Authorization: Bearer {{jwt_token}}
```

---

### 3. Bookings

#### Create Booking
```
POST {{base_url}}/api/bookings
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

{
  "vehicleId": 1,
  "startDate": "2025-10-20",
  "endDate": "2025-10-25",
  "paymentMethod": "CARD",
  "notes": "Need the vehicle early morning"
}
```

#### Get All Bookings (Admin Only)
```
GET {{base_url}}/api/bookings
Authorization: Bearer {{jwt_token}}
```

#### Get My Bookings
```
GET {{base_url}}/api/bookings/my-bookings
Authorization: Bearer {{jwt_token}}
```

#### Get Booking by ID
```
GET {{base_url}}/api/bookings/1
Authorization: Bearer {{jwt_token}}
```

#### Update Booking Status (Admin Only)
```
PATCH {{base_url}}/api/bookings/1/status?status=CONFIRMED
Authorization: Bearer {{jwt_token}}

Status values: PENDING, CONFIRMED, COMPLETED, CANCELLED
```

#### Cancel Booking
```
DELETE {{base_url}}/api/bookings/1
Authorization: Bearer {{jwt_token}}
```

---

### 4. Payments

#### Process Card Payment
```
POST {{base_url}}/api/payments/process
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

{
  "bookingId": 1,
  "amount": 250.00,
  "paymentMethod": "CARD",
  "cardNumber": "4532123456789012",
  "cardHolderName": "John Doe",
  "expiryDate": "12/25",
  "cvv": "123"
}
```

#### Process Cash Payment
```
POST {{base_url}}/api/payments/process
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

{
  "bookingId": 1,
  "amount": 250.00,
  "paymentMethod": "CASH"
}
```

---

## 🔑 Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `204 No Content` - Request successful (no content returned)
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## 🧪 Testing Flow

### Complete User Journey

1. **Register Customer**
   - POST /api/auth/register (userType: CUSTOMER)
   - Save the JWT token from response

2. **Login**
   - POST /api/auth/login
   - Update jwt_token in environment

3. **Browse Vehicles**
   - GET /api/vehicles/available

4. **Create Booking**
   - POST /api/bookings (with vehicleId)
   - Note the booking ID

5. **Process Payment**
   - POST /api/payments/process (with bookingId)

6. **View My Bookings**
   - GET /api/bookings/my-bookings

### Admin Journey

1. **Register/Login as Admin**
   - POST /api/auth/register (userType: ADMIN)
   - POST /api/auth/login

2. **Add Vehicles**
   - POST /api/vehicles

3. **Manage Bookings**
   - GET /api/bookings (view all)
   - PATCH /api/bookings/{id}/status (update status)

4. **Manage Vehicles**
   - PUT /api/vehicles/{id} (update)
   - DELETE /api/vehicles/{id} (delete)

---

## 📝 Notes

- All authenticated endpoints require `Authorization: Bearer {token}` header
- JWT tokens expire after 24 hours (configurable)
- Vehicle availability is automatically updated when booking is created
- Payment must match booking total cost exactly
- Dates must be in ISO format (YYYY-MM-DD)

---

## 🔍 Common Error Scenarios

### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2025-10-16T10:30:00"
}
```

### 400 Bad Request (Validation)
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Email should be valid",
    "password": "Password must be at least 6 characters"
  },
  "timestamp": "2025-10-16T10:30:00"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Vehicle not found with ID: 999",
  "timestamp": "2025-10-16T10:30:00"
}
```
