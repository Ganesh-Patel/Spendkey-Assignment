#!/bin/bash

echo "Starting Ecommerce Backend with Development Profile..."
echo "This will enable sample data initialization if database is empty."
echo ""

# Check if Maven is available
if [ -f "./apache-maven-3.9.6/bin/mvn" ]; then
    echo "Using local Maven installation..."
    ./apache-maven-3.9.6/bin/mvn spring-boot:run -Dspring.profiles.active=dev
else
    echo "Local Maven not found. Please ensure Maven is installed and available in PATH."
    echo "You can also run: mvn spring-boot:run -Dspring.profiles.active=dev"
    exit 1
fi 