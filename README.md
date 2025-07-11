# 🛒 E-Commerce Application

A full-stack e-commerce application built with **Spring Boot** backend and **Angular** frontend, featuring user authentication, product management, shopping cart, and order processing.

## 👨‍💻 **Developed By**

**Ganesh Patel**  
- **Email:** ganesh.oficial158@gmail.com  
- **GitHub:** [Ganesh-Patel](https://github.com/Ganesh-Patel)  
- **LinkedIn:** [LinkedIn Profile](https://www.linkedin.com/in/ganesh-patel-a6a8a3334/)
- **Portfolio:** [my-portfolio](https://my-port-folio-umber.vercel.app/) 

---

## 🏗️ **Architecture**

- **Backend**: Spring Boot 3.x with Spring Data JPA, MySQL, JWT Authentication
- **Frontend**: Angular 15 with TypeScript, Tailwind CSS, RxJS
- **Database**: MySQL with Hibernate ORM
- **Security**: JWT-based authentication and authorization
- **Build Tools**: Maven (Backend), npm (Frontend)

---

## ✨ **Features**

### 🔐 **User Management**
- User registration and login with JWT authentication
- User profile management with secure password handling
- Session management with localStorage persistence
- Role-based access control

### 📂 **Category Management**
- Hierarchical category structure (3 levels deep)
- Category-based product filtering with subcategory support
- Beautiful category icons and color-coded display
- Recursive category tree navigation

### 🛍️ **Product Management**
- Complete product CRUD operations
- Product images and detailed descriptions
- Real-time stock management with availability tracking
- Related products functionality
- Price formatting with precision handling
- Product search and filtering capabilities

### 🛒 **Shopping Cart**
- Add/remove items with quantity management
- Real-time price calculations and totals
- Cart persistence per user across sessions
- Cart animations on add to cart
- Stock validation during cart operations

### 📦 **Order Processing**
- Order creation from cart with customer information
- Order history and status tracking
- Order status management (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- Stock adjustment on order placement
- Cart clearing after successful order placement

### 🎨 **UI/UX Features**
- Responsive design with Tailwind CSS
- Product cards with consistent heights and layout
- Breadcrumb navigation for better user experience
- Loading states and smooth animations
- Success/error notifications
- Modern, clean interface with professional design
- Mobile-first responsive approach

---

## 🚀 **Quick Start**

### **Prerequisites**
- Java 17 or higher
- Node.js 18 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### **1. Clone the Repository**
```bash
git clone https://github.com/Ganesh-Patel/Spendkey-Assignment.git
cd assigenment1
```

### **2. Database Setup**
```bash
# Create MySQL database and user
mysql -u root -p
CREATE DATABASE ecommerce_db;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'ecommerce_pass';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### **3. Backend Setup**
```bash
cd ecommerce-backend

# Update database configuration if needed
# Edit: src/main/resources/application.properties

# Run the application
mvn spring-boot:run
```
**Backend will start on:** `http://localhost:8080`

### **4. Frontend Setup**
```bash
cd ecommerce-frontend

# Install dependencies
npm install

# Start development server
npm start
```
**Frontend will start on:** `http://localhost:4200`

### **5. One-Click Setup (Alternative)**
```bash
# Run the automated setup script
./setup.sh
```

---

## 📁 **Project Structure**

```
assigenment1/
├── ecommerce-backend/          # Spring Boot application
│   ├── src/main/java/
│   │   ├── controllers/        # REST API controllers
│   │   ├── services/          # Business logic layer
│   │   ├── repositories/      # Data access layer
│   │   ├── models/           # Entity classes
│   │   ├── dto/              # Data Transfer Objects
│   │   └── config/           # Configuration classes
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── ecommerce-frontend/         # Angular application
│   ├── src/app/
│   │   ├── auth/             # Authentication components
│   │   ├── pages/            # Main application pages
│   │   ├── services/         # Angular services
│   │   ├── models/           # TypeScript interfaces
│   │   ├── core/             # Core services & guards
│   │   └── shared/           # Shared components
│   ├── src/environments/     # Environment configuration
│   └── package.json
├── README.md                  # Project documentation
└── setup.sh                   # Automated setup script
```

---

## 🔌 **API Endpoints**

### **Authentication**
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/check-username/{username}` - Check username availability

### **Categories**
- `GET /api/categories` - Get all categories
- `GET /api/categories/root` - Get root categories
- `GET /api/categories/{id}` - Get category by ID

### **Products**
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{id}` - Get products by category
- `GET /api/products/{id}/related` - Get related products

### **Cart**
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/update` - Update cart item
- `DELETE /api/cart/{userId}` - Clear cart

### **Orders**
- `POST /api/orders` - Create order
- `GET /api/orders/user/{userId}` - Get user's orders
- `PUT /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

---

## 🗄️ **Database Schema**

### **Core Entities**
- **User** - User accounts and authentication
- **Category** - Hierarchical product categories
- **Product** - Product information with category relationships
- **CartItem** - Shopping cart items linked to users
- **Order** - Customer orders
- **OrderItem** - Individual items within orders

---

## 🎯 **Key Features Demonstrated**

### **Backend Excellence**
- **Layered Architecture**: Controller → Service → Repository pattern
- **JPA/Hibernate**: Object-relational mapping with proper relationships
- **JWT Authentication**: Secure token-based authentication system
- **DTO Pattern**: Clean API contracts with proper data transfer objects
- **Exception Handling**: Comprehensive error handling throughout
- **CORS Configuration**: Cross-origin resource sharing setup
- **Profile-based Configuration**: Development and production profiles
- **Data Validation**: Server-side validation for all inputs

### **Frontend Excellence**
- **Component Architecture**: Modular Angular components with proper separation
- **Service Layer**: HTTP client services for API communication
- **Reactive Programming**: RxJS observables and operators for state management
- **State Management**: Angular services with behavior subjects
- **Responsive Design**: Mobile-first approach with Tailwind CSS
- **Type Safety**: TypeScript interfaces and proper typing
- **Animation System**: Smooth UI transitions and cart animations
- **Error Handling**: User-friendly error messages and loading states

---

## 🧪 **Testing the Application**

### **1. User Registration & Login**
- Navigate to `/signup` to create a new account
- Navigate to `/login` to authenticate
- Verify JWT token storage and session management

### **2. Product Browsing**
- Browse categories on the home page
- View products by category with subcategory support
- Check product details and stock information
- Test product search functionality

### **3. Shopping Cart**
- Add products to cart with quantity selection
- Update quantities and view real-time totals
- Verify cart persistence across sessions
- Test stock validation during cart operations

### **4. Order Processing**
- Complete checkout process with customer information
- Place orders and verify cart clearing
- View order history and status tracking
- Test order cancellation (if applicable)

---

## 🔧 **Configuration**

### **Backend Configuration**
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

### **Frontend Configuration**
```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

---

## 🚀 **Deployment**

### **Backend Deployment**
- Deploy to Railway, Heroku, or DigitalOcean
- Configure MySQL database
- Set environment variables for production

### **Frontend Deployment**
- Deploy to Vercel or Netlify
- Update API URL for production
- Configure build settings

---

## 📊 **Performance Features**

- **Lazy Loading**: Angular modules loaded on demand
- **Image Optimization**: Efficient image handling
- **Caching**: HTTP response caching
- **Bundle Optimization**: Tree shaking and code splitting
- **Database Indexing**: Optimized database queries
- **Compression**: Gzip compression for faster loading

---

## 🔒 **Security Features**

- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt password hashing
- **CORS Protection**: Cross-origin request handling
- **Input Validation**: Server-side validation for all inputs
- **SQL Injection Prevention**: Parameterized queries
- **XSS Protection**: Content Security Policy
- **HTTPS Support**: Secure communication in production

---

## 🎨 **UI/UX Highlights**

- **Responsive Design**: Works perfectly on all device sizes
- **Modern Interface**: Clean, professional design with Tailwind CSS
- **Smooth Animations**: Cart animations and page transitions
- **Loading States**: User feedback during operations
- **Error Handling**: User-friendly error messages
- **Accessibility**: Keyboard navigation and screen reader support
- **Performance**: Optimized for fast loading and smooth interactions

---

## 📝 **Development Notes**

- **Code Quality**: ESLint and Prettier configuration
- **Git Workflow**: Proper branching and commit messages
- **Documentation**: Comprehensive code comments
- **Error Logging**: Structured error handling
- **Performance Monitoring**: Bundle size optimization
- **Testing**: Unit and integration test coverage

---

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📞 **Contact**

**Ganesh Patel**  
- **Email:** ganesh.oficial158@gmail.com  
- **GitHub:** [Ganesh-Patel](https://github.com/Ganesh-Patel)  
- **LinkedIn:** [Your LinkedIn Profile]  

---

## 🙏 **Acknowledgments**

- Spring Boot team for the excellent framework
- Angular team for the powerful frontend framework
- Tailwind CSS for the utility-first CSS framework
- MySQL for the reliable database system
- All open-source contributors whose libraries made this project possible

---

**This application demonstrates modern full-stack development practices with a focus on user experience, performance, security, and maintainability. It serves as a comprehensive example of building scalable e-commerce applications with Spring Boot and Angular.**

---

*Built with ❤️ by Ganesh Patel* 