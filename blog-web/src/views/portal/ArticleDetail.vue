<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <article v-if="article">
      <!-- Article Header -->
      <header class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-4">{{ article.title }}</h1>
        <div class="flex flex-wrap items-center gap-4 text-sm text-gray-500">
          <span class="flex items-center">
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            {{ article.author }}
          </span>
          <span class="flex items-center">
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            {{ formatDate(article.publishTime) }}
          </span>
          <span class="flex items-center">
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
            {{ article.viewCount }} 阅读
          </span>
        </div>
        <!-- Category & Tags -->
        <div class="flex flex-wrap items-center gap-2 mt-4">
          <router-link
            v-if="article.categoryId"
            :to="`/portal/category/${article.categoryId}`"
            class="px-3 py-1 bg-primary-100 text-primary-700 rounded-full text-sm hover:bg-primary-200"
          >
            {{ article.categoryName }}
          </router-link>
          <router-link
            v-for="tag in article.tags"
            :key="tag.id"
            :to="`/portal/tag/${tag.id}`"
            class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-gray-200"
            :style="{ borderColor: tag.color }"
          >
            #{{ tag.name }}
          </router-link>
        </div>
      </header>

      <!-- Cover Image -->
      <img
        v-if="article.coverImage"
        :src="article.coverImage"
        :alt="article.title"
        class="w-full h-64 object-cover rounded-lg mb-8"
      />

      <!-- Article Content -->
      <div class="prose prose-lg max-w-none mb-8" v-html="renderedContent"></div>

      <!-- Article Actions -->
      <div class="flex items-center justify-center gap-4 py-6 border-t border-b border-gray-200">
        <button
          @click="handleLike"
          :class="[
            'flex items-center gap-2 px-6 py-2 rounded-full transition-colors',
            article.isLiked ? 'bg-red-100 text-red-600' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          ]"
        >
          <svg class="w-5 h-5" :fill="article.isLiked ? 'currentColor' : 'none'" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
          </svg>
          <span>{{ article.isLiked ? '已赞' : '点赞' }} ({{ article.likeCount }})</span>
        </button>
        <button
          @click="handleFavorite"
          :class="[
            'flex items-center gap-2 px-6 py-2 rounded-full transition-colors',
            article.isFavorited ? 'bg-yellow-100 text-yellow-600' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          ]"
        >
          <svg class="w-5 h-5" :fill="article.isFavorited ? 'currentColor' : 'none'" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
          </svg>
          <span>{{ article.isFavorited ? '已收藏' : '收藏' }}</span>
        </button>
      </div>

      <!-- Comments Section -->
      <section class="mt-8">
        <h2 class="text-xl font-bold mb-6">评论 ({{ comments.length }})</h2>

        <!-- Comment Form -->
        <div class="mb-6" v-if="isLoggedIn">
          <textarea
            v-model="newComment"
            rows="4"
            class="w-full p-4 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
            placeholder="写下你的评论..."
          ></textarea>
          <div class="flex justify-end mt-2">
            <button
              @click="submitComment"
              :disabled="!newComment.trim()"
              class="px-6 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              发表评论
            </button>
          </div>
        </div>
        <div v-else class="mb-6 p-4 bg-gray-50 rounded-lg text-center">
          <router-link to="/portal/login" class="text-primary-600 hover:underline">登录</router-link>
          后参与评论
        </div>

        <!-- Comment List -->
        <div class="space-y-6">
          <div v-for="comment in comments" :key="comment.id" class="bg-gray-50 rounded-lg p-4">
            <div class="flex items-start gap-3">
              <img :src="comment.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + comment.userName" class="w-10 h-10 rounded-full" />
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-1">
                  <span class="font-medium">{{ comment.userName }}</span>
                  <span class="text-sm text-gray-500">{{ formatDate(comment.createdAt) }}</span>
                </div>
                <p class="text-gray-700">{{ comment.content }}</p>
                <button
                  @click="replyTo(comment)"
                  class="text-sm text-primary-600 hover:underline mt-2"
                >
                  回复
                </button>

                <!-- Nested Comments -->
                <div v-if="comment.children && comment.children.length > 0" class="mt-4 space-y-3 pl-4 border-l-2 border-gray-200">
                  <div v-for="child in comment.children" :key="child.id" class="bg-white p-3 rounded">
                    <div class="flex items-center gap-2 mb-1">
                      <span class="font-medium">{{ child.userName }}</span>
                      <span v-if="child.replyToUserName" class="text-gray-500">
                        回复 <span class="text-primary-600">@{{ child.replyToUserName }}</span>
                      </span>
                      <span class="text-sm text-gray-500">{{ formatDate(child.createdAt) }}</span>
                    </div>
                    <p class="text-gray-700">{{ child.content }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </article>

    <!-- Loading -->
    <div v-else class="text-center py-16">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticleDetail, getArticleComments, createComment, toggleLike, toggleFavorite } from '@/api/portal'
import { useUserStore } from '@/stores/user'
import { formatDate } from '@/utils/format'
import { renderMarkdown } from '@/utils/markdown'
import type { Article, Comment } from '@/types'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const article = ref<Article | null>(null)
const comments = ref<Comment[]>([])
const newComment = ref('')
const replyToComment = ref<Comment | null>(null)

const isLoggedIn = computed(() => userStore.isLoggedIn)
const renderedContent = computed(() => {
  if (!article.value?.content) return ''
  return renderMarkdown(article.value.content)
})

const fetchArticle = async () => {
  try {
    const res = await getArticleDetail(Number(route.params.id))
    article.value = res.data
  } catch (error) {
    console.error('Failed to fetch article:', error)
    router.push('/portal')
  }
}

const fetchComments = async () => {
  try {
    const res = await getArticleComments(Number(route.params.id))
    comments.value = res.data
  } catch (error) {
    console.error('Failed to fetch comments:', error)
  }
}

const handleLike = async () => {
  if (!isLoggedIn.value) {
    router.push('/portal/login')
    return
  }
  if (!article.value) return
  try {
    const res = await toggleLike(article.value.id)
    const isLiked = res as unknown as boolean
    article.value.isLiked = isLiked
    article.value.likeCount += isLiked ? 1 : -1
  } catch (error) {
    console.error('Failed to toggle like:', error)
  }
}

const handleFavorite = async () => {
  if (!isLoggedIn.value) {
    router.push('/portal/login')
    return
  }
  if (!article.value) return
  try {
    const res = await toggleFavorite(article.value.id)
    const isFavorited = res as unknown as boolean
    article.value.isFavorited = isFavorited
  } catch (error) {
    console.error('Failed to toggle favorite:', error)
  }
}

const replyTo = (comment: Comment) => {
  replyToComment.value = comment
  newComment.value = `@${comment.userName} `
}

const submitComment = async () => {
  if (!newComment.value.trim() || !article.value) return

  try {
    const res = await createComment({
      articleId: article.value.id,
      content: newComment.value,
      parentId: replyToComment.value?.id,
      replyToUserId: replyToComment.value?.userId
    })
    comments.value.unshift((res as any).data)
    newComment.value = ''
    replyToComment.value = null
  } catch (error) {
    console.error('Failed to submit comment:', error)
  }
}

onMounted(() => {
  fetchArticle()
  fetchComments()
})
</script>