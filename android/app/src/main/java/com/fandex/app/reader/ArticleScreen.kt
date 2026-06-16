package com.fandex.app.reader

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.MarkdownRenderer

/**
 * 文章阅读页面
 *
 * 功能：使用 WebView 渲染 Markdown 转 HTML 的文档内容
 * 输入：模块 ID、文档 slug、文档标题、是否暗色模式
 * 输出：WebView 展示渲染后的文档（支持语法高亮、代码复制、主题切换）
 * 流程：接收参数 -> AndroidView factory 中加载 Markdown -> 转换为 HTML -> WebView 渲染
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    moduleId: String,
    slug: String,
    title: String,
    isDarkMode: Boolean = false,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    /* 保存 WebView 引用，用于主题切换时调用 JS */
    var webView by remember { mutableStateOf<WebView?>(null) }

    /* 当 isDarkMode 变化时，通过 JS 切换 WebView 主题（无需重新加载） */
    LaunchedEffect(isDarkMode) {
        webView?.evaluateJavascript("toggleTheme($isDarkMode)", null)
    }

    Scaffold(
        topBar = {
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    /* 启用 JavaScript：语法高亮、复制按钮、主题切换均依赖 JS */
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = WebViewClient()

                    /* 在 factory 中一次性加载文档内容 */
                    val markdown = ContentLoader.loadDocumentMarkdown(context, moduleId, slug)
                    val content = markdown ?: "No content available"
                    val html = MarkdownRenderer.render(title, content, isDarkMode)
                    loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)

                    webView = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}
