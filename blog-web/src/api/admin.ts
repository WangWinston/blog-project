import { get, post, put, del } from './request'

// ============ Article APIs ============
export const getArticleList = (params?: {
  page?: number
  size?: number
  status?: string
  categoryId?: number
  keyword?: string
}) => {
  return get<any>('/admin/articles', { params })
}

export const getArticleDetail = (id: number) => {
  return get<any>(`/admin/articles/${id}`)
}

export const createArticle = (data: any) => {
  return post<any>('/admin/articles', data)
}

export const updateArticle = (id: number, data: any) => {
  return put<any>(`/admin/articles/${id}`, data)
}

export const deleteArticle = (id: number) => {
  return del(`/admin/articles/${id}`)
}

export const publishArticle = (id: number) => {
  return post(`/admin/articles/${id}/publish`)
}

export const unpublishArticle = (id: number) => {
  return post(`/admin/articles/${id}/unpublish`)
}

export const scheduleArticle = (id: number, publishTime: string) => {
  return post(`/admin/articles/${id}/schedule`, null, { params: { publishTime } })
}

export const getArticleVersions = (id: number) => {
  return get<any>(`/admin/articles/${id}/versions`)
}

export const rollbackArticle = (id: number, version: number) => {
  return post(`/admin/articles/${id}/rollback`, null, { params: { version } })
}

// ============ Category APIs ============
export const getCategoryTree = () => {
  return get<any>('/admin/categories/tree')
}

export const createCategory = (data: any) => {
  return post<any>('/admin/categories', data)
}

export const updateCategory = (id: number, data: any) => {
  return put<any>(`/admin/categories/${id}`, data)
}

export const deleteCategory = (id: number) => {
  return del(`/admin/categories/${id}`)
}

// ============ Tag APIs ============
export const getTagList = () => {
  return get<any>('/admin/tags')
}

export const createTag = (data: any) => {
  return post<any>('/admin/tags', data)
}

export const updateTag = (id: number, data: any) => {
  return put<any>(`/admin/tags/${id}`, data)
}

export const deleteTag = (id: number) => {
  return del(`/admin/tags/${id}`)
}

// ============ Comment APIs ============
export const getComments = (status: string, page: number = 1, size: number = 20) => {
  return get<any>('/admin/comments', { params: { status, page, size } })
}

export const approveComment = (id: number) => {
  return post(`/admin/comments/${id}/approve`)
}

export const rejectComment = (id: number) => {
  return post(`/admin/comments/${id}/reject`)
}

export const deleteComment = (id: number) => {
  return del(`/admin/comments/${id}`)
}

// ============ User APIs ============
export const getUserList = (page: number = 1, size: number = 20) => {
  return get<any>('/admin/users', { params: { page, size } })
}

export const updateUserStatus = (id: number, status: string) => {
  return put(`/admin/users/${id}/status`, { status })
}

// ============ Dashboard APIs ============
export const getDashboardStats = () => {
  return get<any>('/admin/dashboard/stats')
}

export const getHotArticles = (limit: number = 10) => {
  return get<any>('/admin/dashboard/hot-articles', { params: { limit } })
}

export const getRecentComments = (limit: number = 10) => {
  return get<any>('/admin/dashboard/recent-comments', { params: { limit } })
}