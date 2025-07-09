# Ecommerce Backend

A Spring Boot backend for the ecommerce application with RESTful APIs for products, categories, cart management, and user authentication.

## Features

- **User Authentication**: Login, signup, and user management
- **Category Management**: Hierarchical categories with parent-child relationships
- **Product Management**: CRUD operations for products with category associations
- **Cart Management**: Add, update, remove items from cart
- **Related Products**: Product recommendations system
- **RESTful APIs**: Clean and well-documented API endpoints

## Data Initialization Service

The application includes a `DataInitializationService` that automatically populates the database with sample data for development purposes.

### Purpose
- **Development Convenience**: Automatically creates sample categories, products, and users
- **Testing**: Provides consistent test data across all developers
- **Demo**: Shows how the application works with realistic data

### Configuration

The service can be controlled through Spring profiles and configuration properties:

#### Development Profile (Recommended for development)
```bash
# Run with development profile (enables sample data)
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

#### Production Profile (Recommended for production)
```bash
# Run with production profile (disables sample data)
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

#### Manual Control
You can also control it through the `application.properties`:
```properties
# Enable/disable sample data initialization
app.initialize.sample-data=false
```

### What Gets Created

#### Categories
- **Electronics** → Computers → Laptops, Desktops, Tablets
- **Electronics** → Phones, Accessories
- **Fashion** → Clothing, Shoes, Jewelry
- **Home & Garden** → Furniture, Decor, Garden
- **Sports** → Fitness, Outdoor, Team Sports

#### Sample Products
- Electronics: MacBook Pro, iPhone 15 Pro, Wireless Headphones
- Fashion: Cotton T-Shirt, Denim Jeans, Hoodie
- Home & Garden: Coffee Table, Dining Chair, Bookshelf
- Sports: Yoga Mat, Dumbbells Set, Resistance Bands

#### Sample Users
- **Admin User**: admin/admin123
- **Regular Users**: john_doe/password123, jane_smith/password123, bob_wilson/password123

### When Does It Run?

The service only runs when:
1. **Database is empty** (no categories exist)
2. **Development profile is active** OR `app.initialize.sample-data=true`
3. **Application starts up**

### Production Considerations

- **Disable in production**: Use `prod` profile or set `app.initialize.sample-data=false`
- **Database management**: Production data should be managed through proper admin interfaces
- **Security**: Sample users have weak passwords - change them in production

## Quick Start

1. **Setup Database**
   ```bash
   # Create MySQL database
   mysql -u root -p
   CREATE DATABASE ecommerce_db;
   ```

2. **Run with Development Profile**
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=dev
   ```

3. **Run with Production Profile**
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=prod
   ```

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/tree` - Get category tree

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products?categoryId={id}` - Get products by category

### Cart
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/{cartItemId}` - Update cart item
- `DELETE /api/cart/{userId}/product/{productId}` - Remove item from cart

## Database Schema

The application uses JPA entities with automatic table creation. Key tables:
- `user` - User accounts and authentication
- `category` - Product categories with hierarchy
- `product` - Product information
- `cart` - Shopping cart items
- `product_related` - Related products mapping 