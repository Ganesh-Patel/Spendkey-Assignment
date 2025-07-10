#!/bin/bash

echo "🚀 Setting up E-Commerce Application..."

# Check if MySQL is running
if ! mysqladmin ping -h localhost -u root -p --silent; then
    echo "❌ MySQL is not running. Please start MySQL first."
    exit 1
fi

echo "✅ MySQL is running"

# Create database and user
echo "📦 Setting up database..."
mysql -u root -p << EOF
CREATE DATABASE IF NOT EXISTS ecommerce_db;
CREATE USER IF NOT EXISTS 'ecommerce_user'@'localhost' IDENTIFIED BY 'ecommerce_pass';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
EOF

echo "✅ Database setup complete"

# Start backend
echo "🔧 Starting Spring Boot backend..."
cd ecommerce-backend
./mvnw spring-boot:run &
BACKEND_PID=$!

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 30

# Start frontend
echo "🎨 Starting Angular frontend..."
cd ../ecommerce-frontend
npm install
npm start &
FRONTEND_PID=$!

echo "✅ Application is starting..."
echo "🌐 Backend: http://localhost:8080"
echo "🎨 Frontend: http://localhost:4200"
echo ""
echo "Press Ctrl+C to stop both applications"

# Wait for user to stop
trap "echo '🛑 Stopping applications...'; kill $BACKEND_PID $FRONTEND_PID; exit" INT
wait 