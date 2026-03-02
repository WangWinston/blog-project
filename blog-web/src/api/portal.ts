import { get, post, put } from './request'

// Get article list
export const getArticleList = (params?: {
  page?: number
  size?: number
  categoryId?: number
  tagId?: number
}) => {
  return get<any>('/portal/articles', { params })
}

// Get article detail
export const getArticleDetail = (id: number) => {
  return get<any>(`/portal/articles/${id}`)
}

// Get hot articles
export const getHotArticles = (limit: number = 10) => {
  return get<any>('/portal/articles/hot', { params: { limit } })
}

// Get articles by category
export const getArticlesByCategory = (categoryId: number, page: number = 1, size: number = 10) => {
  return get<any>(`/portal/articles/category/${categoryId}`, { params: { page, size } })
}

// Get articles by tag
export const getArticlesByTag = (tagId: number, page: number = 1, size: number = 10) => {
  return get<any>(`/portal/articles/tag/${tagId}`, { params: { page, size } })
}

// Search articles
export const searchArticles = (keyword: string, page: number = 1, size: number = 10) => {
  return get<any>('/portal/articles/search', { params: { keyword, page, size } })
}

// Get category list
export const getCategoryList = () => {
  return get<any>('/portal/categories')
}

// Get category tree
export const getCategoryTree = () => {
  return get<any>('/portal/categories/tree')
}

// Get tag list
export const getTagList = () => {
  return get<any>('/portal/tags')
}

// Get popular tags
export const getPopularTags = (limit: number = 20) => {
  return get<any>('/portal/tags/popular', { params: { limit } })
}

// Get article comments
export const getArticleComments = (articleId: number) => {
  return get<any>(`/portal/comments/article/${articleId}`)
}

// Create comment
export const createComment = (data: {
  articleId: number
  content: string
  parentId?: number
  replyToUserId?: number
}) => {
  return post<any>('/portal/comments', data)
}

// Get recent comments
export const getRecentComments = (limit: number = 10) => {
  return get<any>('/portal/comments/recent', { params: { limit } })
}

// Toggle like
export const toggleLike = (articleId: number) => {
  return post<boolean>(`/portal/user/articles/${articleId}/like`)
}

// Toggle favorite
export const toggleFavorite = (articleId: number) => {
  return post<boolean>(`/portal/user/articles/${articleId}/favorite`)
}

// Get user favorites
export const getUserFavorites = (page: number = 1, size: number = 10) => {
  return get<any>('/portal/user/favorites', { params: { page, size } })
}

// Get user likes
export const getUserLikes = () => {
  return get<any>('/portal/user/likes')
}

// Get current user
export const getCurrentUser = () => {
  return get<any>('/portal/user/me')
}

// Update profile
export const updateProfile = (data: {
  nickname?: string
  bio?: string
  website?: string
  location?: string
}) => {
  return put<any>('/portal/user/me', data)
}