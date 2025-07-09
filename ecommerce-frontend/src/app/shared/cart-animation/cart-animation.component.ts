import { Component, OnInit, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { CartAnimationService, CartAnimationEvent } from '../../services/cart-animation.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart-animation',
  template: `
    <div 
      #animationElement
      *ngIf="showAnimation"
      class="fixed z-50 pointer-events-none"
      [style.left.px]="animationX"
      [style.top.px]="animationY"
      [@flyingAnimation]="animationState"
    >
      <div class="bg-blue-600 text-white rounded-full w-8 h-8 flex items-center justify-center shadow-lg">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
        </svg>
      </div>
    </div>
  `,
  styles: [`
    :host {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      pointer-events: none;
      z-index: 9999;
    }
  `],
  animations: [
    trigger('flyingAnimation', [
      state('start', style({
        transform: 'scale(1) translate(0, 0)',
        opacity: 1
      })),
      state('flying', style({
        transform: 'scale(0.8) translate(-100px, -100px)',
        opacity: 0.8
      })),
      state('end', style({
        transform: 'scale(0.6) translate(-200px, -200px)',
        opacity: 0
      })),
      transition('start => flying', animate('300ms ease-out')),
      transition('flying => end', animate('200ms ease-in'))
    ])
  ]
})
export class CartAnimationComponent implements OnInit, OnDestroy {
  @ViewChild('animationElement') animationElement!: ElementRef;
  
  showAnimation = false;
  animationX = 0;
  animationY = 0;
  animationState = 'start';
  
  private subscription: Subscription = new Subscription();

  constructor(private cartAnimationService: CartAnimationService) {}

  ngOnInit() {
    this.subscription.add(
      this.cartAnimationService.animationEvent$.subscribe(event => {
        if (event) {
          this.startAnimation(event);
        }
      })
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  private startAnimation(event: CartAnimationEvent) {
    this.animationX = event.x - 16; // Center the 32px element
    this.animationY = event.y - 16;
    this.animationState = 'start';
    this.showAnimation = true;

    // Animate to flying state
    setTimeout(() => {
      this.animationState = 'flying';
    }, 50);

    // Animate to end state
    setTimeout(() => {
      this.animationState = 'end';
    }, 350);

    // Hide animation
    setTimeout(() => {
      this.showAnimation = false;
      this.cartAnimationService.clearAnimation();
    }, 550);
  }
} 