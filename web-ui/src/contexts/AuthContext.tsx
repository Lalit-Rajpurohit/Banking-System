import { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { UserInfo, login as apiLogin, register as apiRegister, LoginRequest, RegisterRequest } from '../api/auth'

/**
 * Authentication context state and methods.
 */
interface AuthContextType {
  user: UserInfo | null
  token: string | null
  isAuthenticated: boolean
  loading: boolean
  login: (credentials: LoginRequest) => Promise<void>
  register: (userData: RegisterRequest) => Promise<void>
  logout: () => void
}

/**
 * Authentication context for managing user state across the app.
 */
const AuthContext = createContext<AuthContextType | undefined>(undefined)

/**
 * Props for AuthProvider component.
 */
interface AuthProviderProps {
  children: ReactNode
}

/**
 * Authentication provider component.
 *
 * Manages authentication state including:
 * - User information
 * - JWT token
 * - Login/logout/register operations
 * - Persistence to localStorage
 */
export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<UserInfo | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  /**
   * Load saved authentication state from localStorage on mount.
   */
  useEffect(() => {
    const savedToken = localStorage.getItem('token')
    const savedUser = localStorage.getItem('user')

    if (savedToken && savedUser) {
      try {
        setToken(savedToken)
        setUser(JSON.parse(savedUser))
      } catch (error) {
        // Invalid saved data, clear it
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      }
    }
    setLoading(false)
  }, [])

  /**
   * Authenticates user with email and password.
   * Stores token and user info in state and localStorage.
   */
  const login = async (credentials: LoginRequest): Promise<void> => {
    const response = await apiLogin(credentials)

    // Store in state
    setToken(response.token)
    setUser(response.user)

    // Persist to localStorage
    localStorage.setItem('token', response.token)
    localStorage.setItem('user', JSON.stringify(response.user))
  }

  /**
   * Registers a new user account.
   * Automatically logs in the user after registration.
   */
  const register = async (userData: RegisterRequest): Promise<void> => {
    const response = await apiRegister(userData)

    // Store in state
    setToken(response.token)
    setUser(response.user)

    // Persist to localStorage
    localStorage.setItem('token', response.token)
    localStorage.setItem('user', JSON.stringify(response.user))
  }

  /**
   * Logs out the current user.
   * Clears state and localStorage.
   */
  const logout = (): void => {
    setToken(null)
    setUser(null)
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const value: AuthContextType = {
    user,
    token,
    isAuthenticated: !!token && !!user,
    loading,
    login,
    register,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

/**
 * Hook to access authentication context.
 *
 * @returns Authentication context value
 * @throws Error if used outside of AuthProvider
 */
export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
