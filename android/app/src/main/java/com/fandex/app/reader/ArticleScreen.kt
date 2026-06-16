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
 * 输入：模块 ID、文档 slug、文档标题
 * 输出：WebView 展示渲染后的文档
 * 流程：接收参数 -> AndroidView factory 中加载 Markdown -> 转换为 HTML -> WebView 渲染
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    moduleId: String,
    slug: String,
    title: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

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
                    settings.javaScriptEnabled = false
                    settings.domStorageEnabled = true
                    webViewClient = WebViewClient()

                    /* 在 factory 中一次性加载文档内容 */
                    val markdown = ContentLoader.loadDocumentMarkdown(context, moduleId, slug)
                    val content = markdown ?: "No content available"
                    val html = MarkdownRenderer.render(title, content)
                    loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}
