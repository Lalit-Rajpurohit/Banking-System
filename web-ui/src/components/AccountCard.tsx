import { Link } from 'react-router-dom'
import { Account } from '../api/accounts'

/**
 * Props for AccountCard component.
 */
interface AccountCardProps {
  account: Account
}

/**
 * Card component displaying account summary information.
 *
 * Shows account type, number, balance, and links to details page.
 */
export default function AccountCard({ account }: AccountCardProps) {
  /**
   * Returns appropriate icon based on account type.
   */
  const getAccountIcon = () => {
    switch (account.accountType) {
      case 'SAVINGS':
        return (
          <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
        )
      case 'CHECKING':
        return (
          <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
          </svg>
        )
      case 'BUSINESS':
        return (
          <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
          </svg>
        )
      default:
        return null
    }
  }

  /**
   * Formats balance as currency string.
   */
  const formatBalance = (balance: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(balance)
  }

  /**
   * Returns color classes based on account type.
   */
  const getTypeColor = () => {
    switch (account.accountType) {
      case 'SAVINGS':
        return 'bg-green-100 text-green-800'
      case 'CHECKING':
        return 'bg-blue-100 text-blue-800'
      case 'BUSINESS':
        return 'bg-purple-100 text-purple-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  return (
    <Link
      to={`/accounts/${account.accountNumber}`}
      className="card hover:shadow-lg transition-shadow duration-200 block"
    >
      <div className="flex items-start justify-between">
        <div className="flex items-center space-x-4">
          <div className={`p-3 rounded-full ${getTypeColor()}`}>
            {getAccountIcon()}
          </div>
          <div>
            <span className={`text-xs font-semibold px-2 py-1 rounded ${getTypeColor()}`}>
              {account.accountType}
            </span>
            <p className="text-gray-500 text-sm mt-1">{account.accountNumber}</p>
          </div>
        </div>
      </div>

      <div className="mt-4">
        <p className="text-gray-500 text-sm">Current Balance</p>
        <p className="text-3xl font-bold text-gray-900">
          {formatBalance(account.balance)}
        </p>
      </div>

      <div className="mt-4 flex justify-end">
        <span className="text-primary-600 text-sm font-medium hover:text-primary-700">
          View Details &rarr;
        </span>
      </div>
    </Link>
  )
}
