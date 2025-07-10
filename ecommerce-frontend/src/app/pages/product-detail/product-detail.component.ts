import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ProductService, Product } from '../../services/product.service';
import { CartService, AddToCartRequest } from '../../services/cart.service';
import { User } from '../../models/auth.model';
import { Subscription } from 'rxjs';
import { PriceUtil } from '../../utils/price.util';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  currentUser: User | null = null;
  product: Product | null = null;
  relatedProducts: Product[] = [];
  loading: boolean = true;
  relatedLoading: boolean = true;
  quantity: number = 1;
  cartItemCount: number = 0;
  private cartSubscription: Subscription | null = null;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    public authService: AuthService,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    
    // Subscribe to cart count changes
    this.cartSubscription = this.cartService.cartItemCount$.subscribe(count => {
      this.cartItemCount = count;
    });
    
    this.route.params.subscribe(params => {
      const productId = Number(params['id']);
      if (productId) {
        this.loadProduct(productId);
        this.loadRelatedProducts(productId);
      }
    });

    if (this.currentUser) {
      this.loadCartItemCount();
    }
  }

  ngOnDestroy() {
    if (this.cartSubscription) {
      this.cartSubscription.unsubscribe();
    }
  }

  // Navigation methods
  navigateToHome() {
    this.router.navigate(['/home']);
  }

  navigateToCart() {
    this.router.navigate(['/cart']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  navigateToSignup() {
    this.router.navigate(['/signup']);
  }

  navigateToProducts() {
    this.router.navigate(['/products']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // Price formatting method
  formatPrice(price: number): string {
    return PriceUtil.formatPrice(price);
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

  loadProduct(productId: number) {
    this.loading = true;
    this.productService.getProductById(productId).subscribe({
      next: (product) => {
        this.product = product;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading product:', error);
        this.loading = false;
      }
    });
  }

  loadRelatedProducts(productId: number) {
    this.relatedLoading = true;
    this.productService.getRelatedProducts(productId).subscribe({
      next: (products) => {
        this.relatedProducts = products;
        this.relatedLoading = false;
      },
      error: (error) => {
        console.error('Error loading related products:', error);
        this.relatedLoading = false;
      }
    });
  }

  addToCart() {
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    if (!this.product) return;

    const request: AddToCartRequest = {
      userId: this.currentUser.id,
      productId: this.product.id,
      quantity: this.quantity
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

  addRelatedToCart(product: Product) {
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
        console.log('Related item added to cart:', cartItem);
        // Cart count will be updated automatically via the observable
      },
      error: (error) => {
        console.error('Error adding related item to cart:', error);
      }
    });
  }

  viewProduct(productId: number) {
    this.router.navigate(['/product', productId]);
  }

  increaseQuantity() {
    if (this.product && this.quantity < this.product.availabilityQty) {
      this.quantity++;
    }
  }

  decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  isProductAvailable(product: Product): boolean {
    return product.available && product.availabilityQty > 0;
  }

  getProductStockText(product: Product): string {
    return product.available && product.availabilityQty > 0 ? 'In Stock' : 'Out of Stock';
  }

  getProductStockClass(product: Product): string {
    return product.available && product.availabilityQty > 0 ? 'text-green-600' : 'text-red-600';
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
} 