package com.fandex.app.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fandex.app.navigation.Screen
import com.fandex.app.reader.ArticleScreen
import com.fandex.app.review.ReviewScreen
import com.fandex.app.ui.theme.FANDEXTheme
import java.net.URLDecoder

/**
 * 首页 Activity
 *
 * 功能：应用主入口，管理 Navigation Compose 路由和底部导航
 * 输入：无
 * 输出：完整的导航框架，包含首页、模块、文章、复习四个路由
 * 流程：onCreate -> 设置 Compose 内容 -> 初始化导航控制器 -> 渲染底部导航和路由
 */
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FANDEXTheme {
                FANDEXApp()
            }
        }
    }
}

/**
 * 底部导航项定义
 *
 * @param route 路由路径
 * @param icon 图标
 * @param label 标签文本
 */
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * FANDEX 应用主框架
 *
 * 功能：管理底部导航和 NavHost 路由
 * 输入：无
 * 输出：底部导航栏 + 页面路由容器
 * 流程：初始化 NavController -> 定义底部导航项 -> 渲染 Scaffold + NavHost
 */
@Composable
fun FANDEXApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home"),
        BottomNavItem(Screen.Review.route, Icons.Default.Refresh, "Review")
    )

    /* 文章阅读页隐藏底部导航 */
    val showBottomBar = currentRoute != null &&
        !currentRoute.startsWith("article/")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route ||
                                (item.route == Screen.Home.route && currentRoute?.startsWith("module/") == true),
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
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
                    onNavigateToModule = { moduleId ->
                        navController.navigate(Screen.Module.createRoute(moduleId))
                    },
                    onNavigateToReview = {
                        navController.navigate(Screen.Review.route)
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
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToArticle = { mod, slug, title ->
                        navController.navigate(Screen.Article.createRoute(mod, slug, title))
                    }
                )
            }

            /* 文章阅读路由 - 使用 URL 编码传递 title */
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
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 复习路由 */
            composable(Screen.Review.route) {
                ReviewScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToArticle = { mod, slug, title ->
                        navController.navigate(Screen.Article.createRoute(mod, slug, title))
                    }
                )
            }
        }
    }
}
