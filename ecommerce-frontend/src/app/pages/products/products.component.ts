import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ProductService, Product, Category } from '../../services/product.service';
import { CartService, AddToCartRequest } from '../../services/cart.service';
import { CartAnimationService } from '../../services/cart-animation.service';
import { User } from '../../models/auth.model';
import { Subscription } from 'rxjs';
import { PriceUtil } from '../../utils/price.util';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, OnDestroy {
  currentUser: User | null = null;
  products: Product[] = [];
  categories: Category[] = [];
  filteredProducts: Product[] = [];
  selectedCategory: number | null = null;
  searchQuery: string = '';
  loading: boolean = true;
  cartItemCount: number = 0;
  showMobileCategories: boolean = false;
  showMobileMenu: boolean = false;
  private cartSubscription: Subscription | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private productService: ProductService,
    private cartService: CartService,
    private cartAnimationService: CartAnimationService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    
    // Subscribe to cart count changes
    this.cartSubscription = this.cartService.cartItemCount$.subscribe(count => {
      this.cartItemCount = count;
    });
    
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

  ngOnDestroy() {
    if (this.cartSubscription) {
      this.cartSubscription.unsubscribe();
    }
  }

  // Mobile menu methods
  toggleMobileCategories() {
    this.showMobileCategories = !this.showMobileCategories;
  }

  toggleMobileMenu() {
    this.showMobileMenu = !this.showMobileMenu;
  }

  // Price formatting method
  formatPrice(price: number): string {
    return PriceUtil.formatPrice(price);
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
      // Get products by category (including subcategories)
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

  applyFilters() {
    this.filteredProducts = [...this.products];
    
    // Apply search filter
    if (this.searchQuery) {
      const query = this.searchQuery.toLowerCase();
      this.filteredProducts = this.filteredProducts.filter(
        product => product.name.toLowerCase().includes(query) ||
                  product.categoryName.toLowerCase().includes(query)
      );
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
          this.cartService.setCartItemCount(count);
        },
        error: (error) => {
          console.error('Error loading cart count:', error);
        }
      });
    }
  }

  onCategoryChange(categoryId: number | null) {
    this.selectedCategory = categoryId;
    this.updateQueryParams();
    this.loadProducts();
    // Close mobile categories after selection
    this.showMobileCategories = false;
  }

  searchProducts() {
    this.updateQueryParams();
    this.loadProducts();
  }

  clearFilters() {
    this.selectedCategory = null;
    this.searchQuery = '';
    this.updateQueryParams();
    this.loadProducts();
  }

  updateQueryParams() {
    const params: any = {};
    if (this.searchQuery) {
      params.search = this.searchQuery;
    }
    if (this.selectedCategory) {
      params.category = this.selectedCategory;
    }
    this.router.navigate([], { queryParams: params, replaceUrl: true });
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
    if (!this.selectedCategory) return '';
    
    const findCategory = (categories: Category[], id: number): Category | null => {
      for (const category of categories) {
        if (category.id === id) return category;
        if (category.children) {
          const found = findCategory(category.children, id);
          if (found) return found;
        }
      }
      return null;
    };
    
    const category = findCategory(this.categories, this.selectedCategory);
    return category ? category.name : '';
  }

  getCategoryBreadcrumb(): string[] {
    if (!this.selectedCategory) return [];
    
    const findCategoryPath = (categories: Category[], id: number, path: string[] = []): string[] | null => {
      for (const category of categories) {
        const currentPath = [...path, category.name];
        if (category.id === id) return currentPath;
        if (category.children) {
          const found = findCategoryPath(category.children, id, currentPath);
          if (found) return found;
        }
      }
      return null;
    };
    
    return findCategoryPath(this.categories, this.selectedCategory) || [];
  }

  getCategoryIdByName(name: string): number | null {
    const findCategoryByName = (categories: Category[], targetName: string): Category | null => {
      for (const category of categories) {
        if (category.name.toLowerCase() === targetName.toLowerCase()) return category;
        if (category.children) {
          const found = findCategoryByName(category.children, targetName);
          if (found) return found;
        }
      }
      return null;
    };
    
    const category = findCategoryByName(this.categories, name);
    return category ? category.id : null;
  }

  getAllCategoriesClass(): string {
    return this.selectedCategory === null 
      ? 'bg-blue-100 text-blue-700 border-blue-200' 
      : 'text-gray-700 hover:bg-gray-50';
  }

  getCategoryClass(categoryId: number): string {
    return this.selectedCategory === categoryId 
      ? 'bg-blue-100 text-blue-700 border-blue-200' 
      : 'text-gray-700 hover:bg-gray-50';
  }

  addToCart(product: Product, event: MouseEvent) {
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    if (!this.isProductInStock(product)) {
      return;
    }

    // Trigger cart animation
    this.cartAnimationService.triggerAddToCartAnimation(
      event, 
      product.id, 
      product.name, 
      product.price
    );

    const request: AddToCartRequest = {
      userId: this.currentUser.id,
      productId: product.id,
      quantity: 1
    };

    this.cartService.addToCart(request).subscribe({
      next: (response) => {
        console.log('Product added to cart:', response);
        // Update cart count
        this.cartService.getCartItemCount(this.currentUser!.id).subscribe(count => {
          this.cartService.setCartItemCount(count);
        });
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
      }
    });
  }

  viewCart() {
    this.router.navigate(['/cart']);
  }

  getFullName(): string {
    if (!this.currentUser) return '';
    return `${this.currentUser.firstName || ''} ${this.currentUser.lastName || ''}`.trim() || this.currentUser.username;
  }

  getInitials(): string {
    if (!this.currentUser) return '';
    const firstName = this.currentUser.firstName || '';
    const lastName = this.currentUser.lastName || '';
    const username = this.currentUser.username || '';
    
    if (firstName && lastName) {
      return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
    } else if (firstName) {
      return firstName.charAt(0).toUpperCase();
    } else if (lastName) {
      return lastName.charAt(0).toUpperCase();
    } else if (username) {
      return username.charAt(0).toUpperCase();
    }
    return 'U';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
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

  goToOrders() {
    this.router.navigate(['/orders']);
  }
} 