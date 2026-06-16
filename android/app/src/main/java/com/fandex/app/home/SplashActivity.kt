package com.fandex.app.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fandex.app.data.DataStoreManager
import kotlinx.coroutines.delay

/**
 * 启动页 Activity
 *
 * 功能：应用冷启动入口，展示品牌信息后跳转至 HomeActivity
 * 输入：无
 * 输出：启动页界面（品牌 Logo、标语、版本号），3 秒后自动跳转
 * 流程：
 *   1. 从 DataStore 读取 is_splash_enabled 设置（默认 true）
 *   2. 若启动页开启：显示启动页 3 秒后跳转 HomeActivity
 *   3. 若启动页关闭：直接跳转 HomeActivity
 *   4. 跳转后 finish() 当前 Activity，防止返回
 */
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            /* 从 DataStore 读取启动页开关设置，默认开启 */
            val isSplashEnabled by DataStoreManager.getSplashEnabled(context)
                .collectAsState(initial = true)

            /* 启动页展示时长（毫秒） */
            val splashDurationMs = 3000L

            /* 监听设置值变化，决定是否展示启动页 */
            LaunchedEffect(isSplashEnabled) {
                try {
                    if (isSplashEnabled) {
                        /* 启动页开启：等待 3 秒后跳转 */
                        delay(splashDurationMs)
                    }
                    /* 跳转到 HomeActivity */
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                    /* 销毁当前 Activity，防止返回键回到启动页 */
                    (context as? ComponentActivity)?.finish()
                } catch (_: Exception) {
                    /* 异常兜底：确保即使出错也能跳转到主页面 */
                    try {
                        val intent = Intent(context, HomeActivity::class.java)
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    } catch (_: Exception) {
                        /* 二次异常静默处理，避免崩溃 */
                    }
                }
            }

            /* 启动页界面渲染 */
            SplashScreenContent()
        }
    }
}

/**
 * 启动页内容组件
 *
 * 功能：渲染启动页的品牌展示界面
 * 输入：无
 * 输出：深色背景 + Logo + 标语 + 性质说明 + 作者 + 版本号
 * 流程：垂直居中排列各元素
 */
@androidx.compose.runtime.Composable
private fun SplashScreenContent() {
    /* 深色背景色 */
    val backgroundColor = Color(0xFF0d0d0d)
    /* 品牌竖色条颜色 */
    val accentColor = Color(0xFF3366cc)
    /* 白色文字颜色 */
    val textColor = Color.White
    /* 次要文字颜色（半透明白色） */
    val secondaryTextColor = Color.White.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /* FANDEX Logo：竖色条 + 深底白字 FANDEX */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                /* 左侧竖色条 */
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(36.dp)
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(10.dp))
                /* FANDEX 品牌名 */
                Text(
                    text = "FANDEX",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* 标语 */
            Text(
                text = "离线技术知识查阅工具",
                fontSize = 14.sp,
                color = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            /* 性质说明 */
            Text(
                text = "无需网络 / 原生渲染 / 三语界面",
                fontSize = 12.sp,
                color = secondaryTextColor.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* 作者 */
            Text(
                text = "fanquanpp",
                fontSize = 12.sp,
                color = secondaryTextColor.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            /* 版本号 */
            Text(
                text = "v1.4.0-beta",
                fontSize = 11.sp,
                color = secondaryTextColor.copy(alpha = 0.3f)
            )
        }
    }
}
