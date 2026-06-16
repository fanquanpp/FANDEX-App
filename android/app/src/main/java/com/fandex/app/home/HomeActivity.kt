package com.fandex.app.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fandex.app.data.ContentIndex
import com.fandex.app.data.ContentLoader
import com.fandex.app.data.DataStoreManager
import com.fandex.app.data.Strings
import com.fandex.app.navigation.Screen
import com.fandex.app.reader.ArticleScreen
import com.fandex.app.ui.theme.FANDEXTheme
import java.net.URLDecoder
import kotlinx.coroutines.launch

/**
 * 首页 Activity
 *
 * 功能：应用主入口，管理 Navigation Compose 路由、侧边栏、底部导航、主题/语言/字体设置持久化
 * 输入：无
 * 输出：完整的导航框架，包含首页、模块、文章三个路由及侧边栏导航
 * 流程：onCreate -> 设置 Compose 内容 -> 从 DataStore 读取偏好 -> 渲染侧边栏和路由
 */
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            /* 从 DataStore 读取深色模式偏好，默认 true */
            val isDarkMode by DataStoreManager.getDarkMode(context)
                .collectAsState(initial = true)

            /* 从 DataStore 读取语言偏好，默认 ZH */
            val languageStr by DataStoreManager.getLanguage(context)
                .collectAsState(initial = "ZH")

            /* 从 DataStore 读取字体缩放偏好，默认 1.0 */
            val fontSizeScale by DataStoreManager.getFontSizeScale(context)
                .collectAsState(initial = 1.0f)

            /* 将语言字符串转换为枚举 */
            val language = remember(languageStr) {
                when (languageStr) {
                    "EN" -> Strings.Language.EN
                    "JA" -> Strings.Language.JA
                    else -> Strings.Language.ZH
                }
            }

            /* 协程作用域，用于 DataStore 写入操作 */
            val scope = rememberCoroutineScope()

            FANDEXTheme(darkTheme = isDarkMode) {
                FANDEXApp(
                    isDarkMode = isDarkMode,
                    onToggleTheme = {
                        /* 切换深色模式并持久化 */
                        scope.launch {
                            try {
                                DataStoreManager.saveDarkMode(context, !isDarkMode)
                            } catch (_: Exception) { /* 写入失败静默处理 */ }
                        }
                    },
                    language = language,
                    onToggleLanguage = {
                        /* ZH -> EN -> JA -> ZH 循环切换并持久化 */
                        val nextLang = when (language) {
                            Strings.Language.ZH -> Strings.Language.EN
                            Strings.Language.EN -> Strings.Language.JA
                            Strings.Language.JA -> Strings.Language.ZH
                        }
                        scope.launch {
                            try {
                                DataStoreManager.saveLanguage(context, nextLang.name)
                            } catch (_: Exception) { /* 写入失败静默处理 */ }
                        }
                    },
                    fontSizeScale = fontSizeScale,
                    onFontSizeChange = { newScale ->
                        /* 字体缩放变更并持久化 */
                        scope.launch {
                            try {
                                DataStoreManager.saveFontSizeScale(context, newScale)
                            } catch (_: Exception) { /* 写入失败静默处理 */ }
                        }
                    }
                )
            }
        }
    }
}

