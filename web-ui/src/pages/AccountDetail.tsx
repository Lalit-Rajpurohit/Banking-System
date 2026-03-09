import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { Account, getAccount } from '../api/accounts'
import { Transaction, deposit, withdraw, getTransactionHistory } from '../api/transactions'
import TransactionList from '../components/TransactionList'

/**
 * Form data for deposit/withdraw operations.
 */
interface TransactionForm {
  amount: number
  description: string
}

/**
 * Account detail page component.
 *
 * Displays account information, allows deposits/withdrawals,
 * and shows transaction history.
 */
export default function AccountDetail() {
  const { accountNumber } = useParams<{ accountNumber: string }>()
  const [account, setAccount] = useState<Account | null>(null)
  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [activeTab, setActiveTab] = useState<'deposit' | 'withdraw'>('deposit')
  const [processing, setProcessing] = useState(false)
  const [successMessage, setSuccessMessage] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<TransactionForm>()

  /**
   * Fetches account details and transaction history.
   */
  useEffect(() => {
    if (accountNumber) {
      fetchData()
    }
  }, [accountNumber])

  const fetchData = async () => {
    try {
      setLoading(true)
      const [accountData, historyData] = await Promise.all([
        getAccount(accountNumber!),
        getTransactionHistory(accountNumber!),
      ])
      setAccount(accountData)
      setTransactions(historyData.content)
      setError(null)
    } catch (err) {
      setError('Failed to load account details')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  /**
   * Handles deposit or withdrawal form submission.
   */
  const onSubmit = async (data: TransactionForm) => {
    if (!accountNumber) return

    setProcessing(true)
    setError(null)
    setSuccessMessage(null)

    try {
      const request = {
        accountNumber,
        amount: data.amount,
        description: data.description || undefined,
      }

      if (activeTab === 'deposit') {
        await deposit(request)
        setSuccessMessage(`Successfully deposited $${data.amount.toFixed(2)}`)
      } else {
        await withdraw(request)
        setSuccessMessage(`Successfully withdrew $${data.amount.toFixed(2)}`)
      }

      // Refresh data
      await fetchData()
      reset()
    } catch (err: unknown) {
      const errorMessage = activeTab === 'deposit' ? 'Deposit failed' : 'Withdrawal failed'
      if (typeof err === 'object' && err !== null && 'response' in err) {
        const axiosError = err as { response?: { data?: { message?: string } } }
        setError(axiosError.response?.data?.message || errorMessage)
      } else {
        setError(errorMessage)
      }
    } finally {
      setProcessing(false)
    }
  }

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

  if (!account) {
    return (
      <div className="card text-center py-12">
        <p className="text-gray-500">Account not found</p>
        <Link to="/dashboard" className="btn-primary mt-4 inline-block">
          Back to Dashboard
        </Link>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      {/* Back Navigation */}
      <Link
        to="/dashboard"
        className="inline-flex items-center text-primary-600 hover:text-primary-700"
      >
        <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
        </svg>
        Back to Dashboard
      </Link>

      {/* Account Summary Card */}
      <div className="card">
        <div className="flex items-start justify-between">
          <div>
            <span className={`text-xs font-semibold px-2 py-1 rounded ${
              account.accountType === 'SAVINGS' ? 'bg-green-100 text-green-800' :
              account.accountType === 'CHECKING' ? 'bg-blue-100 text-blue-800' :
              'bg-purple-100 text-purple-800'
            }`}>
              {account.accountType}
            </span>
            <p className="text-gray-500 text-sm mt-2">{account.accountNumber}</p>
          </div>
        </div>
        <div className="mt-6">
          <p className="text-gray-500 text-sm">Current Balance</p>
          <p className="text-4xl font-bold text-gray-900">{formatCurrency(account.balance)}</p>
        </div>
      </div>

      <div className="grid lg:grid-cols-2 gap-8">
        {/* Transaction Form */}
        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-6">Quick Actions</h2>

          {/* Tabs */}
          <div className="flex space-x-2 mb-6">
            <button
              onClick={() => { setActiveTab('deposit'); setError(null); setSuccessMessage(null); }}
              className={`flex-1 py-2 px-4 rounded-lg font-medium transition-colors ${
                activeTab === 'deposit'
                  ? 'bg-primary-500 text-white'
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
              }`}
            >
              Deposit
            </button>
            <button
              onClick={() => { setActiveTab('withdraw'); setError(null); setSuccessMessage(null); }}
              className={`flex-1 py-2 px-4 rounded-lg font-medium transition-colors ${
                activeTab === 'withdraw'
                  ? 'bg-primary-500 text-white'
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
              }`}
            >
              Withdraw
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-4">
              {error}
            </div>
          )}

          {successMessage && (
            <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg mb-4">
              {successMessage}
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label htmlFor="amount" className="form-label">
                Amount
              </label>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                <input
                  id="amount"
                  type="number"
                  step="0.01"
                  min="0.01"
                  className="input-field pl-8"
                  placeholder="0.00"
                  {...register('amount', {
                    required: 'Amount is required',
                    min: {
                      value: 0.01,
                      message: 'Amount must be greater than 0',
                    },
                    valueAsNumber: true,
                  })}
                />
              </div>
              {errors.amount && (
                <p className="error-message">{errors.amount.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="description" className="form-label">
                Description (optional)
              </label>
              <input
                id="description"
                type="text"
                className="input-field"
                placeholder="Add a note..."
                {...register('description')}
              />
            </div>

            <button
              type="submit"
              disabled={processing}
              className={`w-full ${activeTab === 'deposit' ? 'btn-secondary' : 'btn-primary'}`}
            >
              {processing ? (
                <span className="flex items-center justify-center">
                  <svg className="animate-spin -ml-1 mr-3 h-5 w-5" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Processing...
                </span>
              ) : (
                activeTab === 'deposit' ? 'Deposit Funds' : 'Withdraw Funds'
              )}
            </button>
          </form>

          <div className="mt-4 pt-4 border-t">
            <Link
              to="/transfer"
              className="btn-outline w-full text-center block"
            >
              Transfer to Another Account
            </Link>
          </div>
        </div>

        {/* Transaction History */}
        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-6">Recent Transactions</h2>
          <TransactionList
            transactions={transactions}
            currentAccountNumber={accountNumber!}
          />
        </div>
      </div>
    </div>
  )
}
