package com.ecommerce.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private Long userId;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private Integer totalItems;
    
    // Constructors
    public CartResponse() {}
    
    public CartResponse(Long userId, List<CartItemDto> items, BigDecimal totalPrice, Integer totalItems) {
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public List<CartItemDto> getItems() {
        return items;
    }
    
    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Integer getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
    
    // Helper methods
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
} 