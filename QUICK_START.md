# Quick Start Guide

## 🚀 Get Started in 5 Minutes

This guide will help you get the e-commerce application running quickly.

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL/PostgreSQL
- Maven
- Angular CLI

### Option 1: Automated Setup (Recommended)

1. **Run the setup script**
   ```bash
   ./setup.sh
   ```

2. **Configure database**
   - Create database: `CREATE DATABASE ecommerce_db;`
   - Run schema: `mysql -u root -p ecommerce_db < ecommerce-backend/database/schema.sql`

3. **Start the application**
   ```bash
   # Terminal 1 - Backend
   cd ecommerce-backend
   mvn spring-boot:run
   
   # Terminal 2 - Frontend
   cd ecommerce-frontend
   ng serve
   ```

4. **Access the application**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080

### Option 2: Manual Setup

#### Backend Setup
```bash
cd ecommerce-backend
mvn clean install
mvn spring-boot:run
```

#### Frontend Setup
```bash
cd ecommerce-frontend
npm install
ng serve
```

### Database Setup
```sql
-- Create database
CREATE DATABASE ecommerce_db;

-- Run schema script
SOURCE ecommerce-backend/database/schema.sql;
```

### Configuration
Update `ecommerce-backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 🎯 Test the Application

1. **Browse Categories**: Navigate through the hierarchical category tree
2. **View Products**: See products organized by categories
3. **Add to Cart**: Add products and manage quantities
4. **Search**: Use the search functionality to find products

## 🐛 Troubleshooting

### Common Issues

**Backend won't start**
- Check database connection
- Verify Java version (17+)
- Check if port 8080 is available

**Frontend won't start**
- Check Node.js version (16+)
- Verify Angular CLI is installed
- Check if port 4200 is available

**Database connection issues**
- Verify MySQL/PostgreSQL is running
- Check credentials in application.properties
- Ensure database exists

### Getting Help
- Check the full README.md for detailed documentation
- Review the API documentation in the README
- Check console logs for error messages

## 📱 Application Features

✅ **Category Tree**: Hierarchical category navigation  
✅ **Product Management**: Products with availability tracking  
✅ **Shopping Cart**: Add, remove, update quantities  
✅ **Search & Filter**: Find products easily  
✅ **Responsive Design**: Works on all devices  
✅ **User Authentication**: Login/Signup functionality  

## 🔗 Useful Links

- [Full Documentation](README.md)
- [API Endpoints](README.md#api-endpoints)
- [Database Schema](README.md#database-schema)
- [Architecture Overview](README.md#architecture)

---

**Happy Shopping! 🛒** 