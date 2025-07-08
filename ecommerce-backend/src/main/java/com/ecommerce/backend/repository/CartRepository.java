package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    
    // Find all cart items for a user
    List<CartItem> findByUserId(Long userId);
    
    // Find cart item by user ID and product ID
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    
    // Check if cart item exists for user and product
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    
    // Delete all cart items for a user
    void deleteByUserId(Long userId);
    
    // Delete specific cart item for a user
    void deleteByUserIdAndProductId(Long userId, Long productId);
    
    // Count total items in cart for a user
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    // Find cart items with product details
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.userId = :userId")
    List<CartItem> findByUserIdWithProduct(@Param("userId") Long userId);
    
    // Find cart item with product details
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.id = :cartItemId")
    Optional<CartItem> findByIdWithProduct(@Param("cartItemId") Long cartItemId);
} 