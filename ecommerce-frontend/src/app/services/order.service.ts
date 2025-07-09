import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getEnvironment } from '../../environments/environment';

export interface CreateOrderRequest {
  userId: number;
  customerName: string;
  customerEmail: string;
  customerAddress: string;
}

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  categoryName: string;
  quantity: number;
  price: number;
  totalPrice: number;
}

export interface Order {
  id: number;
  customerName: string;
  customerEmail: string;
  customerAddress: string;
  totalAmount: number;
  status: string;
  orderDate: string;
  orderItems: OrderItem[];
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = `${getEnvironment().apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  /**
   * Create order from cart
   */
  createOrder(request: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, request);
  }

  /**
   * Get order by ID
   */
  getOrderById(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${orderId}`);
  }

  /**
   * Get orders by customer email
   */
  getOrdersByCustomerEmail(email: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/customer/${email}`);
  }

  /**
   * Get all orders (for admin)
   */
  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }

  /**
   * Update order status
   */
  updateOrderStatus(orderId: number, status: string): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${orderId}/status?status=${status}`, {});
  }

  /**
   * Cancel order
   */
  cancelOrder(orderId: number): Observable<Order> {
    return this.http.delete<Order>(`${this.apiUrl}/${orderId}`);
  }
} 