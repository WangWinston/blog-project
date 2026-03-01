<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold text-gray-900 mb-6">评论管理</h1>

    <!-- Tabs -->
    <div class="flex gap-4 mb-6">
      <button
        @click="status = 'PENDING'"
        :class="['px-4 py-2 rounded-lg', status === 'PENDING' ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-700']"
      >
        待审核 ({{ pendingCount }})
      </button>
      <button
        @click="status = 'APPROVED'"
        :class="['px-4 py-2 rounded-lg', status === 'APPROVED' ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-700']"
      >
        已通过
      </button>
      <button
        @click="status = 'SPAM'"
        :class="['px-4 py-2 rounded-lg', status === 'SPAM' ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-700']"
      >
        垃圾评论
      </button>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="divide-y divide-gray-200">
        <div v-for="comment in comments" :key="comment.id" class="p-4">
          <div class="flex items-start gap-4">
            <img :src="comment.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + comment.userName" class="w-10 h-10 rounded-full" />
            <div class="flex-1">
              <div class="flex items-center gap-2 mb-1">
                <span class="font-medium">{{ comment.userName }}</span>
                <span class="text-sm text-gray-500">{{ formatDate(comment.createdAt) }}</span>
              </div>
              <p class="text-gray-700 mb-2">{{ comment.content }}</p>
              <div class="text-sm text-gray-500">
                评论文章：
                <router-link :to="`/portal/article/${comment.articleId}`" class="text-primary-600 hover:underline">
                  {{ comment.articleTitle || '查看文章' }}
                </router-link>
              </div>
              <div class="flex gap-3 mt-3">
                <button v-if="status === 'PENDING'" @click="handleApprove(comment)" class="text-green-600 hover:text-green-800">
                  通过
                </button>
                <button v-if="status === 'PENDING'" @click="handleReject(comment)" class="text-yellow-600 hover:text-yellow-800">
                  拒绝
                </button>
                <button @click="handleDelete(comment)" class="text-red-600 hover:text-red-800">
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { getComments, approveComment, rejectComment, deleteComment } from '@/api/admin'
import { formatDate } from '@/utils/format'
import type { Comment } from '@/types'

const comments = ref<Comment[]>([])
const status = ref('PENDING')
const pendingCount = ref(0)

const fetchComments = async () => {
  try {
    const res = await getComments(status.value)
    comments.value = res.data
    if (status.value === 'PENDING') {
      pendingCount.value = comments.value.length
    }
  } catch (error) {
    console.error('Failed to fetch comments:', error)
  }
}

const handleApprove = async (comment: Comment) => {
  try {
    await approveComment(comment.id)
    fetchComments()
  } catch (error) {
    console.error('Failed to approve comment:', error)
  }
}

const handleReject = async (comment: Comment) => {
  try {
    await rejectComment(comment.id)
    fetchComments()
  } catch (error) {
    console.error('Failed to reject comment:', error)
  }
}

const handleDelete = async (comment: Comment) => {
  if (!confirm('确定要删除该评论吗？')) return
  try {
    await deleteComment(comment.id)
    fetchComments()
  } catch (error) {
    console.error('Failed to delete comment:', error)
  }
}

watch(status, () => {
  fetchComments()
})

onMounted(() => {
  fetchComments()
})
</script>