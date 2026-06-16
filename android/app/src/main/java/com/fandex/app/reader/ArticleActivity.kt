package com.fandex.app.reader

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity

/**
 * 文章阅读 Activity
 *
 * 功能：使用 WebView 渲染 Markdown 转 HTML 的文档内容
 * 输入：Intent extra - moduleId 和 slug
 * 输出：WebView 展示渲染后的文档
 * 流程：接收参数 -> 加载 HTML -> WebView 渲染
 */
class ArticleActivity : ComponentActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val moduleId = intent.getStringExtra("module_id") ?: ""
        val slug = intent.getStringExtra("slug") ?: ""

        webView = WebView(this)
        webView.settings.javaScriptEnabled = false
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()

        // TODO: 从 assets/dist-mobile/ 加载预渲染的 HTML
        webView.loadData("<h1>Loading...</h1>", "text/html", "UTF-8")
        setContentView(webView)
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
