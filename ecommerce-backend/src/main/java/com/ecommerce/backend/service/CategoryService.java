package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Get the full category tree in nested format
     */
    public List<CategoryDto> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(this::convertToDtoWithChildren)
                .collect(Collectors.toList());
    }
    
    /**
     * Get root categories only (categories with no parent)
     */
    public List<CategoryDto> getRootCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get category by ID
     */
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * Get all subcategories of a category
     */
    public List<CategoryDto> getSubcategories(Long parentId) {
        List<Category> subcategories = categoryRepository.findByParentId(parentId);
        return subcategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new category
     */
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category parent = null;
        if (categoryDto.getParentId() != null) {
            parent = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }
        
        Category category = new Category(categoryDto.getName(), parent);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }
    
    /**
     * Update a category
     */
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setName(categoryDto.getName());
        
        if (categoryDto.getParentId() != null && !categoryDto.getParentId().equals(category.getParent() != null ? category.getParent().getId() : null)) {
            Category parent = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }
        
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }
    
    /**
     * Delete a category (only if it has no children)
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        if (categoryRepository.existsByParentId(id)) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }
        
        categoryRepository.delete(category);
    }
    
    /**
     * Get all category IDs in a tree (including the root and all descendants)
     */
    public List<Long> getCategoryIdsInTree(Long categoryId) {
        return categoryRepository.findCategoryIdsInTree(categoryId);
    }
    
    /**
     * Convert Category entity to CategoryDto with children
     */
    private CategoryDto convertToDtoWithChildren(Category category) {
        CategoryDto dto = convertToDto(category);
        if (category.hasChildren()) {
            List<CategoryDto> children = category.getChildren().stream()
                    .map(this::convertToDtoWithChildren)
                    .collect(Collectors.toList());
            dto.setChildren(children);
        }
        return dto;
    }
    
    /**
     * Convert Category entity to CategoryDto
     */
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        return dto;
    }
    
    /**
     * Convert CategoryDto to Category entity
     */
    private Category convertToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
} 