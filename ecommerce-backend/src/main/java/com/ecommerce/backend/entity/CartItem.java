package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart")
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    // Constructors
    public CartItem() {}
    
    public CartItem(Long userId, Product product, Integer quantity) {
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Helper methods
    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
    
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
    
    public void decreaseQuantity(int amount) {
        if (this.quantity > amount) {
            this.quantity -= amount;
        } else {
            this.quantity = 0;
        }
    }
    
    public boolean isValidQuantity() {
        return quantity > 0 && product.hasStock(quantity);
    }
} 