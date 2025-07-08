import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  constructor(private http: HttpClient) {}

  // Get user's cart
  getCart(userId: number): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(`${this.apiUrl}/cart/${userId}`);
  }

  // Add item to cart
  addToCart(request: AddToCartRequest): Observable<CartItem> {
    return this.http.post<CartItem>(`${this.apiUrl}/cart/add`, request);
  }

  // Update cart item quantity
  updateCartItem(cartItemId: number, request: UpdateCartItemRequest): Observable<CartItem> {
    return this.http.put<CartItem>(`${this.apiUrl}/cart/${cartItemId}`, request);
  }

  // Remove item from cart
  removeFromCart(userId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cart/${userId}/product/${productId}`);
  }

  // Clear user's cart
  clearCart(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cart/${userId}`);
  }

  // Check if item exists in cart
  checkItemInCart(userId: number, productId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/cart/${userId}/check/${productId}`);
  }

  // Get cart item count
  getCartItemCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/cart/${userId}/count`);
  }
} 