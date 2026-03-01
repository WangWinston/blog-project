<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-6">
      <div class="relative">
        <input
          v-model="keyword"
          type="text"
          placeholder="搜索文章..."
          class="w-full px-4 py-3 pl-12 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          @keyup.enter="handleSearch"
        />
        <svg class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
    </div>

    <div v-if="hasSearched">
      <h2 class="text-lg text-gray-600 mb-6">
        找到 <span class="font-semibold text-gray-900">{{ total }}</span> 篇关于
        "<span class="font-semibold text-primary-600">{{ searchedKeyword }}</span
        >" 的文章
      </h2>

      <div v-if="articles.length > 0" class="space-y-6">
        <ArticleCard
          v-for="article in articles"
          :key="article.id"
          :article="article"
        />
      </div>
      <div v-else class="text-center py-16 text-gray-500">
        没有找到相关文章
      </div>

      <div v-if="total > pageSize" class="mt-8">
        <Pagination
          :current="currentPage"
          :total="total"
          :page-size="pageSize"
          @change="handlePageChange"
        />
      </div>
    </div>
    <div v-else class="text-center py-16 text-gray-500">
      输入关键词搜索文章
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchArticles } from '@/api/portal'
import ArticleCard from '@/components/portal/ArticleCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import type { Article } from '@/types'

const route = useRoute()
const router = useRouter()

const keyword = ref('')
const searchedKeyword = ref('')
const articles = ref<Article[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const hasSearched = ref(false)

const handleSearch = async () => {
  if (!keyword.value.trim()) return

  currentPage.value = 1
  searchedKeyword.value = keyword.value
  hasSearched.value = true

  router.push({ query: { q: keyword.value } })
  await fetchArticles()
}

const fetchArticles = async () => {
  try {
    const res = await searchArticles(searchedKeyword.value, currentPage.value, pageSize.value)
    articles.value = res.data.records || res.data
    total.value = res.data.total || articles.value.length
  } catch (error) {
    console.error('Failed to search articles:', error)
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

watch(() => route.query.q, (q) => {
  if (q) {
    keyword.value = q as string
    searchedKeyword.value = q as string
    hasSearched.value = true
    fetchArticles()
  }
}, { immediate: true })

onMounted(() => {
  if (route.query.q) {
    keyword.value = route.query.q as string
    searchedKeyword.value = route.query.q as string
    hasSearched.value = true
    fetchArticles()
  }
})
</script>