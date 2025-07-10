# 🛒 E-Commerce Application

A full-stack e-commerce application built with **Spring Boot** backend and **Angular** frontend, featuring user authentication, product management, shopping cart, and order processing.

## 🏗️ Architecture

- **Backend**: Spring Boot 3.x with Spring Data JPA, MySQL, JWT Authentication
- **Frontend**: Angular 17 with TypeScript, Tailwind CSS, RxJS
- **Database**: MySQL with Hibernate ORM
- **Security**: JWT-based authentication and authorization

## ✨ Features

### 🔐 User Management
- User registration and login
- JWT token authentication
- User profile management

### 📂 Category Management
- Hierarchical category structure (3 levels deep)
- Category-based product filtering
- Beautiful category icons and colors

### 🛍️ Product Management
- Product CRUD operations
- Product images and descriptions
- Stock management with availability tracking
- Related products functionality
- Price formatting with precision handling

### 🛒 Shopping Cart
- Add/remove items with quantity management
- Real-time price calculations
- Cart persistence per user
- Cart animations on add to cart

### 📦 Order Processing
- Order creation from cart
- Order history and tracking
- Order status management (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- Stock adjustment on order placement
- Cart clearing after order placement

### 🎨 UI/UX Features
- Responsive design with Tailwind CSS
- Product cards with consistent heights
- Breadcrumb navigation
- Loading states and animations
- Success/error notifications
- Modern, clean interface

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### 1. Clone the Repository
```bash
git clone <repository-url>
cd assigenment1
```

### 2. Database Setup
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE ecommerce_db;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'ecommerce_pass';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Backend Setup
```bash
cd ecommerce-backend

# Update database configuration in application.properties
# Update username, password, and database URL if needed

# Run the application
./mvnw spring-boot:run
```

Backend will start on: `http://localhost:8080`

### 4. Frontend Setup
```bash
cd ecommerce-frontend

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will start on: `http://localhost:4200`

## 📁 Project Structure

```
assigenment1/
├── ecommerce-backend/          # Spring Boot application
│   ├── src/main/java/
│   │   ├── controllers/        # REST API controllers
│   │   ├── services/          # Business logic
│   │   ├── repositories/      # Data access layer
│   │   ├── models/           # Entity classes
│   │   ├── dto/              # Data Transfer Objects
│   │   └── config/           # Configuration classes
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── ecommerce-frontend/         # Angular application
│   ├── src/app/
│   │   ├── components/        # Angular components
│   │   ├── services/          # Angular services
│   │   ├── models/           # TypeScript interfaces
│   │   └── utils/            # Utility functions
│   ├── src/environments/     # Environment configuration
│   └── package.json
└── README.md
```

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Categories
- `GET /api/categories` - All categories
- `GET /api/categories/root` - Root categories
- `GET /api/categories/{id}` - Category by ID

### Products
- `GET /api/products` - All products
- `GET /api/products/{id}` - Product by ID
- `GET /api/products/category/{id}` - Products by category

### Cart
- `GET /api/cart/{userId}` - User's cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/update` - Update cart item
- `DELETE /api/cart/{userId}` - Clear cart

### Orders
- `POST /api/orders` - Create order
- `GET /api/orders/user/{userId}` - User's orders
- `PUT /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

## 🗄️ Database Schema

### Core Entities
- **User** - User accounts and authentication
- **Category** - Hierarchical product categories
- **Product** - Product information with category relationships
- **CartItem** - Shopping cart items linked to users
- **Order** - Customer orders
- **OrderItem** - Individual items within orders

## 🎯 Key Features Demonstrated

### Backend
- **Layered Architecture**: Controller → Service → Repository
- **JPA/Hibernate**: Object-relational mapping
- **JWT Authentication**: Secure API access
- **DTO Pattern**: Data transfer objects for API responses
- **Exception Handling**: Comprehensive error handling
- **CORS Configuration**: Cross-origin resource sharing
- **Profile-based Configuration**: Development and production profiles

### Frontend
- **Component Architecture**: Modular Angular components
- **Service Layer**: HTTP client services for API communication
- **Reactive Programming**: RxJS observables and operators
- **State Management**: Angular services with behavior subjects
- **Responsive Design**: Mobile-first approach with Tailwind CSS
- **Type Safety**: TypeScript interfaces and types
- **Animation System**: Smooth UI transitions and cart animations

## 🧪 Testing the Application

### 1. User Registration
- Navigate to `/signup`
- Create a new user account
- Verify email validation

### 2. User Login
- Navigate to `/login`
- Login with credentials
- Verify JWT token storage

### 3. Product Browsing
- Browse categories on home page
- View products by category
- Check product details and stock

### 4. Shopping Cart
- Add products to cart
- Update quantities
- View cart total
- Verify cart persistence

### 5. Order Processing
- Proceed to checkout
- Fill customer information
- Place order
- Verify order creation and cart clearing

### 6. Order Management
- View order history
- Check order status
- Cancel orders (if applicable)

## 🔧 Configuration

### Backend Configuration
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=ecommerce_user
spring.datasource.password=ecommerce_pass

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# Sample Data
app.initialize.sample-data=true
```

### Frontend Configuration
```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## 🚀 Deployment

### Backend Deployment
- Deploy to Railway, Heroku, or DigitalOcean
- Configure MySQL database
- Set environment variables

### Frontend Deployment
- Deploy to Vercel or Netlify
- Update API URL for production
- Configure build settings

## 📊 Performance Features

- **Lazy Loading**: Angular modules loaded on demand
- **Image Optimization**: Efficient image handling
- **Caching**: HTTP response caching
- **Bundle Optimization**: Tree shaking and code splitting
- **Database Indexing**: Optimized database queries

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt password hashing
- **CORS Protection**: Cross-origin request handling
- **Input Validation**: Server-side validation
- **SQL Injection Prevention**: Parameterized queries
- **XSS Protection**: Content Security Policy

## 🎨 UI/UX Highlights

- **Responsive Design**: Works on all device sizes
- **Modern Interface**: Clean, professional design
- **Smooth Animations**: Cart animations and transitions
- **Loading States**: User feedback during operations
- **Error Handling**: User-friendly error messages
- **Accessibility**: Keyboard navigation and screen reader support

## 📝 Development Notes

- **Code Quality**: ESLint and Prettier configuration
- **Git Workflow**: Proper branching and commit messages
- **Documentation**: Comprehensive code comments
- **Error Logging**: Structured error handling
- **Performance Monitoring**: Bundle size optimization

This application demonstrates modern full-stack development practices with a focus on user experience, performance, and maintainability. 