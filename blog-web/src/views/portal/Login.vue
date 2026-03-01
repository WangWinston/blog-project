<template>
  <div class="min-h-[85vh] flex items-center justify-center px-4 py-12">
    <div class="w-full max-w-md">
      <!-- Header -->
      <div class="text-center mb-10">
        <router-link to="/portal" class="inline-flex items-center gap-3 mb-6">
          <div class="w-12 h-12 bg-gradient-to-br from-amber-400 to-coral-400 rounded-xl flex items-center justify-center shadow-soft">
            <span class="text-white font-bold text-xl font-display">B</span>
          </div>
        </router-link>
        <h1 class="font-display text-3xl font-bold text-ink-950">{{ isLogin ? '欢迎回来' : '创建账号' }}</h1>
        <p class="text-ink-500 mt-2">{{ isLogin ? '登录您的账号继续' : '注册一个新账号' }}</p>
      </div>

      <!-- Card -->
      <div class="card p-8">
        <!-- Tab Switch -->
        <div class="flex bg-cream-100 rounded-xl p-1 mb-8">
          <button @click="isLogin = true" :class="['flex-1 py-2.5 rounded-lg text-sm font-medium transition-all duration-200', isLogin ? 'bg-white text-ink-900 shadow-soft' : 'text-ink-500 hover:text-ink-700']">登录</button>
          <button @click="isLogin = false" :class="['flex-1 py-2.5 rounded-lg text-sm font-medium transition-all duration-200', !isLogin ? 'bg-white text-ink-900 shadow-soft' : 'text-ink-500 hover:text-ink-700']">注册</button>
        </div>

        <!-- Login Form -->
        <form v-if="isLogin" @submit.prevent="handleLogin" class="space-y-5">
          <div>
            <label class="block text-sm font-medium text-ink-700 mb-2">用户名</label>
            <input v-model="loginForm.username" type="text" required class="input-field" placeholder="请输入用户名" />
          </div>
          <div>
            <label class="block text-sm font-medium text-ink-700 mb-2">密码</label>
            <input v-model="loginForm.password" type="password" required class="input-field" placeholder="请输入密码" />
          </div>
          <div class="flex items-center justify-between text-sm">
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="checkbox" v-model="rememberMe" class="w-4 h-4 rounded border-ink-300 text-amber-500 focus:ring-amber-400" />
              <span class="text-ink-600">记住我</span>
            </label>
            <a href="#" class="text-amber-600 hover:text-amber-700">忘记密码？</a>
          </div>
          <button type="submit" :disabled="loading" class="btn btn-primary w-full py-3">{{ loading ? '登录中...' : '登录' }}</button>
        </form>

        <!-- Register Form -->
        <form v-else @submit.prevent="handleRegister" class="space-y-5">
          <div>
            <label class="block text-sm font-medium text-ink-700 mb-2">用户名</label>
            <input v-model="registerForm.username" type="text" required class="input-field" placeholder="请输入用户名" />
          </div>
          <div>
            <label class="block text-sm font-medium text-ink-700 mb-2">邮箱</label>
            <input v-model="registerForm.email" type="email" required class="input-field" placeholder="请输入邮箱" />
          </div>
          <div>
            <label class="block text-sm font-medium text-ink-700 mb-2">密码</label>
            <input v-model="registerForm.password" type="password" required class="input-field" placeholder="请输入密码" />
          </div>
          <div>
            <label class="block text-sm font-medium text-ink-700 mb-2">确认密码</label>
            <input v-model="registerForm.confirmPassword" type="password" required class="input-field" placeholder="请再次输入密码" />
          </div>
          <button type="submit" :disabled="loading" class="btn btn-primary w-full py-3">{{ loading ? '注册中...' : '注册' }}</button>
        </form>

        <!-- Divider -->
        <div class="divider-ornament my-8">
          <span class="text-xs text-ink-400 bg-white px-4">或</span>
        </div>

        <!-- Social Login -->
        <button @click="handleGithubLogin" class="btn btn-secondary w-full">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
          </svg>
          使用 GitHub 登录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { login, register } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isLogin = ref(true)
const loading = ref(false)
const rememberMe = ref(false)

const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', email: '', password: '', confirmPassword: '' })

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(loginForm)
    const loginData = res as any
    userStore.setToken(loginData.accessToken)
    userStore.setUserInfo(loginData.user)
    const redirect = route.query.redirect as string
    router.push(redirect || '/portal')
  } catch (error: any) {
    alert(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  if (registerForm.password !== registerForm.confirmPassword) { alert('两次密码输入不一致'); return }
  loading.value = true
  try {
    await register({ username: registerForm.username, email: registerForm.email, password: registerForm.password })
    alert('注册成功，请登录')
    isLogin.value = true
    loginForm.username = registerForm.username
  } catch (error: any) {
    alert(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}

const handleGithubLogin = () => { window.location.href = '/api/auth/github' }
</script>