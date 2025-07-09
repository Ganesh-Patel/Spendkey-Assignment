export class PriceUtil {
  /**
   * Format price to 2 decimal places and handle floating point precision issues
   * @param price - The price as number
   * @returns Formatted price string with ₹ symbol
   */
  static formatPrice(price: number): string {
    if (price === null || price === undefined || isNaN(price)) {
      return '₹0.00';
    }
    
    // Round to 2 decimal places to handle floating point precision issues
    const roundedPrice = Math.round(price * 100) / 100;
    
    // Format to 2 decimal places
    return `₹${roundedPrice.toFixed(2)}`;
  }

  /**
   * Format price without currency symbol
   * @param price - The price as number
   * @returns Formatted price string without symbol
   */
  static formatPriceNumber(price: number): string {
    if (price === null || price === undefined || isNaN(price)) {
      return '0.00';
    }
    
    // Round to 2 decimal places to handle floating point precision issues
    const roundedPrice = Math.round(price * 100) / 100;
    
    // Format to 2 decimal places
    return roundedPrice.toFixed(2);
  }

  /**
   * Calculate total price for quantity * unit price
   * @param unitPrice - Unit price
   * @param quantity - Quantity
   * @returns Formatted total price string
   */
  static calculateTotalPrice(unitPrice: number, quantity: number): string {
    if (unitPrice === null || unitPrice === undefined || isNaN(unitPrice) ||
        quantity === null || quantity === undefined || isNaN(quantity)) {
      return '₹0.00';
    }
    
    const total = unitPrice * quantity;
    return this.formatPrice(total);
  }

  /**
   * Calculate total price for quantity * unit price (returns number)
   * @param unitPrice - Unit price
   * @param quantity - Quantity
   * @returns Total price as number
   */
  static calculateTotalPriceNumber(unitPrice: number, quantity: number): number {
    if (unitPrice === null || unitPrice === undefined || isNaN(unitPrice) ||
        quantity === null || quantity === undefined || isNaN(quantity)) {
      return 0;
    }
    
    const total = unitPrice * quantity;
    return Math.round(total * 100) / 100;
  }
} 