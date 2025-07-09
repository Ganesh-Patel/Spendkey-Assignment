package com.ecommerce.backend.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItemResponseDto {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private ProductDto product;
    
    // Constructors
    public CartItemResponseDto() {}
    
    public CartItemResponseDto(Long id, Long userId, Long productId, Integer quantity, ProductDto product) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
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
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public ProductDto getProduct() {
        return product;
    }
    
    public void setProduct(ProductDto product) {
        this.product = product;
    }
    
    // Inner class for product details
    public static class ProductDto {
        private Long id;
        private String name;
        private Double price;
        private Integer availabilityQty;
        private Long categoryId;
        private String categoryName;
        private Boolean available;
        
        // Constructors
        public ProductDto() {}
        
        public ProductDto(Long id, String name, BigDecimal price, Integer availabilityQty, 
                         Long categoryId, String categoryName, Boolean available) {
            this.id = id;
            this.name = name;
            this.price = price.setScale(2, RoundingMode.HALF_UP).doubleValue();
            this.availabilityQty = availabilityQty;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.available = available;
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
        
        public Double getPrice() {
            return price;
        }
        
        public void setPrice(Double price) {
            this.price = price;
        }
        
        public Integer getAvailabilityQty() {
            return availabilityQty;
        }
        
        public void setAvailabilityQty(Integer availabilityQty) {
            this.availabilityQty = availabilityQty;
        }
        
        public Long getCategoryId() {
            return categoryId;
        }
        
        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
        
        public String getCategoryName() {
            return categoryName;
        }
        
        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
        
        public Boolean getAvailable() {
            return available;
        }
        
        public void setAvailable(Boolean available) {
            this.available = available;
        }
    }
} 