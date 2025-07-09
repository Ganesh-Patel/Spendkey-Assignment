#!/bin/bash

# Database Reset Script
# This script will reset the database with the new category structure

echo "🔄 Resetting database with new category structure..."

# Database credentials
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="ecommerce_db"
DB_USER="root"
DB_PASSWORD="Ganesh@123"

# Check if MySQL is running
if ! mysqladmin ping -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" --silent; then
    echo "❌ Error: MySQL is not running or credentials are incorrect"
    exit 1
fi

# Drop and recreate database
echo "🗑️  Dropping existing database..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "DROP DATABASE IF EXISTS $DB_NAME;"

echo "🆕 Creating new database..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

echo "✅ Database reset complete!"
echo ""
echo "📝 Next steps:"
echo "1. Start the Spring Boot application"
echo "2. The application will automatically create tables and insert sample data"
echo "3. The new category structure will be applied"
echo ""
echo "🚀 To start the application, run: ./mvnw spring-boot:run" 