package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AddToCartRequest;
import com.ecommerce.backend.dto.CartItemDto;
import com.ecommerce.backend.dto.CartItemResponseDto;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Add product to cart
     */
    public void addToCart(AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.isAvailable()) {
            throw new RuntimeException("Product is not available");
        }
        
        if (!product.hasStock(request.getQuantity())) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductId(
                request.getUserId(), request.getProductId());
        
        if (existingItem.isPresent()) {
            // Update existing item
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            if (!product.hasStock(newQuantity)) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            item.setQuantity(newQuantity);
            cartRepository.save(item);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem(request.getUserId(), product, request.getQuantity());
            cartRepository.save(cartItem);
        }
    }
    
    /**
     * Get cart for user
     */
    public CartResponse getCart(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserIdWithProduct(userId);
        
        List<CartItemDto> itemDtos = cartItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        BigDecimal totalPrice = itemDtos.stream()
                .map(item -> BigDecimal.valueOf(item.getTotalPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Integer totalItems = itemDtos.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
        
        return new CartResponse(userId, itemDtos, totalPrice, totalItems);
    }
    
    /**
     * Get cart items for user with detailed product information (for frontend)
     */
    public List<CartItemResponseDto> getCartItemsForUser(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserIdWithProduct(userId);
        return cartItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get specific cart item by ID with detailed product information
     */
    public CartItemResponseDto getCartItemById(Long cartItemId) {
        CartItem cartItem = cartRepository.findByIdWithProduct(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        return convertToResponseDto(cartItem);
    }
    
    /**
     * Add product to cart and return the added item
     */
    public CartItemResponseDto addToCartAndReturnItem(AddToCartRequest request) {
        addToCart(request);
        
        // Find the cart item that was just added/updated
        Optional<CartItem> cartItem = cartRepository.findByUserIdAndProductId(
                request.getUserId(), request.getProductId());
        
        if (cartItem.isPresent()) {
            return convertToResponseDto(cartItem.get());
        }
        
        throw new RuntimeException("Failed to add item to cart");
    }
    
    /**
     * Update cart item quantity
     */
    public void updateCartItemQuantity(Long cartItemId, Integer newQuantity) {
        CartItem cartItem = cartRepository.findByIdWithProduct(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (newQuantity <= 0) {
            cartRepository.delete(cartItem);
            return;
        }
        
        Product product = cartItem.getProduct();
        if (!product.hasStock(newQuantity)) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        cartItem.setQuantity(newQuantity);
        cartRepository.save(cartItem);
    }
    
    /**
     * Update cart item quantity and return the updated item
     */
    public CartItemResponseDto updateCartItemQuantityAndReturn(Long cartItemId, Integer newQuantity) {
        updateCartItemQuantity(cartItemId, newQuantity);
        return getCartItemById(cartItemId);
    }
    
    /**
     * Remove item from cart
     */
    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartRepository.delete(cartItem);
    }
    
    /**
     * Clear cart for user
     */
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
    
    /**
     * Check if cart item exists
     */
    public boolean isItemInCart(Long userId, Long productId) {
        return cartRepository.existsByUserIdAndProductId(userId, productId);
    }
    
    /**
     * Get cart item count for user
     */
    public Long getCartItemCount(Long userId) {
        return cartRepository.countByUserId(userId);
    }
    
    /**
     * Convert CartItem entity to CartItemDto
     */
    private CartItemDto convertToDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                cartItem.getTotalPrice()
        );
    }
    
    /**
     * Convert CartItem entity to CartItemResponseDto (for frontend)
     */
    private CartItemResponseDto convertToResponseDto(CartItem cartItem) {
        Product product = cartItem.getProduct();
        CartItemResponseDto.ProductDto productDto = new CartItemResponseDto.ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getAvailabilityQty(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.isAvailable()
        );
        
        return new CartItemResponseDto(
                cartItem.getId(),
                cartItem.getUserId(),
                product.getId(),
                cartItem.getQuantity(),
                productDto
        );
    }
} 