# 🔧 Advanced Configuration & Enhancements

## Optional Improvements & Best Practices

This document covers optional enhancements to your authentication system for production readiness and advanced features.

---

## 1. Environment-Based Configuration

### Current Setup (Basic)
**File**: `backend/src/main/resources/application.properties`

### Recommended Setup (Production-Ready)

Create separate property files:

**`application-dev.properties`**:
```properties
# Development Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/drivehub_db
spring.datasource.username=root
spring.datasource.password=dev_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.expiration=86400000
logging.level.com.drivehub=DEBUG
```

**`application-prod.properties`**:
```properties
# Production Configuration
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
jwt.expiration=3600000
logging.level.com.drivehub=INFO
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

**Run with profile**:
```bash
# Development
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

# Production
java -jar application.jar --spring.profiles.active=prod
```

---

## 2. Enhanced Security Features

### 2.1 Account Lockout After Failed Attempts

**Add to AuthServiceImpl**:
```java
@Transactional
public AuthResponse login(LoginRequest request) {
    log.info("User login attempt: {}", request.getEmail());
    
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // Reset failed attempts on successful login
        user.setFailedAttempts(0);
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getName(), 
                                user.getEmail(), user.getRole().name());
                                
    } catch (BadCredentialsException e) {
        user.setFailedAttempts(user.getFailedAttempts() != null ? 
                                user.getFailedAttempts() + 1 : 1);
        
        if (user.getFailedAttempts() >= 5) {
            user.setEnabled(false);
            log.warn("User account locked due to failed attempts: {}", request.getEmail());
        }
        userRepository.save(user);
        throw e;
    }
}
```

**Add fields to User entity**:
```java
@Column(nullable = false)
private Integer failedAttempts = 0;

@Column
private LocalDateTime lockedTime;

@Column
private LocalDateTime passwordChangedTime;
```

### 2.2 Refresh Token Implementation

**Create RefreshToken entity**:
```java
@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    
    @Column(nullable = false)
    private Boolean revoked = false;
}
```

**Enhanced JwtUtil**:
```java
public RefreshToken createRefreshToken(User user) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(generateRefreshTokenString());
    refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
    return refreshTokenRepository.save(refreshToken);
}

private String generateRefreshTokenString() {
    return UUID.randomUUID().toString();
}

public Optional<RefreshToken> verifyRefreshToken(String token) {
    return refreshTokenRepository.findByToken(token)
        .filter(rt -> !rt.getRevoked() && rt.getExpiryDate().isAfter(LocalDateTime.now()));
}
```

**New endpoint**:
```java
@PostMapping("/refresh")
public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
    RefreshToken refreshToken = jwtUtil.verifyRefreshToken(request.getRefreshToken())
        .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
    
    User user = refreshToken.getUser();
    String newToken = jwtUtil.generateToken(user);
    
    return ResponseEntity.ok(new AuthResponse(newToken, user.getId(), user.getName(), 
                                              user.getEmail(), user.getRole().name()));
}
```

### 2.3 Rate Limiting

**Add dependency to pom.xml**:
```xml
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

**Create Rate Limit Filter**:
```java
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        String key = getClientKey(request);
        Bucket bucket = resolveBucket(key);
        
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("Rate limit exceeded");
        }
    }
    
    private String getClientKey(HttpServletRequest request) {
        return request.getRemoteUser() != null ? 
               request.getRemoteUser() : request.getRemoteAddr();
    }
    
    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> createNewBucket());
    }
    
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));
        return Bucket4j.builder()
            .addLimit(limit)
            .build();
    }
}
```

### 2.4 Two-Factor Authentication (2FA) Setup

**Add OTP dependency**:
```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>31.1-jre</version>
</dependency>
```

**Add 2FA fields to User**:
```java
@Column
private String totpSecret;

@Column
private Boolean twoFactorEnabled = false;
```

**2FA Service**:
```java
@Service
public class TwoFactorService {
    public String generateSecret() {
        return new Base32().encode(SecureRandom.getInstanceStrong()
            .generateSeed(20));
    }
    
    public boolean verifyTotp(String secret, String code) {
        TimeBasedOneTimePasswordProvider totp = new TimeBasedOneTimePasswordProvider();
        return totp.verify(secret, Long.parseLong(code));
    }
}
```

---

## 3. Advanced Logging & Monitoring

### 3.1 Structured Logging with ELK Stack

