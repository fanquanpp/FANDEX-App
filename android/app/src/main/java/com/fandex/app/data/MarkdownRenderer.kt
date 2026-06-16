package com.fandex.app.data

import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.node.*
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.AttributeProvider
import org.commonmark.renderer.html.HtmlRenderer

/**
 * Markdown 转 HTML 渲染器
 *
 * 功能：使用 commonmark-java 将 Markdown 文本转换为带完整样式的 HTML 页面
 * 输入：标题、Markdown 内容、是否暗色模式
 * 输出：完整 HTML 字符串（含 CSS 变量、排版样式、代码高亮 JS、复制按钮 JS）
 *
 * 渲染流程：
 * 1. 使用 commonmark Parser 解析 Markdown 为 AST
 * 2. 使用 HtmlRenderer + 自定义 AttributeProvider 生成带 CSS class 的 HTML
 * 3. 包裹 HTML 骨架，嵌入 CSS（仿照 FANDEX-Original-HTML）和 JS（语法高亮 + 复制）
 *
 * 支持的 Markdown 扩展：
 * - GFM 表格
 * - GFM 删除线
 * - 标题、列表、引用、代码块、行内代码、链接、图片、水平线等标准语法
 */
object MarkdownRenderer {

    /**
     * commonmark 解析器实例（线程安全，复用）
     * 启用 GFM 表格和删除线扩展
     */
    private val parser: Parser = Parser.builder()
        .extensions(listOf(TablesExtension.create(), StrikethroughExtension.create()))
        .build()

    /**
     * HTML 渲染器实例（线程安全，复用）
     * 使用自定义 AttributeProvider 为元素添加 CSS class
     */
    private val renderer: HtmlRenderer = HtmlRenderer.builder()
        .attributeProviderFactory { FandexAttributeProvider() }
        .extensions(listOf(TablesExtension.create(), StrikethroughExtension.create()))
        .build()

    /**
     * 将 Markdown 文本转换为完整 HTML 页面
     *
     * 输入：标题、Markdown 内容、是否暗色模式
     * 输出：完整 HTML 字符串
     * 流程：Markdown -> commonmark AST -> HTML body -> 包裹 HTML 骨架和 CSS/JS
     */
    fun render(title: String, markdown: String, isDarkMode: Boolean = false): String {
        val document = parser.parse(markdown)
        val bodyHtml = renderer.render(document)
        return buildHtmlPage(title, bodyHtml, isDarkMode)
    }

