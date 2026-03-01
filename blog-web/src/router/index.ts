import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/portal',
  },
  // Portal routes
  {
    path: '/portal',
    component: () => import('@/views/portal/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/portal/Home.vue'),
        meta: { title: '首页' },
      },
      {
        path: 'article/:id',
        name: 'ArticleDetail',
        component: () => import('@/views/portal/ArticleDetail.vue'),
        meta: { title: '文章详情' },
      },
      {
        path: 'category/:id',
        name: 'Category',
        component: () => import('@/views/portal/Category.vue'),
        meta: { title: '分类文章' },
      },
      {
        path: 'tag/:id',
        name: 'Tag',
        component: () => import('@/views/portal/Tag.vue'),
        meta: { title: '标签文章' },
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import('@/views/portal/Search.vue'),
        meta: { title: '搜索结果' },
      },
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/views/portal/Login.vue'),
        meta: { title: '登录', guest: true },
      },
      {
        path: 'user',
        name: 'UserCenter',
        component: () => import('@/views/portal/UserCenter.vue'),
        meta: { title: '用户中心', requiresAuth: true },
      },
    ],
  },
  // Admin routes
  {
    path: '/admin',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '控制台' },
      },
      {
        path: 'login',
        name: 'AdminLogin',
        component: () => import('@/views/admin/Login.vue'),
        meta: { title: '后台登录', guest: true },
      },
      {
        path: 'articles',
        name: 'ArticleManage',
        component: () => import('@/views/admin/ArticleManage.vue'),
        meta: { title: '文章管理' },
      },
      {
        path: 'articles/edit/:id?',
        name: 'ArticleEdit',
        component: () => import('@/views/admin/ArticleEdit.vue'),
        meta: { title: '编辑文章' },
      },
      {
        path: 'categories',
        name: 'CategoryManage',
        component: () => import('@/views/admin/CategoryManage.vue'),
        meta: { title: '分类管理' },
      },
      {
        path: 'tags',
        name: 'TagManage',
        component: () => import('@/views/admin/TagManage.vue'),
        meta: { title: '标签管理' },
      },
      {
        path: 'comments',
        name: 'CommentManage',
        component: () => import('@/views/admin/CommentManage.vue'),
        meta: { title: '评论管理' },
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理' },
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/admin/Settings.vue'),
        meta: { title: '系统设置' },
      },
    ],
  },
  // 404
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面不存在' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
})

// Navigation guard
router.beforeEach((to, from, next) => {
  // Update page title
  const title = to.meta.title as string
  document.title = title ? `${title} - Blog` : 'Blog'

  // Check authentication
  const token = localStorage.getItem('token')
  const userRole = localStorage.getItem('userRole')

  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.meta.requiresAdmin && userRole !== 'ADMIN') {
    next({ name: 'Home' })
  } else if (to.meta.guest && token) {
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router