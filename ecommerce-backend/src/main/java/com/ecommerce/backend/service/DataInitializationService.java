package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.UserRole;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no categories exist
        if (categoryRepository.count() == 0) {
            initializeCategories();
            initializeProducts();
        }
        
        // Initialize sample users if none exist
        if (userRepository.count() == 0) {
            initializeUsers();
        }
    }
    
    private void initializeCategories() {
        // Root categories
        Category electronics = new Category("Electronics");
        Category fashion = new Category("Fashion");
        Category homeGarden = new Category("Home & Garden");
        Category sports = new Category("Sports");
        
        // Save root categories
        electronics = categoryRepository.save(electronics);
        fashion = categoryRepository.save(fashion);
        homeGarden = categoryRepository.save(homeGarden);
        sports = categoryRepository.save(sports);
        
        // Electronics subcategories
        Category computers = new Category("Computers", electronics);
        Category phones = new Category("Phones", electronics);
        Category accessories = new Category("Accessories", electronics);
        
        computers = categoryRepository.save(computers);
        phones = categoryRepository.save(phones);
        accessories = categoryRepository.save(accessories);
        
        // Computers subcategories
        Category laptops = new Category("Laptops", computers);
        Category desktops = new Category("Desktops", computers);
        Category tablets = new Category("Tablets", computers);
        
        laptops = categoryRepository.save(laptops);
        desktops = categoryRepository.save(desktops);
        tablets = categoryRepository.save(tablets);
        
        // Fashion subcategories
        Category clothing = new Category("Clothing", fashion);
        Category shoes = new Category("Shoes", fashion);
        Category jewelry = new Category("Jewelry", fashion);
        
        clothing = categoryRepository.save(clothing);
        shoes = categoryRepository.save(shoes);
        jewelry = categoryRepository.save(jewelry);
        
        // Home & Garden subcategories
        Category furniture = new Category("Furniture", homeGarden);
        Category decor = new Category("Decor", homeGarden);
        Category garden = new Category("Garden", homeGarden);
        
        furniture = categoryRepository.save(furniture);
        decor = categoryRepository.save(decor);
        garden = categoryRepository.save(garden);
        
        // Sports subcategories
        Category fitness = new Category("Fitness", sports);
        Category outdoor = new Category("Outdoor", sports);
        Category teamSports = new Category("Team Sports", sports);
        
        fitness = categoryRepository.save(fitness);
        outdoor = categoryRepository.save(outdoor);
        teamSports = categoryRepository.save(teamSports);
    }
    
    private void initializeProducts() {
        // Get categories for product assignment
        Category laptops = categoryRepository.findByName("Laptops").orElse(null);
        Category phones = categoryRepository.findByName("Phones").orElse(null);
        Category accessories = categoryRepository.findByName("Accessories").orElse(null);
        Category clothing = categoryRepository.findByName("Clothing").orElse(null);
        Category furniture = categoryRepository.findByName("Furniture").orElse(null);
        Category fitness = categoryRepository.findByName("Fitness").orElse(null);
        
        // Electronics products
        if (laptops != null) {
            createProduct("MacBook Pro", new BigDecimal("1299.99"), 15, laptops);
            createProduct("Dell XPS 13", new BigDecimal("999.99"), 20, laptops);
            createProduct("HP Pavilion", new BigDecimal("699.99"), 25, laptops);
        }
        
        if (phones != null) {
            createProduct("iPhone 15 Pro", new BigDecimal("999.99"), 30, phones);
            createProduct("Samsung Galaxy S24", new BigDecimal("899.99"), 25, phones);
            createProduct("Google Pixel 8", new BigDecimal("699.99"), 20, phones);
        }
        
        if (accessories != null) {
            createProduct("Wireless Headphones", new BigDecimal("199.99"), 50, accessories);
            createProduct("Bluetooth Speaker", new BigDecimal("89.99"), 40, accessories);
            createProduct("Phone Case", new BigDecimal("19.99"), 100, accessories);
        }
        
        // Fashion products
        if (clothing != null) {
            createProduct("Cotton T-Shirt", new BigDecimal("24.99"), 200, clothing);
            createProduct("Denim Jeans", new BigDecimal("79.99"), 150, clothing);
            createProduct("Hoodie", new BigDecimal("49.99"), 100, clothing);
        }
        
        // Home & Garden products
        if (furniture != null) {
            createProduct("Coffee Table", new BigDecimal("299.99"), 10, furniture);
            createProduct("Dining Chair", new BigDecimal("89.99"), 30, furniture);
            createProduct("Bookshelf", new BigDecimal("149.99"), 15, furniture);
        }
        
        // Sports products
        if (fitness != null) {
            createProduct("Yoga Mat", new BigDecimal("29.99"), 80, fitness);
            createProduct("Dumbbells Set", new BigDecimal("79.99"), 25, fitness);
            createProduct("Resistance Bands", new BigDecimal("19.99"), 60, fitness);
        }
    }
    
    private void createProduct(String name, BigDecimal price, Integer quantity, Category category) {
        Product product = new Product(name, price, quantity, category);
        productRepository.save(product);
    }
    
    private void initializeUsers() {
        // Create sample users
        User adminUser = new User("admin", "9876543210", "admin123");
        adminUser.setEmail("admin@ecommerce.com");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setRole(UserRole.ADMIN);
        userRepository.save(adminUser);
        
        User regularUser1 = new User("john_doe", "9876543211", "password123");
        regularUser1.setEmail("john@example.com");
        regularUser1.setFirstName("John");
        regularUser1.setLastName("Doe");
        userRepository.save(regularUser1);
        
        User regularUser2 = new User("jane_smith", "9876543212", "password123");
        regularUser2.setEmail("jane@example.com");
        regularUser2.setFirstName("Jane");
        regularUser2.setLastName("Smith");
        userRepository.save(regularUser2);
        
        User regularUser3 = new User("bob_wilson", "9876543213", "password123");
        regularUser3.setEmail("bob@example.com");
        regularUser3.setFirstName("Bob");
        regularUser3.setLastName("Wilson");
        userRepository.save(regularUser3);
        
        System.out.println("Sample users initialized successfully");
    }
} 