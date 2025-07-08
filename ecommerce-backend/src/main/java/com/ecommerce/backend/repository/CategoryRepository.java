package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find root categories (categories with no parent)
    List<Category> findByParentIsNull();
    
    // Find categories by parent ID
    List<Category> findByParentId(Long parentId);
    
    // Find category by name
    Optional<Category> findByName(String name);
    
    // Check if category has children
    boolean existsByParentId(Long parentId);
    
    // Find all subcategories recursively using CTE (Common Table Expression)
    @Query(value = """
        WITH RECURSIVE category_tree AS (
            SELECT id, name, parent_id, 0 as level
            FROM category
            WHERE id = :categoryId
            
            UNION ALL
            
            SELECT c.id, c.name, c.parent_id, ct.level + 1
            FROM category c
            INNER JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT id, name, parent_id, level
        FROM category_tree
        ORDER BY level, name
        """, nativeQuery = true)
    List<Object[]> findCategoryTree(@Param("categoryId") Long categoryId);
    
    // Find all products in a category and its subcategories
    @Query(value = """
        WITH RECURSIVE category_tree AS (
            SELECT id, name, parent_id
            FROM category
            WHERE id = :categoryId
            
            UNION ALL
            
            SELECT c.id, c.name, c.parent_id
            FROM category c
            INNER JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT DISTINCT p.*
        FROM product p
        INNER JOIN category_tree ct ON p.category_id = ct.id
        WHERE p.availability_qty > 0
        ORDER BY p.name
        """, nativeQuery = true)
    List<Object[]> findProductsInCategoryTree(@Param("categoryId") Long categoryId);
    
    // Find all category IDs in a tree
    @Query(value = """
        WITH RECURSIVE category_tree AS (
            SELECT id, parent_id
            FROM category
            WHERE id = :categoryId
            
            UNION ALL
            
            SELECT c.id, c.parent_id
            FROM category c
            INNER JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT id FROM category_tree
        """, nativeQuery = true)
    List<Long> findCategoryIdsInTree(@Param("categoryId") Long categoryId);
} 