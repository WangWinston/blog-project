import MarkdownIt from 'markdown-it'
import highlightjs from 'highlight.js'
import 'highlight.js/styles/github-dark.css'

// Create markdown renderer
const md: MarkdownIt = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: function (str: string, lang: string): string {
    if (lang && highlightjs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${highlightjs.highlight(str, { language: lang, ignoreIllegals: true }).value}</code></pre>`
      } catch (error) {
        console.error('Highlight error:', error)
      }
    }
    return `<pre class="hljs"><code>${md.utils.escapeHtml(str)}</code></pre>`
  },
})

/**
 * Render markdown to HTML
 */
export const renderMarkdown = (content: string): string => {
  return md.render(content)
}

/**
 * Get plain text excerpt from markdown
 */
export const getExcerpt = (content: string, length: number = 200): string => {
  const plainText = md.render(content)
    .replace(/<[^>]+>/g, '')
    .replace(/\s+/g, ' ')
    .trim()
  return plainText.length > length ? plainText.substring(0, length) + '...' : plainText
}

export default md