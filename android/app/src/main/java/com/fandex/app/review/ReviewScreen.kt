package com.fandex.app.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fandex.app.data.*
import kotlinx.coroutines.launch

/**
 * 复习页面
 *
 * 功能：展示待复习文档列表，支持点击进入答题模式
 * 输入：Room 数据库中的复习进度
 * 输出：待复习列表和统计数据
 * 流程：加载复习进度 -> 展示待复习列表 -> 用户选择 -> 进入答题
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    onNavigateBack: () -> Unit,
    onNavigateToArticle: (String, String, String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getInstance(context) }
    val dao = remember { db.reviewDao() }

    var dueReviews by remember { mutableStateOf<List<ReviewProgress>>(emptyList()) }
    var allReviews by remember { mutableStateOf<List<ReviewProgress>>(emptyList()) }
    var masteredCount by remember { mutableStateOf(0) }
    var showQuiz by remember { mutableStateOf(false) }
    var currentReview by remember { mutableStateOf<ReviewProgress?>(null) }
    var index by remember { mutableStateOf<ContentIndex?>(null) }

    /* 加载数据 */
    LaunchedEffect(Unit) {
        index = ContentLoader.loadIndex(context)
        val now = System.currentTimeMillis()
        dueReviews = dao.getDueReviews(now)
        allReviews = dao.getAllReviews()
        masteredCount = dao.getMasteredCount()
    }

    /* 答题模式 */
    if (showQuiz && currentReview != null) {
        QuizScreen(
            review = currentReview!!,
            onAnswer = { quality ->
                scope.launch {
                    val updated = Sm2Algorithm.calculateNextReview(currentReview!!, quality)
                    dao.upsertReview(updated)
                    val now = System.currentTimeMillis()
                    dueReviews = dao.getDueReviews(now)
                    allReviews = dao.getAllReviews()
                    masteredCount = dao.getMasteredCount()
                }
                showQuiz = false
                currentReview = null
            },
            onBack = {
                showQuiz = false
                currentReview = null
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            /* 统计卡片 */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Due",
                    value = dueReviews.size.toString(),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.error
                )
                StatCard(
                    title = "Learning",
                    value = allReviews.size.toString(),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.tertiary
                )
                StatCard(
                    title = "Mastered",
                    value = masteredCount.toString(),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* 添加文档到复习 */
            val contentIndex = index
            if (contentIndex != null) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            /* 将所有文档添加到复习进度 */
                            val existingIds = allReviews.map { it.id }.toSet()
                            val newProgresses = contentIndex.documents
                                .filter { doc ->
                                    !existingIds.contains(ReviewProgress.createId(doc.module, doc.slug))
                                }
                                .take(50) /* 首次最多添加 50 篇 */
                                .map { doc ->
                                    Sm2Algorithm.createInitialProgress(
                                        module = doc.module,
                                        slug = doc.slug,
                                        title = doc.title,
                                        category = doc.category
                                    )
                                }
                            newProgresses.forEach { dao.upsertReview(it) }
                            val now = System.currentTimeMillis()
                            dueReviews = dao.getDueReviews(now)
                            allReviews = dao.getAllReviews()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Add docs to review")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            /* 待复习列表 */
            if (dueReviews.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (allReviews.isEmpty()) "No review items" else "All caught up!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (allReviews.isEmpty()) "Add documents to start reviewing" else "Come back later for more reviews",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text(
                    text = "Due for review (${dueReviews.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(dueReviews) { review ->
                        ReviewItemCard(
                            review = review,
                            onClick = {
                                currentReview = review
                                showQuiz = true
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 统计卡片
 *
 * 功能：展示复习统计数据
 * 输入：标题、数值、颜色
 * 输出：带颜色标识的统计卡片
 */
@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 复习项卡片
 *
 * 功能：展示待复习的文档条目
 * 输入：ReviewProgress 对象
 * 输出：可点击的复习卡片
 */
@Composable
fun ReviewItemCard(
    review: ReviewProgress,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${review.module} / reviewed ${review.reviewCount}x",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${review.intervalDays}d",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 答题页面
 *
 * 功能：展示自评界面，用户评估自己的记忆程度
 * 输入：当前复习进度
 * 输出：答题质量（0-5）
 * 流程：展示文档标题 -> 用户自评 -> 返回质量分数
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    review: ReviewProgress,
    onAnswer: (Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            /* 文档标题 */
            Text(
                text = review.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.module,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Previously reviewed ${review.reviewCount} times",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            /* 自评提示 */
            Text(
                text = "How well did you remember?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            /* 自评按钮 */
            val ratings = listOf(
                0 to "Forgot",
                1 to "Barely",
                2 to "Hard",
                3 to "OK",
                4 to "Easy",
                5 to "Perfect"
            )

            ratings.forEach { (quality, label) ->
                val btnColor = when (quality) {
                    0, 1 -> MaterialTheme.colorScheme.error
                    2 -> MaterialTheme.colorScheme.tertiary
                    3 -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.primary
                }
                OutlinedButton(
                    onClick = { onAnswer(quality) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = btnColor
                    )
                ) {
                    Text(label)
                }
            }
        }
    }
}
