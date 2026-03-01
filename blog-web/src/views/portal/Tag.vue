<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <h1 class="text-2xl font-bold text-gray-900 mb-6">
      标签：#{{ tag?.name }}
    </h1>

    <div v-if="articles.length > 0" class="space-y-6">
      <ArticleCard
        v-for="article in articles"
        :key="article.id"
        :article="article"
      />
    </div>
    <div v-else class="text-center py-16 text-gray-500">
      该标签下暂无文章
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
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getArticlesByTag } from '@/api/portal'
import { getTagList } from '@/api/portal'
import ArticleCard from '@/components/portal/ArticleCard.vue'
import Pagination from '@/components/common/Pagination.vue'
import type { Article, Tag } from '@/types'

const route = useRoute()

const articles = ref<Article[]>([])
const tag = ref<Tag | null>(null)
const tags = ref<Tag[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchArticles = async () => {
  const tagId = Number(route.params.id)
  try {
    const res = await getArticlesByTag(tagId, currentPage.value, pageSize.value)
    articles.value = res.data.records || res.data
    total.value = res.data.total || articles.value.length
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  }
}

const fetchTag = async () => {
  const tagId = Number(route.params.id)
  try {
    const res = await getTagList()
    tags.value = res.data
    tag.value = tags.value.find(t => t.id === tagId) || null
  } catch (error) {
    console.error('Failed to fetch tag:', error)
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

watch(() => route.params.id, () => {
  currentPage.value = 1
  fetchArticles()
  fetchTag()
})

onMounted(() => {
  fetchArticles()
  fetchTag()
})
</script>