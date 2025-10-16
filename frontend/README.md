# 🚗 DriveHub Frontend - React Application

A professional, modern React frontend for the DriveHub Vehicle Rental Management System built with Vite, Tailwind CSS, and React Router.

## 🎯 Features

- **User Authentication** - JWT-based login and registration
- **Vehicle Browsing** - Search and filter vehicles by type
- **Booking System** - Create and manage vehicle bookings
- **Admin Dashboard** - Manage vehicles and bookings
- **Customer Dashboard** - View personal bookings
- **Responsive Design** - Mobile-first, fully responsive UI
- **Protected Routes** - Role-based access control

## 🛠 Technology Stack

- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Routing**: React Router DOM v6
- **HTTP Client**: Axios
- **State Management**: React Context API
- **Package Manager**: npm

## 📂 Project Structure

```
drivehub-frontend/
├── public/
├── src/
│   ├── assets/               # Images, icons, logos
│   ├── components/           # Reusable components
│   │   ├── Navbar.jsx
│   │   ├── Footer.jsx
│   │   ├── VehicleCard.jsx
│   │   ├── Button.jsx
│   │   ├── LoadingSpinner.jsx
│   │   └── ProtectedRoute.jsx
│   ├── context/              # React Context
│   │   └── AuthContext.jsx
│   ├── hooks/                # Custom hooks
│   │   └── useAuth.js
│   ├── pages/                # Page components
│   │   ├── Home.jsx
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Vehicles.jsx
│   │   ├── Booking.jsx
│   │   └── Dashboard.jsx
│   ├── services/             # API services
│   │   ├── axiosInstance.js
│   │   ├── authService.js
│   │   ├── vehicleService.js
│   │   ├── bookingService.js
│   │   └── paymentService.js
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
└── postcss.config.js
```

## 🚀 Getting Started

### Prerequisites

- Node.js 16+ and npm
- DriveHub backend running on `http://localhost:8080`

### Installation

1. **Navigate to frontend directory**
   ```bash
   cd drivehub-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure API endpoint** (if needed)
   
   Edit `src/services/axiosInstance.js`:
   ```javascript
   baseURL: 'http://localhost:8080/api'
   ```

4. **Start development server**
   ```bash
   npm run dev
   ```

The application will be available at `http://localhost:5173`

## 📜 Available Scripts

```bash
# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

## 🎨 UI Components

### Navbar
- Displays app logo and navigation links
- Shows user info when logged in
- Logout functionality

### VehicleCard
- Displays vehicle information
- Shows availability status
- Book now and view details buttons

### Footer
- Company information
- Quick links
- Contact details

### Button
- Reusable button component
- Multiple variants (primary, secondary, success, danger, outline)
- Different sizes (sm, md, lg)

### LoadingSpinner
- Loading indicator
- Multiple sizes and colors

### ProtectedRoute
- Wraps protected pages
- Redirects unauthenticated users to login
- Supports admin-only routes

## 🔐 Authentication Flow

### Registration
1. User fills registration form
2. Data sent to `/api/auth/register`
3. JWT token received and stored in localStorage
4. User redirected to dashboard

### Login
1. User enters credentials
2. Data sent to `/api/auth/login`
3. JWT token received and stored
4. User redirected to dashboard

### Token Management
- Token stored in localStorage as `token`
- User data stored as `user`
- Axios interceptor adds token to all requests
- Auto-redirect to login on 401 errors

## 📄 Pages

### Home (`/`)
- Hero section with call-to-action
- Featured vehicles display
- Key features showcase

### Vehicles (`/vehicles`)
- List all available vehicles
- Search and filter functionality
- View details and book now options

### Booking (`/booking/:id`)
- Vehicle details display
- Booking form (dates, payment method)
- Real-time cost calculation
- Protected route (requires authentication)

### Login (`/login`)
- Email and password fields
- Link to registration page
- Error handling

### Register (`/register`)
- Comprehensive registration form
- Customer and Admin options
- Password confirmation
- Field validation

### Dashboard (`/dashboard`)
- **Customer View**: View personal bookings
- **Admin View**: Manage all bookings and vehicles
- Add/delete vehicles (Admin)
- Update booking status (Admin)
- Protected route

## 🔌 API Integration

### Services Structure

**axiosInstance.js**
- Base configuration
- Request/response interceptors
- Token management
- Error handling

**authService.js**
- `register(userData)` - Register new user
- `login(credentials)` - Login user
- `logout()` - Clear auth data
- `getCurrentUser()` - Get user from localStorage
- `isAuthenticated()` - Check auth status

**vehicleService.js**
- `getAllVehicles()` - Get all vehicles
- `getAvailableVehicles()` - Get available only
- `getVehicleById(id)` - Get single vehicle
- `createVehicle(data)` - Add vehicle (Admin)
- `updateVehicle(id, data)` - Update vehicle (Admin)
- `deleteVehicle(id)` - Delete vehicle (Admin)

**bookingService.js**
- `createBooking(data)` - Create new booking
- `getAllBookings()` - Get all bookings (Admin)
- `getMyBookings()` - Get user's bookings
- `updateBookingStatus(id, status)` - Update status (Admin)
- `cancelBooking(id)` - Cancel booking

## 🎨 Styling with Tailwind

### Color Scheme
- **Primary**: Blue shades (600, 700)
- **Success**: Green (600)
- **Danger**: Red (600)
- **Gray**: Neutral backgrounds

### Custom Configuration
```javascript
// tailwind.config.js
theme: {
  extend: {
    colors: {
      primary: {
        // Custom blue shades
      }
    }
  }
}
```

### Responsive Breakpoints
- **sm**: 640px
- **md**: 768px
- **lg**: 1024px
- **xl**: 1280px

## 🔒 Protected Routes

Routes requiring authentication:
- `/booking/:id` - Create booking
- `/dashboard` - User dashboard

Admin-only features:
- Add/edit/delete vehicles
- View all bookings
- Update booking status

## 🐛 Error Handling

- Network errors displayed with user-friendly messages
- 401 errors trigger auto-logout and redirect
- Form validation errors shown inline
- API errors caught and displayed

## 🚀 Deployment

### Build for Production

```bash
npm run build
```

Output in `dist/` folder.

### Deploy to Netlify/Vercel

1. Connect repository
2. Set build command: `npm run build`
3. Set publish directory: `dist`
4. Add environment variables if needed

### Environment Variables

Create `.env` file:
```
VITE_API_URL=http://localhost:8080/api
```

Access in code:
```javascript
import.meta.env.VITE_API_URL
```

## 📱 Responsive Design

- Mobile-first approach
- Hamburger menu for mobile (to be implemented)
- Grid layouts adapt to screen size
- Touch-friendly buttons and inputs

## 🧪 Testing

To add testing:

```bash
npm install -D vitest @testing-library/react @testing-library/jest-dom
```

Create `src/tests/` directory for test files.

## 🎯 Future Enhancements

- [ ] Image upload for vehicles
- [ ] Payment gateway integration
- [ ] Real-time notifications
- [ ] Vehicle reviews and ratings
- [ ] Advanced search filters
- [ ] Booking history export
- [ ] Mobile app with React Native

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📞 Support

For issues or questions:
- Email: support@drivehub.com
- Create an issue in the repository

## 📄 License

This project is licensed under the Apache License 2.0.

---

**Built with ❤️ using React + Vite + Tailwind CSS**
