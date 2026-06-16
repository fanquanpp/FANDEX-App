package com.fandex.app.reader

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import com.fandex.app.data.ContentLoader

/**
 * 文章阅读 Activity
 *
 * 功能：使用 WebView 渲染 Markdown 转 HTML 的文档内容
 * 输入：Intent extra - module_id、slug、category、title
 * 输出：WebView 展示渲染后的文档
 * 流程：接收参数 -> 加载 HTML/Markdown -> WebView 渲染
 */
class ArticleActivity : ComponentActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val moduleId = intent.getStringExtra("module_id") ?: ""
        val slug = intent.getStringExtra("slug") ?: ""
        val category = intent.getStringExtra("category") ?: ""
        val title = intent.getStringExtra("title") ?: slug

        webView = WebView(this)
        webView.settings.javaScriptEnabled = false
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()

        /* 尝试加载 HTML，若不存在则加载 Markdown 并包裹基础样式 */
        val html = ContentLoader.loadDocumentHtml(this, category, slug)
        val markdown = ContentLoader.loadDocumentMarkdown(this, category, slug)

        val content = when {
            html != null -> html
            markdown != null -> wrapMarkdownAsHtml(title, markdown)
            else -> wrapMarkdownAsHtml(title, "暂无内容")
        }

        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        setContentView(webView)
    }

    /**
     * 将 Markdown 文本包裹为带基础样式的 HTML
     *
     * 输入：标题、Markdown 内容
     * 输出：完整 HTML 字符串
     * 流程：拼接 HTML 骨架 + CSS 样式 + 内容
     */
    private fun wrapMarkdownAsHtml(title: String, content: String): String {
        return """
        <!DOCTYPE html>
        <html lang="zh-CN">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>$title</title>
            <style>
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                    max-width: 720px;
                    margin: 0 auto;
                    padding: 16px;
                    line-height: 1.7;
                    color: #1c1b1f;
                    background: #fafafa;
                }
                h1 { font-size: 1.5em; border-bottom: 1px solid #e0e0e0; padding-bottom: 8px; }
                h2 { font-size: 1.3em; }
                h3 { font-size: 1.1em; }
                code {
                    background: #f0f0f0;
                    padding: 2px 6px;
                    border-radius: 4px;
                    font-size: 0.9em;
                }
                pre {
                    background: #263238;
                    color: #eeffff;
                    padding: 12px;
                    border-radius: 8px;
                    overflow-x: auto;
                }
                pre code { background: none; color: inherit; }
                blockquote {
                    border-left: 4px solid #4F5BD5;
                    margin: 8px 0;
                    padding: 4px 12px;
                    background: #f5f5f5;
                }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background: #f5f5f5; }
                a { color: #4F5BD5; text-decoration: none; }
            </style>
        </head>
        <body>
            <h1>$title</h1>
            <div id="content"><pre>${escapeHtml(content)}</pre></div>
        </body>
        </html>
        """.trimIndent()
    }

    /**
     * 转义 HTML 特殊字符
     *
     * 输入：原始字符串
     * 输出：转义后的安全字符串
     */
    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
