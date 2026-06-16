package com.fandex.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * FANDEX 应用主题
 *
 * 功能：定义亮色/暗色主题配色
 * 输入：darkTheme 参数
 * 输出：MaterialTheme 配置
 */
@Composable
fun FANDEXTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = PrimaryBlue,
            secondary = FrontendColor,
            tertiary = AiColor
        )
    } else {
        lightColorScheme(
            primary = PrimaryBlue,
            secondary = FrontendColor,
            tertiary = AiColor
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
