import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { CartService, CartItem } from '../../services/cart.service';
import { OrderService, CreateOrderRequest, Order } from '../../services/order.service';
import { User } from '../../models/auth.model';
import { Subscription } from 'rxjs';
import { PriceUtil } from '../../utils/price.util';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {
  currentUser: User | null = null;
  cartItems: CartItem[] = [];
  loading: boolean = true;
  updating: boolean = false;
  checkingOut: boolean = false;
  showCheckoutForm: boolean = false;
  checkoutForm = {
    customerName: '',
    customerEmail: '',
    customerAddress: ''
  };
  private cartSubscription: Subscription | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private cartService: CartService,
    private orderService: OrderService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    // Pre-fill checkout form with user data
    this.checkoutForm.customerName = this.getFullName();
    this.checkoutForm.customerEmail = this.currentUser.email || '';

    this.loadCart();
  }

  ngOnDestroy() {
    if (this.cartSubscription) {
      this.cartSubscription.unsubscribe();
    }
  }

  loadCart() {
    this.loading = true;
    this.cartService.getCart(this.currentUser!.id).subscribe({
      next: (cartItems) => {
        this.cartItems = cartItems;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading cart:', error);
        this.loading = false;
      }
    });
  }

  increaseQuantity(item: CartItem) {
    if (this.updating || item.quantity >= item.product.availabilityQty) return;
    
    this.updating = true;
    this.cartService.updateCartItem(item.id, { quantity: item.quantity + 1 }).subscribe({
      next: (updatedItem) => {
        item.quantity = updatedItem.quantity;
        this.updating = false;
      },
      error: (error) => {
        console.error('Error updating quantity:', error);
        this.updating = false;
      }
    });
  }

  decreaseQuantity(item: CartItem) {
    if (this.updating || item.quantity <= 1) return;
    
    this.updating = true;
    this.cartService.updateCartItem(item.id, { quantity: item.quantity - 1 }).subscribe({
      next: (updatedItem) => {
        item.quantity = updatedItem.quantity;
        this.updating = false;
      },
      error: (error) => {
        console.error('Error updating quantity:', error);
        this.updating = false;
      }
    });
  }

  removeItem(item: CartItem) {
    if (this.updating) return;
    
    this.updating = true;
    this.cartService.removeFromCart(this.currentUser!.id, item.product.id).subscribe({
      next: () => {
        this.cartItems = this.cartItems.filter(cartItem => cartItem.product.id !== item.product.id);
        this.updating = false;
      },
      error: (error) => {
        console.error('Error removing item:', error);
        this.updating = false;
      }
    });
  }

  clearCart() {
    if (this.updating || this.cartItems.length === 0) return;
    
    this.updating = true;
    this.cartService.clearCart(this.currentUser!.id).subscribe({
      next: () => {
        this.cartItems = [];
        this.updating = false;
      },
      error: (error) => {
        console.error('Error clearing cart:', error);
        this.updating = false;
      }
    });
  }

  showCheckout() {
    if (this.cartItems.length === 0) return;
    this.showCheckoutForm = true;
  }

  cancelCheckout() {
    this.showCheckoutForm = false;
    this.checkoutForm = {
      customerName: this.getFullName(),
      customerEmail: this.currentUser?.email || '',
      customerAddress: ''
    };
  }

  checkout() {
    if (this.checkingOut || this.cartItems.length === 0) return;
    
    // Validate form
    if (!this.checkoutForm.customerName || !this.checkoutForm.customerEmail || !this.checkoutForm.customerAddress) {
      alert('Please fill in all required fields');
      return;
    }
    
    this.checkingOut = true;
    
    const request: CreateOrderRequest = {
      userId: this.currentUser!.id,
      customerName: this.checkoutForm.customerName,
      customerEmail: this.checkoutForm.customerEmail,
      customerAddress: this.checkoutForm.customerAddress
    };

    this.orderService.createOrder(request).subscribe({
      next: (order: Order) => {
        console.log('Order created successfully:', order);
        
        // Show success message
        this.showOrderSuccess(order);
        
        // Clear cart items from UI
        this.cartItems = [];
        this.checkingOut = false;
        this.showCheckoutForm = false;
        
        // Reset form
        this.checkoutForm = {
          customerName: this.getFullName(),
          customerEmail: this.currentUser?.email || '',
          customerAddress: ''
        };
      },
      error: (error) => {
        console.error('Error creating order:', error);
        alert('Error creating order. Please try again.');
        this.checkingOut = false;
      }
    });
  }

  showOrderSuccess(order: Order) {
    // Create a success notification
    const notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg z-50 transform transition-all duration-500 ease-out';
    notification.innerHTML = `
      <div class="flex items-center space-x-2">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
        </svg>
        <span>Order #${order.id} placed successfully!</span>
      </div>
    `;
    
    document.body.appendChild(notification);
    
    // Animate in
    setTimeout(() => {
      notification.style.transform = 'translateX(0)';
    }, 100);
    
    // Remove after 5 seconds
    setTimeout(() => {
      notification.style.transform = 'translateX(100%)';
      setTimeout(() => {
        document.body.removeChild(notification);
      }, 500);
    }, 5000);
  }

  getTotalItems(): number {
    return this.cartItems.reduce((total, item) => total + item.quantity, 0);
  }

  getTotalPrice(): number {
    return this.cartItems.reduce((total, item) => total + (item.product.price * item.quantity), 0);
  }

  getFormattedTotalPrice(): string {
    return PriceUtil.formatPrice(this.getTotalPrice());
  }

  formatPrice(price: number): string {
    return PriceUtil.formatPrice(price);
  }

  formatTotalPrice(price: number, quantity: number): string {
    return PriceUtil.formatPrice(price * quantity);
  }

  continueShopping() {
    this.router.navigate(['/products']);
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

  goToProducts() {
    this.router.navigate(['/products']);
  }
} 