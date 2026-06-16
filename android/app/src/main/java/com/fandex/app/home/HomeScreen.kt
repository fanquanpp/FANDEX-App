package com.fandex.app.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 首页界面组件
 *
 * 功能：按分类展示模块列表
 * 输入：模块分类数据
 * 输出：可滚动的分类-模块列表
 * 流程：遍历分类 -> 渲染分类标题 -> 渲染模块卡片
 */
@Composable
fun HomeScreen() {
    // TODO: 从 assets/dist-mobile/index.json 加载模块数据
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "FANDEX",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "循序渐进，从第一行代码到理解整个世界",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        // TODO: 按分类渲染模块卡片
    }
}
