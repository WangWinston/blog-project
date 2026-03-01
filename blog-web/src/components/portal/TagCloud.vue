<template>
  <div class="flex flex-wrap gap-2">
    <router-link
      v-for="tag in tags"
      :key="tag.id"
      :to="`/portal/tag/${tag.id}`"
      class="px-3 py-1.5 text-sm rounded-lg transition-all duration-200 hover:scale-105 border border-transparent"
      :style="{ backgroundColor: tag.color + '15', color: tag.color, borderColor: tag.color + '40' }"
    >
      #{{ tag.name }}
    </router-link>
  </div>
</template>

<script setup lang="ts">
import type { Tag } from '@/types'
defineProps<{ tags: Tag[] }>()
</script>