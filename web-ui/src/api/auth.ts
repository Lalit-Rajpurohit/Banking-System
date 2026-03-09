import api from './api'

/**
 * User information returned from authentication endpoints.
 */
export interface UserInfo {
  id: number
  name: string
  email: string
  role: string
}

/**
 * Authentication response from login/register endpoints.
 */
export interface AuthResponse {
  token: string
  tokenType: string
  user: UserInfo
}

/**
 * Login request payload.
 */
export interface LoginRequest {
  email: string
  password: string
}

/**
 * Registration request payload.
 */
export interface RegisterRequest {
  name: string
  email: string
  password: string
}

/**
 * Authenticates a user with email and password.
 *
 * @param credentials Login credentials
 * @returns Authentication response with JWT token and user info
 */
export async function login(credentials: LoginRequest): Promise<AuthResponse> {
  const response = await api.post<AuthResponse>('/auth/login', credentials)
  return response.data
}

/**
 * Registers a new user account.
 *
 * @param userData Registration details
 * @returns Authentication response with JWT token and user info
 */
export async function register(userData: RegisterRequest): Promise<AuthResponse> {
  const response = await api.post<AuthResponse>('/auth/register', userData)
  return response.data
}
