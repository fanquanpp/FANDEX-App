package com.fandex.app.data

/**
 * 多语言字符串管理
 *
 * 功能：根据当前语言设置返回对应的 UI 字符串
 * 输入：Language 枚举（ZH/EN/JA）
 * 输出：所有 UI 文本的三语版本
 *
 * 默认语言：ZH（中文）
 * 文档内容语言：仅中文
 */
object Strings {

    /** 支持的语言枚举 */
    enum class Language { ZH, EN, JA }

    /** 语言字符串集合 */
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
        val noContent: String,
        val fontSizeIncrease: String,
        val fontSizeDecrease: String,
        val menu: String,
        val documents: String,
        val category: String,
        val description: String,
        val pageOf: String
    )

    /** 获取指定语言的字符串集合 */
    fun get(lang: Language): LangStrings = when (lang) {
        Language.ZH -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "\u4EE3\u7801\u8BED\u6CD5\uFF0C\u79BB\u7EBF\u901F\u67E5",
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
            noContent = "\u6682\u65E0\u5185\u5BB9",
            fontSizeIncrease = "\u5B57\u4F53\u589E\u5927",
            fontSizeDecrease = "\u5B57\u4F53\u7F29\u5C0F",
            menu = "\u83DC\u5355",
            documents = "\u6587\u6863",
            category = "\u5206\u7C7B",
            description = "\u7B80\u4ECB",
            pageOf = "/"
        )
        Language.EN -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "Code Syntax, Offline Reference",
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
            previousDoc = "Prev",
            nextDoc = "Next",
            backToTop = "Top",
            noContent = "No content",
            fontSizeIncrease = "A+",
            fontSizeDecrease = "A-",
            menu = "Menu",
            documents = "Documents",
            category = "Category",
            description = "Desc",
            pageOf = "/"
        )
        Language.JA -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "\u30B3\u30FC\u30C9\u69CB\u6587\u3001\u30AA\u30D5\u30E9\u30A4\u30F3\u53C2\u7167",
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
            noContent = "\u30B3\u30F3\u30C6\u30F3\u30C4\u306A\u3057",
            fontSizeIncrease = "\u62E1\u5927",
            fontSizeDecrease = "\u7E2E\u5C0F",
            menu = "\u30E1\u30CB\u30E5\u30FC",
            documents = "\u30C9\u30AD\u30E5\u30E1\u30F3\u30C8",
            category = "\u30AB\u30C6\u30B4\u30EA",
            description = "\u8AAC\u660E",
            pageOf = "/"
        )
    }
}
