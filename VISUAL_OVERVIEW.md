# 📊 DriveHub Authentication - Visual Overview

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          DRIVEHUB AUTHENTICATION                             │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                            FRONTEND (React 19)                                │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌─────────────┐        ┌──────────────┐        ┌──────────────┐            │
│  │ Login Page  │        │Register Page │        │  Dashboard   │            │
│  │   (tsx)     │        │   (tsx)      │        │  (Protected) │            │
│  └──────┬──────┘        └──────┬───────┘        └──────┬───────┘            │
│         │                      │                       │                      │
│         └──────────┬───────────┴───────────┬───────────┘                     │
│                    │                       │                                 │
│           ┌─────────▼──────────┐   ┌──────▼────────────┐                   │
│           │  AuthContext API   │   │ ProtectedRoute    │                   │
│           │  (State + Hooks)   │   │  (Route Guard)    │                   │
│           └─────────┬──────────┘   └─────────┬────────┘                    │
│                     │                        │                              │
│           ┌─────────▼────────────────────────▼────────┐                    │
│           │    Axios Instance                        │                    │
│           │  (Auto Token Injection + Interceptor)    │                    │
│           │                                          │                    │
│           │  • Request: Add Authorization Header     │                    │
│           │  • Response: Handle 401 Errors           │                    │
│           └─────────┬────────────────────────────────┘                    │
│                     │                                                      │
│           ┌─────────▼────────────────────────────────┐                    │
│           │      authService.js                      │                    │
│           │   • register()  → POST /auth/register    │                    │
│           │   • login()     → POST /auth/login       │                    │
│           │   • logout()    → Clear localStorage     │                    │
│           │   • getMe()     → GET /auth/me           │                    │
│           └─────────┬────────────────────────────────┘                    │
│                     │                                                      │
│           ┌─────────▼────────────────────────────────┐                    │
│           │      LocalStorage                        │                    │
│           │  • token (JWT)                           │                    │
│           │  • user (User Data)                      │                    │
│           └──────────────────────────────────────────┘                    │
│                                                                             │
└──────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ HTTP/HTTPS
                                    │ Bearer Token
                                    │
