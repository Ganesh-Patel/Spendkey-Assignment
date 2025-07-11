import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { LoginRequest, SignupRequest, AuthResponse, User } from '../../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private currentUserSubject = new BehaviorSubject<User | null>(null);

  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Check if user is already logged in from localStorage
    const savedUser = localStorage.getItem('currentUser');
    const savedToken = localStorage.getItem('authToken');
    if (savedUser && savedToken) {
      // Validate the saved token
      this.validateToken(savedToken).subscribe({
        next: (response) => {
          if (response.message === 'Authentication successful') {
            const user: User = {
              id: response.userId,
              username: response.username,
              email: response.email,
              firstName: response.firstName,
              lastName: response.lastName,
              mobileNumber: response.mobileNumber || '',
              role: response.role || 'USER',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            };
            this.currentUserSubject.next(user);
            this.isAuthenticatedSubject.next(true);
            localStorage.setItem('currentUser', JSON.stringify(user));
            localStorage.setItem('authToken', response.token || '');
          } else {
            // Token is invalid, clear storage
            this.logout();
          }
        },
        error: () => {
          // Token validation failed, clear storage
          this.logout();
        }
      });
    }
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, loginRequest)
      .pipe(
        tap(response => {
          if (response.message === 'Authentication successful') {
            // Create user object from response
            const user: User = {
              id: response.userId,
              username: response.username,
              email: response.email,
              firstName: response.firstName,
              lastName: response.lastName,
              mobileNumber: response.mobileNumber || '',
              role: response.role || 'USER',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            };
            
            this.currentUserSubject.next(user);
            this.isAuthenticatedSubject.next(true);
            localStorage.setItem('currentUser', JSON.stringify(user));
            localStorage.setItem('authToken', response.token || '');
          }
        }),
        catchError(error => {
          console.error('Login error:', error);
          return throwError(() => error);
        })
      );
  }

  signup(signupRequest: SignupRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/signup`, signupRequest)
      .pipe(
        tap(response => {
          if (response.message === 'Authentication successful') {
            // For signup, automatically log in the user with the returned token
            const user: User = {
              id: response.userId,
              username: response.username,
              email: response.email,
              firstName: response.firstName,
              lastName: response.lastName,
              mobileNumber: response.mobileNumber || '',
              role: response.role || 'USER',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            };
            
            this.currentUserSubject.next(user);
            this.isAuthenticatedSubject.next(true);
            localStorage.setItem('currentUser', JSON.stringify(user));
            localStorage.setItem('authToken', response.token || '');
          }
        }),
        catchError(error => {
          console.error('Signup error:', error);
          return throwError(() => error);
        })
      );
  }

  logout(): void {
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
  }

  isLoggedIn(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getAuthToken(): string | null {
    return localStorage.getItem('authToken');
  }

  validateToken(token: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/validate-token`, null, {
      params: { token }
    });
  }

  checkUsernameAvailability(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/check-username/${username}`);
  }

  getUserProfile(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/profile/${userId}`);
  }

  updateProfile(userId: number, profileData: SignupRequest): Observable<AuthResponse> {
    return this.http.put<AuthResponse>(`${this.apiUrl}/profile/${userId}`, profileData)
      .pipe(
        tap(response => {
          if (response.message === 'Authentication successful') {
            // Update the stored user data and token
            const user: User = {
              id: response.userId,
              username: response.username,
              email: response.email,
              firstName: response.firstName,
              lastName: response.lastName,
              mobileNumber: response.mobileNumber || '',
              role: response.role || 'USER',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            };
            
            this.currentUserSubject.next(user);
            localStorage.setItem('currentUser', JSON.stringify(user));
            localStorage.setItem('authToken', response.token || '');
          }
        })
      );
  }

  deactivateAccount(userId: number): Observable<AuthResponse> {
    return this.http.delete<AuthResponse>(`${this.apiUrl}/profile/${userId}`)
      .pipe(
        tap(() => {
          // Logout after deactivating account
          this.logout();
        })
      );
  }
} 