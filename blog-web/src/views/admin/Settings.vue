<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold text-gray-900 mb-6">系统设置</h1>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Site Settings -->
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-semibold mb-4">网站设置</h2>
        <form @submit.prevent="saveSiteSettings" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">网站名称</label>
            <input v-model="siteSettings.title" type="text" class="input-field w-full" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">网站描述</label>
            <textarea v-model="siteSettings.description" rows="3" class="input-field w-full"></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">网站关键词</label>
            <input v-model="siteSettings.keywords" type="text" class="input-field w-full" placeholder="多个关键词用逗号分隔" />
          </div>
          <button type="submit" class="btn-primary">保存设置</button>
        </form>
      </div>

      <!-- SEO Settings -->
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-semibold mb-4">SEO 设置</h2>
        <form @submit.prevent="saveSeoSettings" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">站点 URL</label>
            <input v-model="seoSettings.siteUrl" type="url" class="input-field w-full" placeholder="https://example.com" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Google 站点验证码</label>
            <input v-model="seoSettings.googleVerification" type="text" class="input-field w-full" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">百度站点验证码</label>
            <input v-model="seoSettings.baiduVerification" type="text" class="input-field w-full" />
          </div>
          <button type="submit" class="btn-primary">保存设置</button>
        </form>
      </div>

      <!-- GitHub OAuth -->
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-semibold mb-4">GitHub OAuth</h2>
        <form @submit.prevent="saveOAuthSettings" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Client ID</label>
            <input v-model="oauthSettings.clientId" type="text" class="input-field w-full" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Client Secret</label>
            <input v-model="oauthSettings.clientSecret" type="password" class="input-field w-full" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">回调 URL</label>
            <input v-model="oauthSettings.redirectUri" type="url" class="input-field w-full" />
          </div>
          <button type="submit" class="btn-primary">保存设置</button>
        </form>
      </div>

      <!-- Danger Zone -->
      <div class="bg-white rounded-lg shadow p-6 border border-red-200">
        <h2 class="text-lg font-semibold text-red-600 mb-4">危险操作</h2>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <div>
              <p class="font-medium">清空缓存</p>
              <p class="text-sm text-gray-500">清空所有 Redis 缓存</p>
            </div>
            <button @click="clearCache" class="btn-secondary text-red-600 border-red-300 hover:bg-red-50">
              清空缓存
            </button>
          </div>
          <div class="flex items-center justify-between">
            <div>
              <p class="font-medium">重建索引</p>
              <p class="text-sm text-gray-500">重建文章搜索索引</p>
            </div>
            <button @click="rebuildIndex" class="btn-secondary text-red-600 border-red-300 hover:bg-red-50">
              重建索引
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

const siteSettings = reactive({
  title: '我的博客',
  description: '一个简洁的博客系统',
  keywords: '博客,技术,分享'
})

const seoSettings = reactive({
  siteUrl: '',
  googleVerification: '',
  baiduVerification: ''
})

const oauthSettings = reactive({
  clientId: '',
  clientSecret: '',
  redirectUri: ''
})

const saveSiteSettings = () => {
  alert('网站设置已保存')
}

const saveSeoSettings = () => {
  alert('SEO 设置已保存')
}

const saveOAuthSettings = () => {
  alert('OAuth 设置已保存')
}

const clearCache = () => {
  if (!confirm('确定要清空所有缓存吗？')) return
  alert('缓存已清空')
}

const rebuildIndex = () => {
  if (!confirm('确定要重建搜索索引吗？')) return
  alert('索引重建完成')
}
</script>