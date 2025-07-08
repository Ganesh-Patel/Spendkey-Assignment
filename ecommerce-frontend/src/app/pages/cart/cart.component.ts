import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { CartService, CartItem, UpdateCartItemRequest } from '../../services/cart.service';
import { User } from '../../models/auth.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  currentUser: User | null = null;
  cartItems: CartItem[] = [];
  loading: boolean = true;
  updating: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadCart();
  }

  loadCart() {
    this.loading = true;
    this.cartService.getCart(this.currentUser!.id).subscribe({
      next: (items) => {
        this.cartItems = items;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading cart:', error);
        this.loading = false;
      }
    });
  }

  updateQuantity(item: CartItem, newQuantity: number) {
    if (newQuantity < 1) return;
    
    this.updating = true;
    const request: UpdateCartItemRequest = { quantity: newQuantity };
    
    this.cartService.updateCartItem(item.id, request).subscribe({
      next: (updatedItem) => {
        // Update the item in the local array
        const index = this.cartItems.findIndex(i => i.id === item.id);
        if (index !== -1) {
          this.cartItems[index] = updatedItem;
        }
        this.updating = false;
      },
      error: (error) => {
        console.error('Error updating cart item:', error);
        this.updating = false;
      }
    });
  }

  removeItem(item: CartItem) {
    if (!this.currentUser) return;
    
    this.cartService.removeFromCart(this.currentUser.id, item.productId).subscribe({
      next: () => {
        // Remove item from local array
        this.cartItems = this.cartItems.filter(i => i.id !== item.id);
      },
      error: (error) => {
        console.error('Error removing item from cart:', error);
      }
    });
  }

  clearCart() {
    if (!this.currentUser) return;
    
    if (confirm('Are you sure you want to clear your cart?')) {
      this.cartService.clearCart(this.currentUser.id).subscribe({
        next: () => {
          this.cartItems = [];
        },
        error: (error) => {
          console.error('Error clearing cart:', error);
        }
      });
    }
  }

  getTotalPrice(): number {
    return this.cartItems.reduce((total, item) => {
      return total + (item.product.price * item.quantity);
    }, 0);
  }

  getTotalItems(): number {
    return this.cartItems.reduce((total, item) => {
      return total + item.quantity;
    }, 0);
  }

  checkout() {
    // This would typically navigate to a checkout page
    alert('Checkout functionality would be implemented here');
  }

  continueShopping() {
    this.router.navigate(['/products']);
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

  goToProducts() {
    this.router.navigate(['/products']);
  }
} 