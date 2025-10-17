# 🚀 DriveHub Frontend - Quick Start Guide

## ⚡ 3-Minute Setup

### Step 1: Install Dependencies (1 minute)
```bash
cd drivehub-frontend
npm install
```

### Step 2: Start Backend (if not running)
Make sure your Spring Boot backend is running on `http://localhost:8080`

### Step 3: Start Frontend (1 minute)
```bash
npm run dev
```

✅ **Application is running!** Visit: http://localhost:5173

---

## 🎯 First Steps

### 1. Register a Customer Account

1. Click "Register" in the navbar
2. Fill in the form:
   - Name: John Doe
   - Email: john@example.com
   - Password: password123
   - Account Type: Customer
   - License: DL123456
   - Address: 123 Main St

3. Click "Create Account"
4. You'll be redirected to the dashboard

### 2. Browse Vehicles

- Go to "Vehicles" page
- See all available vehicles
- Use search and filters
- Click "View Details" or "Book Now"

### 3. Create a Booking

- Select a vehicle
- Choose start and end dates
- Select payment method
- Add any notes
- Click "Confirm Booking"

### 4. View Your Bookings

- Go to "Dashboard"
- See all your bookings
- Check booking status

### 5. Admin Features (Register as Admin)

Register with Account Type: Admin
- Manage all bookings
- Add new vehicles
- Update booking status
- Delete vehicles

---

## 🖥️ Screenshots Tour

### Home Page
- Hero section with CTA buttons
- Featured vehicles
- Key features

### Vehicles Page
- Search bar
- Type filter
- Vehicle cards with details

### Booking Page
- Vehicle details
- Booking form
- Cost calculation

### Dashboard
- Customer: Personal bookings
- Admin: All bookings + vehicle management

---

## 🎨 UI Components

### Colors
- **Primary**: Blue (#2563eb)
- **Success**: Green
- **Danger**: Red
- **Gray**: Neutral

### Icons (Emojis Used)
- 🚗 Cars
- 🏍️ Bikes
- 🚐 Vans
- ✅ Success
- ❌ Error

---

## 🔧 Troubleshooting

### Port 5173 already in use
```bash
# Change port in package.json
"dev": "vite --port 3000"
```

### Backend not connecting
1. Check backend is running on port 8080
2. Verify `src/services/axiosInstance.js` has correct URL
3. Check browser console for errors

### Tailwind styles not working
```bash
# Reinstall dependencies
rm -rf node_modules
npm install
npm run dev
```

### Login/Register not working
1. Check backend is running
2. Check MySQL database is connected
3. Check network tab in browser DevTools
4. Verify backend endpoints are accessible

---

## 📋 Testing Checklist

- [ ] Register customer account
- [ ] Login with credentials
- [ ] Browse vehicles
- [ ] Filter vehicles by type
- [ ] Search vehicles
- [ ] Create booking
- [ ] View booking in dashboard
- [ ] Logout and login again
- [ ] Register admin account
- [ ] Add new vehicle
- [ ] Update booking status
- [ ] Delete vehicle

---

## 🎓 Project Structure Quick Reference

```
src/
├── components/     # Reusable UI components
├── pages/          # Page components (routes)
├── services/       # API calls
├── context/        # State management
├── hooks/          # Custom React hooks
└── assets/         # Images, icons
```

---

## 🌐 URLs Reference

| Service | URL |
|---------|-----|
| **Frontend** | http://localhost:5173 |
| **Backend** | http://localhost:8080 |
| **Backend API** | http://localhost:8080/api |
| **Swagger** | http://localhost:8080/swagger-ui.html |

---

## 📦 Key Dependencies

```json
{
  "react": "^18.x",
  "react-router-dom": "^6.x",
  "axios": "^1.x",
  "tailwindcss": "^3.x"
}
```

---

## 🚀 Build for Production

```bash
# Create production build
npm run build

# Preview production build locally
npm run preview
```

Build output: `dist/` folder

---

## 💡 Pro Tips

1. **Use Browser DevTools**: Check Network tab for API calls
2. **Check Console**: Look for JavaScript errors
3. **React DevTools**: Install browser extension for debugging
4. **Tailwind IntelliSense**: Install VS Code extension for autocomplete

---

## 📞 Need Help?

- Check README.md for detailed documentation
- Review backend POSTMAN_GUIDE.md for API examples
- Check browser console for errors
- Verify backend is running and accessible

---

## 🎉 You're Ready!

Your DriveHub frontend is fully functional. Explore the features and happy coding! 🚗💨

**Next Steps:**
- Customize styling
- Add more features
- Deploy to production
- Build mobile app
