<template>
  <nav class="flex items-center justify-center gap-2">
    <button @click="$emit('change', current - 1)" :disabled="current <= 1" class="btn btn-ghost px-4 py-2 text-sm disabled:opacity-40">
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
      </svg>
    </button>
    <template v-for="page in pages" :key="page">
      <span v-if="page === '...'" class="px-2 text-ink-400">...</span>
      <button v-else @click="handlePageClick(page)" :class="['w-10 h-10 rounded-xl text-sm font-medium transition-all duration-200', page === current ? 'bg-ink-900 text-cream-100 shadow-soft' : 'text-ink-600 hover:bg-cream-200']">
        {{ page }}
      </button>
    </template>
    <button @click="$emit('change', current + 1)" :disabled="current >= totalPages" class="btn btn-ghost px-4 py-2 text-sm disabled:opacity-40">
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
      </svg>
    </button>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ current: number; total: number; pageSize: number }>()
const emit = defineEmits<{ change: [page: number] }>()

const totalPages = computed(() => Math.ceil(props.total / props.pageSize))

const pages = computed(() => {
  const total = totalPages.value
  const current = props.current
  const result: (number | string)[] = []
  if (total <= 7) {
    for (let i = 1; i <= total; i++) result.push(i)
  } else {
    result.push(1)
    if (current > 3) result.push('...')
    const start = Math.max(2, current - 1)
    const end = Math.min(total - 1, current + 1)
    for (let i = start; i <= end; i++) result.push(i)
    if (current < total - 2) result.push('...')
    result.push(total)
  }
  return result
})

const handlePageClick = (page: number | string) => {
  if (typeof page === 'number') {
    emit('change', page)
  }
}
</script>