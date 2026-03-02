<template>
  <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="flex flex-col lg:flex-row gap-10">
      <!-- Main Content -->
      <div class="flex-1">
        <!-- Hero Section -->
        <div class="mb-10 animate-fade-in">
          <h1 class="font-display text-4xl md:text-5xl font-bold text-ink-950 mb-4 tracking-tight">
            探索与分享
          </h1>
          <p class="text-lg text-ink-500 max-w-xl">
            记录技术成长，分享知识心得
          </p>
        </div>

        <!-- Category Tabs -->
        <div class="mb-8 overflow-x-auto scrollbar-hide">
          <div class="flex gap-2 pb-2">
            <button
              @click="handleCategoryChange(undefined)"
              :class="[
                'px-4 py-2 rounded-full text-sm font-medium transition-all duration-200 whitespace-nowrap',
                !activeCategoryId
                  ? 'bg-ink-900 text-cream-100 shadow-soft'
                  : 'bg-cream-200 text-ink-600 hover:bg-cream-300'
              ]"
            >
              全部
            </button>
            <button
              v-for="cat in categories"
              :key="cat.id"
              @click="handleCategoryChange(cat.id)"
              :class="[
                'px-4 py-2 rounded-full text-sm font-medium transition-all duration-200 whitespace-nowrap',
                activeCategoryId === cat.id
                  ? 'bg-ink-900 text-cream-100 shadow-soft'
                  : 'bg-cream-200 text-ink-600 hover:bg-cream-300'
              ]"
            >
              {{ cat.name }}
            </button>
          </div>
        </div>

        <!-- Article List -->
        <div v-if="articles.length > 0" class="space-y-6">
          <article
            v-for="(article, index) in articles"
            :key="article.id"
            class="article-card group animate-slide-up"
            :style="{ animationDelay: `${index * 0.05}s` }"
          >
            <router-link :to="`/portal/article/${article.id}`" class="block">
              <div class="flex gap-5">
                <!-- Cover -->
                <div v-if="article.coverImage" class="hidden sm:block flex-shrink-0">
                  <img
                    :src="article.coverImage"
                    :alt="article.title"
                    class="w-32 h-24 object-cover rounded-xl ring-1 ring-cream-300"
                  />
                </div>
                <!-- Content -->
                <div class="flex-1 min-w-0">
                  <h2 class="article-card-title font-display">{{ article.title }}</h2>
                  <p v-if="article.summary" class="article-card-excerpt mt-2">
                    {{ article.summary }}
                  </p>
                  <div class="article-card-meta">
                    <span v-if="article.categoryName" class="badge badge-amber">{{ article.categoryName }}</span>
                    <span>{{ formatDate(article.publishedTime || article.publishTime) }}</span>
                    <span class="flex items-center gap-1">
                      <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                      </svg>
                      {{ article.viewCount }}
                    </span>
                  </div>
                </div>
              </div>
            </router-link>
          </article>
        </div>

        <!-- Empty State -->
        <div v-else class="text-center py-20">
          <div class="w-16 h-16 bg-cream-200 rounded-2xl flex items-center justify-center mx-auto mb-4">
            <svg class="w-8 h-8 text-ink-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
          <p class="text-ink-500">暂无文章</p>
        </div>

        <!-- Pagination -->
        <Pagination
          v-if="total > pageSize"
          :current="currentPage"
          :total="total"
          :page-size="pageSize"
          @change="handlePageChange"
        />
      </div>

      <!-- Sidebar -->
      <aside class="lg:w-80 space-y-8">
        <!-- Hot Articles -->
        <div class="card p-6">
          <h3 class="font-display text-lg font-semibold text-ink-900 mb-5 flex items-center gap-2">
            <span class="w-1.5 h-5 bg-amber-400 rounded-full"></span>
            热门文章
          </h3>
          <div class="space-y-4">
            <router-link
              v-for="(article, index) in hotArticles.slice(0, 5)"
              :key="article.id"
              :to="`/portal/article/${article.id}`"
              class="flex items-start gap-3 group"
            >
              <span
                class="flex-shrink-0 w-6 h-6 rounded-lg flex items-center justify-center text-xs font-bold"
                :class="index < 3 ? 'bg-amber-100 text-amber-600' : 'bg-cream-200 text-ink-500'"
              >
                {{ index + 1 }}
              </span>
              <span class="text-sm text-ink-700 group-hover:text-amber-600 transition-colors line-clamp-2">
                {{ article.title }}
              </span>
            </router-link>
          </div>
        </div>

        <!-- Tags -->
        <div class="card p-6">
          <h3 class="font-display text-lg font-semibold text-ink-900 mb-5 flex items-center gap-2">
            <span class="w-1.5 h-5 bg-coral-400 rounded-full"></span>
            标签云
          </h3>
          <TagCloud :tags="tags" />
        </div>

        <!-- Stats -->
        <div class="card-flat p-6">
          <div class="grid grid-cols-2 gap-4 text-center">
            <div>
              <p class="text-2xl font-display font-bold text-ink-900">{{ siteStats.articleCount }}</p>
              <p class="text-xs text-ink-500 mt-1">篇文章</p>
            </div>
            <div>
              <p class="text-2xl font-display font-bold text-ink-900">{{ siteStats.viewCount }}</p>
              <p class="text-xs text-ink-500 mt-1">次阅读</p>
            </div>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleList, getHotArticles, getCategoryList, getTagList } from '@/api/portal'
import type { Article, Category, Tag } from '@/types'
import TagCloud from '@/components/portal/TagCloud.vue'
import Pagination from '@/components/common/Pagination.vue'

const route = useRoute()

const articles = ref<Article[]>([])
const hotArticles = ref<Article[]>([])
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const siteStats = ref({
  articleCount: 0,
  userCount: 0,
  viewCount: 0,
})

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const activeCategoryId = ref<number | undefined>()

const formatDate = (date: string | undefined) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}

const fetchArticles = async () => {
  try {
    const res = await getArticleList({
      page: currentPage.value,
      size: pageSize.value,
      categoryId: activeCategoryId.value,
    })
    articles.value = res.data.records || res.data.list || res.data
    total.value = res.data.total || articles.value.length
    siteStats.value.articleCount = total.value
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  }
}

const fetchHotArticles = async () => {
  try {
    const res = await getHotArticles(10)
    hotArticles.value = res.data
  } catch (error) {
    console.error('Failed to fetch hot articles:', error)
  }
}

const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const fetchTags = async () => {
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
}

const handleCategoryChange = (categoryId: number | undefined) => {
  activeCategoryId.value = categoryId
  currentPage.value = 1
  fetchArticles()
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

watch(() => route.query.categoryId, (newId) => {
  if (newId) {
    activeCategoryId.value = Number(newId)
  }
})

onMounted(() => {
  fetchArticles()
  fetchHotArticles()
  fetchCategories()
  fetchTags()
})
</script>