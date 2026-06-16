package com.fandex.app.data

/**
 * 多语言字符串管理
 *
 * 功能：根据当前语言设置返回对应的 UI 字符串
 * 输入：Language 枚举（ZH/EN）
 * 输出：所有 UI 文本的中英文版本
 *
 * 使用方式：Strings.get(Language.ZH).home
 */
object Strings {

    /**
     * 支持的语言枚举
     */
    enum class Language { ZH, EN }

    /**
     * 语言字符串集合
     *
     * 功能：持有当前语言下的所有 UI 字符串
     */
    data class LangStrings(
        val appName: String,
        val homeSubtitle: String,
        val loading: String,
        val searchHint: String,
        val all: String,
        val docs: String,
        val modules: String,
        val documents: String,
        val noResults: String,
        val noContent: String,
        val home: String,
        val review: String,
        val light: String,
        val dark: String,
        val lightMode: String,
        val darkMode: String,
        val back: String,
        val due: String,
        val learning: String,
        val mastered: String,
        val addDocsToReview: String,
        val noReviewItems: String,
        val allCaughtUp: String,
        val addDocsHint: String,
        val comeBackLater: String,
        val dueForReview: String,
        val reviewed: String,
        val howWellDidYouRemember: String,
        val forgot: String,
        val barely: String,
        val hard: String,
        val ok: String,
        val easy: String,
        val perfect: String,
        val previouslyReviewed: String,
        val day: String,
        val language: String
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
            searchHint = "\u641C\u7D22\u6A21\u5757\u6216\u6587\u6863",
            all = "\u5168\u90E8",
            docs = "\u7BC7",
            modules = "\u4E2A\u6A21\u5757",
            documents = "\u6587\u6863",
            noResults = "\u672A\u627E\u5230\u7ED3\u679C",
            noContent = "\u6682\u65E0\u5185\u5BB9",
            home = "\u9996\u9875",
            review = "\u590D\u4E60",
            light = "\u4EAE\u8272",
            dark = "\u6697\u8272",
            lightMode = "\u4EAE\u8272\u6A21\u5F0F",
            darkMode = "\u6697\u8272\u6A21\u5F0F",
            back = "\u8FD4\u56DE",
            due = "\u5F85\u590D\u4E60",
            learning = "\u5B66\u4E60\u4E2D",
            mastered = "\u5DF2\u638C\u63E1",
            addDocsToReview = "\u6DFB\u52A0\u6587\u6863\u5230\u590D\u4E60",
            noReviewItems = "\u6682\u65E0\u590D\u4E60\u9879",
            allCaughtUp = "\u5168\u90E8\u590D\u4E60\u5B8C\u6210!",
            addDocsHint = "\u6DFB\u52A0\u6587\u6863\u5F00\u59CB\u590D\u4E60",
            comeBackLater = "\u7A0D\u540E\u518D\u6765\u590D\u4E60",
            dueForReview = "\u5F85\u590D\u4E60",
            reviewed = "\u5DF2\u590D\u4E60",
            howWellDidYouRemember = "\u4F60\u8BB0\u5F97\u591A\u719F\uFF1F",
            forgot = "\u5FD8\u8BB0\u4E86",
            barely = "\u6709\u70B9\u5370\u8C61",
            hard = "\u60F3\u4E86\u4E00\u4F1A",
            ok = "\u8FD8\u884C",
            easy = "\u8F7B\u677E",
            perfect = "\u5B8C\u7F8E",
            previouslyReviewed = "\u4E4B\u524D\u590D\u4E60\u8FC7",
            day = "\u5929",
            language = "\u8BED\u8A00"
        )
        Language.EN -> LangStrings(
            appName = "FANDEX",
            homeSubtitle = "Step by step, from the first line of code to understanding the world",
            loading = "Loading...",
            searchHint = "Search modules or docs",
            all = "All",
            docs = "docs",
            modules = "modules",
            documents = "Documents",
            noResults = "No results found",
            noContent = "No content available",
            home = "Home",
            review = "Review",
            light = "Light",
            dark = "Dark",
            lightMode = "Light Mode",
            darkMode = "Dark Mode",
            back = "Back",
            due = "Due",
            learning = "Learning",
            mastered = "Mastered",
            addDocsToReview = "Add docs to review",
            noReviewItems = "No review items",
            allCaughtUp = "All caught up!",
            addDocsHint = "Add documents to start reviewing",
            comeBackLater = "Come back later for more reviews",
            dueForReview = "Due for review",
            reviewed = "reviewed",
            howWellDidYouRemember = "How well did you remember?",
            forgot = "Forgot",
            barely = "Barely",
            hard = "Hard",
            ok = "OK",
            easy = "Easy",
            perfect = "Perfect",
            previouslyReviewed = "Previously reviewed",
            day = "d",
            language = "Language"
        )
    }
}