┌──────────────────────────────────────────────────────────────────────────────┐
│                         BACKEND (Spring Boot 3.2)                             │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│         ┌────────────────────────────────────────────────────────┐          │
│         │            SecurityFilterChain                         │          │
│         ├────────────────────────────────────────────────────────┤          │
│         │ 1. CORS Filter                                         │          │
│         │ 2. CSRF Filter (Disabled)                              │          │
│         │ 3. Authorization Filter                                │          │
│         │ 4. JWT Authentication Filter ←─┐                       │          │
│         │ 5. UserPassword Authentication │                       │          │
│         └────────────────────────────────┼───────────────────────┘          │
│                                          │                                  │
│         ┌────────────────────────────────▼────────────────────┐            │
│         │     JwtAuthenticationFilter                         │            │
│         ├─────────────────────────────────────────────────────┤            │
│         │ 1. Extract JWT from Authorization header            │            │
│         │ 2. Validate token signature & expiration            │            │
│         │ 3. Load UserDetails from database                   │            │
│         │ 4. Set SecurityContext authentication               │            │
│         └────────────────────────┬────────────────────────────┘            │
│                                  │                                          │
│         ┌────────────────────────▼──────────────────────────┐              │
│         │          AuthController                           │              │
│         ├───────────────────────────────────────────────────┤              │
│         │                                                    │              │
│         │  POST /api/auth/register                          │              │
│         │  ├─ Validate input                                │              │
│         │  ├─ Check email uniqueness                        │              │
│         │  ├─ Create User (Customer/Admin)                  │              │
│         │  ├─ Hash password with BCrypt                     │              │
│         │  ├─ Save to database                              │              │
│         │  ├─ Generate JWT token                            │              │
│         │  └─ Return AuthResponse                           │              │
│         │                                                    │              │
│         │  POST /api/auth/login                             │              │
│         │  ├─ Validate input                                │              │
│         │  ├─ Authenticate with AuthenticationManager       │              │
│         │  ├─ BCrypt password match                         │              │
│         │  ├─ Generate JWT token                            │              │
│         │  └─ Return AuthResponse                           │              │
│         │                                                    │              │
│         │  GET /api/auth/me (Protected)                     │              │
│         │  ├─ Extract user from SecurityContext             │              │
│         │  └─ Return user details                           │              │
│         │                                                    │              │
│         └────────────┬────────────────────┬─────────────────┘              │
│                      │                    │                                 │
│                      └────────┬───────────┘                                 │
│                               │                                             │
│         ┌─────────────────────▼────────────────────────────┐               │
│         │     AuthServiceImpl (Business Logic)              │               │
│         ├────────────────────────────────────────────────────┤              │
│         │                                                    │              │
│         │ • Register: Create user + Generate token          │              │
│         │ • Login: Authenticate + Generate token            │              │
│         │ • Password encryption via PasswordEncoder         │              │
│         │ • JWT generation via JwtUtil                      │              │
│         │ • Transaction management (@Transactional)         │              │
│         │                                                    │              │
│         └────────────────────┬─────────────────────────────┘               │
│                              │                                              │
│         ┌────────────────────▼──────────────────────────┐                 │
│         │   JwtUtil (Token Management)                  │                 │
│         ├───────────────────────────────────────────────┤                 │
│         │                                                │                 │
│         │ • generateToken(User) → JWT String            │                 │
│         │ • extractUsername(token) → Email              │                 │
│         │ • validateToken(token, userDetails) → Boolean │                 │
│         │ • extractClaim(token, resolver) → Any         │                 │
│         │ • Signature: HS256 with secret key             │                 │
│         │ • Expiration: 24 hours (configurable)          │                 │
│         │                                                │                 │
│         └────────────────────┬──────────────────────────┘                 │
│                              │                                              │
│         ┌────────────────────▼──────────────────────┐                     │
│         │    UserRepository (Data Access)           │                     │
│         ├──────────────────────────────────────────┤                      │
│         │                                           │                      │
│         │ • findByEmail(email) → Optional<User>    │                      │
│         │ • existsByEmail(email) → Boolean         │                      │
│         │ • save(user) → User                      │                      │
│         │ • findById(id) → Optional<User>          │                      │
│         │                                           │                      │
│         └────────────────────┬──────────────────────┘                     │
│                              │                                              │
│         ┌────────────────────▼──────────────────────┐                     │
│         │    Entity Layer (Domain Models)           │                     │
│         ├──────────────────────────────────────────┤                      │
│         │                                           │                      │
│         │   User (Abstract)                         │                      │
│         │   ├─ id, name, email, phone               │                      │
│         │   ├─ password, role, enabled              │                      │
│         │   └─ Implements UserDetails               │                      │
│         │       │                                   │                      │
│         │       ├─ Customer                         │                      │
│         │       │  └─ drivingLicenseNumber, address │                      │
│         │       │                                   │                      │
│         │       └─ Admin                            │                      │
│         │          └─ department, employeeId        │                      │
│         │                                           │                      │
│         └────────────────────┬──────────────────────┘                     │
│                              │                                              │
│         ┌────────────────────▼──────────────────────┐                     │
│         │  GlobalExceptionHandler                   │                     │
│         ├──────────────────────────────────────────┤                      │
│         │                                           │                      │
│         │ • IllegalArgumentException → 400          │                      │
│         │ • UsernameNotFoundException → 404          │                      │
│         │ • BadCredentialsException → 401            │                      │
│         │ • MethodArgumentNotValid → 400             │                      │
│         │ • Generic Exception → 500                  │                      │
│         │                                           │                      │
│         └──────────────────────────────────────────┘                     │
│                                                                             │
└──────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ JPA/Hibernate
                                    │
