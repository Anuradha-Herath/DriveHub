# 🚀 Quick Start Guide - Testing Authentication

## 5-Minute Quick Start

### Prerequisites
- Both backend and frontend running
- MySQL database created
- Postman or curl (for API testing)

---

## Backend Testing (5 minutes)

### Step 1: Start Backend
```bash
cd backend
mvn spring-boot:run
```
Wait for: `Started DriveHubApplication in X seconds`

### Step 2: Test Registration (Postman/cURL)

**Using Postman**:
1. Create new POST request
2. URL: `http://localhost:8080/api/auth/register`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "phone": "+1234567890",
  "password": "secure123",
  "userType": "CUSTOMER",
  "drivingLicenseNumber": "DL999888",
  "address": "456 Oak Avenue"
}
```
5. Click Send
6. **Save the token** from response

**Using cURL**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "phone": "+1234567890",
    "password": "secure123",
    "userType": "CUSTOMER",
    "drivingLicenseNumber": "DL999888",
    "address": "456 Oak Avenue"
  }'
```

**Expected Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "role": "CUSTOMER"
}
```

✅ **Success**: Copy the token value

### Step 3: Test Login

**Using Postman**:
1. Create new POST request
2. URL: `http://localhost:8080/api/auth/login`
3. Headers: `Content-Type: application/json`
4. Body:
```json
{
  "email": "alice@example.com",
  "password": "secure123"
}
```
5. Click Send

**Using cURL**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "password": "secure123"
  }'
```

✅ **Success**: Should receive the same token

### Step 4: Test Get Current User

**Using Postman**:
1. Create new GET request
2. URL: `http://localhost:8080/api/auth/me`
3. Headers:
   - `Content-Type: application/json`
   - `Authorization: Bearer {paste_your_token_here}`
4. Click Send

**Using cURL**:
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer {paste_your_token_here}"
```

✅ **Success**: Should return user details

### Step 5: Test Invalid Token (Should Fail)

**Using Postman**:
1. Same as Step 4, but use wrong token
2. Should get 401 Unauthorized

---

## Frontend Testing (5 minutes)

### Step 1: Start Frontend
```bash
cd frontend
npm run dev
```
Wait for: `Local: http://localhost:5173/`

### Step 2: Test Registration Form

1. Open `http://localhost:5173/register`
2. Fill in the form:
   - Full Name: `Bob Smith`
   - Email: `bob@example.com`
   - Phone: `+9876543210`
   - Account Type: `Customer`
   - Driving License: `DL123456`
   - Address: `789 Pine Road`
   - Password: `password123`
   - Confirm Password: `password123`
3. Click "Create Account"

**Expected**:
- No error message
- Redirected to `/dashboard`
- User name appears in navbar

**Check localStorage** (Open DevTools → Application → LocalStorage):
```javascript
// Should see:
token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
user: {"id": 1, "name": "Bob Smith", "email": "bob@example.com", "role": "CUSTOMER"}
```

### Step 3: Test Login Form

1. Click Logout button in navbar
2. Verify localStorage cleared (tokens removed)
3. Navigate to `http://localhost:5173/login`
4. Enter credentials:
   - Email: `bob@example.com`
   - Password: `password123`
5. Click "Sign In"

**Expected**:
- Redirected to `/dashboard`
- User name appears in navbar
- localStorage contains token again

### Step 4: Test Protected Routes

1. With token in localStorage:
   - Navigate to `http://localhost:5173/dashboard`
   - Should display page content

2. Clear token from localStorage:
   ```javascript
   // In DevTools console:
   localStorage.removeItem('token')
   localStorage.removeItem('user')
   ```

3. Try accessing `http://localhost:5173/dashboard`
   - Should redirect to `/login`

✅ **Success**: Protection working

### Step 5: Test Error Handling

**Invalid Credentials**:
1. Go to login page
2. Enter: `bob@example.com` and wrong password
3. Click Sign In
4. Should show error: "Invalid email or password"

**Duplicate Email**:
1. Go to register page
2. Try registering with `bob@example.com` (already registered)
3. Should show error: "Email already exists"

**Password Mismatch**:
1. Go to register page
2. Password: `password123`
3. Confirm Password: `different123`
4. Should show error: "Passwords do not match"

---

## Swagger UI Testing

### Access Documentation
1. Open `http://localhost:8080/swagger-ui.html`
2. You should see three endpoints under "Authentication":
   - `POST /api/auth/register`
   - `POST /api/auth/login`
   - `GET /api/auth/me`

### Try It Out
1. Click on `/api/auth/register`
2. Click "Try it out"
3. Enter JSON body:
```json
{
  "name": "Test User",
  "email": "test@example.com",
  "phone": "+1111111111",
  "password": "test123",
  "userType": "CUSTOMER",
  "drivingLicenseNumber": "DL000000",
  "address": "Test Address"
}
```
4. Click "Execute"
5. Check response

---

## Debugging Checklist

### Backend Issues

| Issue | Solution |
|-------|----------|
| Port 8080 already in use | Kill process or change port in `application.properties` |
| Database connection error | Check MySQL is running, database exists, credentials correct |
| Token invalid after login | Check `jwt.secret` matches in all instances |
| CORS error in browser | Add frontend URL to CORS config in `SecurityConfig.java` |
| 401 Unauthorized on /auth/me | Token might be expired (24 hours), login again |

### Frontend Issues