**Add Logback configuration** - `logback-spring.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <springProperty name="APPLICATION_NAME" source="spring.application.name"/>
    
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

### 3.2 Audit Logging for Authentication

**Create Audit Entity**:
```java
@Entity
@Table(name = "auth_audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String userEmail;
    
    @Column
    private String action; // LOGIN, LOGOUT, REGISTER, FAILED_LOGIN
    
    @Column
    private String ipAddress;
    
    @Column
    private String userAgent;
    
    @Column
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Column
    private String status; // SUCCESS, FAILURE
}
```

**Audit Service**:
```java
@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    
    public void logAuthAction(String email, String action, String status, 
                             HttpServletRequest request) {
        AuthAuditLog log = new AuthAuditLog();
        log.setUserEmail(email);
        log.setAction(action);
        log.setStatus(status);
        log.setIpAddress(getClientIp(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        auditLogRepository.save(log);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
```

---

## 4. Email Verification

### 4.1 Email Verification on Registration

**Add dependency**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

**Configuration**:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Email Service**:
```java
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    
    public void sendVerificationEmail(User user, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("DriveHub - Verify Your Email");
        message.setText("Click the link to verify: " +
            "http://localhost:5173/verify?token=" + verificationToken);
        mailSender.send(message);
    }
}
```

---

## 5. Frontend Enhancements

### 5.1 Enhanced AuthContext with Persistence

```javascript
import { createContext, useContext, useReducer, useEffect, useCallback } from 'react';
import authService from '../services/authService';

const AuthContext = createContext();

const initialState = {
  user: null,
  loading: true,
  error: null,
  isAuthenticated: false,
};

const authReducer = (state, action) => {
  switch (action.type) {
    case 'LOGIN_SUCCESS':
      return {
        user: action.payload,
        isAuthenticated: true,
        loading: false,
        error: null,
      };
    case 'LOGIN_FAILURE':
      return {
        user: null,
        isAuthenticated: false,
        loading: false,
        error: action.payload,
      };
    case 'LOGOUT':
      return { ...initialState, loading: false };
    case 'SET_LOADING':
      return { ...state, loading: action.payload };
    default:
      return state;
  }
};

export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Initialize on mount
  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (currentUser && authService.isAuthenticated()) {
      dispatch({
        type: 'LOGIN_SUCCESS',
        payload: currentUser,
      });
    } else {
      dispatch({ type: 'SET_LOADING', payload: false });
    }
  }, []);

  const login = useCallback(async (credentials) => {
    try {
      const userData = await authService.login(credentials);
      dispatch({
        type: 'LOGIN_SUCCESS',
        payload: userData,
      });
      return userData;
    } catch (error) {
      dispatch({
        type: 'LOGIN_FAILURE',
        payload: error.message,
      });
      throw error;
    }
  }, []);

  const register = useCallback(async (userData) => {
    try {
      const newUser = await authService.register(userData);
      dispatch({
        type: 'LOGIN_SUCCESS',
        payload: newUser,
      });
      return newUser;
    } catch (error) {
      dispatch({
        type: 'LOGIN_FAILURE',
        payload: error.message,
      });
      throw error;
    }
  }, []);

  const logout = useCallback(() => {
    authService.logout();
    dispatch({ type: 'LOGOUT' });
  }, []);

  const value = {
    ...state,
    login,
    register,
    logout,
    isAdmin: () => state.user?.role === 'ADMIN',
    isCustomer: () => state.user?.role === 'CUSTOMER',
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
```

### 5.2 Enhanced Error Handling

```javascript
// services/errorHandler.js
export const handleAuthError = (error) => {
  if (error.response) {
    const status = error.response.status;
    const data = error.response.data;

    switch (status) {
      case 401:
        return 'Invalid email or password';
      case 400:
        return data.message || 'Invalid request';
      case 409:
        return 'Email already registered';
      case 429:
        return 'Too many login attempts. Try again later.';
      case 500:
        return 'Server error. Please try again later.';
      default:
        return data.message || 'An error occurred';
    }
  }
  return 'Network error. Please check your connection.';
};
```

### 5.3 TypeScript Types for Auth

```typescript
// types/auth.ts
export interface User {
  id: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'CUSTOMER';
  token?: string;
  type?: string;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterData extends LoginCredentials {
  name: string;
  phone: string;
  userType: 'ADMIN' | 'CUSTOMER';
  confirmPassword: string;
  drivingLicenseNumber?: string;
  address?: string;
  department?: string;
  employeeId?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  name: string;
  email: string;
  role: string;
}

export interface AuthContextType {
  user: User | null;
  loading: boolean;
  error: string | null;
  isAuthenticated: boolean;
  login: (credentials: LoginCredentials) => Promise<User>;
  register: (data: RegisterData) => Promise<User>;
  logout: () => void;
  isAdmin: () => boolean;
  isCustomer: () => boolean;
}
```

---

## 6. Testing Best Practices

### 6.1 Backend Unit Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceImplTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AuthServiceImpl authService;
    
    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        
        // Assert registration succeeds
    }
    
    @Test
    void testRegisterDuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request));
    }
}
```

### 6.2 Frontend Tests

```javascript
// __tests__/AuthContext.test.jsx
import { render, screen, waitFor } from '@testing-library/react';
import { AuthProvider, useAuth } from '../context/AuthContext';
import * as authService from '../services/authService';

jest.mock('../services/authService');

test('should login user successfully', async () => {
  const mockUser = { id: 1, name: 'Test', email: 'test@example.com' };
  authService.login.mockResolvedValue(mockUser);
  
  // Test implementation
});
```

---

## 7. Deployment Checklist

### Backend Deployment
- [ ] Change `jwt.secret` to strong random value (64+ chars)
- [ ] Update database credentials in environment variables
- [ ] Enable HTTPS/SSL
- [ ] Configure CORS for production domain
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not update)
- [ ] Disable SQL logging in production
- [ ] Configure logging to file or ELK stack
- [ ] Set up database backups
- [ ] Enable Spring Security headers
- [ ] Configure database connection pooling

**Enable Security Headers** - Add to SecurityConfig:
```java
.headers(headers -> headers
    .contentSecurityPolicy("default-src 'self'")
    .frameOptions(frameOptions -> frameOptions.deny())
    .xssProtection(xssProtection -> xssProtection.and(true))
)
```

### Frontend Deployment
- [ ] Update `VITE_API_URL` to production backend
- [ ] Enable HTTPS
- [ ] Configure secure cookies (if using)
- [ ] Set proper cache headers
- [ ] Enable gzip compression
- [ ] Minify and optimize builds
- [ ] Set Content Security Policy headers
- [ ] Enable HSTS header

---

## 8. Performance Optimization

### Backend
- **Connection Pooling**: HikariCP (included with Spring Boot)
- **Database Indexing**: Create indexes on frequently queried fields
- **Caching**: Redis for token validation cache
- **Lazy Loading**: Use `@Lazy` for optional relationships

### Frontend
- **Code Splitting**: Lazy load pages with React.lazy()
- **Bundle Optimization**: Analyze with webpack-bundle-analyzer
- **Image Optimization**: Use WebP format
- **Service Worker**: Cache authentication responses

---

## 9. Monitoring & Alerting

### Backend Metrics
```java
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    private final MeterRegistry meterRegistry;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "uptime", ManagementFactory.getRuntimeMXBean().getUptime()
        ));
    }
}
```

### Frontend Analytics
```javascript
// Track authentication events
export const trackAuthEvent = (event, properties) => {
  // Send to analytics service
  console.log(`[ANALYTICS] ${event}:`, properties);
};

// Usage
trackAuthEvent('user_registered', { userType: 'CUSTOMER' });
trackAuthEvent('user_logged_in', { email: 'user@example.com' });
```

---

## 10. Version Control & CI/CD

### GitHub Actions Example

**`.github/workflows/ci.yml`**:
```yaml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build
        run: cd backend && mvn clean package
      - name: Run tests
        run: cd backend && mvn test
```

---

## Summary of Enhancements

| Feature | Priority | Complexity | Impact |
|---------|----------|-----------|--------|
| Environment Configuration | High | Low | Production Ready |
| Refresh Tokens | High | Medium | Better UX |
| Rate Limiting | High | Medium | Security |
| Email Verification | Medium | Medium | Trust |
| 2FA | Low | High | Security |
| Audit Logging | Medium | Low | Compliance |
| Advanced Error Handling | High | Low | UX |
| Performance Optimization | Medium | Medium | Scalability |
| Monitoring | Medium | Medium | Reliability |
| CI/CD Pipeline | Medium | Medium | DevOps |

---

**Recommended implementation order**:
1. Environment Configuration (immediate)
2. Refresh Tokens + Error Handling (week 1)
3. Rate Limiting + Audit Logging (week 2)
4. Email Verification (week 3)
5. Monitoring + CI/CD (week 4)
6. Advanced features (ongoing)

---

Last Updated: October 16, 2025  
Reference: AUTHENTICATION_IMPLEMENTATION_GUIDE.md
