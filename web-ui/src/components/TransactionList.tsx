import { Transaction } from '../api/transactions'

/**
 * Props for TransactionList component.
 */
interface TransactionListProps {
  transactions: Transaction[]
  currentAccountNumber: string
}

/**
 * Component displaying a list of transactions.
 *
 * Shows transaction type, amount, counterparty, and timestamp.
 * Highlights incoming vs outgoing transactions with different colors.
 */
export default function TransactionList({ transactions, currentAccountNumber }: TransactionListProps) {
  /**
   * Formats amount as currency string.
   */
  const formatAmount = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount)
  }

  /**
   * Formats date string for display.
   */
  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  /**
   * Determines if transaction is incoming (credit) to current account.
   */
  const isIncoming = (tx: Transaction) => {
    return tx.toAccountNumber === currentAccountNumber
  }

  /**
   * Gets display icon based on transaction type and direction.
   */
  const getTransactionIcon = (tx: Transaction) => {
    if (tx.type === 'DEPOSIT') {
      return (
        <div className="p-2 bg-green-100 rounded-full">
          <svg className="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
        </div>
      )
    }
    if (tx.type === 'WITHDRAW') {
      return (
        <div className="p-2 bg-red-100 rounded-full">
          <svg className="w-5 h-5 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 12H4" />
          </svg>
        </div>
      )
    }
    // Transfer
    if (isIncoming(tx)) {
      return (
        <div className="p-2 bg-green-100 rounded-full">
          <svg className="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
          </svg>
        </div>
      )
    }
    return (
      <div className="p-2 bg-orange-100 rounded-full">
        <svg className="w-5 h-5 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
        </svg>
      </div>
    )
  }

  /**
   * Gets transaction description text.
   */
  const getDescription = (tx: Transaction) => {
    if (tx.type === 'DEPOSIT') {
      return 'Deposit'
    }
    if (tx.type === 'WITHDRAW') {
      return 'Withdrawal'
    }
    if (isIncoming(tx)) {
      return `From: ${tx.fromAccountNumber}`
    }
    return `To: ${tx.toAccountNumber}`
  }

  if (transactions.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">
        <svg className="w-12 h-12 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
            d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
        <p>No transactions yet</p>
      </div>
    )
  }

  return (
    <div className="space-y-3">
      {transactions.map((tx) => (
        <div
          key={tx.id}
          className="flex items-center justify-between p-4 bg-white rounded-lg border border-gray-100 hover:border-gray-200 transition-colors"
        >
          <div className="flex items-center space-x-4">
            {getTransactionIcon(tx)}
            <div>
              <p className="font-medium text-gray-900">{getDescription(tx)}</p>
              <p className="text-sm text-gray-500">{tx.description}</p>
              <p className="text-xs text-gray-400">{formatDate(tx.createdAt)}</p>
            </div>
          </div>
          <div className="text-right">
            <p className={`font-semibold ${
              tx.type === 'WITHDRAW' || (tx.type === 'TRANSFER' && !isIncoming(tx))
                ? 'text-red-600'
                : 'text-green-600'
            }`}>
              {tx.type === 'WITHDRAW' || (tx.type === 'TRANSFER' && !isIncoming(tx)) ? '-' : '+'}
              {formatAmount(tx.amount)}
            </p>
            <span className={`text-xs px-2 py-1 rounded ${
              tx.status === 'SUCCESS'
                ? 'bg-green-100 text-green-700'
                : tx.status === 'FAILED'
                ? 'bg-red-100 text-red-700'
                : 'bg-yellow-100 text-yellow-700'
            }`}>
              {tx.status}
            </span>
          </div>
        </div>
      ))}
    </div>
  )
}
