// API Response Types
export interface Result<T = unknown> {
  code: number
  message: string
  data: T
  timestamp: number
}

export interface PageResult<T = unknown> {
  current: number
  size: number
  total: number
  pages: number
  records: T[]
}

// User Types
export interface User {
  id: number
  username: string
  email: string
  nickname: string
  avatar: string
  role: 'ADMIN' | 'USER'
  status: 'ACTIVE' | 'DISABLED'
  githubId?: string
  createdAt: string
}

export interface UserDTO {
  id: number
  username: string
  email: string
  nickname: string
  avatar: string
  role: 'ADMIN' | 'USER'
  status: 'ACTIVE' | 'DISABLED'
  githubId?: string
  createdAt: string
}

export interface Profile {
  bio: string
  website: string
  location: string
}

export interface LoginRequest {
  username: string
  password: string
  verifyToken?: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  verifyToken?: string
}

export interface LoginResponse {
  accessToken: string
  user: User
}

// Article Types
export interface Article {
  id: number
  title: string
  summary: string
  coverImage: string
  status: 'DRAFT' | 'PUBLISHED' | 'UNPUBLISHED' | 'SCHEDULED'
  isTop: boolean
  isRecommend: boolean
  viewCount: number
  likeCount: number
  commentCount: number
  categoryId: number
  categoryName: string
  tags: Tag[]
  author: string
  authorName?: string
  authorId?: number
  publishedTime: string
  publishTime?: string  // alias for compatibility
  readTime?: number
  content?: string
  isLiked?: boolean
  isFavorited?: boolean
  createdTime: string
  createdAt?: string  // alias for compatibility
  updatedTime: string
  updatedAt?: string  // alias for compatibility
}

export interface ArticleDetail extends Article {
  content: string
}

export interface ArticleVersion {
  id: number
  articleId: number
  version: number
  content: string
  changeLog: string
  createdAt: string
}

// Category Types
export interface Category {
  id: number
  name: string
  slug: string
  parentId: number | null
  children?: Category[]
  articleCount: number
  sortOrder: number
  description?: string
  createdAt: string
}

// Tag Types
export interface Tag {
  id: number
  name: string
  slug: string
  color: string
  articleCount: number
  description?: string
  createdAt: string
}

// Comment Types
export interface Comment {
  id: number
  articleId: number
  userId: number
  userName: string
  userAvatar: string
  parentId: number | null
  content: string
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'DELETED'
  articleTitle?: string
  replyToUserName?: string
  children?: Comment[]
  createdTime: string
  createdAt?: string  // alias for compatibility
}

// Dashboard Types
export interface DashboardStats {
  articleCount: number
  totalArticles?: number
  publishedCount: number
  draftCount: number
  userCount: number
  totalUsers?: number
  commentCount: number
  totalComments?: number
  pendingCommentCount: number
  pendingComments?: number
  todayViewCount: number
  totalViewCount: number
  totalViews?: number
  hotArticles: Article[]
  recentComments: Comment[]
  viewTrend: {
    date: string
    count: number
  }[]
}