import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface CartItem {
  id: number;
  userId: number;
  productId: number;
  quantity: number;
  product: {
    id: number;
    name: string;
    price: number;
    availabilityQty: number;
    categoryId: number;
    categoryName: string;
    available: boolean;
  };
}

export interface AddToCartRequest {
  userId: number;
  productId: number;
  quantity: number;
}

export interface UpdateCartItemRequest {
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = 'http://localhost:8080/api';
  private cartItemCountSubject = new BehaviorSubject<number>(0);
  public cartItemCount$ = this.cartItemCountSubject.asObservable();

  constructor(private http: HttpClient) {}

  // Get user's cart
  getCart(userId: number): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(`${this.apiUrl}/cart/${userId}`);
  }

  // Add item to cart
  addToCart(request: AddToCartRequest): Observable<CartItem> {
    return this.http.post<CartItem>(`${this.apiUrl}/cart/add`, request).pipe(
      tap(() => this.refreshCartCount(request.userId))
    );
  }

  // Update cart item quantity
  updateCartItem(cartItemId: number, request: UpdateCartItemRequest): Observable<CartItem> {
    return this.http.put<CartItem>(`${this.apiUrl}/cart/${cartItemId}`, request).pipe(
      tap(() => this.refreshCartCount())
    );
  }

  // Remove item from cart
  removeFromCart(userId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cart/${userId}/product/${productId}`).pipe(
      tap(() => this.refreshCartCount(userId))
    );
  }

  // Clear user's cart
  clearCart(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cart/${userId}`).pipe(
      tap(() => this.refreshCartCount(userId))
    );
  }

  // Check if item exists in cart
  checkItemInCart(userId: number, productId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/cart/${userId}/check/${productId}`);
  }

  // Get cart item count
  getCartItemCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/cart/${userId}/count`).pipe(
      tap(count => this.cartItemCountSubject.next(count))
    );
  }

  // Refresh cart count
  refreshCartCount(userId?: number): void {
    // This will be called by components that have access to userId
    // For now, we'll just emit 0 to indicate a refresh is needed
    this.cartItemCountSubject.next(0);
  }

  // Set cart count directly (used when we know the count)
  setCartItemCount(count: number): void {
    this.cartItemCountSubject.next(count);
  }

  // Get current cart count
  getCurrentCartCount(): number {
    return this.cartItemCountSubject.value;
  }
} 