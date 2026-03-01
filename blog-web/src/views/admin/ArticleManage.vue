<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">文章管理</h1>
      <router-link to="/admin/articles/edit" class="btn-primary">
        写文章
      </router-link>
    </div>

    <!-- Filters -->
    <div class="bg-white rounded-lg shadow p-4 mb-6">
      <div class="flex flex-wrap gap-4">
        <select v-model="filters.status" class="input-field w-40" @change="fetchArticles">
          <option value="">全部状态</option>
          <option value="DRAFT">草稿</option>
          <option value="PUBLISHED">已发布</option>
          <option value="UNPUBLISHED">已下架</option>
          <option value="SCHEDULED">定时发布</option>
        </select>
        <select v-model="filters.categoryId" class="input-field w-40" @change="fetchArticles">
          <option value="">全部分类</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
        <input
          v-model="filters.keyword"
          type="text"
          placeholder="搜索文章..."
          class="input-field flex-1"
          @keyup.enter="fetchArticles"
        />
      </div>
    </div>

    <!-- Articles Table -->
    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">文章</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">分类</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">浏览/点赞</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="article in articles" :key="article.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">
              <div class="flex items-center">
                <img v-if="article.coverImage" :src="article.coverImage" class="w-16 h-12 object-cover rounded mr-3" />
                <div>
                  <router-link :to="`/admin/articles/edit/${article.id}`" class="text-gray-900 font-medium hover:text-primary-600">
                    {{ article.title }}
                  </router-link>
                  <div v-if="article.isTop || article.isRecommend" class="flex gap-2 mt-1">
                    <span v-if="article.isTop" class="text-xs bg-red-100 text-red-600 px-2 py-0.5 rounded">置顶</span>
                    <span v-if="article.isRecommend" class="text-xs bg-blue-100 text-blue-600 px-2 py-0.5 rounded">推荐</span>
                  </div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ article.categoryName || '-' }}</td>
            <td class="px-6 py-4">
              <span :class="getStatusClass(article.status)" class="px-2 py-1 text-xs rounded-full">
                {{ getStatusText(article.status) }}
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">
              {{ article.viewCount }} / {{ article.likeCount }}
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ formatDate(article.createdTime) }}</td>
            <td class="px-6 py-4 text-sm">
              <div class="flex gap-2">
                <router-link :to="`/admin/articles/edit/${article.id}`" class="text-primary-600 hover:text-primary-800">编辑</router-link>
                <button v-if="article.status === 'DRAFT'" @click="handlePublish(article)" class="text-green-600 hover:text-green-800">发布</button>
                <button v-if="article.status === 'PUBLISHED'" @click="handleUnpublish(article)" class="text-yellow-600 hover:text-yellow-800">下架</button>
                <button @click="handleDelete(article)" class="text-red-600 hover:text-red-800">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div v-if="total > pageSize" class="mt-6">
      <Pagination :current="currentPage" :total="total" :page-size="pageSize" @change="handlePageChange" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getArticleList, publishArticle, unpublishArticle, deleteArticle } from '@/api/admin'
import { getCategoryList } from '@/api/portal'
import Pagination from '@/components/common/Pagination.vue'
import { formatDate } from '@/utils/format'
import type { Article, Category } from '@/types'

const articles = ref<Article[]>([])
const categories = ref<Category[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filters = reactive({
  status: '',
  categoryId: '',
  keyword: ''
})

const fetchArticles = async () => {
  try {
    const res = await getArticleList({
      page: currentPage.value,
      size: pageSize.value,
      ...filters
    })
    articles.value = res.data.records || res.data
    total.value = res.data.total || articles.value.length
  } catch (error) {
    console.error('Failed to fetch articles:', error)
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

const handlePublish = async (article: Article) => {
  if (!confirm('确定要发布这篇文章吗？')) return
  try {
    await publishArticle(article.id)
    fetchArticles()
  } catch (error) {
    console.error('Failed to publish article:', error)
  }
}

const handleUnpublish = async (article: Article) => {
  if (!confirm('确定要下架这篇文章吗？')) return
  try {
    await unpublishArticle(article.id)
    fetchArticles()
  } catch (error) {
    console.error('Failed to unpublish article:', error)
  }
}

const handleDelete = async (article: Article) => {
  if (!confirm('确定要删除这篇文章吗？此操作不可恢复！')) return
  try {
    await deleteArticle(article.id)
    fetchArticles()
  } catch (error) {
    console.error('Failed to delete article:', error)
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchArticles()
}

const getStatusClass = (status: string) => {
  const classes: Record<string, string> = {
    'DRAFT': 'bg-gray-100 text-gray-600',
    'PUBLISHED': 'bg-green-100 text-green-600',
    'UNPUBLISHED': 'bg-yellow-100 text-yellow-600',
    'SCHEDULED': 'bg-blue-100 text-blue-600'
  }
  return classes[status] || 'bg-gray-100 text-gray-600'
}

const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    'DRAFT': '草稿',
    'PUBLISHED': '已发布',
    'UNPUBLISHED': '已下架',
    'SCHEDULED': '定时发布'
  }
  return texts[status] || status
}

onMounted(() => {
  fetchArticles()
  fetchCategories()
})
</script>