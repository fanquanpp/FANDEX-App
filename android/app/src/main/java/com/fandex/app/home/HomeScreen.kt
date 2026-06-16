package com.fandex.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fandex.app.data.Category
import com.fandex.app.data.ContentIndex
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.Document
import com.fandex.app.data.Module
import com.fandex.app.data.Strings

/**
 * 首页界面组件
 *
 * 功能：按分类展示模块列表，支持搜索和分类筛选
 * 输入：模块分类数据（从 assets 加载）、导航回调、语言设置
 * 输出：可滚动的分类-模块列表
 * 流程：加载索引 -> 渲染搜索栏 -> 渲染分类筛选 -> 渲染模块卡片
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    language: Strings.Language = Strings.Language.ZH,
    onNavigateToModule: (String) -> Unit,
    onNavigateToReview: () -> Unit
) {
    val context = LocalContext.current
    val strings = Strings.get(language)
    var index by remember { mutableStateOf<ContentIndex?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

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
                    text = strings.loading,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    /* 根据搜索和筛选条件过滤模块 */
    val filteredModules = remember(contentIndex, searchQuery, selectedCategory) {
        var modules = contentIndex.modules
        if (selectedCategory != null) {
            modules = modules.filter { it.category == selectedCategory }
        }
        if (searchQuery.isNotBlank()) {
            val query = searchQuery.lowercase()
            modules = modules.filter {
                it.title.lowercase().contains(query) ||
                it.id.lowercase().contains(query) ||
                it.description.lowercase().contains(query)
            }
        }
        modules
    }

    /* 根据搜索条件过滤文档 */
    val filteredDocuments = remember(contentIndex, searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else {
            val query = searchQuery.lowercase()
            contentIndex.documents.filter {
                it.title.lowercase().contains(query) ||
                it.slug.lowercase().contains(query)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
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
                    text = "${contentIndex.documents.size} ${strings.docs} / ${contentIndex.modules.size} ${strings.modules}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            /* 搜索栏 */
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(strings.searchHint) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { /* 触发搜索 */ })
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            /* 分类筛选条 */
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text(strings.all) }
                        )
                    }
                    items(contentIndex.categories) { category ->
                        val catColor = remember(category.color) {
                            try { Color(android.graphics.Color.parseColor(category.color)) }
                            catch (_: Exception) { Color(0xFF4F5BD5) }
                        }
                        FilterChip(
                            selected = selectedCategory == category.id,
                            onClick = {
                                selectedCategory = if (selectedCategory == category.id) null else category.id
                            },
                            label = { Text(category.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = catColor.copy(alpha = 0.15f),
                                selectedLabelColor = catColor
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            /* 搜索结果 - 文档列表 */
            if (searchQuery.isNotBlank() && filteredDocuments.isNotEmpty()) {
                item {
                    Text(
                        text = "${strings.documents} (${filteredDocuments.size})",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(filteredDocuments.take(20)) { document ->
                    DocumentSearchResult(
                        document = document,
                        categories = contentIndex.categories,
                        onClick = { onNavigateToModule(document.module) }
                    )
                }
            }

            /* 模块卡片列表 - 按分类分组 */
            if (filteredModules.isNotEmpty()) {
                val groupedModules = filteredModules.groupBy { it.category }
                val orderedCategories = contentIndex.categories.filter { groupedModules.containsKey(it.id) }

                items(orderedCategories) { category ->
                    val categoryModules = groupedModules[category.id] ?: emptyList()
                    CategoryModuleSection(
                        category = category,
                        modules = categoryModules,
                        strings = strings,
                        onModuleClick = onNavigateToModule
                    )
                }
            } else if (searchQuery.isNotBlank()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(strings.noResults, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

/**
 * 分类模块区块
 *
 * 功能：展示某分类下的所有模块卡片，模块间以分类色短分界线分隔
 * 输入：Category 和该分类下的模块列表
 * 输出：分类标题 + 模块卡片列表
 */
@Composable
fun CategoryModuleSection(
    category: Category,
    modules: List<Module>,
    strings: Strings.LangStrings,
    onModuleClick: (String) -> Unit
) {
    val cardColor = remember(category.color) {
        try { Color(android.graphics.Color.parseColor(category.color)) }
        catch (_: Exception) { Color(0xFF4F5BD5) }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        /* 分类标题 */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
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
                text = "${modules.size} ${strings.modules}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        /* 模块卡片，模块间以分类色短分界线分隔 */
        modules.forEachIndexed { index, module ->
            ModuleCard(
                module = module,
                accentColor = cardColor,
                strings = strings,
                onClick = { onModuleClick(module.id) }
            )
            /* 模块间短分界线（最后一个不加） */
            if (index < modules.size - 1) {
                Box(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 4.dp, bottom = 4.dp)
                        .width(32.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(cardColor.copy(alpha = 0.3f))
                )
            }
        }
    }
}

/**
 * 模块卡片
 *
 * 功能：展示单个模块信息，点击进入模块详情
 * 输入：Module 对象和强调色
 * 输出：可点击的模块卡片
 */
@Composable
fun ModuleCard(
    module: Module,
    accentColor: Color,
    strings: Strings.LangStrings,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* 模块颜色圆点 */
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${module.documents.size} ${strings.docs}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 文档搜索结果项
 *
 * 功能：展示搜索匹配的文档条目
 * 输入：Document 对象
 * 输出：可点击的文档条目
 */
@Composable
fun DocumentSearchResult(
    document: Document,
    categories: List<Category>,
    onClick: () -> Unit
) {
    val accentColor = remember(document.category, categories) {
        val colorStr = categories.find { it.id == document.category }?.color
        try { colorStr?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color(0xFF4F5BD5) }
        catch (_: Exception) { Color(0xFF4F5BD5) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = document.module,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
