<template>
  <div class="card overflow-hidden">
    <div class="relative h-64 sm:h-80">
      <transition-group name="fade">
        <div
          v-for="(article, index) in visibleArticles"
          :key="article.id"
          v-show="index === currentIndex"
          class="absolute inset-0"
        >
          <img
            :src="article.coverImage || '/default-cover.jpg'"
            :alt="article.title"
            class="w-full h-full object-cover"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent" />
          <div class="absolute bottom-0 left-0 right-0 p-6 text-white">
            <router-link :to="`/portal/article/${article.id}`" class="block">
              <h3 class="text-xl sm:text-2xl font-bold mb-2 hover:text-primary-300">
                {{ article.title }}
              </h3>
              <p class="text-sm opacity-80 line-clamp-2">{{ article.summary }}</p>
            </router-link>
          </div>
        </div>
      </transition-group>
      <!-- Indicators -->
      <div class="absolute bottom-4 right-4 flex space-x-2">
        <button
          v-for="(_, index) in visibleArticles"
          :key="index"
          @click="currentIndex = index"
          :class="[
            'w-2 h-2 rounded-full transition-colors',
            currentIndex === index ? 'bg-white' : 'bg-white/50'
          ]"
        />
      </div>
      <!-- Navigation -->
      <button
        @click="prev"
        class="absolute left-4 top-1/2 -translate-y-1/2 w-10 h-10 bg-black/30 hover:bg-black/50 rounded-full flex items-center justify-center text-white"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
      </button>
      <button
        @click="next"
        class="absolute right-4 top-1/2 -translate-y-1/2 w-10 h-10 bg-black/30 hover:bg-black/50 rounded-full flex items-center justify-center text-white"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import type { Article } from '@/types'

const props = defineProps<{
  articles: Article[]
}>()

const currentIndex = ref(0)
let autoPlayTimer: ReturnType<typeof setInterval> | null = null

const visibleArticles = computed(() => props.articles.slice(0, 5))

const prev = () => {
  currentIndex.value = currentIndex.value === 0
    ? visibleArticles.value.length - 1
    : currentIndex.value - 1
}

const next = () => {
  currentIndex.value = currentIndex.value === visibleArticles.value.length - 1
    ? 0
    : currentIndex.value + 1
}

const startAutoPlay = () => {
  autoPlayTimer = setInterval(next, 5000)
}

const stopAutoPlay = () => {
  if (autoPlayTimer) {
    clearInterval(autoPlayTimer)
    autoPlayTimer = null
  }
}

onMounted(() => {
  if (visibleArticles.value.length > 1) {
    startAutoPlay()
  }
})

onUnmounted(() => {
  stopAutoPlay()
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>