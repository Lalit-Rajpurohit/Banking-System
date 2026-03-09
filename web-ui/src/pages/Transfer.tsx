import { useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { Account, getMyAccounts } from '../api/accounts'
import { transfer } from '../api/transactions'

/**
 * Form data for transfer operation.
 */
interface TransferForm {
  fromAccountNumber: string
  toAccountNumber: string
  amount: number
  description: string
}

/**
 * Transfer page component.
 *
 * Allows users to transfer funds between accounts.
 */
export default function Transfer() {
  const navigate = useNavigate()
  const [accounts, setAccounts] = useState<Account[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [processing, setProcessing] = useState(false)
  const [success, setSuccess] = useState(false)

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<TransferForm>()

  const fromAccount = watch('fromAccountNumber')

  /**
   * Fetches user's accounts on mount.
   */
  useEffect(() => {
    fetchAccounts()
  }, [])

  const fetchAccounts = async () => {
    try {
      setLoading(true)
      const data = await getMyAccounts()
      setAccounts(data)
    } catch (err) {
      setError('Failed to load accounts')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  /**
   * Handles transfer form submission.
   */
  const onSubmit = async (data: TransferForm) => {
    setProcessing(true)
    setError(null)

    try {
      await transfer({
        fromAccountNumber: data.fromAccountNumber,
        toAccountNumber: data.toAccountNumber,
        amount: data.amount,
        description: data.description || undefined,
      })
      setSuccess(true)
    } catch (err: unknown) {
      if (typeof err === 'object' && err !== null && 'response' in err) {
        const axiosError = err as { response?: { data?: { message?: string } } }
        setError(axiosError.response?.data?.message || 'Transfer failed')
      } else {
        setError('Transfer failed')
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

  /**
   * Gets selected source account details.
   */
  const selectedFromAccount = accounts.find((a) => a.accountNumber === fromAccount)

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-500"></div>
      </div>
    )
  }

  if (success) {
    return (
      <div className="max-w-md mx-auto">
        <div className="card text-center py-12">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg className="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Transfer Successful!</h2>
          <p className="text-gray-500 mb-6">Your funds have been transferred successfully.</p>
          <div className="space-y-3">
            <button
              onClick={() => navigate('/dashboard')}
              className="btn-primary w-full"
            >
              Back to Dashboard
            </button>
            <button
              onClick={() => {
                setSuccess(false)
                setError(null)
              }}
              className="btn-outline w-full"
            >
              Make Another Transfer
            </button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="max-w-lg mx-auto">
      {/* Back Navigation */}
      <Link
        to="/dashboard"
        className="inline-flex items-center text-primary-600 hover:text-primary-700 mb-6"
      >
        <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
        </svg>
        Back to Dashboard
      </Link>

      <div className="card">
        <div className="text-center mb-8">
          <h1 className="text-2xl font-bold text-gray-900">Transfer Funds</h1>
          <p className="text-gray-500 mt-2">Move money between accounts</p>
        </div>

        {accounts.length < 1 && (
          <div className="text-center py-8">
            <p className="text-gray-500 mb-4">You need at least one account to make a transfer.</p>
            <Link to="/dashboard" className="btn-primary">
              Create an Account
            </Link>
          </div>
        )}

        {accounts.length >= 1 && (
          <>
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              {/* From Account */}
              <div>
                <label htmlFor="fromAccountNumber" className="form-label">
                  From Account
                </label>
                <select
                  id="fromAccountNumber"
                  className="input-field"
                  {...register('fromAccountNumber', {
                    required: 'Please select a source account',
                  })}
                >
                  <option value="">Select account</option>
                  {accounts.map((account) => (
                    <option key={account.accountNumber} value={account.accountNumber}>
                      {account.accountType} - {account.accountNumber} ({formatCurrency(account.balance)})
                    </option>
                  ))}
                </select>
                {errors.fromAccountNumber && (
                  <p className="error-message">{errors.fromAccountNumber.message}</p>
                )}
                {selectedFromAccount && (
                  <p className="text-sm text-gray-500 mt-1">
                    Available balance: {formatCurrency(selectedFromAccount.balance)}
                  </p>
                )}
              </div>

              {/* To Account */}
              <div>
                <label htmlFor="toAccountNumber" className="form-label">
                  To Account Number
                </label>
                <input
                  id="toAccountNumber"
                  type="text"
                  className="input-field"
                  placeholder="ACC-XXXXXXXXXX"
                  {...register('toAccountNumber', {
                    required: 'Destination account is required',
                    validate: (value) =>
                      value !== fromAccount || 'Cannot transfer to the same account',
                  })}
                />
                {errors.toAccountNumber && (
                  <p className="error-message">{errors.toAccountNumber.message}</p>
                )}
                <p className="text-sm text-gray-500 mt-1">
                  Enter the recipient's account number
                </p>
              </div>

              {/* Amount */}
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
                      validate: (value) => {
                        if (selectedFromAccount && value > selectedFromAccount.balance) {
                          return 'Insufficient funds'
                        }
                        return true
                      },
                      valueAsNumber: true,
                    })}
                  />
                </div>
                {errors.amount && (
                  <p className="error-message">{errors.amount.message}</p>
                )}
              </div>

              {/* Description */}
              <div>
                <label htmlFor="description" className="form-label">
                  Description (optional)
                </label>
                <input
                  id="description"
                  type="text"
                  className="input-field"
                  placeholder="What's this transfer for?"
                  {...register('description')}
                />
              </div>

              {/* Submit Button */}
              <button
                type="submit"
                disabled={processing}
                className="btn-primary w-full"
              >
                {processing ? (
                  <span className="flex items-center justify-center">
                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Processing Transfer...
                  </span>
                ) : (
                  'Transfer Funds'
                )}
              </button>
            </form>
          </>
        )}
      </div>
    </div>
  )
}
