import { useState, useEffect } from 'react'
import { Account, AccountType, createAccount, getMyAccounts } from '../api/accounts'
import AccountCard from '../components/AccountCard'
import { useAuth } from '../contexts/AuthContext'

/**
 * Dashboard page component.
 *
 * Displays user's accounts and provides option to create new accounts.
 */
export default function Dashboard() {
  const { user } = useAuth()
  const [accounts, setAccounts] = useState<Account[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showCreateModal, setShowCreateModal] = useState(false)
  const [creating, setCreating] = useState(false)

  /**
   * Fetches user's accounts on component mount.
   */
  useEffect(() => {
    fetchAccounts()
  }, [])

  /**
   * Fetches accounts from API.
   */
  const fetchAccounts = async () => {
    try {
      setLoading(true)
      const data = await getMyAccounts()
      setAccounts(data)
      setError(null)
    } catch (err) {
      setError('Failed to load accounts')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  /**
   * Handles creating a new account.
   */
  const handleCreateAccount = async (accountType: AccountType) => {
    try {
      setCreating(true)
      const newAccount = await createAccount(accountType)
      setAccounts([...accounts, newAccount])
      setShowCreateModal(false)
    } catch (err) {
      setError('Failed to create account')
      console.error(err)
    } finally {
      setCreating(false)
    }
  }

  /**
   * Calculates total balance across all accounts.
   */
  const totalBalance = accounts.reduce((sum, acc) => sum + acc.balance, 0)

  /**
   * Formats amount as currency string.
   */
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount)
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-500"></div>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div className="bg-gradient-to-r from-primary-600 to-primary-700 rounded-2xl p-8 text-white">
        <h1 className="text-3xl font-bold">Welcome back, {user?.name}!</h1>
        <p className="text-primary-100 mt-2">Here's your financial overview</p>
        <div className="mt-6">
          <p className="text-primary-200 text-sm">Total Balance</p>
          <p className="text-4xl font-bold">{formatCurrency(totalBalance)}</p>
        </div>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
          {error}
        </div>
      )}

      {/* Accounts Section */}
      <div>
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900">Your Accounts</h2>
          <button
            onClick={() => setShowCreateModal(true)}
            className="btn-primary flex items-center space-x-2"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            <span>New Account</span>
          </button>
        </div>

        {accounts.length === 0 ? (
          <div className="card text-center py-12">
            <svg className="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
            </svg>
            <h3 className="text-lg font-medium text-gray-900 mb-2">No accounts yet</h3>
            <p className="text-gray-500 mb-6">Create your first account to get started</p>
            <button
              onClick={() => setShowCreateModal(true)}
              className="btn-secondary"
            >
              Create Account
            </button>
          </div>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {accounts.map((account) => (
              <AccountCard key={account.id} account={account} />
            ))}
          </div>
        )}
      </div>

      {/* Create Account Modal */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl p-8 max-w-md w-full mx-4">
            <h3 className="text-xl font-bold text-gray-900 mb-6">Create New Account</h3>
            <p className="text-gray-500 mb-6">Select the type of account you want to create:</p>

            <div className="space-y-3">
              {(['SAVINGS', 'CHECKING', 'BUSINESS'] as AccountType[]).map((type) => (
                <button
                  key={type}
                  onClick={() => handleCreateAccount(type)}
                  disabled={creating}
                  className="w-full p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500
                           hover:bg-primary-50 transition-colors text-left flex items-center space-x-4
                           disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <div className={`p-3 rounded-full ${
                    type === 'SAVINGS' ? 'bg-green-100 text-green-600' :
                    type === 'CHECKING' ? 'bg-blue-100 text-blue-600' :
                    'bg-purple-100 text-purple-600'
                  }`}>
                    {type === 'SAVINGS' && (
                      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                          d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
                      </svg>
                    )}
                    {type === 'CHECKING' && (
                      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                          d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                      </svg>
                    )}
                    {type === 'BUSINESS' && (
                      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                          d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                    )}
                  </div>
                  <div>
                    <p className="font-medium text-gray-900">{type} Account</p>
                    <p className="text-sm text-gray-500">
                      {type === 'SAVINGS' && 'Grow your savings over time'}
                      {type === 'CHECKING' && 'For everyday transactions'}
                      {type === 'BUSINESS' && 'For business operations'}
                    </p>
                  </div>
                </button>
              ))}
            </div>

            <button
              onClick={() => setShowCreateModal(false)}
              className="w-full mt-6 py-2 text-gray-500 hover:text-gray-700"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
