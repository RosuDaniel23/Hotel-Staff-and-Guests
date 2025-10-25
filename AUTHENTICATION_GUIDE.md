# Hotel Management System - Authentication Guide

## Overview
The system now includes complete authentication for both hotel staff (employees) and guests with separate login interfaces and functionalities.

## Features

### Staff Interface
- **Login**: Email and password authentication for hotel employees
- **Dashboard**: Overview of hotel statistics
- **Room Management**: Full CRUD operations for rooms
- **Employee Management**: Manage hotel staff
- **Guest Management**: Manage guest information and room assignments

### Guest Portal
- **Login**: Email and password authentication for guests
- **View Current Room**: See assigned room details (number, type, price, status)
- **Request Upgrades**: Browse available room upgrades and submit upgrade requests
- **Track Requests**: View history and status of upgrade requests (Pending/Approved/Rejected)

## Getting Started

### Backend Setup
1. The database will automatically migrate to add password fields and upgrade request functionality
2. All existing employees and guests will have default password: `password123` (for testing only)
3. Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Navigate to the React frontend:
   ```bash
   cd react-frontend
   npm install
   npm start
   ```
2. The app will open at http://localhost:3000

## Test Credentials

### Staff Login
- Use any employee email from your database
- Password: `password123`
- Example: If you have an employee with email `manager@hotel.com`

### Guest Login
- Use any guest email from your database
- Password: `password123`
- Example: If you have a guest with email `john@example.com`

## Usage Flow

### For Hotel Staff:
1. Navigate to http://localhost:3000
2. Click "Staff Login" tab
3. Enter employee email and password
4. Access full management dashboard
5. Manage rooms, employees, and guests

### For Guests:
1. Navigate to http://localhost:3000
2. Click "Guest Login" tab
3. Enter guest email and password
4. View your current room details
5. Browse available room upgrades
6. Submit upgrade requests
7. Track request status

## API Endpoints

### Authentication
- `POST /auth/employee/login` - Staff login
- `POST /auth/guest/login` - Guest login

### Guest Portal
- `GET /guest-portal/{guestId}/room` - Get guest room info
- `GET /guest-portal/available-upgrades?guestId={id}` - Get available upgrades
- `POST /guest-portal/upgrade-request` - Submit upgrade request
- `GET /guest-portal/{guestId}/upgrade-requests` - Get upgrade request history

## Security Notes

⚠️ **Important**: The current implementation uses plain text passwords for simplicity. In a production environment:
- Use BCrypt or similar for password hashing
- Implement JWT tokens for session management
- Add HTTPS/SSL encryption
- Add rate limiting and CSRF protection
- Implement proper role-based access control

## UI Design

The interface features:
- Modern, clean design with Tailwind CSS
- Responsive layout for mobile and desktop
- Separate navigation for staff and guests
- Color-coded status indicators
- Smooth transitions and hover effects
- Modal dialogs for upgrade requests
-- Add password field to Employee table
ALTER TABLE employee ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Add password field to Guest table  
ALTER TABLE guest ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Create RoomUpgradeRequest table
CREATE TABLE IF NOT EXISTS room_upgrade_request (
    id BIGSERIAL PRIMARY KEY,
    guest_id BIGINT NOT NULL,
    current_room_id BIGINT NOT NULL,
    requested_room_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_guest FOREIGN KEY (guest_id) REFERENCES guest(id),
    CONSTRAINT fk_current_room FOREIGN KEY (current_room_id) REFERENCES room(id),
    CONSTRAINT fk_requested_room FOREIGN KEY (requested_room_id) REFERENCES room(id)
);

-- Insert sample data for testing
-- Update existing employees with passwords (for testing only - use proper hashing in production)
UPDATE employee SET password = 'password123' WHERE email IS NOT NULL;

-- Update existing guests with passwords (for testing only - use proper hashing in production)
UPDATE guest SET password = 'password123' WHERE email IS NOT NULL;