/**
 * FANDEX 应用主框架
 *
 * 功能：管理侧边栏、底部导航、NavHost 路由、主题/语言/字体设置
 * 输入：isDarkMode、onToggleTheme、language、onToggleLanguage、fontSizeScale、onFontSizeChange
 * 输出：侧边栏 + 底部导航栏 + 页面路由容器
 * 流程：初始化导航控制器 -> 判断当前路由 -> 渲染侧边栏/底部栏/顶部栏 -> 路由分发
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FANDEXApp(
    isDarkMode: Boolean = true,
    onToggleTheme: () -> Unit = {},
    language: Strings.Language = Strings.Language.ZH,
    onToggleLanguage: () -> Unit = {},
    fontSizeScale: Float = 1.0f,
    onFontSizeChange: (Float) -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val strings = Strings.get(language)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /* 侧边栏状态 */
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    /* 判断是否在文章阅读页 */
    val isArticleRoute = currentRoute != null && currentRoute.startsWith("article/")

    /* 文章阅读页隐藏底部导航 */
    val showBottomBar = !isArticleRoute

    /* 加载内容索引用于侧边栏 */
    var contentIndex by remember { mutableStateOf<ContentIndex?>(null) }
    LaunchedEffect(Unit) {
        contentIndex = ContentLoader.loadIndex(context)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
                modifier = Modifier.width(260.dp)
            ) {
                SidebarContent(
                    contentIndex = contentIndex,
                    strings = strings,
                    onModuleClick = { moduleId ->
                        /* 点击模块跳转，关闭侧边栏 */
                        scope.launch {
                            try {
                                drawerState.close()
                            } catch (_: Exception) { /* 关闭失败静默处理 */ }
                        }
                        navController.navigate(Screen.Module.createRoute(moduleId)) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    /* 紧凑底部导航栏：3个小图标按钮，无文字标签 */
                    NavigationBar(
                        modifier = Modifier.height(48.dp)
                    ) {
                        /* 首页按钮 */
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = strings.home,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = null,
                            selected = currentRoute == Screen.Home.route ||
                                currentRoute?.startsWith("module/") == true,
                            onClick = {
                                if (currentRoute != Screen.Home.route) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        /* 语言切换按钮 */
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = strings.language,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = null,
                            selected = false,
                            onClick = onToggleLanguage,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        /* 主题切换按钮 */
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = if (isDarkMode) strings.lightMode else strings.darkMode,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = null,
                            selected = false,
                            onClick = onToggleTheme,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                /* 首页路由 */
                composable(Screen.Home.route) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        /* 首页顶部栏：菜单按钮 + 标题 */
                        TopAppBar(
                            title = {
                                Text(
                                    text = "FANDEX",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        try {
                                            drawerState.open()
                                        } catch (_: Exception) { /* 打开失败静默处理 */ }
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = strings.menu
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                        /* 首页内容 */
                        HomeScreen(
                            language = language,
                            onNavigateToModule = { moduleId ->
                                navController.navigate(Screen.Module.createRoute(moduleId))
                            },
                            onOpenDrawer = {
                                scope.launch {
                                    try { drawerState.open() } catch (_: Exception) { /* 静默处理 */ }
                                }
                            }
                        )
                    }
                }

                /* 模块详情路由 */
                composable(
                    route = Screen.Module.route,
                    arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                    Column(modifier = Modifier.fillMaxSize()) {
                        /* 模块页顶部栏：菜单按钮 + 标题 */
                        TopAppBar(
                            title = {
                                Text(
                                    text = moduleId,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        try {
                                            drawerState.open()
                                        } catch (_: Exception) { /* 打开失败静默处理 */ }
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = strings.menu
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                        /* 模块详情内容 */
                        ModuleScreen(
                            moduleId = moduleId,
                            language = language,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToArticle = { mod, slug, title ->
                                navController.navigate(Screen.Article.createRoute(mod, slug, title))
                            },
                            onOpenDrawer = {
                                scope.launch {
                                    try { drawerState.open() } catch (_: Exception) { /* 静默处理 */ }
                                }
                            }
                        )
                    }
                }

                /* 文章阅读路由 */
                composable(
                    route = Screen.Article.route,
                    arguments = listOf(
                        navArgument("moduleId") { type = NavType.StringType },
                        navArgument("slug") { type = NavType.StringType },
                        navArgument("title") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                    val slug = backStackEntry.arguments?.getString("slug") ?: ""
                    val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
                    val title = try {
                        URLDecoder.decode(encodedTitle, "UTF-8")
                    } catch (_: Exception) {
                        encodedTitle
                    }

                    /* 文章阅读页：紧凑顶部栏（返回+标题+字体大小按钮+菜单） */
                    Column(modifier = Modifier.fillMaxSize()) {
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
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = strings.back
                                    )
                                }
                            },
                            actions = {
                                /* 字体增大按钮 */
                                IconButton(onClick = {
                                    val newScale = (fontSizeScale + 0.1f).coerceIn(0.8f, 1.4f)
                                    onFontSizeChange(newScale)
                                }) {
                                    Icon(
                                        Icons.Default.TextIncrease,
                                        contentDescription = strings.fontSizeIncrease,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                /* 字体缩小按钮 */
                                IconButton(onClick = {
                                    val newScale = (fontSizeScale - 0.1f).coerceIn(0.8f, 1.4f)
                                    onFontSizeChange(newScale)
                                }) {
                                    Icon(
                                        Icons.Default.TextDecrease,
                                        contentDescription = strings.fontSizeDecrease,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                /* 菜单按钮，打开侧边栏 */
                                IconButton(onClick = {
                                    scope.launch {
                                        try {
                                            drawerState.open()
                                        } catch (_: Exception) { /* 打开失败静默处理 */ }
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = strings.menu
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                        /* 文章内容 */
                        ArticleScreen(
                            moduleId = moduleId,
                            slug = slug,
                            title = title,
                            isDarkMode = isDarkMode,
                            language = language,
                            fontSizeScale = fontSizeScale,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToArticle = { mod, s, t ->
                                navController.navigate(Screen.Article.createRoute(mod, s, t)) {
                                    popUpTo(Screen.Article.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            onOpenDrawer = {
                                scope.launch {
                                    try { drawerState.open() } catch (_: Exception) { /* 静默处理 */ }
                                }
                            },
                            onFontSizeChange = { newScale ->
                                onFontSizeChange(newScale)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 侧边栏内容组件
 *
 * 功能：展示 FANDEX 标题、分类列表、每个分类下的模块（带颜色圆点和文档数）
 * 输入：ContentIndex 数据、语言字符串、模块点击回调
 * 输出：可滚动的侧边栏导航列表
 * 流程：渲染标题 -> 遍历分类 -> 遍历分类下模块 -> 点击触发导航
 */
@Composable
fun SidebarContent(
    contentIndex: ContentIndex?,
    strings: Strings.LangStrings,
    onModuleClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        /* FANDEX 标题 */
        Text(
            text = "FANDEX",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = strings.homeSubtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))

        /* 分类与模块列表 */
        if (contentIndex != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val groupedModules = contentIndex.modules.groupBy { it.category }
                val orderedCategories = contentIndex.categories.filter { groupedModules.containsKey(it.id) }

                items(orderedCategories) { category ->
                    val categoryModules = groupedModules[category.id] ?: emptyList()

                    /* 分类标题 */
                    CategoryHeader(category = category)
                    Spacer(modifier = Modifier.height(4.dp))

                    /* 分类下的模块列表 */
                    categoryModules.forEach { module ->
                        ModuleSidebarItem(
                            module = module,
                            categoryColor = category.color,
                            strings = strings,
                            onClick = { onModuleClick(module.id) }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

/**
 * 侧边栏分类标题
 *
 * 功能：展示分类名称，带颜色指示条
 * 输入：Category 对象
 * 输出：分类标题行
 */
@Composable
fun CategoryHeader(category: com.fandex.app.data.Category) {
    val catColor = remember(category.color) {
        try {
            Color(android.graphics.Color.parseColor(category.color))
        } catch (_: Exception) {
            Color(0xFF4F5BD5)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(catColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 侧边栏模块条目
 *
 * 功能：展示模块名称、颜色圆点、文档数量，点击跳转模块详情
 * 输入：Module 对象、分类颜色字符串、语言字符串、点击回调
 * 输出：可点击的模块导航项
 */
@Composable
fun ModuleSidebarItem(
    module: com.fandex.app.data.Module,
    categoryColor: String,
    strings: Strings.LangStrings,
    onClick: () -> Unit
) {
    val dotColor = remember(categoryColor) {
        try {
            Color(android.graphics.Color.parseColor(categoryColor))
        } catch (_: Exception) {
            Color(0xFF4F5BD5)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /* 颜色圆点 */
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        /* 模块标题 */
        Text(
            text = module.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        /* 文档数量 */
        Text(
            text = "${module.documents.size}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
