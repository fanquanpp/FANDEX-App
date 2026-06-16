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
import androidx.compose.material.icons.filled.Menu
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
import com.fandex.app.data.Strings

/**
 * 模块详情页面
 *
 * 功能：展示指定模块下的所有文档列表（含编号圆圈），文档间以分类色短分界线分隔
 * 输入：模块 ID、语言设置、导航回调、侧边栏打开回调
 * 输出：带编号圆圈和分界线的文档卡片列表
 * 流程：加载索引 -> 查找模块与分类 -> 渲染紧凑顶部栏 -> 渲染模块信息区 -> 渲染带编号的文档列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleScreen(
    moduleId: String,
    language: Strings.Language = Strings.Language.ZH,
    onNavigateBack: () -> Unit,
    onNavigateToArticle: (String, String, String) -> Unit,
    onOpenDrawer: () -> Unit = {}
) {
    val context = LocalContext.current
    val strings = Strings.get(language)
    var index by remember { mutableStateOf<ContentIndex?>(null) }

    /* 首次渲染时加载索引数据 */
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
     * 输出：Compose Color 对象，解析失败时返回默认色
     * 流程：使用 remember 缓存颜色值，try-catch 包裹颜色解析逻辑
     */
    val accentColor = remember(category?.color) {
        category?.color?.let { colorStr ->
            try { Color(android.graphics.Color.parseColor(colorStr)) } catch (_: Exception) { null }
        } ?: Color(0xFF4F5BD5)
    }

    Scaffold(
        topBar = {
            /* 紧凑顶部栏：返回按钮 + 模块标题 + 菜单按钮 */
            TopAppBar(
                title = {
                    Text(
                        text = module?.title ?: moduleId,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = strings.back
                        )
                    }
                },
                actions = {
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
        }
    ) { paddingValues ->
        if (contentIndex == null || module == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            /* 模块信息区：标题 + 分类名 + 文档数 + 简介描述 */
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    /* 分类色竖条 */
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(24.dp)
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
                        /* 分类名 + 文档数 */
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = category?.label ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${documents.size} ${strings.docs}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                /* 简介描述 */
                if (module.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                /* 模块信息与文档列表间的分类色短分界线 */
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(accentColor.copy(alpha = 0.4f))
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            /* 文档列表（带编号圆圈和分界线） */
            itemsIndexed(documents) { index, document ->
                DocumentListItem(
                    document = document,
                    index = index + 1,
                    accentColor = accentColor,
                    onClick = {
                        onNavigateToArticle(document.module, document.slug, document.title)
                    }
                )
                /* 文档间短分界线（最后一个不加） */
                if (index < documents.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 28.dp, top = 2.dp, bottom = 2.dp)
                            .width(24.dp)
                            .height(1.dp)
                            .clip(RoundedCornerShape(0.5.dp))
                            .background(accentColor.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

/**
 * 文档列表项（带编号圆圈）
 *
 * 功能：展示带编号圆圈的单个文档条目，包含编号、标题、slug
 * 输入：Document 对象、编号、强调色、点击回调
 * 输出：可点击的带编号圆圈文档列表项
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
            .padding(horizontal = 4.dp, vertical = 6.dp),
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
            /* 文档标题 */
            Text(
                text = document.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            /* slug 小字灰色 */
            Text(
                text = document.slug,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
