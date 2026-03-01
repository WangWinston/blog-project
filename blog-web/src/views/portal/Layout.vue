<template>
  <div class="min-h-screen flex flex-col bg-cream-100">
    <!-- Decorative background -->
    <div class="fixed inset-0 pointer-events-none overflow-hidden">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-amber-400/5 rounded-full blur-3xl"></div>
      <div class="absolute top-1/3 -left-20 w-60 h-60 bg-coral-400/5 rounded-full blur-3xl"></div>
      <div class="absolute bottom-1/4 right-1/4 w-40 h-40 bg-sage-400/5 rounded-full blur-3xl"></div>
    </div>

    <!-- Header -->
    <header class="sticky top-0 z-50 bg-cream-50/80 backdrop-blur-md border-b border-cream-300/50">
      <div class="max-w-6xl mx-auto px-4 sm:px-6">
        <div class="flex justify-between items-center h-18">
          <!-- Logo -->
          <router-link to="/portal" class="flex items-center gap-3 group">
            <div class="w-10 h-10 bg-gradient-to-br from-amber-400 to-coral-400 rounded-xl flex items-center justify-center shadow-soft group-hover:shadow-glow transition-shadow duration-300">
              <span class="text-white font-bold text-lg font-display">B</span>
            </div>
            <span class="text-xl font-display font-bold text-ink-900 group-hover:text-amber-600 transition-colors">
              Blog
            </span>
          </router-link>

          <!-- Nav -->
          <nav class="hidden md:flex items-center gap-8">
            <router-link to="/portal" class="nav-link" :class="{ 'active': $route.path === '/portal' }">
              首页
            </router-link>
            <router-link to="/portal/search" class="nav-link">
              探索
            </router-link>
          </nav>

          <!-- Actions -->
          <div class="flex items-center gap-3">
            <!-- Search -->
            <div class="relative hidden sm:block">
              <input
                v-model="searchKeyword"
                type="text"
                placeholder="搜索文章..."
                class="input-field w-52 lg:w-64 pl-10 text-sm"
                @keyup.enter="handleSearch"
              />
              <svg class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-ink-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>

            <!-- User -->
            <template v-if="userStore.isLoggedIn">
              <div class="relative" ref="userMenuRef">
                <button
                  @click="showUserMenu = !showUserMenu"
                  class="flex items-center gap-2 px-3 py-2 rounded-xl hover:bg-cream-200 transition-colors"
                >
                  <img
                    :src="userStore.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + userStore.username"
                    alt="avatar"
                    class="w-8 h-8 rounded-lg object-cover ring-2 ring-cream-300"
                  />
                  <span class="hidden sm:inline text-sm font-medium text-ink-700">{{ userStore.username }}</span>
                  <svg class="w-4 h-4 text-ink-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>

                <!-- Dropdown -->
                <Transition
                  enter-active-class="transition ease-out duration-100"
                  enter-from-class="transform opacity-0 scale-95"
                  enter-to-class="transform opacity-100 scale-100"
                  leave-active-class="transition ease-in duration-75"
                  leave-from-class="transform opacity-100 scale-100"
                  leave-to-class="transform opacity-0 scale-95"
                >
                  <div v-show="showUserMenu" class="dropdown">
                    <router-link to="/portal/user" class="dropdown-item" @click="showUserMenu = false">
                      <svg class="w-4 h-4 mr-2 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                      </svg>
                      用户中心
                    </router-link>
                    <router-link
                      v-if="userStore.isAdmin"
                      to="/admin"
                      class="dropdown-item"
                      @click="showUserMenu = false"
                    >
                      <svg class="w-4 h-4 mr-2 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      </svg>
                      后台管理
                    </router-link>
                    <hr class="my-2 border-cream-200" />
                    <button @click="handleLogout" class="dropdown-item text-coral-500 hover:text-coral-600">
                      <svg class="w-4 h-4 mr-2 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                      </svg>
                      退出登录
                    </button>
                  </div>
                </Transition>
              </div>
            </template>
            <template v-else>
              <router-link to="/portal/login" class="btn btn-primary text-sm">
                登录
              </router-link>
            </template>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-1 relative">
      <router-view v-slot="{ Component }">
        <Transition name="page" mode="out-in">
          <component :is="Component" />
        </Transition>
      </router-view>
    </main>

    <!-- Footer -->
    <footer class="bg-ink-950 text-cream-200 mt-16">
      <div class="max-w-6xl mx-auto px-4 sm:px-6 py-12">
        <div class="flex flex-col md:flex-row justify-between items-center gap-6">
          <!-- Logo & Info -->
          <div class="text-center md:text-left">
            <div class="flex items-center justify-center md:justify-start gap-3 mb-3">
              <div class="w-8 h-8 bg-gradient-to-br from-amber-400 to-coral-400 rounded-lg flex items-center justify-center">
                <span class="text-white font-bold font-display">B</span>
              </div>
              <span class="text-lg font-display font-bold text-cream-100">Blog</span>
            </div>
            <p class="text-sm text-ink-400">分享知识，记录成长</p>
          </div>

          <!-- Links -->
          <div class="flex items-center gap-8 text-sm">
            <router-link to="/portal" class="text-ink-400 hover:text-cream-100 transition-colors">首页</router-link>
            <router-link to="/portal/search" class="text-ink-400 hover:text-cream-100 transition-colors">探索</router-link>
            <a href="https://github.com" target="_blank" class="text-ink-400 hover:text-cream-100 transition-colors">GitHub</a>
          </div>

          <!-- Copyright -->
          <div class="text-center md:text-right text-xs text-ink-500">
            <p>© {{ new Date().getFullYear() }} Blog. All rights reserved.</p>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { onClickOutside } from '@vueuse/core'

const router = useRouter()
const userStore = useUserStore()

const searchKeyword = ref('')
const showUserMenu = ref(false)
const userMenuRef = ref<HTMLElement | null>(null)

onClickOutside(userMenuRef, () => {
  showUserMenu.value = false
})

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ name: 'Search', query: { q: searchKeyword.value } })
  }
}

const handleLogout = () => {
  showUserMenu.value = false
  userStore.logout()
}
</script>

<style scoped>
/* Page transitions */
.page-enter-active,
.page-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.page-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>