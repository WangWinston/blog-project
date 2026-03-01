import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types'
import { getCurrentUser, logout as logoutApi } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<User | null>(null)

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')
  const username = computed(() => userInfo.value?.username || '')
  const avatar = computed(() => userInfo.value?.avatar || '')
  const user = computed(() => userInfo.value)

  // Actions
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (user: User) => {
    userInfo.value = user
    localStorage.setItem('userRole', user.role)
    localStorage.setItem('userInfo', JSON.stringify(user))
  }

  // Alias for setUserInfo
  const setUser = (user: User) => {
    setUserInfo(user)
  }

  const updateUser = (user: Partial<User>) => {
    if (userInfo.value) {
      userInfo.value = { ...userInfo.value, ...user }
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    }
  }

  const fetchUserInfo = async () => {
    if (!token.value) return
    try {
      const res = await getCurrentUser()
      setUserInfo(res as unknown as User)
    } catch (error) {
      console.error('Failed to fetch user info:', error)
      clearAuth()
    }
  }

  const clearAuth = () => {
    token.value = null
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userRole')
    localStorage.removeItem('userInfo')
  }

  const logout = async () => {
    try {
      await logoutApi()
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      clearAuth()
      router.push({ name: 'Home' })
    }
  }

  // Initialize from localStorage
  const initAuth = () => {
    const savedUserInfo = localStorage.getItem('userInfo')
    if (savedUserInfo) {
      try {
        userInfo.value = JSON.parse(savedUserInfo)
      } catch (error) {
        console.error('Failed to parse user info:', error)
      }
    }
  }

  // Initialize on store creation
  initAuth()

  return {
    token,
    userInfo,
    user,
    isLoggedIn,
    isAdmin,
    username,
    avatar,
    setToken,
    setUserInfo,
    setUser,
    updateUser,
    fetchUserInfo,
    clearAuth,
    logout,
  }
})