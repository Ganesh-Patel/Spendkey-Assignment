#!/bin/bash

# Full-Stack E-commerce Application Setup Script
# This script automates the setup process for both backend and frontend

echo "🚀 Setting up Full-Stack E-commerce Application..."
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if required tools are installed
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check Java
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
        print_success "Java found: $JAVA_VERSION"
    else
        print_error "Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    # Check Node.js
    if command -v node &> /dev/null; then
        NODE_VERSION=$(node --version)
        print_success "Node.js found: $NODE_VERSION"
    else
        print_error "Node.js is not installed. Please install Node.js 16 or higher."
        exit 1
    fi
    
    # Check npm
    if command -v npm &> /dev/null; then
        NPM_VERSION=$(npm --version)
        print_success "npm found: $NPM_VERSION"
    else
        print_error "npm is not installed."
        exit 1
    fi
    
    # Check Maven
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn --version | head -n 1)
        print_success "Maven found: $MVN_VERSION"
    else
        print_error "Maven is not installed. Please install Maven."
        exit 1
    fi
    
    # Check Angular CLI
    if command -v ng &> /dev/null; then
        NG_VERSION=$(ng version | grep "Angular CLI" | cut -d' ' -f3)
        print_success "Angular CLI found: $NG_VERSION"
    else
        print_warning "Angular CLI not found. Installing globally..."
        npm install -g @angular/cli
        print_success "Angular CLI installed"
    fi
}

# Setup backend
setup_backend() {
    print_status "Setting up Spring Boot Backend..."
    
    if [ ! -d "ecommerce-backend" ]; then
        print_error "Backend directory not found. Please ensure you're in the project root."
        exit 1
    fi
    
    cd ecommerce-backend
    
    # Check if pom.xml exists
    if [ ! -f "pom.xml" ]; then
        print_error "pom.xml not found in backend directory."
        exit 1
    fi
    
    # Download dependencies
    print_status "Downloading Maven dependencies..."
    mvn dependency:resolve
    
    if [ $? -eq 0 ]; then
        print_success "Backend dependencies downloaded successfully"
    else
        print_error "Failed to download backend dependencies"
        exit 1
    fi
    
    # Compile the project
    print_status "Compiling backend project..."
    mvn compile
    
    if [ $? -eq 0 ]; then
        print_success "Backend compiled successfully"
    else
        print_error "Failed to compile backend"
        exit 1
    fi
    
    cd ..
}

# Setup frontend
setup_frontend() {
    print_status "Setting up Angular Frontend..."
    
    if [ ! -d "ecommerce-frontend" ]; then
        print_error "Frontend directory not found. Please ensure you're in the project root."
        exit 1
    fi
    
    cd ecommerce-frontend
    
    # Check if package.json exists
    if [ ! -f "package.json" ]; then
        print_error "package.json not found in frontend directory."
        exit 1
    fi
    
    # Install dependencies
    print_status "Installing npm dependencies..."
    npm install
    
    if [ $? -eq 0 ]; then
        print_success "Frontend dependencies installed successfully"
    else
        print_error "Failed to install frontend dependencies"
        exit 1
    fi
    
    cd ..
}

# Database setup instructions
setup_database() {
    print_status "Database Setup Instructions:"
    echo ""
    echo "1. Create a MySQL/PostgreSQL database:"
    echo "   mysql -u root -p"
    echo "   CREATE DATABASE ecommerce_db;"
    echo ""
    echo "2. Run the schema script:"
    echo "   mysql -u root -p ecommerce_db < ecommerce-backend/database/schema.sql"
    echo ""
    echo "3. Configure database connection in:"
    echo "   ecommerce-backend/src/main/resources/application.properties"
    echo ""
    print_warning "Please complete the database setup manually before running the application."
}

# Create environment configuration files
create_config_files() {
    print_status "Creating configuration files..."
    
    # Backend application.properties template
    if [ ! -f "ecommerce-backend/src/main/resources/application.properties" ]; then
        cat > ecommerce-backend/src/main/resources/application.properties << EOF
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=8080

# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:4200
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*

# Logging
logging.level.com.ecommerce.backend=DEBUG
logging.level.org.springframework.web=DEBUG
EOF
        print_success "Backend configuration file created"
    fi
    
    # Frontend environment template
    if [ ! -f "ecommerce-frontend/src/environments/environment.ts" ]; then
        mkdir -p ecommerce-frontend/src/environments
        cat > ecommerce-frontend/src/environments/environment.ts << EOF
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
EOF
        print_success "Frontend environment file created"
    fi
}

# Main setup function
main() {
    echo ""
    print_status "Starting setup process..."
    echo ""
    
    # Check prerequisites
    check_prerequisites
    echo ""
    
    # Create configuration files
    create_config_files
    echo ""
    
    # Setup backend
    setup_backend
    echo ""
    
    # Setup frontend
    setup_frontend
    echo ""
    
    # Database setup instructions
    setup_database
    echo ""
    
    print_success "Setup completed successfully!"
    echo ""
    print_status "Next steps:"
    echo "1. Configure your database connection in application.properties"
    echo "2. Run the database schema script"
    echo "3. Start the backend: cd ecommerce-backend && mvn spring-boot:run"
    echo "4. Start the frontend: cd ecommerce-frontend && ng serve"
    echo ""
    print_status "Backend will be available at: http://localhost:8080"
    print_status "Frontend will be available at: http://localhost:4200"
    echo ""
}

# Run the setup
main 