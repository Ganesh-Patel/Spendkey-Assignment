package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * GET /products - Get all available products
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllAvailableProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /products?categoryId={id} - Get products by category
     */
    @GetMapping(params = "categoryId")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@RequestParam Long categoryId) {
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /products/{id} - Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /products/{id}/related - Get related products
     */
    @GetMapping("/{id}/related")
    public ResponseEntity<List<ProductDto>> getRelatedProducts(@PathVariable Long id) {
        List<ProductDto> relatedProducts = productService.getRelatedProducts(id);
        return ResponseEntity.ok(relatedProducts);
    }
    
    /**
     * GET /products/search?name={name} - Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String name) {
        List<ProductDto> products = productService.searchProducts(name);
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /products/price-range?minPrice={min}&maxPrice={max} - Get products by price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * POST /products - Create new product
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        try {
            ProductDto createdProduct = productService.createProduct(productDto);
            return ResponseEntity.ok(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /products/{id} - Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /products/{id} - Delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /products/{id}/stock - Update product stock
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateProductStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            productService.updateProductStock(id, quantity);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 