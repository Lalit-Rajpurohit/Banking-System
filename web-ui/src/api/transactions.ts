import api from './api'
import { Account } from './accounts'

/**
 * Transaction type enumeration.
 */
export type TransactionType = 'DEPOSIT' | 'WITHDRAW' | 'TRANSFER'

/**
 * Transaction status enumeration.
 */
export type TransactionStatus = 'SUCCESS' | 'FAILED' | 'PENDING'

/**
 * Transaction information returned from API.
 */
export interface Transaction {
  id: number
  fromAccountNumber: string | null
  toAccountNumber: string | null
  amount: number
  type: TransactionType
  status: TransactionStatus
  description: string
  createdAt: string
}

/**
 * Paginated response for transaction history.
 */
export interface TransactionPage {
  content: Transaction[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

/**
 * Request payload for deposit operation.
 */
export interface DepositRequest {
  accountNumber: string
  amount: number
  description?: string
}

/**
 * Request payload for withdrawal operation.
 */
export interface WithdrawRequest {
  accountNumber: string
  amount: number
  description?: string
}

/**
 * Request payload for transfer operation.
 */
export interface TransferRequest {
  fromAccountNumber: string
  toAccountNumber: string
  amount: number
  description?: string
}

/**
 * Deposits funds into an account.
 *
 * @param request Deposit details
 * @returns Updated account information
 */
export async function deposit(request: DepositRequest): Promise<Account> {
  const response = await api.post<Account>('/transactions/deposit', request)
  return response.data
}

/**
 * Withdraws funds from an account.
 *
 * @param request Withdrawal details
 * @returns Updated account information
 */
export async function withdraw(request: WithdrawRequest): Promise<Account> {
  const response = await api.post<Account>('/transactions/withdraw', request)
  return response.data
}

/**
 * Transfers funds between accounts.
 *
 * @param request Transfer details
 * @returns Transaction record
 */
export async function transfer(request: TransferRequest): Promise<Transaction> {
  const response = await api.post<Transaction>('/transactions/transfer', request)
  return response.data
}

/**
 * Retrieves transaction history for an account.
 *
 * @param accountNumber Account to get history for
 * @param page Page number (0-indexed)
 * @param size Page size
 * @returns Paginated transaction history
 */
export async function getTransactionHistory(
  accountNumber: string,
  page: number = 0,
  size: number = 20
): Promise<TransactionPage> {
  const response = await api.get<TransactionPage>(
    `/transactions/history/${accountNumber}`,
    { params: { page, size } }
  )
  return response.data
}
