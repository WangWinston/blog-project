<template>
  <div class="card p-6">
    <h3 class="text-lg font-semibold mb-4">网站统计</h3>
    <div class="grid grid-cols-2 gap-4">
      <div class="text-center">
        <div class="text-2xl font-bold text-primary-600">{{ stats.articleCount }}</div>
        <div class="text-sm text-gray-500">文章数</div>
      </div>
      <div class="text-center">
        <div class="text-2xl font-bold text-primary-600">{{ stats.userCount }}</div>
        <div class="text-sm text-gray-500">用户数</div>
      </div>
      <div class="text-center">
        <div class="text-2xl font-bold text-primary-600">{{ formatNumber(stats.viewCount) }}</div>
        <div class="text-sm text-gray-500">浏览量</div>
      </div>
      <div class="text-center">
        <div class="text-2xl font-bold text-primary-600">{{ stats.commentCount || 0 }}</div>
        <div class="text-sm text-gray-500">评论数</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { formatNumber } from '@/utils/format'

defineProps<{
  stats: {
    articleCount: number
    userCount: number
    viewCount: number
    commentCount?: number
  }
}>()
</script>