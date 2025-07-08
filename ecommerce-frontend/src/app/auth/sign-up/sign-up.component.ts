import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { SignupRequest } from '../../models/auth.model';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  signupRequest: SignupRequest = {
    username: '',
    mobileNumber: '',
    password: '',
    email: '',
    firstName: '',
    lastName: ''
  };
  confirmPassword = '';
  isLoading = false;
  errorMessage = '';
  showPassword = false;
  showConfirmPassword = false;
  usernameAvailable = true;
  checkingUsername = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    // If already logged in, redirect to home
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home']);
    }
  }

  onRegister() {
    this.isLoading = true;
    this.errorMessage = '';

    // Validate form
    if (!this.signupRequest.username || !this.signupRequest.mobileNumber || 
        !this.signupRequest.password || !this.confirmPassword) {
      this.errorMessage = 'Please fill in all required fields';
      this.isLoading = false;
      return;
    }
    
    // Username validation
    if (this.signupRequest.username.length < 3) {
      this.errorMessage = 'Username must be at least 3 characters long';
      this.isLoading = false;
      return;
    }
    
    // Mobile validation (10 digits)
    const mobileRegex = /^[0-9]{10}$/;
    if (!mobileRegex.test(this.signupRequest.mobileNumber)) {
      this.errorMessage = 'Please enter a valid 10-digit mobile number';
      this.isLoading = false;
      return;
    }
    
    // Email validation (optional but if provided, must be valid)
    if (this.signupRequest.email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.signupRequest.email)) {
        this.errorMessage = 'Please enter a valid email address';
        this.isLoading = false;
        return;
      }
    }
    
    // Password validation
    if (this.signupRequest.password.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters long';
      this.isLoading = false;
      return;
    }

    // Password match validation
    if (this.signupRequest.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      this.isLoading = false;
      return;
    }

    // Check username availability
    if (!this.usernameAvailable) {
      this.errorMessage = 'Username is already taken. Please choose another one.';
      this.isLoading = false;
      return;
    }
    
    // Call the auth service
    this.authService.signup(this.signupRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.message === 'User registered successfully') {
          this.errorMessage = '';
          // Show success message and redirect to login
          alert('Registration successful! Please login with your credentials.');
          this.router.navigate(['/login']);
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.isLoading = false;
        if (error.error && error.error.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
        console.error('Signup error:', error);
      }
    });
  }

  checkUsernameAvailability() {
    if (this.signupRequest.username.length >= 3) {
      this.checkingUsername = true;
      this.authService.checkUsernameAvailability(this.signupRequest.username).subscribe({
        next: (available) => {
          this.usernameAvailable = available;
          this.checkingUsername = false;
        },
        error: (error) => {
          console.error('Username check error:', error);
          this.checkingUsername = false;
        }
      });
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  // Helper method to check password strength
  getPasswordStrength() {
    if (!this.signupRequest.password) return '';
    if (this.signupRequest.password.length < 6) return 'weak';
    if (this.signupRequest.password.length < 10) return 'medium';
    return 'strong';
  }

  getPasswordStrengthColor() {
    const strength = this.getPasswordStrength();
    switch (strength) {
      case 'weak': return 'text-red-500';
      case 'medium': return 'text-yellow-500';
      case 'strong': return 'text-green-500';
      default: return 'text-gray-400';
    }
  }

  getPasswordStrengthText() {
    const strength = this.getPasswordStrength();
    switch (strength) {
      case 'weak': return 'Weak';
      case 'medium': return 'Medium';
      case 'strong': return 'Strong';
      default: return '';
    }
  }

  // Helper method to get full name from first and last name
  getFullName(): string {
    const firstName = this.signupRequest.firstName || '';
    const lastName = this.signupRequest.lastName || '';
    return `${firstName} ${lastName}`.trim();
  }
}
