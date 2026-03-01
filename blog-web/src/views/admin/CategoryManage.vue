<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">分类管理</h1>
      <button @click="showModal = true" class="btn-primary">添加分类</button>
    </div>

    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">分类名称</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Slug</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">文章数</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">排序</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="category in categories" :key="category.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">
              <div class="flex items-center">
                <span v-if="category.parentId" class="mr-2 text-gray-400">└</span>
                {{ category.name }}
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ category.slug }}</td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ category.articleCount }}</td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ category.sortOrder }}</td>
            <td class="px-6 py-4 text-sm">
              <button @click="editCategory(category)" class="text-primary-600 hover:text-primary-800 mr-3">编辑</button>
              <button @click="handleDelete(category)" class="text-red-600 hover:text-red-800">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-md p-6">
        <h3 class="text-lg font-semibold mb-4">{{ editingCategory ? '编辑分类' : '添加分类' }}</h3>
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">名称</label>
            <input v-model="form.name" type="text" required class="input-field w-full" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
            <textarea v-model="form.description" rows="2" class="input-field w-full"></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">父分类</label>
            <select v-model="form.parentId" class="input-field w-full">
              <option :value="null">无（顶级分类）</option>
              <option v-for="cat in rootCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">排序</label>
            <input v-model.number="form.sortOrder" type="number" class="input-field w-full" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { getCategoryTree, createCategory, updateCategory, deleteCategory } from '@/api/admin'
import type { Category } from '@/types'

const categories = ref<Category[]>([])
const showModal = ref(false)
const editingCategory = ref<Category | null>(null)

const form = reactive({
  name: '',
  description: '',
  parentId: null as number | null,
  sortOrder: 0
})

const rootCategories = computed(() => {
  return categories.value.filter(c => !c.parentId)
})

const fetchCategories = async () => {
  try {
    const res = await getCategoryTree()
    categories.value = res.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const editCategory = (category: Category) => {
  editingCategory.value = category
  form.name = category.name
  form.description = category.description || ''
  form.parentId = category.parentId
  form.sortOrder = category.sortOrder || 0
  showModal.value = true
}

const handleSubmit = async () => {
  try {
    if (editingCategory.value) {
      await updateCategory(editingCategory.value.id, form)
    } else {
      await createCategory(form)
    }
    closeModal()
    fetchCategories()
  } catch (error) {
    console.error('Failed to save category:', error)
    alert('保存失败')
  }
}

const handleDelete = async (category: Category) => {
  if (category.articleCount > 0) {
    alert('该分类下有文章，无法删除')
    return
  }
  if (!confirm('确定要删除该分类吗？')) return
  try {
    await deleteCategory(category.id)
    fetchCategories()
  } catch (error) {
    console.error('Failed to delete category:', error)
  }
}

const closeModal = () => {
  showModal.value = false
  editingCategory.value = null
  form.name = ''
  form.description = ''
  form.parentId = null
  form.sortOrder = 0
}

onMounted(() => {
  fetchCategories()
})
</script>