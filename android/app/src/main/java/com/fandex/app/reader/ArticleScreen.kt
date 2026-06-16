package com.fandex.app.reader

import android.graphics.Color as AndroidColor
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fandex.app.data.ContentIndex
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.Document
import com.fandex.app.data.MarkdownRenderer
import com.fandex.app.data.Strings

/**
 * 文章阅读页面
 *
 * 功能：使用 WebView 渲染 Markdown 转 HTML 的文档内容，支持翻页和返回顶部
 * 输入：模块 ID、文档 slug、文档标题、是否暗色模式、语言设置
 * 输出：WebView 展示渲染后的文档 + 底部翻页按钮 + 返回顶部按钮
 * 流程：接收参数 -> 查找当前模块文档列表 -> AndroidView factory 中加载 Markdown -> 渲染 HTML
 *
 * 修复项：
 * - 闪屏：WebView 背景色在 factory 中预先设置，避免白色闪烁
 * - 复制按钮重叠：CSS 中复制按钮定位修正
 * - 代码块溢出：CSS 中 pre/code 强制 word-wrap + overflow-x: auto
 * - 翻页：底部上一页/下一页按钮
 * - 返回顶部：悬浮按钮
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    moduleId: String,
    slug: String,
    title: String,
    isDarkMode: Boolean = false,
    language: Strings.Language = Strings.Language.ZH,
    onNavigateBack: () -> Unit,
    onNavigateToArticle: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    val strings = Strings.get(language)
    var webView by remember { mutableStateOf<WebView?>(null) }

    /* 加载索引，获取当前模块的文档列表用于翻页 */
    var contentIndex by remember { mutableStateOf<ContentIndex?>(null) }
    LaunchedEffect(Unit) {
        contentIndex = ContentLoader.loadIndex(context)
    }

    /* 查找当前文档在模块中的位置 */
    val documents = contentIndex?.documents?.filter { it.module == moduleId } ?: emptyList()
    val currentIndex = documents.indexOfFirst { it.slug == slug }
    val prevDoc: Document? = if (currentIndex > 0) documents[currentIndex - 1] else null
    val nextDoc: Document? = if (currentIndex >= 0 && currentIndex < documents.size - 1) documents[currentIndex + 1] else null

    /* 当 isDarkMode 变化时，通过 JS 切换 WebView 主题（无需重新加载） */
    LaunchedEffect(isDarkMode) {
        webView?.evaluateJavascript("toggleTheme($isDarkMode)", null)
    }

    /* WebView 背景色，匹配 Compose 主题，避免闪屏 */
    val webViewBgColor = if (isDarkMode) AndroidColor.parseColor("#0d0d0d") else AndroidColor.parseColor("#fafafa")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            /* 顶部栏 */
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            /* WebView 内容区 */
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        /* 预设背景色，防止白色闪屏 */
                        setBackgroundColor(webViewBgColor)
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = WebViewClient()

                        /* 在 factory 中一次性加载文档内容 */
                        val markdown = ContentLoader.loadDocumentMarkdown(context, moduleId, slug)
                        val content = markdown ?: strings.noContent
                        val html = MarkdownRenderer.render(title, content, isDarkMode)
                        loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)

                        webView = this
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            /* 底部翻页按钮栏 */
            if (documents.size > 1) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /* 上一篇 */
                        TextButton(
                            onClick = {
                                prevDoc?.let { doc ->
                                    onNavigateToArticle(doc.module, doc.slug, doc.title)
                                }
                            },
                            enabled = prevDoc != null
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = strings.previousDoc,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = strings.previousDoc,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        /* 页码 */
                        Text(
                            text = "${currentIndex + 1} / ${documents.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        /* 下一篇 */
                        TextButton(
                            onClick = {
                                nextDoc?.let { doc ->
                                    onNavigateToArticle(doc.module, doc.slug, doc.title)
                                }
                            },
                            enabled = nextDoc != null
                        ) {
                            Text(
                                text = strings.nextDoc,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = strings.nextDoc,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        /* 返回顶部悬浮按钮 */
        FloatingActionButton(
            onClick = {
                webView?.pageUp(true)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = if (documents.size > 1) 64.dp else 16.dp
                ),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = strings.backToTop,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
