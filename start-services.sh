#!/bin/bash

echo "🏨 Hotel Management System - Setup & Test Script"
echo "================================================"
echo ""

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: Please run this script from the hotel-management root directory"
    exit 1
fi

echo "📦 Step 1: Building backend..."
./mvnw -q clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Backend build failed"
    exit 1
fi
echo "✅ Backend built successfully"
echo ""

echo "📦 Step 2: Installing frontend dependencies..."
cd react-frontend
npm install --silent
if [ $? -ne 0 ]; then
    echo "❌ Frontend installation failed"
    exit 1
fi
echo "✅ Frontend dependencies installed"
cd ..
echo ""

echo "🚀 Step 3: Starting services..."
echo ""
echo "Starting backend on http://localhost:8080..."
./mvnw -q spring-boot:run -Dspring-boot.run.profiles=dev &
BACKEND_PID=$!

echo "Waiting for backend to start..."
sleep 5

echo "Starting frontend on http://localhost:3000..."
cd react-frontend
npm start &
FRONTEND_PID=$!

echo ""
echo "✅ Services started successfully!"
echo ""
echo "================================================"
echo "🎉 Your Hotel Management System is ready!"
echo "================================================"
echo ""
echo "🌐 Frontend: http://localhost:3000"
echo "🔧 Backend:  http://localhost:8080"
echo ""
echo "📝 Test Credentials:"
echo "   Staff Login:"
echo "   - Email: admin@hotel.com"
echo "   - Password: password"
echo ""
echo "   Guest Login:"
echo "   - Email: guest@hotel.com"
echo "   - Password: password"
echo ""
echo "To stop the services:"
echo "   kill $BACKEND_PID $FRONTEND_PID"
echo ""
