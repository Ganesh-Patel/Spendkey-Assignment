import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ProductService, Product, Category } from '../../services/product.service';
import { CartService, AddToCartRequest } from '../../services/cart.service';
import { CartAnimationService } from '../../services/cart-animation.service';
import { User } from '../../models/auth.model';
import { Subscription } from 'rxjs';
import { PriceUtil } from '../../utils/price.util';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  currentUser: User | null = null;
  featuredProducts: Product[] = [];
  categories: Category[] = [];
  cartItemCount: number = 0;
  loading: boolean = true;
  searchQuery: string = '';
  private cartSubscription: Subscription | null = null;

  constructor(
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
    
    this.loadFeaturedProducts();
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

  // Price formatting method
  formatPrice(price: number): string {
    return PriceUtil.formatPrice(price);
  }

  loadFeaturedProducts() {
    this.productService.getProducts().subscribe({
      next: (products) => {
        // Take first 4 products as featured
        this.featuredProducts = products.slice(0, 4);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.loading = false;
      }
    });
  }

  loadCategories() {
    this.productService.getRootCategories().subscribe({
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

  searchProducts() {
    if (this.searchQuery.trim()) {
      this.router.navigate(['/products'], { queryParams: { search: this.searchQuery } });
    }
  }

  addToCart(product: Product, event: MouseEvent) {
    if (!this.currentUser) {
      this.router.navigate(['/login']);
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
      next: (cartItem) => {
        console.log('Item added to cart:', cartItem);
        // Cart count will be updated automatically via the observable
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

  goToProducts() {
    this.router.navigate(['/products']);
  }

  goToCategory(categoryId: number) {
    this.router.navigate(['/products'], { queryParams: { category: categoryId } });
  }

  goToOrders() {
    this.router.navigate(['/orders']);
  }

  // Get category-specific colors
  getCategoryColors(categoryName: string): { bg: string, icon: string } {
    const name = categoryName.toLowerCase();
    
    if (name.includes('electronics')) {
      return { bg: 'from-blue-500 to-blue-600', icon: 'from-blue-50 to-blue-100' };
    } else if (name.includes('fashion')) {
      return { bg: 'from-pink-500 to-pink-600', icon: 'from-pink-50 to-pink-100' };
    } else if (name.includes('home') || name.includes('garden')) {
      return { bg: 'from-green-500 to-green-600', icon: 'from-green-50 to-green-100' };
    } else if (name.includes('sports')) {
      return { bg: 'from-orange-500 to-orange-600', icon: 'from-orange-50 to-orange-100' };
    } else {
      return { bg: 'from-purple-500 to-purple-600', icon: 'from-purple-50 to-purple-100' };
    }
  }
}
