import api from './api'

/**
 * Account type enumeration.
 */
export type AccountType = 'SAVINGS' | 'CHECKING' | 'BUSINESS'

/**
 * Account information returned from API.
 */
export interface Account {
  id: number
  accountNumber: string
  balance: number
  accountType: AccountType
  userId: number
  ownerName: string
  createdAt: string
}

/**
 * Request payload for creating a new account.
 */
export interface CreateAccountRequest {
  accountType: AccountType
}

/**
 * Creates a new bank account for the authenticated user.
 *
 * @param accountType Type of account to create
 * @returns Created account details
 */
export async function createAccount(accountType: AccountType): Promise<Account> {
  const response = await api.post<Account>('/accounts', { accountType })
  return response.data
}

/**
 * Retrieves all accounts for the authenticated user.
 *
 * @returns List of user's accounts
 */
export async function getMyAccounts(): Promise<Account[]> {
  const response = await api.get<Account[]>('/accounts')
  return response.data
}

/**
 * Retrieves details for a specific account.
 *
 * @param accountNumber The account number to look up
 * @returns Account details
 */
export async function getAccount(accountNumber: string): Promise<Account> {
  const response = await api.get<Account>(`/accounts/${accountNumber}`)
  return response.data
}
