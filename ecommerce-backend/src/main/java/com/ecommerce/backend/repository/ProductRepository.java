package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by category ID
    List<Product> findByCategoryId(Long categoryId);
    
    // Find available products (quantity > 0)
    List<Product> findByAvailabilityQtyGreaterThan(Integer quantity);
    
    // Find products by category ID and available quantity
    List<Product> findByCategoryIdAndAvailabilityQtyGreaterThan(Long categoryId, Integer quantity);
    
    // Find product by name
    Optional<Product> findByName(String name);
    
    // Find products with related products
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.relatedProducts WHERE p.id = :productId")
    Optional<Product> findByIdWithRelatedProducts(@Param("productId") Long productId);
    
    // Find related products for a given product
    @Query("SELECT rp FROM Product p JOIN p.relatedProducts rp WHERE p.id = :productId")
    List<Product> findRelatedProducts(@Param("productId") Long productId);
    
    // Search products by name containing (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products in multiple categories
    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds AND p.availabilityQty > 0")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
    
    // Find products with price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.availabilityQty > 0")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);
    
    // Find all available products ordered by name
    @Query("SELECT p FROM Product p WHERE p.availabilityQty > 0 ORDER BY p.name ASC")
    List<Product> findAllAvailableProductsOrderedByName();
    
    // Find products with low stock (quantity <= 10)
    @Query("SELECT p FROM Product p WHERE p.availabilityQty <= 10 AND p.availabilityQty > 0")
    List<Product> findProductsWithLowStock();
} 