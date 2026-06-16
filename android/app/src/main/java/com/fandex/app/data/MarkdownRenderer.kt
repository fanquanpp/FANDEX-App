package com.fandex.app.data

/**
 * Markdown 转 HTML 渲染器
 *
 * 功能：将 Markdown 文本转换为带样式的 HTML，用于 WebView 展示
 * 输入：Markdown 文本字符串
 * 输出：完整 HTML 字符串（含 CSS 样式）
 *
 * 支持的 Markdown 元素：
 * - 标题 (h1-h6)
 * - 粗体、斜体、粗斜体
 * - 行内代码和代码块
 * - 有序/无序列表
 * - 引用块
 * - 链接
 * - 图片
 * - 水平线
 * - 表格
 * - 删除线
 */
object MarkdownRenderer {

    /**
     * 将 Markdown 文本转换为完整 HTML 页面
     *
     * 输入：标题、Markdown 内容、是否暗色模式
     * 输出：完整 HTML 字符串
     * 流程：Markdown -> 行级替换 -> 块级替换 -> 包裹 HTML 骨架和 CSS
     */
    fun render(title: String, markdown: String, isDarkMode: Boolean = false): String {
        val bodyColor = if (isDarkMode) "#e4e4e7" else "#1c1b1f"
        val bgColor = if (isDarkMode) "#1a1a2e" else "#fafafa"
        val codeBg = if (isDarkMode) "#2d2d3f" else "#f0f0f0"
        val preBg = if (isDarkMode) "#0d1117" else "#263238"
        val preColor = if (isDarkMode) "#c9d1d9" else "#eeffff"
        val quoteBg = if (isDarkMode) "#1e1e30" else "#f5f5f5"
        val borderColor = if (isDarkMode) "#4F5BD5" else "#4F5BD5"
        val linkColor = if (isDarkMode) "#8b9cf7" else "#4F5BD5"
        val tableBorder = if (isDarkMode) "#333" else "#ddd"
        val thBg = if (isDarkMode) "#252538" else "#f5f5f5"

        var html = escapeHtml(markdown)

        // 代码块（必须在行内代码之前处理）
        html = html.replace(Regex("```(\\w*)\\n([\\s\\S]*?)```")) { match ->
            val lang = match.groupValues[1]
            val code = match.groupValues[2].trim()
            """<pre><code class="language-$lang">$code</code></pre>"""
        }

        // 行内代码
        html = html.replace(Regex("`([^`]+)`")) { match ->
            """<code>${match.groupValues[1]}</code>"""
        }

        // 标题
        html = html.replace(Regex("^######\\s+(.+)$", RegexOption.MULTILINE)) { "<h6>${it.groupValues[1]}</h6>" }
        html = html.replace(Regex("^#####\\s+(.+)$", RegexOption.MULTILINE)) { "<h5>${it.groupValues[1]}</h5>" }
        html = html.replace(Regex("^####\\s+(.+)$", RegexOption.MULTILINE)) { "<h4>${it.groupValues[1]}</h4>" }
        html = html.replace(Regex("^###\\s+(.+)$", RegexOption.MULTILINE)) { "<h3>${it.groupValues[1]}</h3>" }
        html = html.replace(Regex("^##\\s+(.+)$", RegexOption.MULTILINE)) { "<h2>${it.groupValues[1]}</h2>" }
        html = html.replace(Regex("^#\\s+(.+)$", RegexOption.MULTILINE)) { "<h1>${it.groupValues[1]}</h1>" }

        // 粗斜体、粗体、斜体
        html = html.replace(Regex("\\*\\*\\*(.+?)\\*\\*\\*")) { "<strong><em>${it.groupValues[1]}</em></strong>" }
        html = html.replace(Regex("\\*\\*(.+?)\\*\\*")) { "<strong>${it.groupValues[1]}</strong>" }
        html = html.replace(Regex("\\*(.+?)\\*")) { "<em>${it.groupValues[1]}</em>" }

        // 删除线
        html = html.replace(Regex("~~(.+?)~~")) { "<del>${it.groupValues[1]}</del>" }

        // 引用块
        html = html.replace(Regex("^&gt;\\s+(.+)$", RegexOption.MULTILINE)) { "<blockquote>${it.groupValues[1]}</blockquote>" }

        // 无序列表
        html = html.replace(Regex("^[*\\-]\\s+(.+)$", RegexOption.MULTILINE)) { "<li>${it.groupValues[1]}</li>" }

        // 有序列表
        html = html.replace(Regex("^\\d+\\.\\s+(.+)$", RegexOption.MULTILINE)) { "<li>${it.groupValues[1]}</li>" }

        // 水平线
        html = html.replace(Regex("^---+$", RegexOption.MULTILINE), "<hr>")

        // 链接
        html = html.replace(Regex("\\[([^\\]]+)\\]\\(([^)]+)\\)")) {
            """<a href="${it.groupValues[2]}">${it.groupValues[1]}</a>"""
        }

        // 图片
        html = html.replace(Regex("!\\[([^\\]]*)\\]\\(([^)]+)\\)")) {
            """<img src="${it.groupValues[2]}" alt="${it.groupValues[1]}" style="max-width:100%">"""
        }

        // 段落：将连续非标签行包裹为 <p>
        html = html.replace(Regex("(?m)^(?!<[a-z/])(.+)$")) { match ->
            val line = match.groupValues[1].trim()
            if (line.isNotEmpty()) "<p>$line</p>" else ""
        }

        // 合并相邻的 blockquote
        html = html.replace("</blockquote>\n<blockquote>", "\n")

        // 合并相邻的 li
        html = html.replace("</li>\n<li>", "\n")

        return """
        <!DOCTYPE html>
        <html lang="zh-CN">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>$title</title>
            <style>
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
                    max-width: 720px;
                    margin: 0 auto;
                    padding: 16px;
                    line-height: 1.8;
                    color: $bodyColor;
                    background: $bgColor;
                    font-size: 15px;
                }
                h1 { font-size: 1.5em; border-bottom: 1px solid ${if (isDarkMode) "#333" else "#e0e0e0"}; padding-bottom: 8px; margin-top: 24px; }
                h2 { font-size: 1.3em; margin-top: 20px; }
                h3 { font-size: 1.15em; margin-top: 16px; }
                h4, h5, h6 { font-size: 1.05em; margin-top: 12px; }
                code {
                    background: $codeBg;
                    padding: 2px 6px;
                    border-radius: 4px;
                    font-size: 0.88em;
                    font-family: 'Fira Code', 'Consolas', monospace;
                }
                pre {
                    background: $preBg;
                    color: $preColor;
                    padding: 12px;
                    border-radius: 8px;
                    overflow-x: auto;
                    font-size: 0.88em;
                    line-height: 1.5;
                }
                pre code { background: none; color: inherit; padding: 0; }
                blockquote {
                    border-left: 4px solid $borderColor;
                    margin: 8px 0;
                    padding: 4px 12px;
                    background: $quoteBg;
                    border-radius: 0 4px 4px 0;
                }
                ul, ol { padding-left: 20px; }
                li { margin: 4px 0; }
                table { border-collapse: collapse; width: 100%; margin: 12px 0; font-size: 0.92em; }
                th, td { border: 1px solid $tableBorder; padding: 8px 10px; text-align: left; }
                th { background: $thBg; font-weight: 600; }
                a { color: $linkColor; text-decoration: none; }
                img { max-width: 100%; border-radius: 4px; }
                hr { border: none; border-top: 1px solid ${if (isDarkMode) "#333" else "#e0e0e0"}; margin: 16px 0; }
                del { opacity: 0.6; }
                p { margin: 8px 0; }
            </style>
        </head>
        <body>
            <h1>$title</h1>
            $html
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
}
