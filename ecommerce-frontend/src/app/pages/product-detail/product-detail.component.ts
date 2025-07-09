import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ProductService, Product } from '../../services/product.service';
import { CartService, AddToCartRequest } from '../../services/cart.service';
import { User } from '../../models/auth.model';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  currentUser: User | null = null;
  product: Product | null = null;
  relatedProducts: Product[] = [];
  loading: boolean = true;
  relatedLoading: boolean = true;
  quantity: number = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    
    this.route.params.subscribe(params => {
      const productId = Number(params['id']);
      if (productId) {
        this.loadProduct(productId);
        this.loadRelatedProducts(productId);
      }
    });
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
        // You could add a toast notification here
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
} 