package com.fandex.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fandex.app.data.ContentIndex
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.Document

/**
 * 模块详情页面
 *
 * 功能：展示指定模块下的所有文档列表（含编号）
 * 输入：模块 ID、导航回调
 * 输出：带编号的文档卡片列表
 * 流程：加载索引 -> 查找模块 -> 渲染带编号的文档列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleScreen(
    moduleId: String,
    onNavigateBack: () -> Unit,
    onNavigateToArticle: (String, String, String) -> Unit
) {
    val context = LocalContext.current
    var index by remember { mutableStateOf<ContentIndex?>(null) }

    LaunchedEffect(Unit) {
        index = ContentLoader.loadIndex(context)
    }

    val contentIndex = index
    val module = contentIndex?.modules?.find { it.id == moduleId }
    val category = module?.let { m ->
        contentIndex.categories.find { it.id == m.category }
    }
    val documents = module?.let { m ->
        contentIndex.documents.filter { it.module == m.id }
    } ?: emptyList()

    /**
     * 解析分类颜色字符串为 Compose Color
     *
     * 输入：category?.color（十六进制颜色字符串）
     * 输出：Compose Color 对象，解析失败时返回主题 primary 色
     * 流程：使用 remember 缓存颜色值，try-catch 仅包裹非 Composable 的颜色解析逻辑
     */
    val accentColor = remember(category?.color) {
        category?.color?.let { colorStr ->
            try { Color(android.graphics.Color.parseColor(colorStr)) } catch (_: Exception) { null }
        } ?: Color(0xFF4F5BD5)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = module?.title ?: moduleId,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
        if (contentIndex == null || module == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            /* 模块信息 */
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(28.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${documents.size} docs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (module.description.isNotBlank()) {
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(8.dp))
            }

            /* 文档列表（带编号） */
            itemsIndexed(documents) { index, document ->
                DocumentListItem(
                    document = document,
                    index = index + 1,
                    accentColor = accentColor,
                    onClick = {
                        onNavigateToArticle(document.module, document.slug, document.title)
                    }
                )
            }
        }
    }
}

/**
 * 文档列表项（带编号）
 *
 * 功能：展示带编号的单个文档条目，点击跳转阅读
 * 输入：Document 对象、编号、强调色
 * 输出：可点击的带编号文档列表项
 */
@Composable
fun DocumentListItem(
    document: Document,
    index: Int,
    accentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /* 编号圆圈 */
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$index",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = document.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
