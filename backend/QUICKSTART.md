# 🚀 DriveHub - Quick Start Guide

## ⚡ 5-Minute Setup

### Step 1: Database Setup (2 minutes)
```sql
-- Open MySQL and run:
CREATE DATABASE drivehub_db;
```

### Step 2: Configure Application (1 minute)
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Run Application (2 minutes)
```bash
# Build and run
mvn spring-boot:run
```

✅ **Application is running!** Visit: http://localhost:8080/swagger-ui.html

---

## 🎯 First API Calls

### 1. Register a Customer
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "password": "password123",
    "userType": "CUSTOMER",
    "drivingLicenseNumber": "DL123456",
    "address": "123 Main St"
  }'
```

**Response:** You'll get a JWT token. Copy it!

### 2. Register an Admin
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@drivehub.com",
    "phone": "+1234567890",
    "password": "admin123",
    "userType": "ADMIN",
    "department": "Management",
    "employeeId": "EMP001"
  }'
```

### 3. Add a Vehicle (Use Admin Token)
```bash
curl -X POST http://localhost:8080/api/vehicles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "vehicleType": "CAR",
    "brand": "Toyota",
    "model": "Camry",
    "rentalPricePerDay": 50.00,
    "registrationNumber": "ABC-1234",
    "yearOfManufacture": 2022,
    "availability": true,
    "numberOfSeats": 5,
    "fuelType": "PETROL",
    "transmissionType": "AUTOMATIC",
    "hasAirConditioning": true
  }'
```

### 4. Get Available Vehicles
```bash
curl http://localhost:8080/api/vehicles/available \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 5. Create a Booking (Use Customer Token)
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_CUSTOMER_TOKEN" \
  -d '{
    "vehicleId": 1,
    "startDate": "2025-10-20",
    "endDate": "2025-10-25",
    "paymentMethod": "CARD"
  }'
```

---

## 🔧 Troubleshooting

### Problem: Port 8080 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

### Problem: Database connection failed
**Solution:** Verify MySQL is running and credentials are correct:
```bash
mysql -u root -p
```

### Problem: JWT token expired
**Solution:** Login again to get a new token:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

---

## 📊 Project Status Check

### Verify Build
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Check Dependencies
```bash
mvn dependency:tree
```

---

## 🎨 Using Swagger UI

1. Open browser: http://localhost:8080/swagger-ui.html
2. Click "Authorize" button (top right)
3. Enter: `Bearer YOUR_JWT_TOKEN`
4. Now you can test all APIs interactively!

---

## 📱 Recommended Tools

- **API Testing**: Postman or Swagger UI
- **Database**: MySQL Workbench or DBeaver
- **IDE**: IntelliJ IDEA or VS Code with Java extensions

---

## 🎓 Learning Path

1. ✅ **Start Here**: Register users and explore authentication
2. ✅ **Next**: Add vehicles (as admin) and view them
3. ✅ **Then**: Create bookings (as customer)
4. ✅ **Finally**: Process payments using different strategies

---

## 📞 Need Help?

- Check logs: `tail -f logs/application.log`
- Enable debug: Add `logging.level.com.drivehub=DEBUG` to properties
- Review README.md for detailed documentation
- Check POSTMAN_GUIDE.md for API examples

---

## 🎉 You're All Set!

Your DriveHub application is ready to go. Happy coding! 🚗💨
