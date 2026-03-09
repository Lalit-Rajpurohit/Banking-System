import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios'

/**
 * Base Axios instance for API calls.
 *
 * Configured with:
 * - Base URL pointing to backend server
 * - Automatic JWT token attachment via interceptor
 * - JSON content type headers
 */
const api: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

/**
 * Request interceptor to attach JWT token to all requests.
 * Reads token from localStorage and adds it to Authorization header.
 */
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

/**
 * Response interceptor for handling common error scenarios.
 * Redirects to login on 401 Unauthorized responses.
 */
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Clear token and redirect to login
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api
