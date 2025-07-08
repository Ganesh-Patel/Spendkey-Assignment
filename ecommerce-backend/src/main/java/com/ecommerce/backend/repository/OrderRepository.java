package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find orders by customer email
    List<Order> findByCustomerEmailOrderByOrderDateDesc(String customerEmail);
    
    // Find orders by status
    List<Order> findByStatusOrderByOrderDateDesc(Order.OrderStatus status);
    
    // Find orders within a date range
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders with total amount greater than specified value
    List<Order> findByTotalAmountGreaterThanOrderByOrderDateDesc(Double amount);
    
    // Custom query to find recent orders
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(LocalDateTime startDate);
    
    // Custom query to find orders by customer name containing
    @Query("SELECT o FROM Order o WHERE LOWER(o.customerName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY o.orderDate DESC")
    List<Order> findByCustomerNameContaining(String name);
} 