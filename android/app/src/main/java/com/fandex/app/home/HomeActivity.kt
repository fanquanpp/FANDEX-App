package com.fandex.app.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fandex.app.ui.theme.FANDEXTheme

/**
 * 首页 Activity
 *
 * 功能：展示模块分类列表，支持按分类浏览和搜索
 * 输入：无（从本地 JSON 索引加载模块数据）
 * 输出：Compose 界面，展示模块卡片网格
 * 流程：onCreate -> 加载索引 -> 渲染分类列表
 */
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FANDEXTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}
