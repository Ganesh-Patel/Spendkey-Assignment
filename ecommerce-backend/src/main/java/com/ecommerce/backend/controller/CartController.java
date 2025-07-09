package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AddToCartRequest;
import com.ecommerce.backend.dto.CartItemResponseDto;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * POST /cart/add - Add product to cart
     */
    @PostMapping("/add")
    public ResponseEntity<CartItemResponseDto> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            CartItemResponseDto cartItem = cartService.addToCartAndReturnItem(request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /cart/{userId} - Get cart for user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponseDto>> getCart(@PathVariable Long userId) {
        List<CartItemResponseDto> cartItems = cartService.getCartItemsForUser(userId);
        return ResponseEntity.ok(cartItems);
    }
    
    /**
     * GET /cart - Get cart for user (alternative endpoint)
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCartByParam(@RequestParam Long userId) {
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * PUT /cart/{cartItemId} - Update cart item quantity
     */
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody UpdateQuantityRequest request) {
        try {
            CartItemResponseDto updatedItem = cartService.updateCartItemQuantityAndReturn(cartItemId, request.getQuantity());
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /cart/{userId}/product/{productId} - Remove item from cart
     */
    @DeleteMapping("/{userId}/product/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            // Find the cart item by userId and productId, then remove it
            List<CartItemResponseDto> cartItems = cartService.getCartItemsForUser(userId);
            CartItemResponseDto itemToRemove = cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .orElse(null);
            
            if (itemToRemove != null) {
                cartService.removeFromCart(itemToRemove.getId());
            }
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /cart/{cartItemId} - Remove item from cart (alternative endpoint)
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCartById(@PathVariable Long cartItemId) {
        try {
            cartService.removeFromCart(cartItemId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /cart - Clear cart for user
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * GET /cart/{userId}/count - Get cart item count for user
     */
    @GetMapping("/{userId}/count")
    public ResponseEntity<Long> getCartItemCount(@PathVariable Long userId) {
        Long count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * GET /cart/count - Get cart item count for user (alternative endpoint)
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCartItemCountByParam(@RequestParam Long userId) {
        Long count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * GET /cart/{userId}/check/{productId} - Check if item is in cart
     */
    @GetMapping("/{userId}/check/{productId}")
    public ResponseEntity<Boolean> isItemInCart(@PathVariable Long userId, @PathVariable Long productId) {
        boolean isInCart = cartService.isItemInCart(userId, productId);
        return ResponseEntity.ok(isInCart);
    }
    
    /**
     * GET /cart/check - Check if item is in cart (alternative endpoint)
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isItemInCartByParam(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        boolean isInCart = cartService.isItemInCart(userId, productId);
        return ResponseEntity.ok(isInCart);
    }
    
    // DTO for update quantity request
    public static class UpdateQuantityRequest {
        private Integer quantity;
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
} 