# 🏨 Hotel Management System - Test Accounts

## 📧 Test Email Accounts & Passwords

All test accounts use the password: **`hotel2024`**

---

## 👨‍💼 STAFF/EMPLOYEE ACCOUNTS

### 1. Hotel Manager
- **Email**: `manager@grandhotel.com`
- **Password**: `hotel2024`
- **Role**: MANAGER
- **Name**: John Anderson

### 2. Front Desk Receptionist
- **Email**: `receptionist@grandhotel.com`
- **Password**: `hotel2024`
- **Role**: RECEPTIONIST
- **Name**: Sarah Johnson

### 3. Housekeeping Staff
- **Email**: `housekeeping@grandhotel.com`
- **Password**: `hotel2024`
- **Role**: CLEANER
- **Name**: Maria Garcia

### 4. Additional Receptionist
- **Email**: `reception2@grandhotel.com`
- **Password**: `hotel2024`
- **Role**: RECEPTIONIST
- **Name**: Michael Chen

---

## 🎫 GUEST ACCOUNTS

### 1. Premium Guest (Has Room Assigned)
- **Email**: `alice.premium@email.com`
- **Password**: `hotel2024`
- **Name**: Alice Williams
- **Room**: Suite 301 (Assigned)

### 2. Standard Guest (Has Room Assigned)
- **Email**: `bob.standard@email.com`
- **Password**: `hotel2024`
- **Name**: Bob Thompson
- **Room**: Double Room 202 (Assigned)

### 3. New Guest (No Room Yet)
- **Email**: `carol.newguest@email.com`
- **Password**: `hotel2024`
- **Name**: Carol Martinez
- **Room**: Not assigned yet

### 4. VIP Guest (Has Room Assigned)
- **Email**: `david.vip@email.com`
- **Password**: `hotel2024`
- **Name**: David Robinson
- **Room**: Deluxe Suite 401 (Assigned)

---

## 🚀 Quick Start

### Access the Application:
1. **Frontend**: http://localhost:3000
2. **Backend API**: http://localhost:8080

### Login Steps:
1. Go to http://localhost:3000
2. Toggle between **Staff Login** or **Guest Login**
3. Enter one of the email addresses above
4. Enter password: `hotel2024`
5. Click **Sign In**

---

## 🎯 What You Can Do

### As Staff Member:
- View dashboard with hotel statistics
- Manage all rooms (create, update, delete)
- Manage employees
- Manage guests
- Assign rooms to guests
- Change room status (Available, Booked, Maintenance)

### As Guest:
- View your assigned room details
- See room type, price, and status
- Browse available room upgrades
- Request room upgrades
- Track upgrade request status (Pending/Approved/Rejected)

---

## 📝 Database Info

The system will automatically create these accounts when you start the backend server.
All data is stored in your PostgreSQL database: `hotel_management`

---

## 🔄 Reset Instructions

If you need to reset the data:
```bash
# Stop the backend
pkill -f spring-boot

# Restart the backend (will re-run migrations)
cd /Users/rosudaniel/Desktop/hotel-management
./mvnw spring-boot:run
```

---

## 📞 Support

If you encounter any issues:
1. Make sure backend is running on port 8080
2. Make sure frontend is running on port 3000
3. Check browser console for errors
4. Verify database is running

**Enjoy your Hotel Management System! 🎉**