┌──────────────────────────────────────────────────────────────────────────────┐
│                         DATABASE (MySQL)                                      │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│   ┌──────────────────────────────────────────────────────────┐              │
│   │           users table (Single Table Inheritance)         │              │
│   ├──────────────────────────────────────────────────────────┤              │
│   │                                                           │              │
│   │  ┌─────────────┬──────────┬─────────────────────────┐   │              │
│   │  │ id (PK)     │ BIGINT   │ AUTO_INCREMENT          │   │              │
│   │  │ name        │ VARCHAR  │ NOT NULL                │   │              │
│   │  │ email (UQ)  │ VARCHAR  │ NOT NULL, UNIQUE        │   │              │
│   │  │ phone       │ VARCHAR  │ NOT NULL                │   │              │
│   │  │ password    │ VARCHAR  │ NOT NULL (hashed)       │   │              │
│   │  │ role        │ ENUM     │ ADMIN, CUSTOMER         │   │              │
│   │  │ enabled     │ BOOLEAN  │ DEFAULT true            │   │              │
│   │  │ user_type   │ VARCHAR  │ ADMIN, CUSTOMER         │   │              │
│   │  │                                                    │   │              │
│   │  │ -- Customer Fields                               │   │              │
│   │  │ driving_license_number │ VARCHAR │ NULL          │   │              │
│   │  │ address                │ VARCHAR │ NULL          │   │              │
│   │  │                                                    │   │              │
│   │  │ -- Admin Fields                                   │   │              │
│   │  │ department  │ VARCHAR  │ NULL                     │   │              │
│   │  │ employee_id │ VARCHAR  │ NULL                     │   │              │
│   │  │                                                    │   │              │
│   │  └─────────────┴──────────┴─────────────────────────┘   │              │
│   │                                                           │              │
│   │  Indexes:                                                │              │
│   │  • PRIMARY KEY: id                                       │              │
│   │  • UNIQUE INDEX: email                                   │              │
│   │                                                           │              │
│   └──────────────────────────────────────────────────────────┘              │
│                                                                             │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Authentication Flow Diagrams

### Registration Flow

```
┌─────────────────┐
│  User Input     │
│  (Form Data)    │
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│ Frontend Validation     │
│ • Email format          │
│ • Password length       │
│ • Passwords match       │
└────────┬────────────────┘
         │ Valid
         ▼
┌─────────────────────────┐
│ POST /api/auth/register │
│ + JSON Body             │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────────────┐
│ AuthController.register()       │
│ • @Valid annotation check       │
│ • Call AuthService.register()   │
└────────┬────────────────────────┘
         │
         ▼
┌─────────────────────────────────┐
│ AuthServiceImpl.register()       │
│ 1. Check email uniqueness       │
│ 2. Create User object           │
│ 3. Set role & enabled flag      │
│ 4. Encode password (BCrypt)     │
│ 5. Save to database             │
└────────┬────────────────────────┘
         │
         ▼
┌─────────────────────────────────┐
│ JwtUtil.generateToken()         │
│ • Extract claims (id, role)     │
│ • Set subject (email)           │
│ • Set issued time & expiration  │
│ • Sign with HS256               │
│ • Return JWT string             │
└────────┬────────────────────────┘
         │
         ▼
┌─────────────────────────────────┐
│ AuthResponse                    │
│ {                               │
│   "token": "eyJh...",           │
│   "id": 1,                      │
│   "name": "John",               │
│   "email": "john@email.com",    │
│   "role": "CUSTOMER"            │
│ }                               │
└────────┬────────────────────────┘
         │
         ▼
┌─────────────────────────────────┐
│ Frontend Receives Response       │
│ • Store token in localStorage   │
│ • Store user in localStorage    │
│ • Update AuthContext            │
│ • Redirect to /dashboard        │
└─────────────────────────────────┘
```

### Login Flow

```
┌─────────────────────────┐
│  User Credentials       │
│  (Email + Password)     │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│ Frontend Validation     │
│ • Email not empty       │
│ • Password not empty    │
└────────┬────────────────┘
         │ Valid
         ▼
┌────────────────────────┐
│ POST /api/auth/login   │
│ + JSON Body            │
└────────┬───────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ AuthController.login()           │
│ • @Valid annotation check        │
│ • Call AuthService.login()       │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ AuthServiceImpl.login()           │
│ 1. Call AuthenticationManager    │
│ 2. Pass email & password         │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ AuthenticationManager            │
│ 1. Call UserDetailsService       │
│ 2. Load user by email            │
│ 3. Get user from database        │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ PasswordEncoder.matches()        │
│ 1. Compare plain & hashed pwd    │
│ 2. Return true/false             │
└────────┬─────────────────────────┘
         │
    ┌────┴────┐
    │          │
    ▼          ▼
Success      Failure
    │          │
    ▼          ▼
┌──────┐  ┌──────────────────────┐
│Good  │  │ BadCredentials       │
│      │  │ Exception thrown     │
│      │  │                      │
│      │  │ → 401 Unauthorized   │
└──┬───┘  └──────────────────────┘
   │
   ▼
┌──────────────────────────────────┐
│ Generate JWT Token               │
│ • Same as registration           │
│ • Return AuthResponse            │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Frontend                         │
│ • Store token in localStorage    │
│ • Store user in localStorage     │
│ • Update AuthContext             │
│ • Redirect to /dashboard         │
└──────────────────────────────────┘
```