    /**
     * 构建完整 HTML 页面
     *
     * 输入：标题、HTML body 内容、是否暗色模式
     * 输出：完整 HTML 字符串，包含 CSS 变量、排版样式、代码高亮和复制按钮 JS
     */
    private fun buildHtmlPage(title: String, body: String, isDarkMode: Boolean): String {
        val themeAttr = if (isDarkMode) "dark" else "light"
        return """
        <!DOCTYPE html>
        <html lang="zh-CN" data-theme="$themeAttr">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <title>${escapeHtml(title)}</title>
            <style>
                ${getCssVariables(isDarkMode)}
                ${getResetCss()}
                ${getProseCss()}
                ${getCodeCss()}
                ${getCopyBtnCss()}
                ${getHighlightCss()}
                ${getMobileCss()}
            </style>
        </head>
        <body>
            <article class="prose">
                <h1>${escapeHtml(title)}</h1>
                $body
            </article>
            <script>
                ${getHighlightJs()}
                ${getCopyButtonJs()}
                ${getThemeJs()}
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    /**
     * CSS 变量定义（仿照 FANDEX-Original-HTML variables.css）
     *
     * 输入：是否暗色模式
     * 输出：:root 或 [data-theme='dark'] 下的 CSS 变量声明
     */
    private fun getCssVariables(isDarkMode: Boolean): String {
        return if (isDarkMode) """
        :root {
            --color-primary: #6ea8fe;
            --color-primary-hover: #93c0ff;
            --color-secondary: #55efc4;
            --color-tertiary: #f09070;
            --color-bg: #0d0d0d;
            --color-bg-card: #181818;
            --color-bg-code: #202020;
            --color-bg-hover: #282828;
            --color-text: #ebebeb;
            --color-text-secondary: #b3b3b3;
            --color-text-tertiary: #8a8a8a;
            --color-text-inverse: #0d0d0d;
            --color-border: #525252;
            --color-border-light: #383838;
            --color-success: #55efc4;
            --color-nav-bg: rgba(13, 13, 13, 0.95);
            --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.2);
            --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.3);
            --font-body: 'Noto Sans SC', 'PingFang SC', 'Microsoft YaHei', 'Hiragino Sans GB', sans-serif;
            --font-code: 'JetBrains Mono', 'Fira Code', 'Consolas', 'Courier New', monospace;
            --font-display: 'Noto Sans SC', 'PingFang SC', 'Microsoft YaHei', sans-serif;
            --transition-fast: 0.12s ease;
            --transition-base: 0.22s ease;
        }
        """ else """
        :root {
            --color-primary: #3366cc;
            --color-primary-hover: #264da8;
            --color-secondary: #00b894;
            --color-tertiary: #e05a2b;
            --color-bg: #fafafa;
            --color-bg-card: #eeeeee;
            --color-bg-code: #e6e6e6;
            --color-bg-hover: #dcdcdc;
            --color-text: #0d0d0d;
            --color-text-secondary: #4d4d4d;
            --color-text-tertiary: #808080;
            --color-text-inverse: #ffffff;
            --color-border: #2e2e2e;
            --color-border-light: #c4c4c4;
            --color-success: #00b894;
            --color-nav-bg: rgba(250, 250, 250, 0.95);
            --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
            --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.1);
            --font-body: 'Noto Sans SC', 'PingFang SC', 'Microsoft YaHei', 'Hiragino Sans GB', sans-serif;
            --font-code: 'JetBrains Mono', 'Fira Code', 'Consolas', 'Courier New', monospace;
            --font-display: 'Noto Sans SC', 'PingFang SC', 'Microsoft YaHei', sans-serif;
            --transition-fast: 0.12s ease;
            --transition-base: 0.22s ease;
        }
        """
    }

    /**
     * 全局重置样式（仿照 FANDEX-Original-HTML global.css）
     *
     * 输出：全局重置、body 基础样式、滚动条、选区样式
     */
    private fun getResetCss(): String = """
    *, *::before, *::after { margin: 0; padding: 0; box-sizing: border-box; }
    html { font-size: 16px; overscroll-behavior: none; }
    body {
        min-height: 100vh;
        font-family: var(--font-body);
        font-size: 1.0625rem;
        line-height: 1.85;
        color: var(--color-text);
        background: var(--color-bg);
        transition: background var(--transition-base), color var(--transition-base);
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        overflow-wrap: break-word;
        -webkit-user-select: none;
        user-select: none;
    }
    ::selection { background: var(--color-primary); color: var(--color-text-inverse); }
    code, pre, input, textarea { -webkit-user-select: text; user-select: text; }
    ::-webkit-scrollbar { width: 6px; height: 6px; }
    ::-webkit-scrollbar-track { background: transparent; }
    ::-webkit-scrollbar-thumb { background: var(--color-border-light); border-radius: 3px; }
    ::-webkit-scrollbar-thumb:hover { background: var(--color-border); }
    """

    /**
     * 排版样式（仿照 FANDEX-Original-HTML typography.css）
     *
     * 输出：.prose 容器下所有 Markdown 元素的排版规则
     */
    private fun getProseCss(): String = """
    .prose {
        max-width: 100%;
        margin: 0 auto;
        padding: 0 16px;
        line-height: 1.9;
    }
    .prose h1, .prose h2, .prose h3, .prose h4, .prose h5, .prose h6 {
        font-family: var(--font-body);
        font-weight: 700;
        line-height: 1.35;
        color: var(--color-text);
        margin-top: 1.6em;
        margin-bottom: 0.6em;
    }
    .prose h1 {
        font-size: 1.7em;
        border-left: 3px solid var(--color-primary);
        border-bottom: 1px solid var(--color-border-light);
        padding-left: 0.5em;
        padding-bottom: 0.35em;
    }
    .prose h2 {
        font-size: 1.35em;
        border-bottom: 2px solid var(--color-secondary);
        padding-bottom: 0.3em;
    }
    .prose h3 {
        font-size: 1.15em;
        border-left: 3px solid var(--color-tertiary);
        padding-left: 0.5em;
    }
    .prose h4 { font-size: 1.05em; text-decoration: underline; text-underline-offset: 0.25em; text-decoration-color: var(--color-border-light); }
    .prose h5 { font-size: 1em; }
    .prose h6 { font-size: 0.95em; color: var(--color-text-secondary); }
    .prose h1:first-child { margin-top: 0; }
    .prose p { margin: 0.9em 0; text-align: justify; }
    .prose a {
        color: var(--color-primary);
        text-decoration: underline;
        text-decoration-style: dotted;
        text-underline-offset: 0.15em;
        text-decoration-thickness: 1px;
        transition: color var(--transition-fast), text-decoration-style var(--transition-fast);
    }
    .prose a:hover { color: var(--color-primary-hover); text-decoration-style: solid; }
    .prose ul, .prose ol { padding-left: 2em; margin: 0.8em 0; }
    .prose ul { list-style: none; }
    .prose ul > li { position: relative; padding-left: 0.5em; }
    .prose ul > li::before {
        content: '';
        position: absolute;
        left: -1.2em;
        top: 0.65em;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background: var(--color-primary);
        opacity: 0.6;
    }
    .prose ol { counter-reset: prose-ol; list-style: none; }
    .prose ol > li { position: relative; padding-left: 0.5em; counter-increment: prose-ol; }
    .prose ol > li::before {
        content: counter(prose-ol) '.';
        position: absolute;
        left: -2em;
        width: 1.5em;
        text-align: right;
        font-family: var(--font-display);
        font-size: 0.85em;
        font-weight: 600;
        color: var(--color-primary);
    }
    .prose li { margin: 0.35em 0; }
    .prose blockquote {
        border-left: 3px solid var(--color-primary);
        padding: 0.85em 1.25em;
        margin: 1.2em 0;
        background: var(--color-bg-card);
        color: var(--color-text-secondary);
        border-radius: 0 4px 4px 0;
    }
    .prose blockquote > p:first-child { margin-top: 0; }
    .prose blockquote > p:last-child { margin-bottom: 0; }
    .prose table {
        width: 100%;
        border-collapse: collapse;
        margin: 1em 0;
        border: 1px solid var(--color-border);
        font-size: 0.92em;
        display: block;
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
    }
    .prose thead { display: table-header-group; }
    .prose tbody { display: table-row-group; }
    .prose tr { display: table-row; }
    .prose th, .prose td {
        padding: 0.7em 1em;
        text-align: left;
        border: 1px solid var(--color-border-light);
        display: table-cell;
    }
    .prose th {
        background: var(--color-primary);
        color: var(--color-text-inverse);
        font-family: var(--font-display);
        font-size: 0.9em;
        font-weight: 600;
        white-space: nowrap;
    }
    .prose tr:nth-child(even) td { background: var(--color-bg-card); }
    .prose img { max-width: 100%; border: 1px solid var(--color-border); border-radius: 4px; }
    .prose hr { border: none; border-top: 3px solid var(--color-border); margin: 2.5em 0; }
    .prose strong { color: var(--color-primary); font-weight: 700; }
    .prose del { opacity: 0.6; }
    """

    /**
     * 代码块样式（仿照 FANDEX-Original-HTML code.css）
     *
     * 输出：行内代码、代码块、语言标签样式
     */
    private fun getCodeCss(): String = """
    .prose code {
        font-family: var(--font-code);
        background: var(--color-bg-code);
        color: var(--color-text);
        padding: 0.15em 0.4em;
        font-size: 0.875em;
        border: 1px solid var(--color-border-light);
        border-radius: 3px;
    }
    .prose pre {
        margin: 1em 0;
        overflow-x: auto;
        border: 1px solid var(--color-border);
        border-radius: 4px;
        position: relative;
    }
    .prose pre code {
        display: block;
        padding: 1.25em 1em;
        background: var(--color-bg-code);
        color: var(--color-text);
        border: none;
        border-radius: 0;
        font-size: 0.875em;
        line-height: 1.6;
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
    }
    .code-block {
        margin: 1em 0;
        border: 1px solid var(--color-border);
        position: relative;
        border-radius: 4px;
        overflow: hidden;
    }
    .code-block pre {
        margin: 0;
        border: none;
        border-radius: 0;
    }
    .code-block pre code {
        padding: 1.25em 1em;
    }
    .code-block[data-lang]::before {
        content: attr(data-lang);
        position: absolute;
        top: 0;
        right: 0;
        padding: 0.2em 0.75em;
        font-family: var(--font-display);
        font-size: 0.7em;
        text-transform: uppercase;
        letter-spacing: 0.1em;
        font-weight: 700;
        color: var(--color-text-tertiary);
        background: var(--color-bg-card);
        border-bottom-left-radius: 6px;
        opacity: 0.5;
        transition: opacity var(--transition-base);
        z-index: 1;
    }
    .code-block[data-lang]:hover::before { opacity: 0; }
    """

    /**
     * 复制按钮样式（仿照 FANDEX-Original-HTML code.css）
     *
     * 输出：复制按钮的默认、悬停、已复制状态样式
     */
    private fun getCopyBtnCss(): String = """
    .copy-btn {
        position: absolute;
        top: 8px;
        right: 8px;
        z-index: 2;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 4px;
        padding: 6px 12px;
        border: 1px solid var(--color-border-light);
        background: var(--color-bg-card);
        color: var(--color-text-secondary);
        font-family: var(--font-display);
        font-size: 0.78em;
        cursor: pointer;
        opacity: 0.6;
        transition: opacity var(--transition-base), background var(--transition-base), color var(--transition-base), border-color var(--transition-base);
        border-radius: 3px;
    }
    .code-block:hover .copy-btn { opacity: 1; }
    .copy-btn:hover {
        background: var(--color-primary);
        color: var(--color-text-inverse);
        border-color: var(--color-primary);
    }
    .copy-btn.copied {
        background: var(--color-tertiary);
        color: var(--color-text);
        border-color: var(--color-tertiary);
        opacity: 1;
    }
    .copy-btn svg { width: 14px; height: 14px; }
    """

    /**
     * 语法高亮 CSS class 样式
     *
     * 输出：.hl-keyword / .hl-string / .hl-comment / .hl-number / .hl-type / .hl-function 的颜色定义
     */
    private fun getHighlightCss(): String = """
    .hl-keyword { color: #c678dd; font-weight: 600; }
    .hl-string { color: #98c379; }
    .hl-comment { color: var(--color-text-tertiary); font-style: italic; }
    .hl-number { color: #d19a66; }
    .hl-type { color: #e5c07b; }
    .hl-function { color: #61afef; }
    .hl-operator { color: #56b6c2; }
    .hl-tag { color: #e06c75; }
    .hl-attr { color: #d19a66; }
    .hl-built_in { color: #e5c07b; }
    [data-theme="light"] .hl-keyword { color: #a626a4; }
    [data-theme="light"] .hl-string { color: #50a14f; }
    [data-theme="light"] .hl-comment { color: #a0a1a7; font-style: italic; }
    [data-theme="light"] .hl-number { color: #986801; }
    [data-theme="light"] .hl-type { color: #c18401; }
    [data-theme="light"] .hl-function { color: #4078f2; }
    [data-theme="light"] .hl-operator { color: #0184bc; }
    [data-theme="light"] .hl-tag { color: #e45649; }
    [data-theme="light"] .hl-attr { color: #986801; }
    [data-theme="light"] .hl-built_in { color: #c18401; }
    """

    /**
     * 移动端响应式适配样式
     *
     * 输出：移动端字号缩小、间距调整、表格横向滚动
     */
    private fun getMobileCss(): String = """
    @media (max-width: 768px) {
        .prose { padding: 0 12px; font-size: 0.88em; }
        .prose h1 { font-size: 1.35em; }
        .prose h2 { font-size: 1.15em; }
        .prose h3 { font-size: 1.05em; }
        .prose h4 { font-size: 1em; }
        .prose p { text-align: left; }
        .prose ul, .prose ol { padding-left: 1.5em; }
        .prose blockquote { padding: 0.65em 1em; margin: 1em 0; }
        .prose th, .prose td { padding: 0.5em 0.65em; font-size: 0.9em; }
        .prose pre code { padding: 1em 0.75em; font-size: 0.8em; }
        .code-block pre code { padding: 1em 0.75em; font-size: 0.8em; }
        .copy-btn { padding: 3px 8px; font-size: 0.78em; }
        .code-block[data-lang]::before { font-size: 0.6em; padding: 0.15em 0.5em; }
    }
    """

    /**
     * 语法高亮 JavaScript
     *
     * 功能：对 pre > code 元素进行基于正则的轻量级语法高亮
     * 输入：DOM 中的 code 元素
     * 输出：高亮后的 HTML（使用 span.hl-xxx class）
     *
     * 支持的语言：javascript, typescript, python, java, kotlin, go, c, cpp, sql,
     *             html, css, json, yaml, bash, shell, rust, swift, php, ruby, dart
     *
     * 算法：使用占位符法，先提取字符串和注释（避免内部关键字被误匹配），
     *       再匹配关键字、数字、类型等，最后还原占位符
     */
    private fun getHighlightJs(): String = """
    (function() {
        var LANG_KEYWORDS = {
            javascript: 'async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|export|extends|finally|for|from|function|if|import|in|instanceof|let|new|of|return|static|super|switch|this|throw|try|typeof|var|void|while|with|yield',
            typescript: 'abstract|any|as|async|await|boolean|break|case|catch|class|const|constructor|continue|debugger|declare|default|delete|do|else|enum|export|extends|finally|for|from|function|if|implements|import|in|instanceof|interface|let|module|namespace|new|null|number|of|package|private|protected|public|readonly|return|static|string|super|switch|this|throw|try|type|typeof|undefined|union|var|void|while|with|yield',
            python: 'and|as|assert|async|await|break|class|continue|def|del|elif|else|except|finally|for|from|global|if|import|in|is|lambda|nonlocal|not|or|pass|raise|return|try|while|with|yield|True|False|None',
            java: 'abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while',
            kotlin: 'abstract|actual|annotation|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|else|enum|expect|external|false|final|finally|for|fun|if|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|true|try|typealias|typeof|val|var|vararg|when|where|while',
            go: 'break|case|chan|const|continue|default|defer|else|fallthrough|for|func|go|goto|if|import|interface|map|package|range|return|select|struct|switch|type|var|true|false|nil|iota|append|cap|close|copy|delete|len|make|new|panic|print|println|recover',
            c: 'auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while|NULL|include|define|ifdef|ifndef|endif',
            cpp: 'alignas|alignof|and|and_eq|asm|auto|bitand|bitor|bool|break|case|catch|char|char16_t|char32_t|class|compl|concept|const|const_cast|constexpr|continue|decltype|default|delete|do|double|dynamic_cast|else|enum|explicit|export|extern|false|float|for|friend|goto|if|inline|int|long|mutable|namespace|new|noexcept|not|not_eq|nullptr|operator|or|or_eq|private|protected|public|register|reinterpret_cast|requires|return|short|signed|sizeof|static|static_assert|static_cast|struct|switch|template|this|thread_local|throw|true|try|typedef|typeid|typename|union|unsigned|using|virtual|void|volatile|wchar_t|while|xor|xor_eq|override|final',
            sql: 'select|from|where|insert|into|values|update|set|delete|create|table|alter|drop|index|join|inner|left|right|outer|on|and|or|not|null|is|in|between|like|order|by|group|having|as|distinct|count|sum|avg|min|max|limit|offset|union|all|exists|case|when|then|else|end|primary|key|foreign|references|constraint|default|check|unique|grant|revoke|commit|rollback|begin|transaction',
            rust: 'as|async|await|break|const|continue|crate|dyn|else|enum|extern|false|fn|for|if|impl|in|let|loop|match|mod|move|mut|pub|ref|return|self|Self|static|struct|super|trait|true|type|unsafe|use|where|while|yield',
            swift: 'associatedtype|as|async|await|break|case|catch|class|continue|default|defer|deinit|do|else|enum|extension|fallthrough|false|fileprivate|final|for|func|guard|if|import|in|init|inout|internal|is|lazy|let|mutating|nil|none|nonmutating|open|operator|optional|override|postfix|precedence|prefix|private|protocol|public|repeat|required|return|self|Self|static|struct|subscript|super|switch|throw|throws|true|try|typealias|unowned|var|weak|where|while|willSet',
            php: 'abstract|and|array|as|break|callable|case|catch|class|clone|const|continue|declare|default|die|do|echo|else|elseif|empty|enddeclare|endfor|endforeach|endif|endswitch|endwhile|eval|exit|extends|final|finally|fn|for|foreach|function|global|goto|if|implements|include|include_once|instanceof|insteadof|interface|isset|list|match|namespace|new|or|print|private|protected|public|require|require_once|return|static|switch|throw|trait|try|unset|use|var|while|xor|yield',
            ruby: 'alias|and|begin|break|case|class|def|defined|do|else|elsif|end|ensure|false|for|if|in|module|next|nil|not|or|redo|rescue|retry|return|self|super|then|true|undef|unless|until|when|while|yield',
            dart: 'abstract|as|assert|async|await|break|case|catch|class|const|continue|covariant|default|deferred|do|dynamic|else|enum|export|extends|extension|external|factory|false|final|finally|for|Function|get|hide|if|implements|import|in|interface|is|late|library|mixin|new|null|on|operator|part|required|rethrow|return|set|show|static|super|switch|sync|this|throw|true|try|typedef|var|void|while|with|yield',
            bash: 'if|then|else|elif|fi|case|esac|for|while|until|do|done|in|function|select|time|coproc|return|exit|break|continue|declare|export|local|readonly|typeset|unset|source|alias|echo|printf|read|cd|pwd|ls|mkdir|rm|cp|mv|cat|grep|sed|awk|find|sort|uniq|wc|head|tail|chmod|chown|sudo|apt|yum|npm|pip|git|docker',
            shell: 'if|then|else|elif|fi|case|esac|for|while|until|do|done|in|function|select|time|coproc|return|exit|break|continue|declare|export|local|readonly|typeset|unset|source|alias|echo|printf|read',
            yaml: 'true|false|null|yes|no',
            json: 'true|false|null',
            css: 'align|animation|background|border|bottom|box|clear|clip|color|content|cursor|direction|display|filter|flex|float|font|grid|height|justify|left|letter|line|list|margin|max|min|opacity|order|outline|overflow|padding|perspective|pointer|position|resize|right|scroll|shadow|table|text|top|transform|transition|user|vertical|visibility|white|width|word|z-index',
            html: 'a|abbr|address|area|article|aside|audio|b|base|blockquote|body|br|button|canvas|caption|cite|code|col|data|datalist|dd|del|details|dfn|dialog|div|dl|dt|em|embed|fieldset|figure|footer|form|h1|h2|h3|h4|h5|h6|head|header|hgroup|hr|html|i|iframe|img|input|ins|kbd|label|legend|li|link|main|map|mark|menu|meta|meter|nav|noscript|object|ol|optgroup|option|output|p|param|picture|pre|progress|q|rp|rt|ruby|s|samp|script|section|select|slot|small|source|span|strong|style|sub|summary|sup|table|tbody|td|template|textarea|tfoot|th|thead|time|title|tr|track|u|ul|var|video|wbr'
        };
        var BUILT_INS = {
            javascript: 'console|document|window|Array|Object|String|Number|Boolean|Date|Math|JSON|Promise|Map|Set|RegExp|Error|TypeError|RangeError|Symbol|BigInt|Proxy|Reflect|parseInt|parseFloat|isNaN|isFinite|encodeURIComponent|decodeURIComponent|setTimeout|setInterval|clearTimeout|clearInterval|fetch|require|module|exports|process|Buffer',
            typescript: 'console|document|window|Array|Object|String|Number|Boolean|Date|Math|JSON|Promise|Map|Set|RegExp|Error|Partial|Required|Readonly|Record|Pick|Omit|Exclude|Extract|NonNullable|ReturnType|InstanceType|Parameters|ConstructorParameters',
            python: 'print|input|len|range|int|str|float|list|dict|set|tuple|type|isinstance|issubclass|hasattr|getattr|setattr|delattr|property|staticmethod|classmethod|super|object|Exception|ValueError|TypeError|KeyError|IndexError|AttributeError|RuntimeError|NotImplementedError|ImportError|FileNotFoundError|OSError|IOError|ZeroDivisionError|OverflowError|SyntaxError|NameError|UnboundLocalError|StopIteration|GeneratorExit|SystemExit|KeyboardInterrupt|AbortError|BaseException|ArithmeticError|LookupError|EnvironmentError|MemoryError|RecursionError|BufferError|EOFError|ReferenceError|SystemError|Warning|UserWarning|DeprecationWarning|PendingDeprecationWarning|RuntimeWarning|SyntaxWarning|ResourceWarning|FutureWarning|ImportWarning|UnicodeWarning|BytesWarning',
            java: 'System|String|Integer|Long|Double|Float|Boolean|Byte|Short|Character|Object|Class|Thread|Runnable|Exception|RuntimeException|NullPointerException|IllegalArgumentException|IllegalStateException|IndexOutOfBoundsException|ArrayIndexOutOfBoundsException|ClassCastException|ArithmeticException|NumberFormatException|UnsupportedOperationException|ConcurrentModificationException|IOException|FileNotFoundException|SQLException|ClassNotFoundException|InterruptedException|ArrayList|HashMap|HashSet|LinkedList|TreeMap|TreeSet|LinkedHashMap|LinkedHashSet|Vector|Hashtable|Stack|PriorityQueue|ArrayDeque|EnumMap|EnumSet|BitSet|Collections|Arrays|List|Map|Set|Queue|Deque|Iterator|ListIterator|Comparable|Comparator|Comparator|Optional|Stream|IntStream|LongStream|DoubleStream|Collectors|Supplier|Consumer|Function|Predicate|BiFunction|BiConsumer|UnaryOperator|BinaryOperator',
            kotlin: 'Unit|Boolean|Byte|Short|Int|Long|Float|Double|String|Array|List|Map|Set|MutableList|MutableMap|MutableSet|Collection|Sequence|Iterator|Iterable|Range|Progression|Pair|Triple|Result|Nothing|Any|Throwable|Exception|RuntimeException|NullPointerException|IllegalArgumentException|IllegalStateException|IndexOutOfBoundsException|ClassCastException|NumberFormatException|ArithmeticException|UnsupportedOperationException|ConcurrentModificationException|IOException|println|print|readLine|lazy|also|apply|let|run|with|use|takeIf|takeUnless|repeat|assert|check|require',
            go: 'true|false|nil|iota|append|cap|close|copy|delete|len|make|new|panic|print|println|recover|error|string|int|int8|int16|int32|int64|uint|uint8|uint16|uint32|uint64|float32|float64|complex64|complex128|byte|rune|bool|error|fmt|Print|Printf|Println|Sprintf|Errorf|Fprintf|Fprint|Fprintln',
            rust: 'Some|None|Ok|Err|Self|Box|Vec|String|Option|Result|HashMap|HashSet|BTreeMap|BTreeSet|Cow|Arc|Rc|Cell|RefCell|Mutex|RwLock|println|print|format|eprintln|eprint|panic|assert|assert_eq|assert_ne|todo|unimplemented|unreachable|dbg|vec|boxed|clone|Default|Display|Debug|From|Into|Iterator|IntoIterator|FromIterator|Extend|ExactSizeIterator|DoubleEndedIterator|Clone|Copy|Send|Sync|Sized|Unpin|Drop|Fn|FnMut|FnOnce|AsRef|AsMut|Borrow|BorrowMut|ToOwned|Deref|DerefMut|Index|IndexMut|Add|Sub|Mul|Div|Rem|Neg|Not|BitAnd|BitOr|BitXor|Shl|Shr|PartialEq|Eq|PartialOrd|Ord|Hash|Read|Write|Seek|BufRead|SeekFrom|Error|io|fs|path|os|net|thread|sync|time|process|env|args|stdin|stdout|stderr|File|OpenOptions|Metadata|Permissions|DirBuilder|DirEntry|FileType|ReadDir',
            sql: 'INTEGER|TEXT|REAL|BLOB|VARCHAR|CHAR|DATE|TIME|DATETIME|TIMESTAMP|BOOLEAN|FLOAT|DOUBLE|DECIMAL|NUMERIC|BIGINT|SMALLINT|TINYINT|MEDIUMINT|INT|BOOLEAN|SERIAL|BIGSERIAL|UUID|JSON|JSONB|ARRAY|BYTEA|CIDR|INET|MACADDR|MONEY|INTERVAL|GEOMETRY|POINT|LINE|LSEG|BOX|PATH|POLYGON|CIRCLE',
            swift: 'Int|Double|Float|String|Bool|Array|Dictionary|Set|Optional|Result|URL|Date|Data|UUID|Character|Substring|Range|ClosedRange|CountableRange|CountableClosedRange|Any|AnyObject|Self|Type|Protocol|Metatype|Void|Never|Error|FatalError|print|debugPrint|assert|assertionFailure|precondition|preconditionFailure|fatalError|withUnsafePointer|withUnsafeMutablePointer|withUnsafeBytes|withUnsafeMutableBytes|unsafeBitCast|unsafeDowncast|assumeIsolated|assumeIsolated|MainActor|Sendable|async|await|Task|TaskGroup|withTaskGroup|withThrowingTaskGroup|AsyncStream|AsyncThrowingStream|Continuation|AsyncIterator|AsyncSequence',
            php: 'echo|print|var_dump|print_r|isset|empty|unset|array|strlen|strpos|substr|str_replace|explode|implode|trim|strtolower|strtoupper|ucfirst|lcfirst|ucwords|number_format|round|floor|ceil|abs|max|min|count|sort|rsort|usort|in_array|array_push|array_pop|array_shift|array_unshift|array_merge|array_keys|array_values|array_map|array_filter|array_reduce|array_search|array_unique|array_reverse|array_slice|array_splice|array_flip|array_count_values|json_encode|json_decode|header|redirect|session_start|session_destroy|cookie|setcookie|filter_input|htmlspecialchars|htmlentities|nl2br|strip_tags|urlencode|urldecode|base64_encode|base64_decode|file_get_contents|file_put_contents|fopen|fclose|fread|fwrite|file_exists|is_file|is_dir|mkdir|rmdir|unlink|rename|copy|glob|scandir|opendir|readdir|closedir|date|time|strtotime|mktime|checkdate|strftime|gmdate|datetime|timezone|PDO|mysqli|curl_init|curl_exec|curl_close',
            ruby: 'puts|gets|chomp|to_i|to_f|to_s|to_a|to_h|length|size|each|map|select|reject|reduce|inject|collect|find|detect|any|all|none|one|count|min|max|sort|sort_by|reverse|flatten|compact|uniq|join|split|strip|gsub|sub|match|scan|include|start_with|end_with|nil|true|false|require|require_relative|include|extend|attr_accessor|attr_reader|attr_writer|raise|rescue|ensure|begin|end|def|class|module|do|yield|block_given|lambda|proc|Array|Hash|String|Integer|Float|Symbol|Regexp|Range|Enumerator|File|Dir|IO|Time|Date|DateTime|Exception|StandardError|ArgumentError|TypeError|NameError|NoMethodError|IndexError|KeyError|RuntimeError|RangeError|ZeroDivisionError|FloatDomainError|IOError|EOFError|SystemCallError|Errno|SignalException|Interrupt|SystemExit|ScriptError|SyntaxError|LoadError|NotImplementedError|SecurityError|ThreadError|FrozenError|LocalJumpError|RegexpError|Thread|Mutex|Queue|ConditionVariable|Monitor|Fiber|Ractor|GC|ObjectSpace|Method|UnboundMethod|Proc|Binding|Struct|OpenStruct|Set|SortedSet|Matrix|Vector|Complex|Rational|BigDecimal|Prime|Math|Comparable|Enumerable|Iterator|Numeric|Integer|Float|Rational|Complex|BigDecimal|Time|Date|DateTime|URI|Net|HTTP|OpenURI|JSON|CSV|YAML|ERB|IRB|RDoc|Gem|Bundler|Rake|Test|MiniTest|RSpec|Capybara|Selenium|Pry|Byebug|Rack|Rails|ActiveRecord|ActiveSupport|ActionController|ActionView|ActionMailer|ActionDispatch|ActionCable|ActiveJob|ActiveStorage|ActionMailbox|ActionText',
            dart: 'print|int|double|String|bool|List|Map|Set|Object|dynamic|var|final|const|void|Null|Never|Future|Stream|Iterable|Duration|DateTime|Uri|RegExp|BigInt|num|Comparable|num|Pattern|Match|Symbol|Enum|Type|Function|Invocation|StackTrace|Error|Exception|FormatException|IntegerDivisionByZeroException|IOException|IsolateSpawnException|NoSuchMethodError|NullPointerException|UnsupportedError|UnimplementedError|StateError|RangeError|ArgumentError|TypeError|CastError|AsyncError|TimeoutException|DeferredLoadException|ConcurrentModificationException|StackOverflowError|OutOfMemoryError|FileSystemException|HttpException|HandshakeException|RedirectException|SocketException|TlsException|WebSocketException|ProcessException|StdinException|StdoutException|FileSystemException|PathAccessException'
        };

        function escapeHtml(text) {
            return text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
        }

        function highlight(code, lang) {
            var html = escapeHtml(code);
            var tokens = [];
            var result = html;

            /* 1. 提取字符串（单引号、双引号、模板字符串） */
            result = result.replace(/(&#39;(?:[^&]|&(?!#39;))*?&#39;|&quot;(?:[^&]|&(?!quot;))*?&quot;)/g, function(m) {
                var idx = tokens.length;
                tokens.push('<span class="hl-string">' + m + '</span>');
                return '%%TK' + idx + '%%';
            });

            /* 2. 提取注释（单行 // 和多行 /* *​/） */
            result = result.replace(/(\/\/[^\n]*)/g, function(m) {
                var idx = tokens.length;
                tokens.push('<span class="hl-comment">' + m + '</span>');
                return '%%TK' + idx + '%%';
            });
            result = result.replace(/(\/\*[\s\S]*?\*\/)/g, function(m) {
                var idx = tokens.length;
                tokens.push('<span class="hl-comment">' + m + '</span>');
                return '%%TK' + idx + '%%';
            });

            /* 3. 提取 Python/Ruby 多行字符串和注释 */
            if (lang === 'python' || lang === 'ruby') {
                result = result.replace(/(#[^\n]*)/g, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-comment">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
                result = result.replace(/(&quot;&quot;&quot;[\s\S]*?&quot;&quot;&quot;|&#39;&#39;&#39;[\s\S]*?&#39;&#39;&#39;)/g, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-string">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
            }

            /* 4. SQL 单行注释 */
            if (lang === 'sql') {
                result = result.replace(/(--[^\n]*)/g, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-comment">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
            }

            /* 5. HTML 注释 */
            if (lang === 'html' || lang === 'xml') {
                result = result.replace(/(&lt;!--[\s\S]*?--&gt;)/g, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-comment">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
            }

            /* 6. 提取内置类型/函数 */
            var builtIns = BUILT_INS[lang];
            if (builtIns) {
                var biPattern = new RegExp('\\b(' + builtIns + ')\\b', 'g');
                result = result.replace(biPattern, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-built_in">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
            }

            /* 7. 提取关键字 */
            var keywords = LANG_KEYWORDS[lang];
            if (keywords) {
                var kwPattern = new RegExp('\\b(' + keywords + ')\\b', 'g');
                result = result.replace(kwPattern, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-keyword">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
            }

            /* 8. 提取数字 */
            result = result.replace(/\b(\d+\.?\d*(?:e[+-]?\d+)?)\b/gi, function(m) {
                var idx = tokens.length;
                tokens.push('<span class="hl-number">' + m + '</span>');
                return '%%TK' + idx + '%%';
            });

            /* 9. HTML 标签 */
            if (lang === 'html' || lang === 'xml') {
                result = result.replace(/(&lt;\/?[\w-]+)/g, function(m) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-tag">' + m + '</span>');
                    return '%%TK' + idx + '%%';
                });
            }

            /* 10. CSS 属性 */
            if (lang === 'css') {
                result = result.replace(/([\w-]+)(\s*:)/g, function(m, p1, p2) {
                    var idx = tokens.length;
                    tokens.push('<span class="hl-attr">' + p1 + '</span>' + p2);
                    return '%%TK' + idx + '%%';
                });
            }

            /* 还原占位符 */
            for (var i = tokens.length - 1; i >= 0; i--) {
                result = result.replace('%%TK' + i + '%%', tokens[i]);
            }

            return result;
        }

        function highlightAll() {
            document.querySelectorAll('pre code[class*="language-"]').forEach(function(block) {
                var lang = (block.className.match(/language-(\w+)/) || [])[1];
                if (!lang) return;
                var text = block.textContent;
                block.innerHTML = highlight(text, lang);
            });
        }

        highlightAll();
    })();
    """

    /**
     * 复制按钮 JavaScript
     *
     * 功能：为每个代码块添加复制按钮，点击后复制代码内容到剪贴板
     * 输入：DOM 中的 pre > code 元素
     * 输出：每个代码块右上角的复制按钮
     *
     * 算法：
     * 1. 遍历所有 pre > code 元素
     * 2. 创建 .code-block 容器包裹 pre
     * 3. 添加语言标签（data-lang 属性）
     * 4. 创建复制按钮，绑定点击事件
     * 5. 优先使用 Clipboard API，回退到 execCommand
     */
    private fun getCopyButtonJs(): String = """
    (function() {
        function initCopyButtons() {
            document.querySelectorAll('pre > code').forEach(function(codeEl) {
                var pre = codeEl.parentElement;
                if (!pre || pre.parentElement.classList.contains('code-block')) return;

                var wrapper = document.createElement('div');
                wrapper.className = 'code-block';

                var lang = (codeEl.className.match(/language-(\S+)/) || [])[1] || '';
                if (lang) wrapper.setAttribute('data-lang', lang);

                var btn = document.createElement('button');
                btn.className = 'copy-btn';
                btn.setAttribute('aria-label', 'Copy code');
                btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>';

                btn.addEventListener('click', function() {
                    var text = codeEl.textContent || '';
                    try {
                        if (navigator.clipboard && navigator.clipboard.writeText) {
                            navigator.clipboard.writeText(text).then(function() {
                                showCopied(btn);
                            }).catch(function() {
                                fallbackCopy(text, btn);
                            });
                        } else {
                            fallbackCopy(text, btn);
                        }
                    } catch(e) {
                        fallbackCopy(text, btn);
                    }
                });

                pre.parentNode.insertBefore(wrapper, pre);
                wrapper.appendChild(pre);
                wrapper.appendChild(btn);
            });
        }

        function showCopied(btn) {
            btn.classList.add('copied');
            btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>';
            setTimeout(function() {
                btn.classList.remove('copied');
                btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>';
            }, 2000);
        }

        function fallbackCopy(text, btn) {
            var ta = document.createElement('textarea');
            ta.value = text;
            ta.style.cssText = 'position:fixed;opacity:0;left:-9999px';
            document.body.appendChild(ta);
            ta.select();
            document.execCommand('copy');
            document.body.removeChild(ta);
            showCopied(btn);
        }

        initCopyButtons();
    })();
    """

    /**
     * 主题切换 JavaScript
     *
     * 功能：提供 toggleTheme 函数，供 Kotlin 端通过 evaluateJavascript 调用
     * 输入：isDark 布尔值
     * 输出：切换 HTML 根元素的 data-theme 属性，触发 CSS 变量切换
     */
    private fun getThemeJs(): String = """
    function toggleTheme(isDark) {
        document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
    }
    """

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

    /**
     * 自定义 AttributeProvider
     *
     * 功能：为 commonmark AST 节点添加 CSS class，使生成的 HTML 匹配原版样式
     * 输入：AST 节点和属性映射
     * 输出：为特定节点添加 class 属性
     *
     * 处理逻辑：
     * - FencedCodeBlock: 添加 language-xxx class 到 code 元素
     * - Heading: 添加 h1-h6 级别标识
     * - BlockQuote: 添加 blockquote 标识
     * - 其他节点保持默认行为
     */
    private class FandexAttributeProvider : AttributeProvider {
        override fun setAttributes(node: Node, tagName: String, attributes: MutableMap<String, String>) {
            when (node) {
                is FencedCodeBlock -> {
                    val lang = node.info
                    if (lang.isNotBlank()) {
                        attributes["class"] = "language-$lang"
                    }
                }
            }
        }
    }
}
