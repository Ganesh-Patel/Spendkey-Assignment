package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * Get all available products
     */
    public List<ProductDto> getAllAvailableProducts() {
        return productRepository.findByAvailabilityQtyGreaterThan(0)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products by category ID (including subcategories)
     */
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        // Get all category IDs in the tree (including subcategories)
        List<Long> categoryIds = categoryService.getCategoryIdsInTree(categoryId);
        
        // Get products from all categories in the tree
        return productRepository.findByCategoryIds(categoryIds)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get product by ID
     */
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * Get related products for a product
     */
    public List<ProductDto> getRelatedProducts(Long productId) {
        return productRepository.findRelatedProducts(productId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Search products by name
     */
    public List<ProductDto> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .filter(product -> product.getAvailabilityQty() > 0)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products by price range
     */
    public List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new product
     */
    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        Product product = new Product(
                productDto.getName(),
                BigDecimal.valueOf(productDto.getPrice()),
                productDto.getAvailabilityQty(),
                category
        );
        
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }
    
    /**
     * Update a product
     */
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(productDto.getName());
        product.setPrice(BigDecimal.valueOf(productDto.getPrice()));
        product.setAvailabilityQty(productDto.getAvailabilityQty());
        
        if (!productDto.getCategoryId().equals(product.getCategory().getId())) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }
    
    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
    
    /**
     * Update product stock
     */
    public void updateProductStock(Long productId, Integer newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setAvailabilityQty(newQuantity);
        productRepository.save(product);
    }
    
    /**
     * Add related products
     */
    public void addRelatedProducts(Long productId, List<Long> relatedProductIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        List<Product> relatedProducts = productRepository.findAllById(relatedProductIds);
        
        for (Product relatedProduct : relatedProducts) {
            product.addRelatedProduct(relatedProduct);
        }
        
        productRepository.save(product);
    }
    
    /**
     * Convert Product entity to ProductDto
     */
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice().doubleValue());
        dto.setAvailabilityQty(product.getAvailabilityQty());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }
} 