<template>
  <router-link :to="`/portal/article/${article.id}`" class="card block group">
    <div class="flex flex-col sm:flex-row">
      <!-- Cover Image -->
      <div v-if="article.coverImage" class="sm:w-48 sm:h-32 flex-shrink-0 overflow-hidden rounded-t-xl sm:rounded-l-xl sm:rounded-tr-none">
        <img :src="article.coverImage" :alt="article.title" class="w-full h-48 sm:h-full object-cover group-hover:scale-105 transition-transform duration-300" />
      </div>
      <!-- Content -->
      <div class="flex-1 p-5">
        <div class="flex items-center gap-2 mb-2">
          <span v-if="article.isTop" class="badge badge-coral">置顶</span>
          <span v-if="article.isRecommend" class="badge badge-amber">推荐</span>
        </div>
        <h2 class="font-display text-lg font-semibold text-ink-900 mb-2 line-clamp-1 group-hover:text-amber-600 transition-colors">
          {{ article.title }}
        </h2>
        <p class="text-ink-500 text-sm mb-3 line-clamp-2">{{ article.summary }}</p>
        <div class="flex items-center justify-between text-sm text-ink-400">
          <div class="flex items-center gap-4">
            <span>{{ article.authorName || article.author }}</span>
            <span>{{ formatDate(article.publishedTime) }}</span>
          </div>
          <div class="flex items-center gap-4">
            <span class="flex items-center gap-1">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
              {{ article.viewCount }}
            </span>
            <span class="flex items-center gap-1">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
              </svg>
              {{ article.likeCount }}
            </span>
          </div>
        </div>
        <!-- Tags -->
        <div v-if="article.tags?.length" class="mt-3 flex flex-wrap gap-2">
          <span v-for="tag in article.tags" :key="tag.id" class="tag" :style="{ borderColor: tag.color, color: tag.color }">
            {{ tag.name }}
          </span>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script setup lang="ts">
import type { Article } from '@/types'

defineProps<{ article: Article }>()

const formatDate = (date: string) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}
</script>