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
 * 输出：完整 HTML 字符串（含 CSS + JS）
 *
 * 设计原则：
 * - CSS 风格匹配 Material 3 主题，与主页风格统一
 * - 代码块自适应宽度，不溢出屏幕
 * - 复制按钮不遮挡代码内容
 * - 暗色/亮色模式通过 CSS 变量切换，无需重载
 */
object MarkdownRenderer {

    /** commonmark 解析器（线程安全，复用） */
    private val parser: Parser = Parser.builder()
        .extensions(listOf(TablesExtension.create(), StrikethroughExtension.create()))
        .build()

    /** HTML 渲染器（线程安全，复用） */
    private val renderer: HtmlRenderer = HtmlRenderer.builder()
        .attributeProviderFactory { FandexAttributeProvider() }
        .extensions(listOf(TablesExtension.create(), StrikethroughExtension.create()))
        .build()

    /**
     * 将 Markdown 文本转换为完整 HTML 页面
     *
     * 输入：标题、Markdown 内容、是否暗色模式
     * 输出：完整 HTML 字符串
     */
    fun render(title: String, markdown: String, isDarkMode: Boolean = false): String {
        val document = parser.parse(markdown)
        val bodyHtml = renderer.render(document)
        return buildHtmlPage(title, bodyHtml, isDarkMode)
    }

