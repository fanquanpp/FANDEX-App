package com.fandex.app.review

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 复习 Activity
 *
 * 功能：间隔重复复习卡片，支持填空题和选择题
 * 输入：复习卡片数据（从 Room 数据库加载）
 * 输出：Compose 界面，展示复习卡片和答题交互
 * 流程：加载待复习卡片 -> 展示题目 -> 用户作答 -> 记录结果
 */
class ReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ReviewScreen()
            }
        }
    }
}

@Composable
fun ReviewScreen() {
    // TODO: 实现复习卡片 UI
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "复习模式",
            modifier = Modifier.padding(16.dp)
        )
    }
}
