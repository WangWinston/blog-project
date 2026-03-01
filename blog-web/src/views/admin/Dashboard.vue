<template>
  <div>
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
      <div class="card p-5 group hover:shadow-soft-lg">
        <div class="flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-amber-100 flex items-center justify-center text-amber-600 group-hover:scale-110 transition-transform">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-ink-500">文章总数</p>
            <p class="text-2xl font-display font-bold text-ink-900">{{ stats.totalArticles }}</p>
          </div>
        </div>
      </div>

      <div class="card p-5 group hover:shadow-soft-lg">
        <div class="flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-sage-100 flex items-center justify-center text-sage-600 group-hover:scale-110 transition-transform">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-ink-500">用户数量</p>
            <p class="text-2xl font-display font-bold text-ink-900">{{ stats.totalUsers }}</p>
          </div>
        </div>
      </div>

      <div class="card p-5 group hover:shadow-soft-lg">
        <div class="flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-coral-100 flex items-center justify-center text-coral-600 group-hover:scale-110 transition-transform">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-ink-500">评论数量</p>
            <p class="text-2xl font-display font-bold text-ink-900">{{ stats.totalComments }}</p>
          </div>
        </div>
      </div>

      <div class="card p-5 group hover:shadow-soft-lg">
        <div class="flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-ink-100 flex items-center justify-center text-ink-600 group-hover:scale-110 transition-transform">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-ink-500">总浏览量</p>
            <p class="text-2xl font-display font-bold text-ink-900">{{ stats.totalViews }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Alert -->
    <div v-if="stats.pendingComments > 0" class="bg-amber-50 border border-amber-200 rounded-2xl p-4 mb-8 flex items-center justify-between">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-amber-100 flex items-center justify-center">
          <svg class="w-5 h-5 text-amber-600" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
          </svg>
        </div>
        <span class="text-amber-800">有 {{ stats.pendingComments }} 条评论待审核</span>
      </div>
      <router-link to="/admin/comments" class="btn btn-primary text-sm py-2">去审核</router-link>
    </div>

    <!-- Two Columns -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
      <!-- Hot Articles -->
      <div class="card">
        <div class="p-5 border-b border-cream-200 flex items-center justify-between">
          <h2 class="font-display font-semibold text-ink-900">热门文章</h2>
          <router-link to="/admin/articles" class="text-sm text-amber-600 hover:text-amber-700">查看全部</router-link>
        </div>
        <div class="p-5">
          <div v-for="(article, index) in hotArticles" :key="article.id" class="flex items-center gap-4 py-3 border-b border-cream-100 last:border-0">
            <span class="w-8 h-8 rounded-lg flex items-center justify-center text-sm font-bold"
              :class="index < 3 ? 'bg-amber-100 text-amber-600' : 'bg-cream-100 text-ink-500'">
              {{ index + 1 }}
            </span>
            <div class="flex-1 min-w-0">
              <router-link :to="`/admin/articles/edit/${article.id}`" class="text-ink-800 hover:text-amber-600 text-sm font-medium line-clamp-1">
                {{ article.title }}
              </router-link>
              <p class="text-xs text-ink-400 mt-0.5">{{ article.viewCount }} 阅读</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Recent Comments -->
      <div class="card">
        <div class="p-5 border-b border-cream-200 flex items-center justify-between">
          <h2 class="font-display font-semibold text-ink-900">最新评论</h2>
          <router-link to="/admin/comments" class="text-sm text-amber-600 hover:text-amber-700">查看全部</router-link>
        </div>
        <div class="p-5">
          <div v-for="comment in recentComments" :key="comment.id" class="flex items-start gap-3 py-3 border-b border-cream-100 last:border-0">
            <img :src="comment.userAvatar || '/default-avatar.png'" class="w-8 h-8 rounded-lg object-cover" />
            <div class="flex-1 min-w-0">
              <p class="text-sm"><span class="font-medium text-ink-800">{{ comment.userName }}</span> <span class="text-ink-400">评论了</span></p>
              <p class="text-sm text-ink-600 line-clamp-1 mt-1">{{ comment.content }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="flex flex-wrap gap-3">
      <router-link to="/admin/articles/edit" class="btn btn-primary">写文章</router-link>
      <router-link to="/admin/categories" class="btn btn-secondary">管理分类</router-link>
      <router-link to="/admin/tags" class="btn btn-secondary">管理标签</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getDashboardStats, getHotArticles, getRecentComments } from '@/api/admin'
import type { DashboardStats, Article, Comment } from '@/types'

const stats = ref<DashboardStats>({ articleCount: 0, publishedCount: 0, draftCount: 0, userCount: 0, commentCount: 0, pendingCommentCount: 0, todayViewCount: 0, totalViewCount: 0, hotArticles: [], recentComments: [], viewTrend: [] })
const hotArticles = ref<Article[]>([])
const recentComments = ref<Comment[]>([])

onMounted(async () => {
  try {
    const [statsRes, hotRes, commentsRes] = await Promise.all([getDashboardStats(), getHotArticles(10), getRecentComments(5)])
    stats.value = statsRes.data
    hotArticles.value = hotRes.data
    recentComments.value = commentsRes.data
  } catch (e) { console.error(e) }
})
</script>