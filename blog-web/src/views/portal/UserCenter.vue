<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <h1 class="text-2xl font-bold text-gray-900 mb-8">用户中心</h1>

    <!-- User Info Card -->
    <div class="card p-6 mb-8">
      <div class="flex items-center gap-4">
        <img :src="userInfo?.avatar || '/default-avatar.png'" class="w-20 h-20 rounded-full" />
        <div>
          <h2 class="text-xl font-semibold">{{ userInfo?.nickname || userInfo?.username }}</h2>
          <p class="text-gray-500">@{{ userInfo?.username }}</p>
        </div>
      </div>
    </div>

    <!-- Tabs -->
    <div class="border-b border-gray-200 mb-6">
      <nav class="flex gap-8">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          @click="activeTab = tab.key"
          :class="[
            'pb-4 border-b-2 font-medium transition-colors',
            activeTab === tab.key
              ? 'border-primary-500 text-primary-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          ]"
        >
          {{ tab.label }}
        </button>
      </nav>
    </div>

    <!-- Favorites Tab -->
    <div v-if="activeTab === 'favorites'">
      <div v-if="favorites.length > 0" class="space-y-6">
        <ArticleCard
          v-for="article in favorites"
          :key="article.id"
          :article="article"
        />
      </div>
      <div v-else class="text-center py-16 text-gray-500">
        暂无收藏的文章
      </div>
    </div>

    <!-- Likes Tab -->
    <div v-if="activeTab === 'likes'">
      <div v-if="likes.length > 0" class="space-y-6">
        <ArticleCard
          v-for="article in likes"
          :key="article.id"
          :article="article"
        />
      </div>
      <div v-else class="text-center py-16 text-gray-500">
        暂无点赞的文章
      </div>
    </div>

    <!-- Profile Tab -->
    <div v-if="activeTab === 'profile'" class="max-w-lg">
      <form @submit.prevent="handleUpdateProfile" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">昵称</label>
          <input
            v-model="profileForm.nickname"
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
          <input
            v-model="profileForm.email"
            type="email"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>
        <button
          type="submit"
          :disabled="loading"
          class="px-6 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 disabled:opacity-50"
        >
          {{ loading ? '保存中...' : '保存' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { getUserFavorites, getUserLikes, updateProfile } from '@/api/portal'
import ArticleCard from '@/components/portal/ArticleCard.vue'
import type { Article, User } from '@/types'

const userStore = useUserStore()
const userInfo = computed<User | null>(() => userStore.userInfo)

const tabs = [
  { key: 'favorites', label: '我的收藏' },
  { key: 'likes', label: '我的点赞' },
  { key: 'profile', label: '个人资料' }
]

const activeTab = ref('favorites')
const favorites = ref<Article[]>([])
const likes = ref<Article[]>([])
const loading = ref(false)

const profileForm = reactive({
  nickname: '',
  email: ''
})

const fetchFavorites = async () => {
  try {
    const res = await getUserFavorites()
    favorites.value = res.data || []
  } catch (error) {
    console.error('Failed to fetch favorites:', error)
  }
}

const fetchLikes = async () => {
  try {
    const res = await getUserLikes()
    likes.value = res.data || []
  } catch (error) {
    console.error('Failed to fetch likes:', error)
  }
}

const handleUpdateProfile = async () => {
  loading.value = true
  try {
    await updateProfile(profileForm)
    userStore.updateUser(profileForm)
    alert('保存成功')
  } catch (error) {
    console.error('Failed to update profile:', error)
    alert('保存失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchFavorites()
  fetchLikes()

  if (userInfo.value) {
    profileForm.nickname = userInfo.value.nickname || ''
    profileForm.email = userInfo.value.email || ''
  }
})
</script>