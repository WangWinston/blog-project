<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold text-gray-900 mb-6">用户管理</h1>

    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">邮箱</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">角色</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">注册时间</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="user in users" :key="user.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">
              <div class="flex items-center">
                <img :src="user.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + user.username" class="w-10 h-10 rounded-full mr-3" />
                <div>
                  <p class="font-medium text-gray-900">{{ user.nickname || user.username }}</p>
                  <p class="text-sm text-gray-500">@{{ user.username }}</p>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ user.email || '-' }}</td>
            <td class="px-6 py-4">
              <span :class="user.role === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-gray-100 text-gray-700'" class="px-2 py-1 text-xs rounded-full">
                {{ user.role === 'ADMIN' ? '管理员' : '用户' }}
              </span>
            </td>
            <td class="px-6 py-4">
              <span :class="user.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'" class="px-2 py-1 text-xs rounded-full">
                {{ user.status === 'ACTIVE' ? '正常' : '禁用' }}
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ formatDate(user.createdAt) }}</td>
            <td class="px-6 py-4 text-sm">
              <button v-if="user.role !== 'ADMIN'" @click="toggleStatus(user)" class="text-primary-600 hover:text-primary-800">
                {{ user.status === 'ACTIVE' ? '禁用' : '启用' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getUserList, updateUserStatus } from '@/api/admin'
import { formatDate } from '@/utils/format'
import type { User } from '@/types'

const users = ref<User[]>([])

const fetchUsers = async () => {
  try {
    const res = await getUserList()
    users.value = res.data
  } catch (error) {
    console.error('Failed to fetch users:', error)
  }
}

const toggleStatus = async (user: User) => {
  const newStatus = user.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE'
  if (!confirm(`确定要${newStatus === 'ACTIVE' ? '启用' : '禁用'}该用户吗？`)) return
  try {
    await updateUserStatus(user.id, newStatus)
    user.status = newStatus
  } catch (error) {
    console.error('Failed to update user status:', error)
  }
}

onMounted(() => {
  fetchUsers()
})
</script>