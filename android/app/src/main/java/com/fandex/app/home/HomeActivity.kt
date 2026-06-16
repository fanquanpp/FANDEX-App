package com.fandex.app.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fandex.app.data.Strings
import com.fandex.app.navigation.Screen
import com.fandex.app.reader.ArticleScreen
import com.fandex.app.ui.theme.FANDEXTheme
import java.net.URLDecoder

/**
 * 首页 Activity
 *
 * 功能：应用主入口，管理 Navigation Compose 路由、底部导航、白昼/黑夜模式切换、语言切换
 * 输入：无
 * 输出：完整的导航框架，包含首页、模块、文章三个路由
 * 流程：onCreate -> 设置 Compose 内容 -> 初始化导航控制器 -> 渲染底部导航和路由
 */
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /* isDarkMode 状态持久化 */
            var isDarkMode by rememberSaveable { mutableStateOf(false) }
            /* language 状态持久化，默认中文 */
            var language by rememberSaveable { mutableStateOf(Strings.Language.ZH) }
            FANDEXTheme(darkTheme = isDarkMode) {
                FANDEXApp(
                    isDarkMode = isDarkMode,
                    onToggleTheme = { isDarkMode = !isDarkMode },
                    language = language,
                    onToggleLanguage = {
                        /* ZH -> EN -> JA -> ZH 循环切换 */
                        language = when (language) {
                            Strings.Language.ZH -> Strings.Language.EN
                            Strings.Language.EN -> Strings.Language.JA
                            Strings.Language.JA -> Strings.Language.ZH
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
 * 功能：管理底部导航、NavHost 路由、白昼/黑夜模式切换、语言切换
 * 输入：isDarkMode、onToggleTheme、language、onToggleLanguage
 * 输出：底部导航栏 + 页面路由容器
 */
@Composable
fun FANDEXApp(
    isDarkMode: Boolean = false,
    onToggleTheme: () -> Unit = {},
    language: Strings.Language = Strings.Language.ZH,
    onToggleLanguage: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val strings = Strings.get(language)

    /* 文章阅读页隐藏底部导航 */
    val showBottomBar = currentRoute != null &&
        !currentRoute.startsWith("article/")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    /* 首页 */
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = strings.home) },
                        label = { Text(strings.home) },
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
                        }
                    )
                    /* 语言切换按钮：显示当前语言缩写 */
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = strings.language
                            )
                        },
                        label = {
                            Text(
                                when (language) {
                                    Strings.Language.ZH -> "ZH"
                                    Strings.Language.EN -> "EN"
                                    Strings.Language.JA -> "JA"
                                }
                            )
                        },
                        selected = false,
                        onClick = onToggleLanguage
                    )
                    /* 白昼/黑夜模式切换按钮 */
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isDarkMode) strings.lightMode else strings.darkMode
                            )
                        },
                        label = { Text(if (isDarkMode) strings.light else strings.dark) },
                        selected = false,
                        onClick = onToggleTheme
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
                HomeScreen(
                    language = language,
                    onNavigateToModule = { moduleId ->
                        navController.navigate(Screen.Module.createRoute(moduleId))
                    }
                )
            }

            /* 模块详情路由 */
            composable(
                route = Screen.Module.route,
                arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                ModuleScreen(
                    moduleId = moduleId,
                    language = language,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToArticle = { mod, slug, title ->
                        navController.navigate(Screen.Article.createRoute(mod, slug, title))
                    }
                )
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
                val title = try { URLDecoder.decode(encodedTitle, "UTF-8") } catch (_: Exception) { encodedTitle }
                ArticleScreen(
                    moduleId = moduleId,
                    slug = slug,
                    title = title,
                    isDarkMode = isDarkMode,
                    language = language,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToArticle = { mod, s, t ->
                        navController.navigate(Screen.Article.createRoute(mod, s, t)) {
                            popUpTo(Screen.Article.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
