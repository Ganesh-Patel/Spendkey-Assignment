package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "availability_qty", nullable = false)
    private Integer availabilityQty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "product_related",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "related_product_id")
    )
    private List<Product> relatedProducts = new ArrayList<>();
    
    // Constructors
    public Product() {}
    
    public Product(String name, BigDecimal price, Integer availabilityQty, Category category) {
        this.name = name;
        this.price = price;
        this.availabilityQty = availabilityQty;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getAvailabilityQty() {
        return availabilityQty;
    }
    
    public void setAvailabilityQty(Integer availabilityQty) {
        this.availabilityQty = availabilityQty;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }
    
    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
    
    // Helper methods
    public boolean isAvailable() {
        return availabilityQty > 0;
    }
    
    public boolean hasStock(int quantity) {
        return availabilityQty >= quantity;
    }
    
    public void reduceStock(int quantity) {
        if (hasStock(quantity)) {
            this.availabilityQty -= quantity;
        } else {
            throw new IllegalStateException("Insufficient stock for product: " + name);
        }
    }
    
    public void addStock(int quantity) {
        this.availabilityQty += quantity;
    }
    
    public void addRelatedProduct(Product relatedProduct) {
        if (!relatedProducts.contains(relatedProduct)) {
            relatedProducts.add(relatedProduct);
            relatedProduct.getRelatedProducts().add(this);
        }
    }
    
    public void removeRelatedProduct(Product relatedProduct) {
        relatedProducts.remove(relatedProduct);
        relatedProduct.getRelatedProducts().remove(this);
    }
} 