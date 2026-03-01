<template>
  <div class="flex items-center space-x-4 overflow-x-auto pb-2">
    <button
      @click="$emit('change', undefined)"
      :class="[
        'px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors',
        activeId === undefined
          ? 'bg-primary-600 text-white'
          : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
      ]"
    >
      全部
    </button>
    <button
      v-for="category in categories"
      :key="category.id"
      @click="$emit('change', category.id)"
      :class="[
        'px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors',
        activeId === category.id
          ? 'bg-primary-600 text-white'
          : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
      ]"
    >
      {{ category.name }}
      <span class="ml-1 text-xs opacity-70">({{ category.articleCount }})</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import type { Category } from '@/types'

defineProps<{
  categories: Category[]
  activeId?: number
}>()

defineEmits<{
  change: [categoryId: number | undefined]
}>()
</script>