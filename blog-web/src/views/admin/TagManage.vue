<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">标签管理</h1>
      <button @click="showModal = true" class="btn-primary">添加标签</button>
    </div>

    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">标签</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Slug</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">文章数</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="tag in tags" :key="tag.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">
              <span class="px-3 py-1 rounded-full text-sm" :style="{ backgroundColor: tag.color + '20', color: tag.color }">
                {{ tag.name }}
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ tag.slug }}</td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ tag.articleCount }}</td>
            <td class="px-6 py-4 text-sm">
              <button @click="editTag(tag)" class="text-primary-600 hover:text-primary-800 mr-3">编辑</button>
              <button @click="handleDelete(tag)" class="text-red-600 hover:text-red-800">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-md p-6">
        <h3 class="text-lg font-semibold mb-4">{{ editingTag ? '编辑标签' : '添加标签' }}</h3>
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">名称</label>
            <input v-model="form.name" type="text" required class="input-field w-full" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
            <div class="flex gap-2">
              <input v-model="form.color" type="color" class="w-12 h-10 rounded cursor-pointer" />
              <input v-model="form.color" type="text" class="input-field flex-1" placeholder="#3B82F6" />
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
            <textarea v-model="form.description" rows="2" class="input-field w-full"></textarea>
          </div>
          <div class="flex justify-end gap-3 pt-4">
            <button type="button" @click="closeModal" class="btn-secondary">取消</button>
            <button type="submit" class="btn-primary">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getTagList, createTag, updateTag, deleteTag } from '@/api/admin'
import type { Tag } from '@/types'

const tags = ref<Tag[]>([])
const showModal = ref(false)
const editingTag = ref<Tag | null>(null)

const form = reactive({
  name: '',
  color: '#3B82F6',
  description: ''
})

const fetchTags = async () => {
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
}

const editTag = (tag: Tag) => {
  editingTag.value = tag
  form.name = tag.name
  form.color = tag.color || '#3B82F6'
  form.description = tag.description || ''
  showModal.value = true
}

const handleSubmit = async () => {
  try {
    if (editingTag.value) {
      await updateTag(editingTag.value.id, form)
    } else {
      await createTag(form)
    }
    closeModal()
    fetchTags()
  } catch (error) {
    console.error('Failed to save tag:', error)
    alert('保存失败')
  }
}

const handleDelete = async (tag: Tag) => {
  if (!confirm('确定要删除该标签吗？')) return
  try {
    await deleteTag(tag.id)
    fetchTags()
  } catch (error) {
    console.error('Failed to delete tag:', error)
  }
}

const closeModal = () => {
  showModal.value = false
  editingTag.value = null
  form.name = ''
  form.color = '#3B82F6'
  form.description = ''
}

onMounted(() => {
  fetchTags()
})
</script>