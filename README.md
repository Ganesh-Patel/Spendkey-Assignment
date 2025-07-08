# Full-Stack E-commerce Application with Category Tree & Cart

## Project Overview

This is a complete full-stack e-commerce application built with **Spring Boot (Java)** backend and **Angular** frontend, featuring a hierarchical category tree structure and shopping cart functionality. The application demonstrates advanced data structures like trees (for categories) and graphs (for related products).

## 🎯 Assignment Requirements Met

### ✅ Backend (Spring Boot)
- **Category Tree Structure**: Implemented recursive category hierarchy with parent-child relationships
- **Product Management**: Products organized under categories with availability tracking
- **Cart Operations**: Full CRUD operations for shopping cart
- **RESTful APIs**: Clean, well-documented API endpoints
- **ORM Usage**: JPA/Hibernate for database operations
- **Modular Architecture**: Services, Controllers, Repositories pattern
- **DTOs**: Clean API contracts with proper data transfer objects
- **Recursive Logic**: Both application-level recursion and SQL CTEs for tree traversal

### ✅ Frontend (Angular)
- **Category Navigation**: Recursive tree view with expandable categories
- **Product Listing**: Products displayed with availability checks
- **Cart Management**: Full cart functionality with quantity updates
- **Form Handling**: Template-driven forms for cart operations
- **Responsive Design**: Modern UI with Tailwind CSS

### ✅ Database Schema
- **Category Table**: Tree structure with parent_id foreign key
- **Product Table**: Products with availability_qty tracking
- **Cart Table**: User cart items with quantities
- **Related Products**: Graph-like associations (bonus feature)

## 🏗️ Architecture

```
ecommerce-app/
├── ecommerce-backend/          # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/ecommerce/backend/
│   │       ├── controller/     # REST Controllers
│   │       ├── service/        # Business Logic
│   │       ├── repository/     # Data Access Layer
│   │       ├── entity/         # JPA Entities
│   │       └── dto/           # Data Transfer Objects
│   ├── database/              # SQL Schema & Data
│   └── pom.xml               # Maven Dependencies
│
└── ecommerce-frontend/        # Angular Frontend
    ├── src/app/
    │   ├── auth/             # Authentication Components
    │   ├── pages/            # Main Application Pages
    │   ├── services/         # API Services
    │   ├── models/           # TypeScript Interfaces
    │   └── core/             # Core Services & Guards
    ├── src/assets/           # Static Assets
    └── package.json          # Node Dependencies
```

## 🚀 Features

### Core Features
1. **Hierarchical Category Tree**
   - Dynamic category levels (Electronics → Computers → Laptops)
   - Recursive tree traversal
   - Nested category display

2. **Product Management**
   - Products organized under categories
   - Availability tracking (only show products with qty > 0)
   - Search and filtering capabilities

3. **Shopping Cart**
   - Add/remove products
   - Quantity updates
   - Total price calculation
   - Cart persistence

### Bonus Features
1. **Related Products** (Graph-like associations)
2. **User Authentication** (Login/Signup)
3. **Responsive Design** with Tailwind CSS
4. **Advanced Filtering** and Search
5. **Real-time Cart Updates**

## 📋 API Endpoints

### Categories
- `GET /api/categories` - Get full category tree (nested format)
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/categories/{id}/subcategories` - Get subcategories

### Products
- `GET /api/products` - Get all available products
- `GET /api/products?categoryId={id}` - Get products by category (including subcategories)
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/{id}/related` - Get related products (bonus)

### Cart
- `GET /api/cart` - Get user's cart items
- `POST /api/cart/add` - Add product to cart
- `PUT /api/cart/{id}` - Update cart item quantity
- `DELETE /api/cart/{id}` - Remove item from cart
- `DELETE /api/cart/clear` - Clear entire cart

## 🛠️ Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL/PostgreSQL**
- **Maven**

### Frontend
- **Angular 15+**
- **TypeScript**
- **Tailwind CSS**
- **RxJS**

### Database
- **MySQL/PostgreSQL**
- **JPA/Hibernate**

## 📦 Installation & Setup

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MySQL or PostgreSQL
- Maven
- Angular CLI

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ecommerce-backend
   ```

2. **Database Configuration**
   ```bash
   # Create database
   mysql -u root -p
   CREATE DATABASE ecommerce_db;
   
   # Run schema script
   mysql -u root -p ecommerce_db < database/schema.sql
   ```

3. **Configure application.properties**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Backend will be available at: `http://localhost:8080`

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
   ng serve
   ```
   Frontend will be available at: `http://localhost:4200`