### Protected Route Access Flow

```
┌──────────────────────────────────┐
│ GET /dashboard (Protected Route)  │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ ProtectedRoute Component         │
│ • Check isAuthenticated flag     │
│ • Check loading state            │
└────────┬─────────────────────────┘
         │
    ┌────┴─────────────┐
    │                  │
    ▼                  ▼
Logged In          Not Logged
(Has Token)        (No Token)
    │                  │
    ▼                  ▼
┌────────────┐  ┌──────────────────┐
│ Render     │  │ Navigate         │
│ Children   │  │ to /login        │
│            │  │                  │
│ Continue   │  │ 401 Unauthorized │
│ to Request │  └──────────────────┘
└────┬───────┘
     │
     ▼
┌──────────────────────────────────┐
│ Add JWT to Request Headers       │
│ Authorization: Bearer {token}    │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ JwtAuthenticationFilter          │
│ 1. Extract token from header     │
│ 2. Parse & validate token        │
│ 3. Check expiration              │
│ 4. Verify signature              │
└────────┬─────────────────────────┘
         │
    ┌────┴───────┐
    │             │
    ▼             ▼
Valid        Invalid/Expired
Token        Token
    │             │
    ▼             ▼
┌──────┐  ┌──────────────┐
│ Set  │  │ SecurityContext
│ Auth │  │ removed
│      │  │
│      │  │ → Request fails
└──┬───┘  │ → 401 response
   │      │
   ▼      │
┌──────┐  │
│Next  │  │
│Filter│  │
└──┬───┘  │
   │      │
   ▼      │
┌────────────────────┐
│ AuthController or  │
│ Other Endpoint     │
│                    │
│ SecurityContext    │
│ available in       │
│ Authentication obj │
└────────────────────┘
```

---

## Component Interaction Diagram

```
┌────────────────────────────────────────────────────────────────────┐
│                    FRONTEND COMPONENT TREE                         │
├────────────────────────────────────────────────────────────────────┤
│                                                                    │
│  App.tsx                                                           │
│  ├── AuthProvider                                                  │
│  │   ├── Navbar                                                    │
│  │   │   ├─ Shows: Welcome, {User}, Logout (when logged in)       │
│  │   │   └─ Shows: Login, Register (when not logged in)           │
│  │   │                                                             │
│  │   └── Router (Routes)                                           │
│  │       ├── Route "/" → Home                                      │
│  │       │                                                         │
│  │       ├── Route "/login" → Login Page                           │
│  │       │   └─ Form → authService.login() → AuthContext.login()  │
│  │       │                                                         │
│  │       ├── Route "/register" → Register Page                     │
│  │       │   └─ Form → authService.register() → AuthContext.reg() │
│  │       │                                                         │
│  │       ├── Route "/vehicles" → Vehicles (Public)                 │
│  │       │                                                         │
│  │       ├── Route "/dashboard" → ProtectedRoute                   │
│  │       │   ├─ Check: isAuthenticated?                           │
│  │       │   ├─ Yes → Dashboard (useAuth hook)                     │
│  │       │   └─ No → Redirect to /login                           │
│  │       │                                                         │
│  │       └── Route "/booking/:id" → ProtectedRoute                 │
│  │           ├─ Check: isAuthenticated?                           │
│  │           ├─ Yes → Booking Page                                 │
│  │           └─ No → Redirect to /login                           │
│  │                                                                 │
│  └── Footer                                                        │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│               DATA FLOW BETWEEN COMPONENTS                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  AuthContext                                                     │
│  ├─ State: user, loading, isAuthenticated                        │
│  ├─ Methods: login, register, logout                             │
│  └─ Provides: useAuth() hook                                     │
│      │                                                            │
│      ├─→ Login Component uses useAuth()                          │
│      ├─→ Register Component uses useAuth()                       │
│      ├─→ ProtectedRoute uses useAuth()                           │
│      ├─→ Navbar uses useAuth()                                   │
│      └─→ Dashboard uses useAuth()                                │
│                                                                  │
│  localStorage                                                    │
│  ├─ Key: token (JWT string)                                      │
│  ├─ Key: user (User object JSON)                                 │
│  │                                                                │
│  └─→ Persists across page reloads                                │
│      └─→ AuthContext initializes from localStorage                │
│      └─→ axiosInstance reads token from localStorage              │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## Security Layers Visualization

```
┌─────────────────────────────────────────────────────────────┐
│                   SECURITY LAYERS                           │
└─────────────────────────────────────────────────────────────┘

