package com.ecommerce.backend.dto;

import java.math.BigDecimal;

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer availabilityQty;
    private Long categoryId;
    private String categoryName;
    
    // Constructors
    public ProductDto() {}
    
    public ProductDto(Long id, String name, BigDecimal price, Integer availabilityQty, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availabilityQty = availabilityQty;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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
    
    // Helper methods
    public boolean isAvailable() {
        return availabilityQty > 0;
    }
} 