    /**
     * 构建完整 HTML 页面
     */
    private fun buildHtmlPage(title: String, body: String, isDarkMode: Boolean): String {
        val themeAttr = if (isDarkMode) "dark" else "light"
        return """
        <!DOCTYPE html>
        <html lang="zh-CN" data-theme="$themeAttr">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
            <title>${escapeHtml(title)}</title>
            <style>
                ${getCssVariables(isDarkMode)}
                ${getBaseCss()}
                ${getProseCss()}
                ${getCodeCss()}
                ${getCopyBtnCss()}
                ${getHighlightCss()}
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
     * CSS 变量定义（匹配 Material 3 色彩体系）
     */
    private fun getCssVariables(isDarkMode: Boolean): String {
        return if (isDarkMode) """
        :root {
            --md-primary: #6ea8fe;
            --md-on-primary: #0d0d0d;
            --md-secondary: #55efc4;
            --md-tertiary: #f09070;
            --md-bg: #0d0d0d;
            --md-surface: #181818;
            --md-surface-variant: #202020;
            --md-on-bg: #ebebeb;
            --md-on-surface: #b3b3b3;
            --md-on-surface-variant: #8a8a8a;
            --md-outline: #525252;
            --md-outline-variant: #383838;
            --md-code-bg: #1a1a1a;
            --md-code-border: #333333;
            --md-table-header-bg: #3366cc;
            --md-table-header-fg: #ffffff;
            --md-blockquote-bg: #181818;
            --md-blockquote-border: #6ea8fe;
            --font-body: 'Noto Sans SC','PingFang SC','Microsoft YaHei',sans-serif;
            --font-code: 'JetBrains Mono','Fira Code','Consolas',monospace;
        }
        """ else """
        :root {
            --md-primary: #3366cc;
            --md-on-primary: #ffffff;
            --md-secondary: #00b894;
            --md-tertiary: #e05a2b;
            --md-bg: #fafafa;
            --md-surface: #ffffff;
            --md-surface-variant: #f0f0f0;
            --md-on-bg: #0d0d0d;
            --md-on-surface: #4d4d4d;
            --md-on-surface-variant: #808080;
            --md-outline: #2e2e2e;
            --md-outline-variant: #c4c4c4;
            --md-code-bg: #f5f5f5;
            --md-code-border: #e0e0e0;
            --md-table-header-bg: #3366cc;
            --md-table-header-fg: #ffffff;
            --md-blockquote-bg: #f5f5f5;
            --md-blockquote-border: #3366cc;
            --font-body: 'Noto Sans SC','PingFang SC','Microsoft YaHei',sans-serif;
            --font-code: 'JetBrains Mono','Fira Code','Consolas',monospace;
        }
        """
    }

    /**
     * 基础重置样式
     */
    private fun getBaseCss(): String = """
    *,*::before,*::after{margin:0;padding:0;box-sizing:border-box;}
    html{font-size:16px;overscroll-behavior:none;}
    body{
        min-height:100vh;
        font-family:var(--font-body);
        font-size:1rem;
        line-height:1.85;
        color:var(--md-on-bg);
        background:var(--md-bg);
        -webkit-font-smoothing:antialiased;
        overflow-wrap:break-word;
        word-break:break-word;
        -webkit-user-select:none;
        user-select:none;
    }
    code,pre,input,textarea{-webkit-user-select:text;user-select:text;}
    ::selection{background:var(--md-primary);color:var(--md-on-primary);}
    ::-webkit-scrollbar{width:4px;height:4px;}
    ::-webkit-scrollbar-track{background:transparent;}
    ::-webkit-scrollbar-thumb{background:var(--md-outline-variant);border-radius:2px;}
    """

    /**
     * 排版样式（匹配主页 Material 3 风格）
     */
    private fun getProseCss(): String = """
    .prose{max-width:100%;margin:0 auto;padding:0 16px 32px;line-height:1.9;}
    .prose h1,.prose h2,.prose h3,.prose h4,.prose h5,.prose h6{
        font-family:var(--font-body);font-weight:700;line-height:1.4;color:var(--md-on-bg);margin-top:1.5em;margin-bottom:0.5em;
    }
    .prose h1{font-size:1.5em;border-left:3px solid var(--md-primary);padding-left:0.5em;padding-bottom:0.3em;}
    .prose h2{font-size:1.25em;border-bottom:2px solid var(--md-secondary);padding-bottom:0.25em;}
    .prose h3{font-size:1.1em;border-left:3px solid var(--md-tertiary);padding-left:0.5em;}
    .prose h4{font-size:1em;text-decoration:underline;text-underline-offset:0.2em;text-decoration-color:var(--md-outline-variant);}
    .prose h5{font-size:0.95em;}
    .prose h6{font-size:0.9em;color:var(--md-on-surface);}
    .prose h1:first-child{margin-top:0;}
    .prose p{margin:0.8em 0;text-align:justify;}
    .prose a{color:var(--md-primary);text-decoration:underline;text-decoration-style:dotted;text-underline-offset:0.15em;}
    .prose a:hover{text-decoration-style:solid;}
    .prose ul,.prose ol{padding-left:1.8em;margin:0.7em 0;}
    .prose ul{list-style:none;}
    .prose ul>li{position:relative;padding-left:0.4em;}
    .prose ul>li::before{content:'';position:absolute;left:-1.1em;top:0.65em;width:5px;height:5px;border-radius:50%;background:var(--md-primary);opacity:0.6;}
    .prose ol{counter-reset:ol-counter;list-style:none;}
    .prose ol>li{position:relative;padding-left:0.4em;counter-increment:ol-counter;}
    .prose ol>li::before{content:counter(ol-counter) '.';position:absolute;left:-1.8em;width:1.4em;text-align:right;font-size:0.85em;font-weight:600;color:var(--md-primary);}
    .prose li{margin:0.3em 0;}
    .prose blockquote{border-left:3px solid var(--md-blockquote-border);padding:0.75em 1em;margin:1em 0;background:var(--md-blockquote-bg);color:var(--md-on-surface);border-radius:0 4px 4px 0;}
    .prose blockquote>p:first-child{margin-top:0;}
    .prose blockquote>p:last-child{margin-bottom:0;}
    .prose table{width:100%;border-collapse:collapse;margin:1em 0;border:1px solid var(--md-outline-variant);font-size:0.9em;display:block;overflow-x:auto;-webkit-overflow-scrolling:touch;}
    .prose thead{display:table-header-group;}
    .prose tbody{display:table-row-group;}
    .prose tr{display:table-row;}
    .prose th,.prose td{padding:0.6em 0.8em;text-align:left;border:1px solid var(--md-outline-variant);display:table-cell;}
    .prose th{background:var(--md-table-header-bg);color:var(--md-table-header-fg);font-weight:600;white-space:nowrap;}
    .prose tr:nth-child(even) td{background:var(--md-surface-variant);}
    .prose img{max-width:100%;border-radius:4px;}
    .prose hr{border:none;border-top:2px solid var(--md-outline-variant);margin:2em 0;}
    .prose strong{color:var(--md-primary);font-weight:700;}
    .prose del{opacity:0.5;}
    """

    /**
     * 代码块样式（修复溢出和复制按钮重叠）
     */
    private fun getCodeCss(): String = """
    .prose code{
        font-family:var(--font-code);
        background:var(--md-code-bg);
        color:var(--md-on-bg);
        padding:0.12em 0.35em;
        font-size:0.875em;
        border:1px solid var(--md-code-border);
        border-radius:3px;
        word-break:break-all;
    }
    .prose pre{
        margin:1em 0;
        overflow-x:auto;
        -webkit-overflow-scrolling:touch;
        border:1px solid var(--md-outline-variant);
        border-radius:6px;
        position:relative;
        background:var(--md-code-bg);
    }
    .prose pre code{
        display:block;
        padding:1em 1em 1em 1em;
        background:transparent;
        color:var(--md-on-bg);
        border:none;
        border-radius:0;
        font-size:0.85em;
        line-height:1.6;
        overflow-x:auto;
        white-space:pre;
        word-break:normal;
        word-wrap:normal;
    }
    .code-block{
        margin:1em 0;
        border:1px solid var(--md-outline-variant);
        position:relative;
        border-radius:6px;
        overflow:hidden;
        background:var(--md-code-bg);
    }
    .code-block pre{margin:0;border:none;border-radius:0;}
    .code-block pre code{padding:1em 1em 1em 1em;}
    .code-block[data-lang]::before{
        content:attr(data-lang);
        position:absolute;
        top:0;right:0;
        padding:0.15em 0.6em;
        font-family:var(--font-body);
        font-size:0.65em;
        text-transform:uppercase;
        letter-spacing:0.05em;
        font-weight:600;
        color:var(--md-on-surface-variant);
        background:var(--md-surface-variant);
        border-bottom-left-radius:4px;
        opacity:0.5;
        z-index:1;
        pointer-events:none;
    }
    .code-block:hover::before{opacity:0;}
    """

    /**
     * 复制按钮样式（不遮挡代码内容）
     */
    private fun getCopyBtnCss(): String = """
    .copy-btn{
        position:absolute;
        top:6px;right:6px;
        z-index:2;
        display:inline-flex;
        align-items:center;
        justify-content:center;
        gap:3px;
        padding:4px 8px;
        border:1px solid var(--md-outline-variant);
        background:var(--md-surface);
        color:var(--md-on-surface);
        font-family:var(--font-body);
        font-size:0.7em;
        cursor:pointer;
        opacity:0;
        transition:opacity 0.2s,background 0.2s,color 0.2s;
        border-radius:4px;
    }
    .code-block:hover .copy-btn{opacity:0.8;}
    .copy-btn:hover{background:var(--md-primary);color:var(--md-on-primary);opacity:1;}
    .copy-btn.copied{background:var(--md-tertiary);color:var(--md-on-primary);opacity:1;}
    .copy-btn svg{width:12px;height:12px;}
    """

    /**
     * 语法高亮 CSS
     */
    private fun getHighlightCss(): String = """
    .hl-keyword{color:#c678dd;font-weight:600;}
    .hl-string{color:#98c379;}
    .hl-comment{color:var(--md-on-surface-variant);font-style:italic;}
    .hl-number{color:#d19a66;}
    .hl-type{color:#e5c07b;}
    .hl-function{color:#61afef;}
    .hl-operator{color:#56b6c2;}
    .hl-tag{color:#e06c75;}
    .hl-attr{color:#d19a66;}
    .hl-built_in{color:#e5c07b;}
    [data-theme="light"] .hl-keyword{color:#a626a4;}
    [data-theme="light"] .hl-string{color:#50a14f;}
    [data-theme="light"] .hl-comment{color:#a0a1a7;font-style:italic;}
    [data-theme="light"] .hl-number{color:#986801;}
    [data-theme="light"] .hl-type{color:#c18401;}
    [data-theme="light"] .hl-function{color:#4078f2;}
    [data-theme="light"] .hl-operator{color:#0184bc;}
    [data-theme="light"] .hl-tag{color:#e45649;}
    [data-theme="light"] .hl-attr{color:#986801;}
    [data-theme="light"] .hl-built_in{color:#c18401;}
    """

    /**
     * 语法高亮 JavaScript（轻量级正则匹配）
     */
    private fun getHighlightJs(): String = """
    (function(){
        var LANG_KEYWORDS = {
            javascript:'async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|export|extends|finally|for|from|function|if|import|in|instanceof|let|new|of|return|static|super|switch|this|throw|try|typeof|var|void|while|with|yield',
            typescript:'abstract|any|as|async|await|boolean|break|case|catch|class|const|constructor|continue|debugger|declare|default|delete|do|else|enum|export|extends|finally|for|from|function|if|implements|import|in|instanceof|interface|let|module|namespace|new|null|number|of|package|private|protected|public|readonly|return|static|string|super|switch|this|throw|try|type|typeof|undefined|var|void|while|with|yield',
            python:'and|as|assert|async|await|break|class|continue|def|del|elif|else|except|finally|for|from|global|if|import|in|is|lambda|nonlocal|not|or|pass|raise|return|try|while|with|yield|True|False|None',
            java:'abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while',
            kotlin:'abstract|actual|annotation|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|else|enum|expect|external|false|final|finally|for|fun|if|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|true|try|typealias|typeof|val|var|vararg|when|where|while',
            go:'break|case|chan|const|continue|default|defer|else|fallthrough|for|func|go|goto|if|import|interface|map|package|range|return|select|struct|switch|type|var|true|false|nil',
            c:'auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while|NULL',
            cpp:'alignas|alignof|and|and_eq|asm|auto|bitand|bitor|bool|break|case|catch|char|class|compl|concept|const|const_cast|constexpr|continue|decltype|default|delete|do|double|dynamic_cast|else|enum|explicit|export|extern|false|float|for|friend|goto|if|inline|int|long|mutable|namespace|new|noexcept|not|not_eq|nullptr|operator|or|or_eq|private|protected|public|register|reinterpret_cast|requires|return|short|signed|sizeof|static|static_assert|static_cast|struct|switch|template|this|thread_local|throw|true|try|typedef|typeid|typename|union|unsigned|using|virtual|void|volatile|wchar_t|while|xor|xor_eq',
            sql:'select|from|where|insert|into|values|update|set|delete|create|table|alter|drop|index|join|inner|left|right|outer|on|and|or|not|null|is|in|between|like|order|by|group|having|as|distinct|count|sum|avg|min|max|limit|offset|union|all|exists|case|when|then|else|end|primary|key|foreign|references',
            rust:'as|async|await|break|const|continue|crate|dyn|else|enum|extern|false|fn|for|if|impl|in|let|loop|match|mod|move|mut|pub|ref|return|self|Self|static|struct|super|trait|true|type|unsafe|use|where|while|yield',
            bash:'if|then|else|elif|fi|case|esac|for|while|until|do|done|in|function|select|time|return|exit|break|continue|declare|export|local|readonly|unset|source|alias|echo|printf|read',
            shell:'if|then|else|elif|fi|case|esac|for|while|until|do|done|in|function|select|time|return|exit|break|continue|declare|export|local|readonly|unset|source|alias|echo|printf|read',
            yaml:'true|false|null|yes|no',
            json:'true|false|null',
            css:'align|animation|background|border|bottom|box|clear|clip|color|content|cursor|direction|display|filter|flex|float|font|grid|height|justify|left|letter|line|list|margin|max|min|opacity|order|outline|overflow|padding|perspective|pointer|position|resize|right|scroll|shadow|table|text|top|transform|transition|user|vertical|visibility|white|width|word|z-index',
            html:'a|abbr|address|area|article|aside|audio|b|base|blockquote|body|br|button|canvas|caption|cite|code|col|data|datalist|dd|del|details|dfn|dialog|div|dl|dt|em|embed|fieldset|figure|footer|form|h1|h2|h3|h4|h5|h6|head|header|hgroup|hr|html|i|iframe|img|input|ins|kbd|label|legend|li|link|main|map|mark|menu|meta|meter|nav|noscript|object|ol|optgroup|option|output|p|param|picture|pre|progress|q|rp|rt|ruby|s|samp|script|section|select|slot|small|source|span|strong|style|sub|summary|sup|table|tbody|td|template|textarea|tfoot|th|thead|time|title|tr|track|u|ul|var|video|wbr',
            swift:'associatedtype|as|async|await|break|case|catch|class|continue|default|defer|deinit|do|else|enum|extension|fallthrough|false|fileprivate|final|for|func|guard|if|import|in|init|inout|internal|is|lazy|let|mutating|nil|none|nonmutating|open|operator|optional|override|postfix|precedence|prefix|private|protocol|public|repeat|required|return|self|Self|static|struct|subscript|super|switch|throw|throws|true|try|typealias|unowned|var|weak|where|while|willSet',
            php:'abstract|and|array|as|break|callable|case|catch|class|clone|const|continue|declare|default|die|do|echo|else|elseif|empty|enddeclare|endfor|endforeach|endif|endswitch|endwhile|eval|exit|extends|final|finally|fn|for|foreach|function|global|goto|if|implements|include|include_once|instanceof|insteadof|interface|isset|list|match|namespace|new|or|print|private|protected|public|require|require_once|return|static|switch|throw|trait|try|unset|use|var|while|xor|yield',
            ruby:'alias|and|begin|break|case|class|def|defined|do|else|elsif|end|ensure|false|for|if|in|module|next|nil|not|or|redo|rescue|retry|return|self|super|then|true|undef|unless|until|when|while|yield',
            dart:'abstract|as|assert|async|await|break|case|catch|class|const|continue|covariant|default|deferred|do|dynamic|else|enum|export|extends|extension|external|factory|false|final|finally|for|Function|get|hide|if|implements|import|in|interface|is|late|library|mixin|new|null|on|operator|part|required|rethrow|return|set|show|static|super|switch|sync|this|throw|true|try|typedef|var|void|while|with|yield'
        };
        function escapeHtml(t){return t.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');}
        function highlight(code,lang){
            var html=escapeHtml(code),tokens=[],result=html;
            result=result.replace(/(&#39;(?:[^&]|&(?!#39;))*?&#39;|&quot;(?:[^&]|&(?!quot;))*?&quot;)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-string">'+m+'</span>');return '%%TK'+i+'%%';});
            result=result.replace(/(\/\/[^\n]*)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-comment">'+m+'</span>');return '%%TK'+i+'%%';});
            result=result.replace(/(\/\*[\s\S]*?\*\/)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-comment">'+m+'</span>');return '%%TK'+i+'%%';});
            if(lang==='python'||lang==='ruby'){result=result.replace(/(#[^\n]*)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-comment">'+m+'</span>');return '%%TK'+i+'%%';});}
            if(lang==='sql'){result=result.replace(/(--[^\n]*)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-comment">'+m+'</span>');return '%%TK'+i+'%%';});}
            if(lang==='html'||lang==='xml'){result=result.replace(/(&lt;!--[\s\S]*?--&gt;)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-comment">'+m+'</span>');return '%%TK'+i+'%%';});}
            var kw=LANG_KEYWORDS[lang];
            if(kw){result=result.replace(new RegExp('\\b('+kw+')\\b','g'),function(m){var i=tokens.length;tokens.push('<span class="hl-keyword">'+m+'</span>');return '%%TK'+i+'%%';});}
            result=result.replace(/\b(\d+\.?\d*(?:e[+-]?\d+)?)\b/gi,function(m){var i=tokens.length;tokens.push('<span class="hl-number">'+m+'</span>');return '%%TK'+i+'%%';});
            if(lang==='html'||lang==='xml'){result=result.replace(/(&lt;\/?[\w-]+)/g,function(m){var i=tokens.length;tokens.push('<span class="hl-tag">'+m+'</span>');return '%%TK'+i+'%%';});}
            for(var i=tokens.length-1;i>=0;i--){result=result.replace('%%TK'+i+'%%',tokens[i]);}
            return result;
        }
        document.querySelectorAll('pre code[class*="language-"]').forEach(function(block){
            var lang=(block.className.match(/language-(\w+)/)||[])[1];
            if(!lang)return;
            block.innerHTML=highlight(block.textContent,lang);
        });
    })();
    """

    /**
     * 复制按钮 JavaScript
     */
    private fun getCopyButtonJs(): String = """
    (function(){
        document.querySelectorAll('pre > code').forEach(function(codeEl){
            var pre=codeEl.parentElement;
            if(!pre||pre.parentElement.classList.contains('code-block'))return;
            var wrapper=document.createElement('div');
            wrapper.className='code-block';
            var lang=(codeEl.className.match(/language-(\S+)/)||[])[1]||'';
            if(lang)wrapper.setAttribute('data-lang',lang);
            var btn=document.createElement('button');
            btn.className='copy-btn';
            btn.setAttribute('aria-label','Copy');
            btn.innerHTML='<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>';
            btn.addEventListener('click',function(){
                var text=codeEl.textContent||'';
                try{
                    if(navigator.clipboard&&navigator.clipboard.writeText){
                        navigator.clipboard.writeText(text).then(function(){showCopied(btn);}).catch(function(){fallbackCopy(text,btn);});
                    }else{fallbackCopy(text,btn);}
                }catch(e){fallbackCopy(text,btn);}
            });
            pre.parentNode.insertBefore(wrapper,pre);
            wrapper.appendChild(pre);
            wrapper.appendChild(btn);
        });
        function showCopied(btn){
            btn.classList.add('copied');
            btn.innerHTML='<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>';
            setTimeout(function(){btn.classList.remove('copied');btn.innerHTML='<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>';},2000);
        }
        function fallbackCopy(text,btn){
            var ta=document.createElement('textarea');ta.value=text;ta.style.cssText='position:fixed;opacity:0;left:-9999px';document.body.appendChild(ta);ta.select();document.execCommand('copy');document.body.removeChild(ta);showCopied(btn);
        }
    })();
    """

    /**
     * 主题切换 JavaScript
     */
    private fun getThemeJs(): String = """
    function toggleTheme(isDark){document.documentElement.setAttribute('data-theme',isDark?'dark':'light');}
    """

    /** 转义 HTML 特殊字符 */
    private fun escapeHtml(text: String): String {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;")
    }

    /**
     * 自定义 AttributeProvider：为代码块添加 language-xxx class
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
