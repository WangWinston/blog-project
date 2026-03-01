<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">{{ isEdit ? '编辑文章' : '写文章' }}</h1>
      <router-link to="/admin/articles" class="text-gray-600 hover:text-gray-900">
        返回列表
      </router-link>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Main Editor -->
      <div class="lg:col-span-2 space-y-6">
        <!-- Title -->
        <div class="bg-white rounded-lg shadow p-4">
          <input
            v-model="form.title"
            type="text"
            placeholder="文章标题"
            class="w-full text-2xl font-bold border-0 focus:ring-0 p-0"
          />
        </div>

        <!-- Content Editor -->
        <div class="bg-white rounded-lg shadow">
          <div class="border-b border-gray-200 p-2 flex gap-2">
            <button @click="insertMarkdown('**', '**')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded">B</button>
            <button @click="insertMarkdown('*', '*')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded italic">I</button>
            <button @click="insertMarkdown('## ', '')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded">H2</button>
            <button @click="insertMarkdown('### ', '')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded">H3</button>
            <button @click="insertMarkdown('`', '`')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded font-mono">code</button>
            <button @click="insertMarkdown('\n```\n', '\n```\n')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded font-mono">block</button>
            <button @click="insertMarkdown('- ', '')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded">列表</button>
            <button @click="insertMarkdown('> ', '')" class="px-3 py-1 text-sm hover:bg-gray-100 rounded">引用</button>
          </div>
          <div class="grid grid-cols-2 divide-x divide-gray-200">
            <textarea
              ref="editorRef"
              v-model="form.content"
              @input="handleContentChange"
              class="p-4 min-h-[500px] focus:outline-none resize-none font-mono text-sm"
              placeholder="使用 Markdown 编写文章内容..."
            ></textarea>
            <div class="p-4 min-h-[500px] prose max-w-none overflow-auto" v-html="previewContent"></div>
          </div>
        </div>
      </div>

      <!-- Sidebar Settings -->
      <div class="space-y-6">
        <!-- Publish Settings -->
        <div class="bg-white rounded-lg shadow p-4">
          <h3 class="font-semibold mb-4">发布设置</h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm text-gray-600 mb-1">分类</label>
              <select v-model="form.categoryId" class="input-field w-full">
                <option :value="null">选择分类</option>
                <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">标签</label>
              <div class="flex flex-wrap gap-2 p-2 border rounded-lg min-h-[80px]">
                <span
                  v-for="tag in selectedTags"
                  :key="tag.id"
                  class="px-2 py-1 text-sm rounded cursor-pointer hover:opacity-75"
                  :style="{ backgroundColor: tag.color + '20', color: tag.color }"
                  @click="removeTag(tag)"
                >
                  {{ tag.name }} ×
                </span>
                <select @change="addTag($event)" class="border-0 text-sm focus:ring-0">
                  <option value="">添加标签</option>
                  <option v-for="tag in availableTags" :key="tag.id" :value="tag.id">{{ tag.name }}</option>
                </select>
              </div>
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">封面图片</label>
              <input v-model="form.coverImage" type="text" class="input-field w-full" placeholder="图片URL" />
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">摘要</label>
              <textarea v-model="form.summary" rows="3" class="input-field w-full" placeholder="文章摘要..."></textarea>
            </div>
            <div class="flex items-center gap-4">
              <label class="flex items-center">
                <input type="checkbox" v-model="form.isTop" class="rounded text-primary-600" />
                <span class="ml-2 text-sm">置顶</span>
              </label>
              <label class="flex items-center">
                <input type="checkbox" v-model="form.isRecommend" class="rounded text-primary-600" />
                <span class="ml-2 text-sm">推荐</span>
              </label>
            </div>
          </div>
        </div>

        <!-- Actions -->
        <div class="bg-white rounded-lg shadow p-4 space-y-3">
          <button @click="handleSaveDraft" :disabled="saving" class="btn-secondary w-full">
            {{ saving ? '保存中...' : '保存草稿' }}
          </button>
          <button @click="handlePublish" :disabled="saving" class="btn-primary w-full">
            {{ saving ? '发布中...' : '发布文章' }}
          </button>
          <button v-if="isEdit" @click="showVersions = true" class="btn-secondary w-full">
            查看版本历史
          </button>
        </div>
      </div>
    </div>

    <!-- Version History Modal -->
    <div v-if="showVersions" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[80vh] overflow-hidden">
        <div class="p-4 border-b flex justify-between items-center">
          <h3 class="font-semibold">版本历史</h3>
          <button @click="showVersions = false" class="text-gray-500 hover:text-gray-700">×</button>
        </div>
        <div class="p-4 overflow-auto max-h-[60vh]">
          <div v-for="version in versions" :key="version.id" class="border-b py-3">
            <div class="flex justify-between items-center">
              <div>
                <span class="font-medium">版本 {{ version.version }}</span>
                <span class="text-sm text-gray-500 ml-2">{{ version.changeLog }}</span>
              </div>
              <button @click="handleRollback(version.version)" class="text-sm text-primary-600 hover:underline">
                恢复此版本
              </button>
            </div>
            <p class="text-sm text-gray-500 mt-1">{{ formatDate(version.createdAt) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticleDetail, createArticle, updateArticle, publishArticle, getArticleVersions, rollbackArticle } from '@/api/admin'
import { getCategoryList, getTagList } from '@/api/portal'
import { renderMarkdown } from '@/utils/markdown'
import { formatDate } from '@/utils/format'
import type { Category, Tag, ArticleVersion } from '@/types'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const editorRef = ref<HTMLTextAreaElement | null>(null)
const saving = ref(false)
const showVersions = ref(false)

const form = reactive({
  title: '',
  content: '',
  summary: '',
  coverImage: '',
  categoryId: null as number | null,
  tagIds: [] as number[],
  isTop: false,
  isRecommend: false
})

const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const versions = ref<ArticleVersion[]>([])

const selectedTags = computed(() => {
  return tags.value.filter(t => form.tagIds.includes(t.id))
})

const availableTags = computed(() => {
  return tags.value.filter(t => !form.tagIds.includes(t.id))
})

const previewContent = computed(() => {
  return renderMarkdown(form.content)
})

const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const fetchTags = async () => {
  try {
    const res = await getTagList()
    tags.value = res.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
}

const fetchArticle = async () => {
  if (!route.params.id) return
  try {
    const res = await getArticleDetail(Number(route.params.id))
    const article = res.data
    form.title = article.title
    form.content = article.content
    form.summary = article.summary || ''
    form.coverImage = article.coverImage || ''
    form.categoryId = article.categoryId
    form.tagIds = article.tags?.map((t: Tag) => t.id) || []
    form.isTop = article.isTop
    form.isRecommend = article.isRecommend
  } catch (error) {
    console.error('Failed to fetch article:', error)
  }
}

const fetchVersions = async () => {
  if (!route.params.id) return
  try {
    const res = await getArticleVersions(Number(route.params.id))
    versions.value = res.data
  } catch (error) {
    console.error('Failed to fetch versions:', error)
  }
}

const insertMarkdown = (prefix: string, suffix: string) => {
  const textarea = editorRef.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const text = form.content
  const selected = text.substring(start, end)

  form.content = text.substring(0, start) + prefix + selected + suffix + text.substring(end)

  // Reset cursor position
  setTimeout(() => {
    textarea.focus()
    textarea.setSelectionRange(start + prefix.length, start + prefix.length + selected.length)
  }, 0)
}

const addTag = (event: Event) => {
  const select = event.target as HTMLSelectElement
  const tagId = Number(select.value)
  if (tagId && !form.tagIds.includes(tagId)) {
    form.tagIds.push(tagId)
  }
  select.value = ''
}

const removeTag = (tag: Tag) => {
  form.tagIds = form.tagIds.filter(id => id !== tag.id)
}

const handleContentChange = () => {
  // Auto-generate summary from first 200 chars
  if (!form.summary && form.content) {
    form.summary = form.content.replace(/[#*`>\-\[\]]/g, '').substring(0, 200).trim()
  }
}

const handleSaveDraft = async () => {
  await saveArticle('DRAFT')
}

const handlePublish = async () => {
  await saveArticle('PUBLISHED')
}

const saveArticle = async (status: string) => {
  if (!form.title.trim()) {
    alert('请输入文章标题')
    return
  }

  saving.value = true
  try {
    if (isEdit.value) {
      await updateArticle(Number(route.params.id), form)
      if (status === 'PUBLISHED') {
        await publishArticle(Number(route.params.id))
      }
    } else {
      const res = await createArticle(form)
      if (status === 'PUBLISHED') {
        await publishArticle(res.data.id)
      }
    }
    router.push('/admin/articles')
  } catch (error) {
    console.error('Failed to save article:', error)
    alert('保存失败')
  } finally {
    saving.value = false
  }
}

const handleRollback = async (version: number) => {
  if (!confirm('确定要恢复到此版本吗？')) return
  try {
    await rollbackArticle(Number(route.params.id), version)
    fetchArticle()
    showVersions.value = false
  } catch (error) {
    console.error('Failed to rollback:', error)
  }
}

onMounted(() => {
  fetchCategories()
  fetchTags()
  if (isEdit.value) {
    fetchArticle()
    fetchVersions()
  }
})
</script>