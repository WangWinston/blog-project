import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

/**
 * Format date to string
 */
export const formatDate = (date: string | Date | undefined, format: string = 'YYYY-MM-DD HH:mm:ss'): string => {
  if (!date) return ''
  return dayjs(date).format(format)
}

/**
 * Get relative time
 */
export const fromNow = (date: string | Date | undefined): string => {
  if (!date) return ''
  return dayjs(date).fromNow()
}

/**
 * Format number with suffix
 */
export const formatNumber = (num: number): string => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

/**
 * Decode JWT token payload
 */
export const decodeJwt = (token: string): Record<string, unknown> | null => {
  try {
    const base64Url = token.split('.')[1]
    if (!base64Url) return null
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch {
    return null
  }
}

/**
 * Check if JWT token is expired
 */
export const isTokenExpired = (token: string): boolean => {
  const payload = decodeJwt(token)
  if (!payload || !payload.exp) return true

  // exp is in seconds, Date.now() is in milliseconds
  const expirationTime = (payload.exp as number) * 1000
  // Add 30 seconds buffer to handle clock skew
  return Date.now() >= expirationTime - 30000
}