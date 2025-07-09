package com.ecommerce.backend.dto;

import com.ecommerce.backend.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemDto> orderItems;
    
    // Constructors
    public OrderDto() {}
    
    public OrderDto(Order order) {
        this.id = order.getId();
        this.customerName = order.getCustomerName();
        this.customerEmail = order.getCustomerEmail();
        this.customerAddress = order.getCustomerAddress();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus().name();
        this.orderDate = order.getOrderDate();
        this.orderItems = order.getOrderItems() != null ? 
            order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList()) : null;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerAddress() {
        return customerAddress;
    }
    
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
} 