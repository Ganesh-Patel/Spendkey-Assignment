import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ProductService, Product, Category } from '../../services/product.service';
import { CartService, AddToCartRequest } from '../../services/cart.service';
import { User } from '../../models/auth.model';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  currentUser: User | null = null;
  products: Product[] = [];
  categories: Category[] = [];
  filteredProducts: Product[] = [];
  selectedCategory: number | null = null;
  searchQuery: string = '';
  loading: boolean = true;
  cartItemCount: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    
    // Get query parameters
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['search'] || '';
      this.selectedCategory = params['category'] ? Number(params['category']) : null;
      this.loadProducts();
    });

    this.loadCategories();
    if (this.currentUser) {
      this.loadCartItemCount();
    }
  }

  loadProducts() {
    this.loading = true;
    
    if (this.searchQuery) {
      // Search products
      this.productService.searchProducts(this.searchQuery).subscribe({
        next: (products) => {
          this.products = products;
          this.applyFilters();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error searching products:', error);
          this.loading = false;
        }
      });
    } else if (this.selectedCategory) {
      // Get products by category
      this.productService.getProductsByCategory(this.selectedCategory).subscribe({
        next: (products) => {
          this.products = products;
          this.applyFilters();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading products by category:', error);
          this.loading = false;
        }
      });
    } else {
      // Get all products
      this.productService.getProducts().subscribe({
        next: (products) => {
          this.products = products;
          this.applyFilters();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading products:', error);
          this.loading = false;
        }
      });
    }
  }

  loadCategories() {
    this.productService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
      }
    });
  }

  loadCartItemCount() {
    if (this.currentUser) {
      this.cartService.getCartItemCount(this.currentUser.id).subscribe({
        next: (count) => {
          this.cartItemCount = count;
        },
        error: (error) => {
          console.error('Error loading cart count:', error);
        }
      });
    }
  }

  applyFilters() {
    this.filteredProducts = [...this.products];
    
    // Apply category filter
    if (this.selectedCategory) {
      this.filteredProducts = this.filteredProducts.filter(
        product => product.categoryId === this.selectedCategory
      );
    }
    
    // Apply search filter
    if (this.searchQuery) {
      const query = this.searchQuery.toLowerCase();
      this.filteredProducts = this.filteredProducts.filter(
        product => product.name.toLowerCase().includes(query) ||
                  product.categoryName.toLowerCase().includes(query)
      );
    }
  }

  onCategoryChange(categoryId: number | null) {
    this.selectedCategory = categoryId;
    this.updateQueryParams();
  }

  onSearch() {
    this.updateQueryParams();
  }

  clearFilters() {
    this.searchQuery = '';
    this.selectedCategory = null;
    this.updateQueryParams();
  }

  updateQueryParams() {
    const params: any = {};
    if (this.searchQuery) params.search = this.searchQuery;
    if (this.selectedCategory) params.category = this.selectedCategory;
    
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: params,
      queryParamsHandling: 'merge'
    });
  }

  isProductDisabled(product: Product): boolean {
    return !product.available || product.availabilityQty === 0;
  }

  isProductInStock(product: Product): boolean {
    return product.available && product.availabilityQty > 0;
  }

  getProductStockText(product: Product): string {
    return this.isProductInStock(product) ? 'In Stock' : 'Out of Stock';
  }

  getProductStockClass(product: Product): string {
    return this.isProductInStock(product) ? 'text-green-600' : 'text-red-600';
  }

  getButtonText(product: Product): string {
    return this.isProductInStock(product) ? 'Add to Cart' : 'Out of Stock';
  }

  getProductCountText(): string {
    return this.filteredProducts.length !== 1 ? 's' : '';
  }

  getSelectedCategoryName(): string {
    const category = this.categories.find(c => c.id === this.selectedCategory);
    return category ? category.name : '';
  }

  getAllCategoriesClass(): string {
    return this.selectedCategory === null ? 'text-blue-600 font-medium' : 'text-gray-600 hover:text-blue-600';
  }

  getCategoryClass(categoryId: number): string {
    return this.selectedCategory === categoryId ? 'text-blue-600 font-medium' : 'text-gray-600 hover:text-blue-600';
  }

  addToCart(product: Product) {
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    const request: AddToCartRequest = {
      userId: this.currentUser.id,
      productId: product.id,
      quantity: 1
    };

    this.cartService.addToCart(request).subscribe({
      next: (cartItem) => {
        console.log('Item added to cart:', cartItem);
        this.loadCartItemCount();
        // You could add a toast notification here
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
      }
    });
  }

  viewCart() {
    if (this.currentUser) {
      this.router.navigate(['/cart']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  getFullName(): string {
    if (!this.currentUser) return 'User';
    const firstName = this.currentUser.firstName || '';
    const lastName = this.currentUser.lastName || '';
    const fullName = `${firstName} ${lastName}`.trim();
    return fullName || this.currentUser.username || 'User';
  }

  getInitials(): string {
    if (!this.currentUser) return 'U';
    const firstName = this.currentUser.firstName || '';
    const lastName = this.currentUser.lastName || '';
    if (firstName && lastName) {
      return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
    } else if (firstName) {
      return firstName.charAt(0).toUpperCase();
    } else if (this.currentUser.username) {
      return this.currentUser.username.charAt(0).toUpperCase();
    }
    return 'U';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  goToSignup() {
    this.router.navigate(['/signup']);
  }

  goToHome() {
    this.router.navigate(['/']);
  }
} 