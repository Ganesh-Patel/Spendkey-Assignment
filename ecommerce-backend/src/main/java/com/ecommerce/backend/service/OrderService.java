package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CreateOrderRequest;
import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.OrderItemRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Create order from user's cart
     */
    public OrderDto createOrderFromCart(CreateOrderRequest request) {
        // Get user's cart items
        List<CartItem> cartItems = cartRepository.findByUserIdWithProduct(request.getUserId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Calculate total amount
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();
        
        // Create order
        Order order = new Order(
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerAddress(),
                totalAmount
        );
        
        Order savedOrder = orderRepository.save(order);
        
        // Create order items and update product stock
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    
                    // Check stock availability
                    if (!product.hasStock(cartItem.getQuantity())) {
                        throw new RuntimeException("Insufficient stock for product: " + product.getName());
                    }
                    
                    // Reduce product stock
                    product.reduceStock(cartItem.getQuantity());
                    productRepository.save(product);
                    
                    // Create order item
                    OrderItem orderItem = new OrderItem(
                            savedOrder,
                            product,
                            cartItem.getQuantity(),
                            product.getPrice().doubleValue()
                    );
                    
                    return orderItemRepository.save(orderItem);
                })
                .collect(Collectors.toList());
        
        // Clear user's cart
        cartRepository.deleteByUserId(request.getUserId());
        
        // Set order items and return
        savedOrder.setOrderItems(orderItems);
        return new OrderDto(savedOrder);
    }
    
    /**
     * Get order by ID
     */
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Load order items with products
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(orderId);
        order.setOrderItems(orderItems);
        
        return new OrderDto(order);
    }
    
    /**
     * Get orders by customer email
     */
    public List<OrderDto> getOrdersByCustomerEmail(String customerEmail) {
        List<Order> orders = orderRepository.findByCustomerEmailOrderByOrderDateDesc(customerEmail);
        
        return orders.stream()
                .map(order -> {
                    // Load order items for each order
                    List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(order.getId());
                    order.setOrderItems(orderItems);
                    return new OrderDto(order);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get all orders
     */
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        
        return orders.stream()
                .map(order -> {
                    // Load order items for each order
                    List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(order.getId());
                    order.setOrderItems(orderItems);
                    return new OrderDto(order);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Update order status
     */
    public OrderDto updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        
        // Load order items
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(orderId);
        savedOrder.setOrderItems(orderItems);
        
        return new OrderDto(savedOrder);
    }
    
    /**
     * Cancel order and restore product stock
     */
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if order can be cancelled
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled in current status: " + order.getStatus());
        }
        
        // Load order items
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(orderId);
        
        // Restore product stock
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.addStock(orderItem.getQuantity());
            productRepository.save(product);
        }
        
        // Update order status
        order.setStatus(Order.OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        savedOrder.setOrderItems(orderItems);
        
        return new OrderDto(savedOrder);
    }
} 