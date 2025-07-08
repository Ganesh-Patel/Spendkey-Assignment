package com.ecommerce.backend.dto;

import java.util.List;

public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryDto> children;
    
    // Constructors
    public CategoryDto() {}
    
    public CategoryDto(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public List<CategoryDto> getChildren() {
        return children;
    }
    
    public void setChildren(List<CategoryDto> children) {
        this.children = children;
    }
} 