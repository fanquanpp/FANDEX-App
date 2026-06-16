package com.fandex.app.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fandex.app.data.MarkdownContent
import com.fandex.app.data.ContentIndex
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.Document
import com.fandex.app.data.Strings
import kotlinx.coroutines.launch

/**
 * 文章阅读页面
 *
 * 功能：使用 ComposeMarkdown 原生渲染 Markdown 文档内容，支持翻页、字体缩放和返回顶部
 * 输入：
 *   - moduleId: 当前模块 ID
 *   - slug: 文档唯一标识
 *   - title: 文档标题
 *   - isDarkMode: 是否暗色模式
 *   - language: 语言设置
 *   - fontSizeScale: 字体缩放比例（0.8-1.4）
 *   - onNavigateBack: 返回上一页回调
 *   - onNavigateToArticle: 翻页导航回调（moduleId, slug, title）
 *   - onOpenDrawer: 打开侧边栏回调
 *   - onFontSizeChange: 字体大小变更回调，通知外部持久化
 * 输出：原生 Compose 渲染的文档阅读界面
 * 流程：
 *   1. 加载索引，查找当前文档在模块中的位置
 *   2. 加载 Markdown 文本，传入 MarkdownContent 原生渲染
 *   3. 顶部栏提供返回、标题、字体缩放、菜单操作
 *   4. 底部栏提供上一篇/下一篇翻页及页码显示
 *   5. 右下角返回顶部悬浮按钮
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    moduleId: String,
    slug: String,
    title: String,
    isDarkMode: Boolean = true,
    language: Strings.Language = Strings.Language.ZH,
    fontSizeScale: Float = 1.0f,
    onNavigateBack: () -> Unit,
    onNavigateToArticle: (String, String, String) -> Unit = { _, _, _ -> },
    onOpenDrawer: () -> Unit = {},
    onFontSizeChange: (Float) -> Unit = {}
) {
    val context = LocalContext.current
    val strings = Strings.get(language)

    /* 滚动状态，用于返回顶部动画 */
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    /* 字体缩放步进值：每次点击增减 0.1 */
    val fontSizeStep = 0.1f

    /* 加载索引，获取当前模块的文档列表用于翻页 */
    var contentIndex by remember { mutableStateOf<ContentIndex?>(null) }
    LaunchedEffect(Unit) {
        contentIndex = ContentLoader.loadIndex(context)
    }

    /* 加载当前文档的 Markdown 文本 */
    var markdownText by remember { mutableStateOf("") }
    LaunchedEffect(moduleId, slug) {
        val loaded = ContentLoader.loadDocumentMarkdown(context, moduleId, slug)
        markdownText = loaded ?: strings.noContent
    }

    /* 查找当前文档在模块中的位置，计算上一篇/下一篇 */
    val documents = contentIndex?.documents?.filter { it.module == moduleId } ?: emptyList()
    val currentIndex = documents.indexOfFirst { it.slug == slug }
    val prevDoc: Document? = if (currentIndex > 0) documents[currentIndex - 1] else null
    val nextDoc: Document? = if (currentIndex >= 0 && currentIndex < documents.size - 1) documents[currentIndex + 1] else null

    /* 底部翻页栏高度，用于返回顶部按钮的底部偏移计算 */
    val bottomBarHeight = if (documents.size > 1) 56.dp else 0.dp

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            /* 紧凑顶部栏：返回 + 标题 + 字体缩放 + 菜单 */
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
                    /* 返回按钮 */
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = strings.back
                        )
                    }
                },
                actions = {
                    /* 字体缩小按钮 (A-) */
                    IconButton(
                        onClick = {
                            val newScale = (fontSizeScale - fontSizeStep).coerceIn(0.8f, 1.4f)
                            onFontSizeChange(newScale)
                        },
                        enabled = fontSizeScale > 0.8f
                    ) {
                        Icon(
                            Icons.Filled.TextDecrease,
                            contentDescription = "A-"
                        )
                    }

                    /* 字体放大按钮 (A+) */
                    IconButton(
                        onClick = {
                            val newScale = (fontSizeScale + fontSizeStep).coerceIn(0.8f, 1.4f)
                            onFontSizeChange(newScale)
                        },
                        enabled = fontSizeScale < 1.4f
                    ) {
                        Icon(
                            Icons.Filled.TextIncrease,
                            contentDescription = "A+"
                        )
                    }

                    /* 菜单按钮，打开侧边栏 */
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = strings.menu
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            /* Markdown 原生渲染内容区，scrollState 传入以支持返回顶部动画 */
            Box(modifier = Modifier.weight(1f)) {
                MarkdownContent(
                    markdown = markdownText,
                    isDarkMode = isDarkMode,
                    fontSizeScale = fontSizeScale,
                    scrollState = scrollState
                )
            }

            /* 底部翻页栏：紧凑设计，仅在有多个文档时显示 */
            if (documents.size > 1) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /* 上一篇按钮 */
                        TextButton(
                            onClick = {
                                prevDoc?.let { doc ->
                                    onNavigateToArticle(doc.module, doc.slug, doc.title)
                                }
                            },
                            enabled = prevDoc != null,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = strings.previousDoc,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = strings.previousDoc,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        /* 页码指示器（如 3/15） */
                        Text(
                            text = "${currentIndex + 1}/${documents.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        /* 下一篇按钮 */
                        TextButton(
                            onClick = {
                                nextDoc?.let { doc ->
                                    onNavigateToArticle(doc.module, doc.slug, doc.title)
                                }
                            },
                            enabled = nextDoc != null,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = strings.nextDoc,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(2.dp))
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

        /* 返回顶部悬浮按钮：右下角小圆形，使用 scrollState.animateScrollTo(0) */
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 12.dp,
                    bottom = bottomBarHeight + 12.dp
                )
                .size(36.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
        ) {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                contentDescription = strings.backToTop,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