Layer 1: TRANSPORT SECURITY
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  HTTP/HTTPS                                                 │
│  └─ HTTPS in production (TLS/SSL encryption)               │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
Layer 2: CORS SECURITY
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  CORS Configuration                                         │
│  ├─ Only allow: localhost:3000, 4200, 5173                │
│  ├─ Only allow: GET, POST, PUT, DELETE                    │
│  ├─ Only allow: Authorization, Content-Type headers       │
│  └─ Credentials allowed                                    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
Layer 3: INPUT VALIDATION
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  Frontend Validation                Backend Validation     │
│  ├─ Email format                    ├─ @Valid annotations  │
│  ├─ Password length                 ├─ Email format       │
│  ├─ Required fields                 ├─ Password length    │
│  └─ Password match                  └─ Field constraints   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
Layer 4: AUTHENTICATION
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  JWT Authentication                                         │
│  ├─ Bearer token in Authorization header                   │
│  ├─ Token signature validation (HS256)                     │
│  ├─ Token expiration check (24 hours)                      │
│  ├─ Claims validation (user ID, role)                      │
│  └─ JwtAuthenticationFilter intercepts all requests         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
Layer 5: PASSWORD SECURITY
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  BCrypt Password Encryption                                │
│  ├─ Strength: 10 rounds (configurable)                     │
│  ├─ Never stored in plain text                             │
│  ├─ Salt automatically generated                           │
│  ├─ Resistant to rainbow table attacks                     │
│  └─ Time complexity: ~100ms per hash                        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
Layer 6: AUTHORIZATION
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  Role-Based Access Control (RBAC)                           │
│  ├─ ROLE_CUSTOMER                                           │
│  ├─ ROLE_ADMIN                                              │
│  ├─ Endpoints protected by role                            │
│  ├─ SecurityContext has user principal                     │
│  └─ Authorization decisions at method level                 │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
Layer 7: ERROR HANDLING
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  GlobalExceptionHandler                                    │
│  ├─ No stack traces in production                          │
│  ├─ Secure error messages                                  │
│  ├─ Proper HTTP status codes                               │
│  ├─ Logging for debugging                                  │
│  └─ No sensitive data exposure                             │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Database Schema

