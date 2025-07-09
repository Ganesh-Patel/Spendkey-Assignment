package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.UserRole;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
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
    
    @Autowired
    private Environment environment;
    
    // Configuration property to enable/disable data initialization
    @Value("${app.initialize.sample-data:false}")
    private boolean initializeSampleData;
    
    @Override
    public void run(String... args) throws Exception {
        // Only run in development environment or when explicitly enabled
        boolean isDevelopment = Arrays.asList(environment.getActiveProfiles()).contains("dev") || 
                               Arrays.asList(environment.getActiveProfiles()).contains("development");
        
        if (!initializeSampleData && !isDevelopment) {
            System.out.println("Data initialization skipped - not in development mode and not explicitly enabled");
            return;
        }
        
        System.out.println("Starting data initialization...");
        
        // Only initialize if no categories exist
        if (categoryRepository.count() == 0) {
            System.out.println("Initializing categories and products...");
            initializeCategories();
            initializeProducts();
            System.out.println("Categories and products initialized successfully");
        } else {
            System.out.println("Categories already exist, skipping category/product initialization");
        }
        
        // Initialize sample users if none exist
        if (userRepository.count() == 0) {
            System.out.println("Initializing sample users...");
            initializeUsers();
            System.out.println("Sample users initialized successfully");
        } else {
            System.out.println("Users already exist, skipping user initialization");
        }
        
        System.out.println("Data initialization completed");
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
        Category audio = new Category("Audio & Music", electronics);
        Category gaming = new Category("Gaming", electronics);
        
        computers = categoryRepository.save(computers);
        phones = categoryRepository.save(phones);
        audio = categoryRepository.save(audio);
        gaming = categoryRepository.save(gaming);
        
        // Computers subcategories
        Category laptops = new Category("Laptops", computers);
        Category desktops = new Category("Desktops", computers);
        Category tablets = new Category("Tablets", computers);
        Category accessories = new Category("Computer Accessories", computers);
        
        laptops = categoryRepository.save(laptops);
        desktops = categoryRepository.save(desktops);
        tablets = categoryRepository.save(tablets);
        accessories = categoryRepository.save(accessories);
        
        // Phones subcategories
        Category smartphones = new Category("Smartphones", phones);
        Category phoneAccessories = new Category("Phone Accessories", phones);
        
        smartphones = categoryRepository.save(smartphones);
        phoneAccessories = categoryRepository.save(phoneAccessories);
        
        // Audio subcategories
        Category headphones = new Category("Headphones", audio);
        Category speakers = new Category("Speakers", audio);
        Category microphones = new Category("Microphones", audio);
        
        headphones = categoryRepository.save(headphones);
        speakers = categoryRepository.save(speakers);
        microphones = categoryRepository.save(microphones);
        
        // Gaming subcategories
        Category consoles = new Category("Gaming Consoles", gaming);
        Category games = new Category("Video Games", gaming);
        Category gamingAccessories = new Category("Gaming Accessories", gaming);
        
        consoles = categoryRepository.save(consoles);
        games = categoryRepository.save(games);
        gamingAccessories = categoryRepository.save(gamingAccessories);
        
        // Fashion subcategories
        Category clothing = new Category("Clothing", fashion);
        Category shoes = new Category("Shoes", fashion);
        Category jewelry = new Category("Jewelry", fashion);
        Category bags = new Category("Bags & Accessories", fashion);
        
        clothing = categoryRepository.save(clothing);
        shoes = categoryRepository.save(shoes);
        jewelry = categoryRepository.save(jewelry);
        bags = categoryRepository.save(bags);
        
        // Clothing subcategories
        Category mensClothing = new Category("Men's Clothing", clothing);
        Category womensClothing = new Category("Women's Clothing", clothing);
        Category kidsClothing = new Category("Kids' Clothing", clothing);
        
        mensClothing = categoryRepository.save(mensClothing);
        womensClothing = categoryRepository.save(womensClothing);
        kidsClothing = categoryRepository.save(kidsClothing);
        
        // Home & Garden subcategories
        Category furniture = new Category("Furniture", homeGarden);
        Category decor = new Category("Decor", homeGarden);
        Category garden = new Category("Garden", homeGarden);
        Category kitchen = new Category("Kitchen & Dining", homeGarden);
        
        furniture = categoryRepository.save(furniture);
        decor = categoryRepository.save(decor);
        garden = categoryRepository.save(garden);
        kitchen = categoryRepository.save(kitchen);
        
        // Furniture subcategories
        Category livingRoom = new Category("Living Room", furniture);
        Category bedroom = new Category("Bedroom", furniture);
        Category office = new Category("Office", furniture);
        
        livingRoom = categoryRepository.save(livingRoom);
        bedroom = categoryRepository.save(bedroom);
        office = categoryRepository.save(office);
        
        // Sports subcategories
        Category fitness = new Category("Fitness", sports);
        Category outdoor = new Category("Outdoor", sports);
        Category teamSports = new Category("Team Sports", sports);
        Category waterSports = new Category("Water Sports", sports);
        
        fitness = categoryRepository.save(fitness);
        outdoor = categoryRepository.save(outdoor);
        teamSports = categoryRepository.save(teamSports);
        waterSports = categoryRepository.save(waterSports);
        
        // Fitness subcategories
        Category cardio = new Category("Cardio Equipment", fitness);
        Category strength = new Category("Strength Training", fitness);
        Category yoga = new Category("Yoga & Pilates", fitness);
        
        cardio = categoryRepository.save(cardio);
        strength = categoryRepository.save(strength);
        yoga = categoryRepository.save(yoga);
    }
    
    private void initializeProducts() {
        // Get categories for product assignment
        Category laptops = categoryRepository.findByName("Laptops").orElse(null);
        Category smartphones = categoryRepository.findByName("Smartphones").orElse(null);
        Category computerAccessories = categoryRepository.findByName("Computer Accessories").orElse(null);
        Category phoneAccessories = categoryRepository.findByName("Phone Accessories").orElse(null);
        Category headphones = categoryRepository.findByName("Headphones").orElse(null);
        Category speakers = categoryRepository.findByName("Speakers").orElse(null);
        Category mensClothing = categoryRepository.findByName("Men's Clothing").orElse(null);
        Category womensClothing = categoryRepository.findByName("Women's Clothing").orElse(null);
        Category livingRoom = categoryRepository.findByName("Living Room").orElse(null);
        Category bedroom = categoryRepository.findByName("Bedroom").orElse(null);
        Category cardio = categoryRepository.findByName("Cardio Equipment").orElse(null);
        Category strength = categoryRepository.findByName("Strength Training").orElse(null);
        Category yoga = categoryRepository.findByName("Yoga & Pilates").orElse(null);
        
        // Electronics products
        if (laptops != null) {
            createProduct("MacBook Pro 14\"", new BigDecimal("1999.99"), 15, laptops);
            createProduct("Dell XPS 13", new BigDecimal("999.99"), 20, laptops);
            createProduct("HP Pavilion Gaming", new BigDecimal("899.99"), 25, laptops);
            createProduct("Lenovo ThinkPad X1", new BigDecimal("1499.99"), 18, laptops);
        }
        
        if (smartphones != null) {
            createProduct("iPhone 15 Pro", new BigDecimal("999.99"), 30, smartphones);
            createProduct("Samsung Galaxy S24", new BigDecimal("899.99"), 25, smartphones);
            createProduct("Google Pixel 8", new BigDecimal("699.99"), 20, smartphones);
            createProduct("OnePlus 11", new BigDecimal("649.99"), 22, smartphones);
        }
        
        if (computerAccessories != null) {
            createProduct("Wireless Mouse", new BigDecimal("29.99"), 50, computerAccessories);
            createProduct("Mechanical Keyboard", new BigDecimal("89.99"), 40, computerAccessories);
            createProduct("USB-C Hub", new BigDecimal("39.99"), 60, computerAccessories);
            createProduct("Monitor Stand", new BigDecimal("49.99"), 35, computerAccessories);
        }
        
        if (phoneAccessories != null) {
            createProduct("Phone Case", new BigDecimal("19.99"), 100, phoneAccessories);
            createProduct("Wireless Charger", new BigDecimal("34.99"), 45, phoneAccessories);
            createProduct("Screen Protector", new BigDecimal("9.99"), 80, phoneAccessories);
            createProduct("Phone Stand", new BigDecimal("14.99"), 55, phoneAccessories);
        }
        
        if (headphones != null) {
            createProduct("Sony WH-1000XM4", new BigDecimal("349.99"), 25, headphones);
            createProduct("Apple AirPods Pro", new BigDecimal("249.99"), 40, headphones);
            createProduct("Bose QuietComfort", new BigDecimal("299.99"), 30, headphones);
            createProduct("Samsung Galaxy Buds", new BigDecimal("149.99"), 35, headphones);
        }
        
        if (speakers != null) {
            createProduct("JBL Flip 6", new BigDecimal("89.99"), 40, speakers);
            createProduct("Bose SoundLink", new BigDecimal("129.99"), 30, speakers);
            createProduct("UE Boom 3", new BigDecimal("99.99"), 35, speakers);
            createProduct("Sony SRS-XB23", new BigDecimal("79.99"), 45, speakers);
        }
        
        // Fashion products
        if (mensClothing != null) {
            createProduct("Cotton T-Shirt", new BigDecimal("24.99"), 200, mensClothing);
            createProduct("Denim Jeans", new BigDecimal("79.99"), 150, mensClothing);
            createProduct("Hoodie", new BigDecimal("49.99"), 100, mensClothing);
            createProduct("Polo Shirt", new BigDecimal("34.99"), 120, mensClothing);
        }
        
        if (womensClothing != null) {
            createProduct("Summer Dress", new BigDecimal("59.99"), 80, womensClothing);
            createProduct("Blouse", new BigDecimal("39.99"), 90, womensClothing);
            createProduct("Skinny Jeans", new BigDecimal("69.99"), 110, womensClothing);
            createProduct("Cardigan", new BigDecimal("44.99"), 75, womensClothing);
        }
        
        // Home & Garden products
        if (livingRoom != null) {
            createProduct("Coffee Table", new BigDecimal("299.99"), 10, livingRoom);
            createProduct("Sofa Set", new BigDecimal("899.99"), 8, livingRoom);
            createProduct("TV Stand", new BigDecimal("199.99"), 15, livingRoom);
            createProduct("Floor Lamp", new BigDecimal("89.99"), 25, livingRoom);
        }
        
        if (bedroom != null) {
            createProduct("Queen Bed Frame", new BigDecimal("399.99"), 12, bedroom);
            createProduct("Dresser", new BigDecimal("299.99"), 10, bedroom);
            createProduct("Nightstand", new BigDecimal("149.99"), 18, bedroom);
            createProduct("Bedside Lamp", new BigDecimal("59.99"), 30, bedroom);
        }
        
        // Sports products
        if (cardio != null) {
            createProduct("Treadmill", new BigDecimal("899.99"), 8, cardio);
            createProduct("Exercise Bike", new BigDecimal("599.99"), 12, cardio);
            createProduct("Elliptical Machine", new BigDecimal("799.99"), 10, cardio);
            createProduct("Rowing Machine", new BigDecimal("699.99"), 9, cardio);
        }
        
        if (strength != null) {
            createProduct("Dumbbells Set", new BigDecimal("79.99"), 25, strength);
            createProduct("Weight Bench", new BigDecimal("199.99"), 15, strength);
            createProduct("Resistance Bands", new BigDecimal("19.99"), 60, strength);
            createProduct("Kettlebell", new BigDecimal("39.99"), 40, strength);
        }
        
        if (yoga != null) {
            createProduct("Yoga Mat", new BigDecimal("29.99"), 80, yoga);
            createProduct("Yoga Block Set", new BigDecimal("19.99"), 70, yoga);
            createProduct("Meditation Cushion", new BigDecimal("24.99"), 45, yoga);
            createProduct("Yoga Strap", new BigDecimal("9.99"), 90, yoga);
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