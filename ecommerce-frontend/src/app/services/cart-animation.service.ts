import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface CartAnimationEvent {
  productId: number;
  productName: string;
  price: number;
  x: number;
  y: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartAnimationService {
  private animationEventSubject = new BehaviorSubject<CartAnimationEvent | null>(null);
  public animationEvent$ = this.animationEventSubject.asObservable();

  constructor() {}

  /**
   * Trigger cart animation from a specific position
   */
  triggerAddToCartAnimation(event: MouseEvent, productId: number, productName: string, price: number) {
    const animationEvent: CartAnimationEvent = {
      productId,
      productName,
      price,
      x: event.clientX,
      y: event.clientY
    };
    
    this.animationEventSubject.next(animationEvent);
  }

  /**
   * Clear animation event
   */
  clearAnimation() {
    this.animationEventSubject.next(null);
  }
} 