export interface LoginRequest {
  usernameOrMobile: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  mobileNumber: string;
  password: string;
  email?: string;
  firstName?: string;
  lastName?: string;
}

export interface AuthResponse {
  userId: number;
  username: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  mobileNumber?: string;
  role?: string;
  token?: string;
  loginTime: string;
  message: string;
}

export interface User {
  id: number;
  username: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  mobileNumber: string;
  role: string;
  isActive: boolean;
  createdAt: string;
  updatedAt?: string;
} 