```
┌─────────────────────────────────────────────────┐
│              users table                         │
│     (Single Table Inheritance Strategy)          │
├─────────────────────────────────────────────────┤
│                                                  │
│ PRIMARY KEY:                                     │
│ ├─ id BIGINT AUTO_INCREMENT                     │
│                                                  │
│ UNIQUE CONSTRAINT:                               │
│ ├─ email VARCHAR(255) UNIQUE                    │
│                                                  │
│ MANDATORY FIELDS:                                │
│ ├─ name VARCHAR(255) NOT NULL                   │
│ ├─ email VARCHAR(255) NOT NULL                  │
│ ├─ phone VARCHAR(20) NOT NULL                   │
│ ├─ password VARCHAR(255) NOT NULL (hashed)      │
│ ├─ role ENUM('ADMIN', 'CUSTOMER') NOT NULL      │
│ ├─ enabled BOOLEAN NOT NULL DEFAULT true        │
│ ├─ user_type VARCHAR(31) NOT NULL               │
│                                                  │
│ OPTIONAL FIELDS (Customer):                      │
│ ├─ driving_license_number VARCHAR(255)          │
│ ├─ address VARCHAR(255)                         │
│                                                  │
│ OPTIONAL FIELDS (Admin):                         │
│ ├─ department VARCHAR(255)                      │
│ ├─ employee_id VARCHAR(255)                     │
│                                                  │
│ AUDIT FIELDS:                                    │
│ ├─ created_at TIMESTAMP DEFAULT NOW             │
│ ├─ updated_at TIMESTAMP ON UPDATE NOW           │
│                                                  │
├─────────────────────────────────────────────────┤
│ INDEXES:                                         │
│ ├─ PRIMARY KEY: id                               │
│ ├─ UNIQUE INDEX: email                           │
│ ├─ INDEX: role (for queries)                     │
│ ├─ INDEX: user_type (for inheritance)           │
│                                                  │
├─────────────────────────────────────────────────┤
│ EXAMPLE ROWS:                                    │
│                                                  │
│ Customer Row:                                    │
│ ├─ id: 1                                         │
│ ├─ name: John Doe                                │
│ ├─ email: john@example.com                       │
│ ├─ password: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg... │
│ ├─ role: CUSTOMER                                │
│ ├─ user_type: CUSTOMER                           │
│ ├─ driving_license_number: DL123456              │
│ ├─ address: 123 Main Street                      │
│ ├─ enabled: true                                 │
│                                                  │
│ Admin Row:                                       │
│ ├─ id: 2                                         │
│ ├─ name: Admin User                              │
│ ├─ email: admin@example.com                      │
│ ├─ password: $2a$10$M8qo8uLOickgx2ZMRZoMyeIjZAgcg... │
│ ├─ role: ADMIN                                   │
│ ├─ user_type: ADMIN                              │
│ ├─ department: Operations                        │
│ ├─ employee_id: EMP001                           │
│ ├─ enabled: true                                 │
│                                                  │
└─────────────────────────────────────────────────┘
```

---

## API Endpoints Overview

```
┌────────────────────────────────────────────────────────────┐
│            DriveHub Authentication API Endpoints           │
├────────────────────────────────────────────────────────────┤
│                                                             │
│ 1. REGISTRATION (Public)                                   │
│    ─────────────────────────────                           │
│    POST /api/auth/register                                 │
│    │                                                        │
│    ├─ Request:                                             │
│    │  {                                                     │
│    │    "name": "John Doe",                                │
│    │    "email": "john@example.com",                       │
│    │    "phone": "+1234567890",                            │
│    │    "password": "password123",                         │
│    │    "userType": "CUSTOMER",                            │
│    │    "drivingLicenseNumber": "DL123456",               │
│    │    "address": "123 Main St"                           │
│    │  }                                                     │
│    │                                                        │
│    ├─ Response (201 Created):                              │
│    │  {                                                     │
│    │    "token": "eyJhbGciOiJIUzI1NiIs...",              │
│    │    "type": "Bearer",                                  │
│    │    "id": 1,                                           │
│    │    "name": "John Doe",                                │
│    │    "email": "john@example.com",                       │
│    │    "role": "CUSTOMER"                                 │
│    │  }                                                     │
│    │                                                        │
│    └─ Errors:                                              │
│       • 400: Email already exists                          │
│       • 400: Invalid email format                          │
│       • 400: Password too short                            │
│       • 500: Server error                                  │
│                                                             │
├────────────────────────────────────────────────────────────┤
│                                                             │
│ 2. LOGIN (Public)                                          │
│    ──────────────                                          │
│    POST /api/auth/login                                    │
│    │                                                        │
│    ├─ Request:                                             │
│    │  {                                                     │
│    │    "email": "john@example.com",                       │
│    │    "password": "password123"                          │
│    │  }                                                     │
│    │                                                        │
│    ├─ Response (200 OK):                                   │
│    │  {                                                     │
│    │    "token": "eyJhbGciOiJIUzI1NiIs...",              │
│    │    "type": "Bearer",                                  │
│    │    "id": 1,                                           │
│    │    "name": "John Doe",                                │
│    │    "email": "john@example.com",                       │
│    │    "role": "CUSTOMER"                                 │
│    │  }                                                     │
│    │                                                        │
│    └─ Errors:                                              │
│       • 401: Invalid email or password                     │
│       • 400: Email required                                │
│       • 400: Password required                             │
│       • 404: User not found                                │
│       • 500: Server error                                  │
│                                                             │
├────────────────────────────────────────────────────────────┤
│                                                             │
│ 3. GET CURRENT USER (Protected)                            │
│    ──────────────────────────────                          │
│    GET /api/auth/me                                        │
│    │                                                        │
│    ├─ Headers:                                             │
│    │  Authorization: Bearer eyJhbGciOiJIUzI1NiIs...       │
│    │                                                        │
│    ├─ Response (200 OK):                                   │
│    │  {                                                     │
│    │    "id": 1,                                           │
│    │    "name": "John Doe",                                │
│    │    "email": "john@example.com",                       │
│    │    "phone": "+1234567890",                            │
│    │    "role": "CUSTOMER",                                │
│    │    "enabled": true,                                   │
│    │    "drivingLicenseNumber": "DL123456",               │
│    │    "address": "123 Main St"                           │
│    │  }                                                     │
│    │                                                        │
│    └─ Errors:                                              │
│       • 401: No token provided                             │
│       • 401: Invalid token                                 │
│       • 401: Token expired                                 │
│       • 500: Server error                                  │
│                                                             │
└────────────────────────────────────────────────────────────┘
```

