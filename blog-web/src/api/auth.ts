import { get, post } from './request'
import type { LoginResponse, UserDTO } from '@/types'

// Login
export const login = (data: { username: string; password: string; captchaToken?: string }) => {
  return post<LoginResponse>('/auth/login', data)
}

// Register
export const register = (data: { username: string; email: string; password: string; captchaToken?: string }) => {
  return post<UserDTO>('/auth/register', data)
}

// GitHub OAuth - redirects to GitHub
export const githubLogin = () => {
  window.location.href = '/api/auth/github'
}

// GitHub OAuth callback
export const githubCallback = (code: string) => {
  return get<LoginResponse>('/auth/github/callback', { params: { code } })
}

// Logout
export const logout = () => {
  return post('/auth/logout')
}

// Get current user info
export const getCurrentUser = () => {
  return get<UserDTO>('/portal/user/me')
}

// Update user profile
export const updateProfile = (data: Partial<UserDTO>) => {
  return post<UserDTO>('/portal/user/me', data)
}