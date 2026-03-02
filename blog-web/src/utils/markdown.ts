import MarkdownIt from 'markdown-it'
import highlightjs from 'highlight.js'
import DOMPurify from 'dompurify'
import 'highlight.js/styles/github-dark.css'

// Create markdown renderer
// Note: html: false to prevent XSS attacks (raw HTML not allowed)
const md: MarkdownIt = new MarkdownIt({
  html: false,
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
 * Render markdown to HTML with XSS protection
 */
export const renderMarkdown = (content: string): string => {
  // Render markdown then sanitize with DOMPurify for extra security
  const rawHtml = md.render(content)
  return DOMPurify.sanitize(rawHtml, {
    ALLOWED_TAGS: [
      'p', 'br', 'strong', 'em', 'u', 's', 'del', 'ins',
      'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
      'ul', 'ol', 'li',
      'blockquote', 'pre', 'code',
      'a', 'img',
      'table', 'thead', 'tbody', 'tr', 'th', 'td',
      'hr', 'div', 'span'
    ],
    ALLOWED_ATTR: ['href', 'src', 'alt', 'title', 'class', 'id', 'target', 'rel']
  })
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