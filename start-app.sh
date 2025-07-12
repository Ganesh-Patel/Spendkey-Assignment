#!/bin/bash

echo "🚀 Starting E-commerce Application..."

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo "❌ Port $1 is already in use. Please stop the service using port $1 first."
        exit 1
    fi
}

# Check if ports are available
echo "🔍 Checking port availability..."
check_port 8080
check_port 4200

# Start Backend
echo "🔧 Starting Backend (Spring Boot)..."
cd ecommerce-backend
echo "📦 Building backend..."
mvn clean install -DskipTests > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Backend build successful"
    echo "🚀 Starting backend server on http://localhost:8080"
    mvn spring-boot:run &
    BACKEND_PID=$!
    echo "📝 Backend PID: $BACKEND_PID"
else
    echo "❌ Backend build failed"
    exit 1
fi

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Start Frontend
echo "🎨 Starting Frontend (Angular)..."
cd ../ecommerce-frontend
echo "📦 Installing frontend dependencies..."
npm install > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Frontend dependencies installed"
    echo "🚀 Starting frontend server on http://localhost:4200"
    npm start &
    FRONTEND_PID=$!
    echo "📝 Frontend PID: $FRONTEND_PID"
else
    echo "❌ Frontend dependency installation failed"
    kill $BACKEND_PID 2>/dev/null
    exit 1
fi

echo ""
echo "🎉 Application started successfully!"
echo "📱 Frontend: http://localhost:4200"
echo "🔧 Backend:  http://localhost:8080"
echo ""
echo "💡 To stop the application, press Ctrl+C"
echo "🔄 To restart, run this script again"

# Wait for user to stop
wait 