# Hotel Management Java Client

## Prerequisites
- Java 11 or higher
- Backend server running on http://localhost:8080

## Starting the Backend
```bash
cd /Users/rosudaniel/Desktop/hotel-management
./mvnw spring-boot:run
```

## Running the Client
```bash
cd /Users/rosudaniel/Desktop/hotel-management/client
./run-client.sh
```

## Test Credentials

### Guest Account
- Email: `guest@hotel.com`
- Password: `password`

### Staff Account
- Email: `admin@hotel.com`
- Password: `password`

## Features

### Guest Login
1. Select "Guest" radio button
2. Enter guest credentials
3. Click "Login"
4. View your room details and available upgrades

### Staff Login
1. Select "Staff" radio button
2. Enter staff credentials
3. Click "Login"
4. Access staff dashboard

## Troubleshooting

If you see errors:
1. Make sure the backend is running (check http://localhost:8080)
2. Verify database is accessible
3. Check that credentials are correct