| Issue | Solution |
|-------|----------|
| Port 3000/5173 already in use | Kill process or Vite will auto-use next port |
| API calls failing | Check backend is running on 8080 |
| localStorage empty after login | Check network tab - registration/login response has token |
| Redirect loops | Check ProtectedRoute component, ensure token is being set |
| Styling issues | Run `npm run dev` again, clear browser cache |

---

## Test Scenarios Checklist

### ✅ To Verify Everything Works

**Registration**:
- [ ] Register with all fields → Success
- [ ] Register with duplicate email → Error
- [ ] Register with invalid email → Error
- [ ] Register with short password → Error
- [ ] Token received and stored in localStorage

**Login**:
- [ ] Login with correct credentials → Success
- [ ] Login with wrong password → Error
- [ ] Login with non-existent email → Error
- [ ] Token received and stored in localStorage
- [ ] User name appears in navbar

**Protected Routes**:
- [ ] Logged in user can access /dashboard → Success
- [ ] Logged out user redirected from /dashboard → Success
- [ ] Logged in user cannot access /login (optional) → Varies
- [ ] Navigation works from navbar

**JWT Token**:
- [ ] Token saved in localStorage → Check DevTools
- [ ] Token sent in Authorization header → Check Network tab
- [ ] GET /auth/me works with token → Success
- [ ] GET /auth/me fails without token → 401 Error
- [ ] Logout clears localStorage → Check DevTools

**Error Handling**:
- [ ] Registration error shown on form → Success
- [ ] Login error shown on form → Success
- [ ] API errors don't crash app → Success
- [ ] Error messages are user-friendly → Success

---

## Sample Data for Testing

### Customer Account
```
Email: customer@example.com
Password: customer123
Driving License: DL123456
Address: 123 Main Street
```

### Admin Account
```
Email: admin@example.com
Password: admin123
Department: Operations
Employee ID: EMP001
```

### Test Multiple Users
Register 3 different users with different emails to test:
- Multiple concurrent sessions
- Email uniqueness enforcement
- Different user types

---

## Performance Testing

### Test Concurrent Logins
```bash
# Run this 10 times in quick succession
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "alice@example.com", "password": "secure123"}'
```

**Expected**: All requests succeed within 1-2 seconds

### Test With Large Data
Register 5+ users and verify:
- Login still works
- Performance is acceptable
- Database queries are efficient

---

## Network Inspection

### Monitor API Calls
1. Open DevTools (F12) → Network tab
2. Register a new user
3. You should see:
   - `POST /api/auth/register` → 200 OK
   - Response contains token and user data
   - Request headers include `Content-Type: application/json`

4. Click "Me" endpoint
5. You should see:
   - `GET /api/auth/me` → 200 OK
   - Request headers include `Authorization: Bearer ...`
   - Response contains user data

### Monitor localStorage
1. Open DevTools → Application → LocalStorage
2. Select your localhost entry
3. After login, you should see:
   - `token`: JWT string
   - `user`: JSON string with user data

---

## Troubleshooting Commands

### View Backend Logs
```bash
# If backend is running in terminal, check output for errors
# Common errors:
# - Connection to database refused
# - Port already in use
# - JWT secret mismatch
```

### Reset Frontend State
```javascript
// In DevTools Console:
localStorage.clear()
sessionStorage.clear()
location.reload()
```

### Test Backend Health
```bash
curl http://localhost:8080/swagger-ui.html
# Should return HTML page (no JSON)

curl http://localhost:8080/api/auth/register
# Should return 405 Method Not Allowed (POST required)
# This proves backend is running
```

---

## Common Error Messages

| Error | Cause | Fix |
|-------|-------|-----|
| `Email already exists` | Email already registered | Use different email |
| `Invalid email or password` | Wrong credentials | Check email and password |
| `Password must be at least 6 characters` | Password too short | Use 6+ chars |
| `CORS error in browser` | Backend CORS not configured | Add frontend URL to CORS config |
| `401 Unauthorized` | No/invalid token | Login again |
| `Network error` | Backend not running | Start backend: `mvn spring-boot:run` |

---

## Success Indicators

### Backend Ready ✅
- [ ] `http://localhost:8080/swagger-ui.html` loads
- [ ] `/api/auth/register` endpoint visible in Swagger
- [ ] Registration returns token
- [ ] Login works with correct credentials
- [ ] `/api/auth/me` requires valid token

### Frontend Ready ✅
- [ ] `http://localhost:5173` loads without errors
- [ ] Register page displays all form fields
- [ ] Login page accepts credentials
- [ ] Can register new user successfully
- [ ] Can login and see dashboard
- [ ] Logout clears session
- [ ] Protected routes redirect when logged out

### Integration Ready ✅
- [ ] Registration → Auto-login → Dashboard redirect
- [ ] Logout → Redirect to login
- [ ] Invalid credentials → Error display
- [ ] Navbar shows username when logged in
- [ ] Token persists on page refresh

---

## Next Steps After Testing

1. **Verify Everything Works**:
   - All test scenarios in checklist pass
   - No console errors
   - No network errors

2. **Review Security**:
   - Passwords are hashed (BCrypt)
   - Tokens are sent via HTTPS in production
   - CORS is configured correctly

3. **Ready for Production**:
   - Update `application.properties` with production database
   - Change `jwt.secret` to a strong random value
   - Set `jwt.expiration` appropriately
   - Configure HTTPS/SSL certificates
   - Update frontend .env with production API URL

4. **Continue Development**:
   - Add vehicle listing endpoints
   - Add booking functionality
   - Add payment processing
   - Add admin dashboard

---

**Happy Testing! 🎉**

For detailed information, see `AUTHENTICATION_IMPLEMENTATION_GUIDE.md`
