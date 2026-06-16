package com.fandex.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 应用设置持久化管理
 *
 * 功能：使用 DataStore Preferences 持久化存储用户偏好设置
 * 输入：Context 对象
 * 输出：Flow 形式的设置值，支持异步读取和写入
 *
 * 持久化项：
 * - isDarkMode: 是否深色模式（默认 true）
 * - language: 界面语言代码（默认 "ZH"）
 * - fontSizeScale: 字体缩放比例（默认 1.0，范围 0.8-1.4）
 */
object DataStoreManager {

    /** DataStore 实例（懒加载，全局单例） */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fandex_settings")

    /** 深色模式偏好键 */
    private val KEY_IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")

    /** 语言偏好键 */
    private val KEY_LANGUAGE = stringPreferencesKey("language")

    /** 字体缩放偏好键 */
    private val KEY_FONT_SIZE_SCALE = floatPreferencesKey("font_size_scale")

    /** 启动页开关偏好键 */
    private val KEY_IS_SPLASH_ENABLED = booleanPreferencesKey("is_splash_enabled")

    /** 图标选择偏好键（"default" 或 "classic"） */
    private val KEY_ICON_STYLE = stringPreferencesKey("icon_style")

    /**
     * 读取深色模式设置
     *
     * 输入：Context
     * 输出：Flow<Boolean>，默认 true（深色）
     */
    fun getDarkMode(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { it[KEY_IS_DARK_MODE] ?: true }
    }

    /**
     * 保存深色模式设置
     *
     * 输入：Context、是否深色
     * 输出：无（异步写入）
     */
    suspend fun saveDarkMode(context: Context, isDark: Boolean) {
        context.dataStore.edit { it[KEY_IS_DARK_MODE] = isDark }
    }

    /**
     * 读取语言设置
     *
     * 输入：Context
     * 输出：Flow<String>，默认 "ZH"
     */
    fun getLanguage(context: Context): Flow<String> {
        return context.dataStore.data.map { it[KEY_LANGUAGE] ?: "ZH" }
    }

    /**
     * 保存语言设置
     *
     * 输入：Context、语言代码
     * 输出：无（异步写入）
     */
    suspend fun saveLanguage(context: Context, lang: String) {
        context.dataStore.edit { it[KEY_LANGUAGE] = lang }
    }

    /**
     * 读取字体缩放设置
     *
     * 输入：Context
     * 输出：Flow<Float>，默认 1.0
     */
    fun getFontSizeScale(context: Context): Flow<Float> {
        return context.dataStore.data.map { it[KEY_FONT_SIZE_SCALE] ?: 1.0f }
    }

    /**
     * 保存字体缩放设置
     *
     * 输入：Context、缩放比例
     * 输出：无（异步写入）
     */
    suspend fun saveFontSizeScale(context: Context, scale: Float) {
        val clamped = scale.coerceIn(0.8f, 1.4f)
        context.dataStore.edit { it[KEY_FONT_SIZE_SCALE] = clamped }
    }

    /**
     * 读取启动页开关设置
     *
     * 输入：Context
     * 输出：Flow<Boolean>，默认 true（开启启动页）
     */
    fun getSplashEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { it[KEY_IS_SPLASH_ENABLED] ?: true }
    }

    /**
     * 保存启动页开关设置
     *
     * 输入：Context、是否开启启动页
     * 输出：无（异步写入）
     */
    suspend fun saveSplashEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[KEY_IS_SPLASH_ENABLED] = enabled }
    }

    /**
     * 读取图标选择设置
     *
     * 输入：Context
     * 输出：Flow<String>，默认 "default"（新图标）
     */
    fun getIconStyle(context: Context): Flow<String> {
        return context.dataStore.data.map { it[KEY_ICON_STYLE] ?: "default" }
    }

    /**
     * 保存图标选择设置
     *
     * 输入：Context、图标风格代码（"default" 或 "classic"）
     * 输出：无（异步写入）
     */
    suspend fun saveIconStyle(context: Context, style: String) {
        context.dataStore.edit { it[KEY_ICON_STYLE] = style }
    }
}
