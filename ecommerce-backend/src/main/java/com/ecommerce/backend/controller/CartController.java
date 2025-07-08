package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AddToCartRequest;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * POST /cart/add - Add product to cart
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            cartService.addToCart(request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /cart - Get cart for user
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam Long userId) {
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * PUT /cart/{cartItemId}/quantity - Update cart item quantity
     */
    @PutMapping("/{cartItemId}/quantity")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        try {
            cartService.updateCartItemQuantity(cartItemId, quantity);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /cart/{cartItemId} - Remove item from cart
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
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
     * GET /cart/count - Get cart item count for user
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCartItemCount(@RequestParam Long userId) {
        Long count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * GET /cart/check - Check if item is in cart
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isItemInCart(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        boolean isInCart = cartService.isItemInCart(userId, productId);
        return ResponseEntity.ok(isInCart);
    }
} 