import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginRequest: LoginRequest = {
    usernameOrMobile: '',
    password: ''
  };
  isLoading = false;
  errorMessage = '';
  showPassword = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    // If already logged in, redirect to home
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home']);
    }
  }

  onLogin() {
    this.isLoading = true;
    this.errorMessage = '';

    // Basic validation
    if (!this.loginRequest.usernameOrMobile || !this.loginRequest.password) {
      this.errorMessage = 'Please fill in all fields';
      this.isLoading = false;
      return;
    }

    // Call the auth service
    this.authService.login(this.loginRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.message === 'Authentication successful') {
          this.router.navigate(['/home']);
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.isLoading = false;
        if (error.error && error.error.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Login failed. Please try again.';
        }
        console.error('Login error:', error);
      }
    });
  }

  goToSignup() {
    this.router.navigate(['/signup']);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  // Test credentials helper
  fillTestCredentials() {
    this.loginRequest.usernameOrMobile = 'newuser';
    this.loginRequest.password = 'password123';
  }

  // Alternative test with mobile number
  fillTestCredentialsMobile() {
    this.loginRequest.usernameOrMobile = '1234567890';
    this.loginRequest.password = 'password123';
  }
}
