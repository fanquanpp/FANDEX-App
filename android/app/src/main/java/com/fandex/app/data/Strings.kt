package com.fandex.app.data

/**
 * 多语言字符串管理
 *
 * 功能：根据当前语言设置返回对应的 UI 字符串
 * 输入：Language 枚举（ZH/EN/JA）
 * 输出：所有 UI 文本的三语版本
 *
 * 使用方式：Strings.get(Language.ZH).home
 * 默认语言：ZH（中文）
 */
object Strings {

    /**
     * 支持的语言枚举
     */
    enum class Language { ZH, EN, JA }

    /**
     * 语言字符串集合
     */
    data class LangStrings(
        val appName: String,
        val homeSubtitle: String,
        val loading: String,
        val all: String,
        val docs: String,
        val modules: String,
        val home: String,
        val light: String,
        val dark: String,
        val lightMode: String,
        val darkMode: String,
        val back: String,
        val language: String,
        val previousDoc: String,
        val nextDoc: String,
        val backToTop: String,
        val noContent: String
    )

    /**
     * 获取指定语言的字符串集合
     *
     * 输入：Language 枚举值
     * 输出：对应的 LangStrings 实例
     */
    fun get(lang: Language): LangStrings = when (lang) {
        Language.ZH -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "\u5FAA\u5E8F\u6E10\u8FDB\uFF0C\u4ECE\u7B2C\u4E00\u884C\u4EE3\u7801\u5230\u7406\u89E3\u6574\u4E2A\u4E16\u754C",
            loading = "\u52A0\u8F7D\u4E2D...",
            all = "\u5168\u90E8",
            docs = "\u7BC7",
            modules = "\u4E2A\u6A21\u5757",
            home = "\u9996\u9875",
            light = "\u4EAE\u8272",
            dark = "\u6697\u8272",
            lightMode = "\u4EAE\u8272\u6A21\u5F0F",
            darkMode = "\u6697\u8272\u6A21\u5F0F",
            back = "\u8FD4\u56DE",
            language = "\u8BED\u8A00",
            previousDoc = "\u4E0A\u4E00\u7BC7",
            nextDoc = "\u4E0B\u4E00\u7BC7",
            backToTop = "\u56DE\u5230\u9876\u90E8",
            noContent = "\u6682\u65E0\u5185\u5BB9"
        )
        Language.EN -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "Step by step, from the first line of code to understanding the world",
            loading = "Loading...",
            all = "All",
            docs = "docs",
            modules = "modules",
            home = "Home",
            light = "Light",
            dark = "Dark",
            lightMode = "Light Mode",
            darkMode = "Dark Mode",
            back = "Back",
            language = "Language",
            previousDoc = "Previous",
            nextDoc = "Next",
            backToTop = "Top",
            noContent = "No content available"
        )
        Language.JA -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "\u9806\u6B21\u9032\u3081\u3001\u6700\u521D\u306E\u884C\u304B\u3089\u4E16\u754C\u3092\u7406\u89E3\u3059\u308B",
            loading = "\u8AAD\u307F\u8FBC\u307F\u4E2D...",
            all = "\u3059\u3079\u3066",
            docs = "\u4EF6",
            modules = "\u30E2\u30B8\u30E5\u30FC\u30EB",
            home = "\u30DB\u30FC\u30E0",
            light = "\u660E\u308B\u3044",
            dark = "\u6697\u3044",
            lightMode = "\u30E9\u30A4\u30C8\u30E2\u30FC\u30C9",
            darkMode = "\u30C0\u30FC\u30AF\u30E2\u30FC\u30C9",
            back = "\u623B\u308B",
            language = "\u8A00\u8A9E",
            previousDoc = "\u524D\u3078",
            nextDoc = "\u6B21\u3078",
            backToTop = "\u30C8\u30C3\u30D7\u3078",
            noContent = "\u30B3\u30F3\u30C6\u30F3\u30C4\u304C\u3042\u308A\u307E\u305B\u3093"
        )
    }
}
