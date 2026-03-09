import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

/**
 * Header component with navigation and user controls.
 *
 * Shows different options based on authentication state:
 * - Logged out: Login and Register links
 * - Logged in: Dashboard, Transfer links and Logout button
 */
export default function Header() {
  const { user, isAuthenticated, logout } = useAuth()
  const navigate = useNavigate()

  /**
   * Handles user logout and redirects to login page.
   */
  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="bg-primary-700 text-white shadow-lg">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo / Brand */}
          <Link to="/" className="flex items-center space-x-2">
            <svg
              className="w-8 h-8"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
            <span className="text-xl font-bold">BankingSystem</span>
          </Link>

          {/* Navigation */}
          <nav className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <Link
                  to="/dashboard"
                  className="hover:text-primary-200 transition-colors"
                >
                  Dashboard
                </Link>
                <Link
                  to="/transfer"
                  className="hover:text-primary-200 transition-colors"
                >
                  Transfer
                </Link>
                <span className="text-primary-200">|</span>
                <span className="text-primary-200">{user?.name}</span>
                <button
                  onClick={handleLogout}
                  className="bg-accent-500 hover:bg-accent-600 px-4 py-2 rounded-lg
                           transition-colors font-medium"
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/login"
                  className="hover:text-primary-200 transition-colors"
                >
                  Login
                </Link>
                <Link
                  to="/register"
                  className="bg-accent-500 hover:bg-accent-600 px-4 py-2 rounded-lg
                           transition-colors font-medium"
                >
                  Register
                </Link>
              </>
            )}
          </nav>
        </div>
      </div>
    </header>
  )
}