---

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────┐
│               PRODUCTION DEPLOYMENT                      │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  FRONTEND (React SPA)                                    │
│  ┌─────────────────────────────────────────────┐        │
│  │ CDN / Web Server (Nginx)                    │        │
│  │ • Static file serving                       │        │
│  │ • Gzip compression                          │        │
│  │ • Cache headers                             │        │
│  │ • SSL/TLS certificate                       │        │
│  │ • Redirect HTTP → HTTPS                     │        │
│  │ • CORS headers configured                   │        │
│  │                                             │        │
│  │ .env.production                             │        │
│  │ • VITE_API_URL=https://api.example.com     │        │
│  └────────────────────┬────────────────────────┘        │
│                       │ HTTPS                           │
│                       │                                 │
│  BACKEND (Spring Boot)                                  │
│  ┌────────────────────▼────────────────────────┐        │
│  │ Application Server                          │        │
│  │ • Spring Boot JAR                           │        │
│  │ • JVM with production settings              │        │
│  │ • Load balancer (nginx/HAProxy)             │        │
│  │ • Health checks endpoint                    │        │
│  │ • Monitoring/Logging                        │        │
│  │                                             │        │
│  │ application-prod.properties                 │        │
│  │ • DB_URL (environment variable)             │        │
│  │ • DB_USERNAME (environment variable)        │        │
│  │ • JWT_SECRET (environment variable)         │        │
│  │ • DDL: validate (not update)                │        │
│  │ • Logging: INFO level                       │        │
│  │ • Show SQL: false                           │        │
│  │                                             │        │
│  └────────────────────┬────────────────────────┘        │
│                       │ Connection Pool                 │
│                       │ (HikariCP)                      │
│  DATABASE (MySQL)                                       │
│  ┌────────────────────▼────────────────────────┐        │
│  │ MySQL Server                                │        │
│  │ • Production database                       │        │
│  │ • Encrypted connections                     │        │
│  │ • Regular backups                           │        │
│  │ • Replication setup                         │        │
│  │ • indexes on email, role, user_type         │        │
│  │                                             │        │
│  │ drivehub_db                                 │        │
│  │ └─ users table                              │        │
│  │    └─ Single table inheritance              │        │
│  └─────────────────────────────────────────────┘        │
│                                                          │
│  MONITORING & LOGGING                                   │
│  ┌──────────────────────────────────────────────┐       │
│  │ • ELK Stack (Elasticsearch, Logstash, Kibana)│      │
│  │ • Prometheus metrics                         │       │
│  │ • Alert notifications                        │       │
│  │ • Application Performance Monitoring (APM)   │       │
│  └──────────────────────────────────────────────┘       │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

---

**Visual Guide Created**: October 16, 2025  
**For detailed documentation**: See AUTHENTICATION_IMPLEMENTATION_GUIDE.md
