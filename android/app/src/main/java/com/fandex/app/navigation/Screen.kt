package com.fandex.app.navigation

/**
 * 导航路由定义
 *
 * 功能：定义应用内所有页面的路由路径
 * 输入：无
 * 输出：路由路径常量
 */
sealed class Screen(val route: String) {

    /**
     * 首页路由
     *
     * 展示分类和模块列表
     */
    data object Home : Screen("home")

    /**
     * 模块详情路由
     *
     * 展示指定模块下的文档列表
     * 参数：moduleId - 模块 ID
     */
    data object Module : Screen("module/{moduleId}") {
        fun createRoute(moduleId: String): String = "module/$moduleId"
    }

    /**
     * 文档阅读路由
     *
     * 展示文档内容
     * 参数：moduleId、slug、title
     */
    data object Article : Screen("article/{moduleId}/{slug}/{title}") {
        fun createRoute(moduleId: String, slug: String, title: String): String {
            return "article/$moduleId/$slug/${title.replace("/", "_")}"
        }
    }

    /**
     * 复习路由
     *
     * 展示复习卡片和答题交互
     */
    data object Review : Screen("review")
}