## 🗄️ Database Schema

### Core Tables

```sql
-- Category Table (Tree Structure)
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT NULL,
    FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Product Table
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    availability_qty INT NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Cart Table
CREATE TABLE cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Related Products Table (Bonus)
CREATE TABLE product_related (
    product_id BIGINT NOT NULL,
    related_product_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, related_product_id),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (related_product_id) REFERENCES product(id) ON DELETE CASCADE
);
```

## 🌳 Data Structures Implementation

### Tree Structure (Categories)
- **Entity Level**: JPA relationships with `@ManyToOne` and `@OneToMany`
- **Application Level**: Recursive methods for tree traversal
- **Database Level**: Common Table Expressions (CTEs) for recursive queries

### Graph Structure (Related Products)
- **Many-to-Many Relationship**: Products can be related to multiple other products
- **Bidirectional Associations**: Related products can reference back

## 🎨 Key Features Demonstrated

### 1. Recursive Category Tree
```java
// Recursive tree building in CategoryService
private CategoryDto convertToDtoWithChildren(Category category) {
    CategoryDto dto = convertToDto(category);
    if (category.hasChildren()) {
        List<CategoryDto> children = category.getChildren().stream()
                .map(this::convertToDtoWithChildren)
                .collect(Collectors.toList());
        dto.setChildren(children);
    }
    return dto;
}
```

### 2. SQL CTE for Tree Traversal
```sql
WITH RECURSIVE category_tree AS (
    SELECT id, name, parent_id, 0 as level
    FROM category
    WHERE id = :categoryId
    
    UNION ALL
    
    SELECT c.id, c.name, c.parent_id, ct.level + 1
    FROM category c
    INNER JOIN category_tree ct ON c.parent_id = ct.id
)
SELECT * FROM category_tree ORDER BY level, name
```

### 3. Products with Subcategories
```java
public List<ProductDto> getProductsByCategory(Long categoryId) {
    // Get all category IDs in the tree (including subcategories)
    List<Long> categoryIds = categoryService.getCategoryIdsInTree(categoryId);
    
    // Get products from all categories in the tree
    return productRepository.findByCategoryIds(categoryIds)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
}
```

## 🧪 Testing

### Backend Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

### Frontend Testing
```bash
# Run unit tests
ng test

# Run e2e tests
ng e2e
```

## 📱 Usage Examples

### 1. Browse Categories
- Navigate to the home page
- Click on category cards to explore the hierarchy
- Use the category sidebar in the products page

### 2. View Products
- Select a category to see all products (including subcategories)
- Use search functionality to find specific products
- Filter by availability (only products with qty > 0 are shown)

### 3. Manage Cart
- Click "Add to Cart" on any available product
- View cart contents and update quantities
- Remove items or clear the entire cart

## 🔧 Configuration

### Environment Variables
```bash
# Backend
SPRING_PROFILES_ACTIVE=dev
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce_db
DB_USERNAME=root
DB_PASSWORD=password

# Frontend
API_BASE_URL=http://localhost:8080/api
```

### CORS Configuration
The backend is configured to allow requests from `http://localhost:4200` (Angular dev server).

## 📊 Performance Considerations

1. **Database Indexing**: Proper indexes on foreign keys and frequently queried columns
2. **Lazy Loading**: JPA entities use lazy loading for relationships
3. **Pagination**: API endpoints support pagination for large datasets
4. **Caching**: Spring Boot caching for frequently accessed data

## 🚀 Deployment

### Backend Deployment
```bash
# Build JAR file
mvn clean package

# Run with production profile
java -jar target/ecommerce-backend-1.0.0.jar --spring.profiles.active=prod
```

### Frontend Deployment
```bash
# Build for production
ng build --prod

# Deploy to web server (nginx, Apache, etc.)
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

[Your Name]
- Email: ganesh.oficial158@gmail.com
- GitHub: Ganesh-Patel

---

**Note**: This application demonstrates advanced concepts in full-stack development, including hierarchical data structures, recursive algorithms, and modern web development practices. It serves as a comprehensive example of building scalable e-commerce applications with Spring Boot and Angular. 