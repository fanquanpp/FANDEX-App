package com.fandex.app.home

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fandex.app.data.Category
import com.fandex.app.data.ContentIndex
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.Document
import com.fandex.app.reader.ArticleActivity

/**
 * 首页界面组件
 *
 * 功能：按分类展示模块列表，支持点击进入文档阅读
 * 输入：模块分类数据（从 assets 加载）
 * 输出：可滚动的分类-文档列表
 * 流程：加载索引 -> 遍历分类 -> 渲染分类标题 -> 渲染文档卡片
 */
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var index by remember { mutableStateOf<ContentIndex?>(null) }

    /* 首次渲染时加载索引数据 */
    LaunchedEffect(Unit) {
        index = ContentLoader.loadIndex(context)
    }

    val contentIndex = index

    if (contentIndex == null) {
        /* 加载中状态 */
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "FANDEX",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "正在加载内容索引...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        /* 标题区域 */
        item {
            Text(
                text = "FANDEX",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "循序渐进，从第一行代码到理解整个世界",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        /* 按分类渲染文档列表 */
        val documents = contentIndex.documents
        val categories = contentIndex.categories

        if (documents.isEmpty()) {
            /* 无文档时显示分类概览卡片 */
            items(categories) { category ->
                CategoryOverviewCard(category = category)
            }
        } else {
            /* 有文档时按分类分组渲染 */
            val groupedDocs = documents.groupBy { it.category }
            items(categories) { category ->
                val categoryDocs = groupedDocs[category.id] ?: emptyList()
                if (categoryDocs.isNotEmpty()) {
                    CategorySection(
                        category = category,
                        documents = categoryDocs
                    )
                }
            }
        }
    }
}

/**
 * 分类概览卡片
 *
 * 功能：在无文档数据时展示分类信息
 * 输入：Category 对象
 * 输出：带颜色标识的分类卡片
 */
@Composable
fun CategoryOverviewCard(category: Category) {
    val cardColor = try {
        Color(android.graphics.Color.parseColor(category.color))
    } catch (_: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* 分类颜色标识条 */
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(cardColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = category.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = category.id,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 分类文档区块
 *
 * 功能：展示某分类下的所有文档
 * 输入：Category 和该分类下的文档列表
 * 输出：分类标题 + 文档卡片列表
 */
@Composable
fun CategorySection(
    category: Category,
    documents: List<Document>
) {
    val cardColor = try {
        Color(android.graphics.Color.parseColor(category.color))
    } catch (_: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        /* 分类标题 */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(cardColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${documents.size} 篇",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        /* 文档卡片列表 */
        documents.forEach { document ->
            DocumentCard(
                document = document,
                accentColor = cardColor
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/**
 * 文档卡片
 *
 * 功能：展示单个文档条目，点击跳转阅读
 * 输入：Document 对象和强调色
 * 输出：可点击的文档卡片
 */
@Composable
fun DocumentCard(
    document: Document,
    accentColor: Color
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ArticleActivity::class.java).apply {
                    putExtra("module_id", document.module)
                    putExtra("slug", document.slug)
                    putExtra("category", document.category)
                    putExtra("title", document.title)
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* 文档颜色圆点 */
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            /* 文档标题 */
            Text(
                text = document.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
        }
    }
}
