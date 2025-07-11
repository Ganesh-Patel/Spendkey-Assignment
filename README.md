# Full-Stack E-commerce App with Category Tree & Cart

A full-stack e-commerce application built with Spring Boot backend and Angular frontend, featuring hierarchical category navigation and shopping cart functionality.

## Tech Stack

- **Backend**: Java Spring Boot 3.2.0
- **Frontend**: Angular 17
- **Database**: MySQL 8.0
- **Authentication**: JWT Tokens

## Core Features

### Backend APIs
- `GET /api/categories` - Returns full category tree (nested format)
- `GET /api/products?categoryId={id}` - Returns products under a category including subcategories
- `POST /api/cart/add` - Adds a product to cart (productId, quantity)
- `GET /api/cart/{userId}` - Returns all cart items with quantity and total price
- `GET /api/products/{id}/related` - Returns related products (bonus feature)

### Frontend Pages
1. **Category Navigation Page** - Recursive tree view of categories
2. **Product Listing Page** - Products for selected category (including subcategories)
3. **Cart Page** - Display cart items with name, quantity, unit price, and total cost
4. **Product Detail Page** - Product details with related products

## Database Schema

```sql
-- Category Table (Tree Structure)
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT NULL,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Product Table
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    availability_qty INT NOT NULL,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Cart Table
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- MySQL 8.0
- Maven 3.6+

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ecommerce-backend
   ```

2. **Configure database**
   - Create MySQL database named `ecommerce_db`
   - Update database credentials in `src/main/resources/application.properties`

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd ecommerce-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Run the application**
   ```bash
   npm start
   ```
   Frontend will start on `http://localhost:4200`

## Solution Overview

### Backend Architecture
- **Controllers**: Handle HTTP requests and responses
- **Services**: Business logic implementation
- **Repositories**: Data access layer using Spring Data JPA
- **DTOs**: Clean API contracts for data transfer
- **Security**: JWT-based authentication with Spring Security

### Frontend Architecture
- **Components**: Modular UI components
- **Services**: HTTP client services for API communication
- **Models**: TypeScript interfaces for type safety
- **Routing**: Angular Router for navigation
- **Forms**: Reactive forms for user input

### Key Implementation Details
- **Recursive Category Tree**: Implemented using recursive SQL queries and Java recursion
- **Cart Management**: Session-based cart with user authentication
- **Product Filtering**: Products filtered by category hierarchy
- **JWT Authentication**: Secure token-based authentication

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories (tree structure) |
| GET | `/api/products?categoryId={id}` | Get products by category |
| POST | `/api/cart/add` | Add product to cart |
| GET | `/api/cart/{userId}` | Get user's cart |
| GET | `/api/products/{id}/related` | Get related products |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/signup` | User registration |

## Running the Application

1. Start the backend: `mvn spring-boot:run`
2. Start the frontend: `npm start`
3. Open browser: `http://localhost:4200`
4. Register/login to access cart functionality

The application includes sample data that will be automatically loaded on first run.

---

## 📞 Contact

**Ganesh Patel**
- **Email:** ganesh.oficial158@gmail.com
- **GitHub:** [Ganesh-Patel](https://github.com/Ganesh-Patel)
- **Portfolio:** [my-portfolio](https://my-port-folio-umber.vercel.app/)

---

## 🙏 Acknowledgments

- **Spring Boot team** for the excellent framework
- **Angular team** for the powerful frontend framework
- **Tailwind CSS** for the utility-first CSS framework
- **MySQL** for the reliable database system
- **All open-source contributors** whose libraries made this project possible

This application demonstrates modern full-stack development practices with a focus on user experience, performance, security, and maintainability. It serves as a comprehensive example of building scalable e-commerce applications with Spring Boot and Angular.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

*Built with ❤️ by Ganesh Patel* 