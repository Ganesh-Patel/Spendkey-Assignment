import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ProductService, Product, Category } from '../../services/product.service';
import { CartService, AddToCartRequest } from '../../services/cart.service';
import { User } from '../../models/auth.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  currentUser: User | null = null;
  featuredProducts: Product[] = [];
  categories: Category[] = [];
  cartItemCount: number = 0;
  loading: boolean = true;
  searchQuery: string = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadFeaturedProducts();
    this.loadCategories();
    if (this.currentUser) {
      this.loadCartItemCount();
    }
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
          this.cartItemCount = count;
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
        this.loadCartItemCount(); // Refresh cart count
        // You could add a toast notification here
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
        // You could add error handling here
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
}
