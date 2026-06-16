package com.fandex.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * 内容索引数据模型
 *
 * 对应 assets/dist-mobile/index.json 的数据结构
 * 输入：JSON 字符串
 * 输出：结构化的分类与模块数据
 */
data class ContentIndex(
    val version: String,
    @SerializedName("generatedAt") val generatedAt: String,
    val categories: List<Category>,
    val modules: List<Module>,
    val documents: List<Document>
)

/**
 * 分类数据模型
 *
 * 输入：JSON 中的分类对象
 * 输出：分类 ID、标签、颜色
 */
data class Category(
    val id: String,
    val label: String,
    val color: String
)

/**
 * 模块数据模型
 *
 * 输入：JSON 中的模块对象
 * 输出：模块 ID、标题、分类、文档列表
 */
data class Module(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val documents: List<String>
)

/**
 * 文档数据模型
 *
 * 输入：JSON 中的文档对象
 * 输出：文档 slug、标题、所属模块、分类、难度、描述
 */
data class Document(
    val slug: String,
    val title: String,
    val module: String,
    val category: String,
    val difficulty: String,
    val description: String
)

/**
 * 复习进度 Room 实体
 *
 * 功能：记录每篇文档的间隔重复复习进度
 * 输入：文档标识（module + slug）
 * 输出：下次复习时间、复习次数、难度因子等
 */
@Entity(tableName = "review_progress")
data class ReviewProgress(
    @PrimaryKey
    val id: String,
    val module: String,
    val slug: String,
    val title: String,
    val category: String,
    val nextReviewAt: Long,
    val lastReviewAt: Long,
    val reviewCount: Int,
    val easeFactor: Double,
    val intervalDays: Int
) {
    companion object {
        /**
         * 生成复习记录的唯一标识
         *
         * 输入：模块 ID、文档 slug
         * 输出：组合字符串 "module:slug"
         */
        fun createId(module: String, slug: String): String = "$module:$slug"
    }
}

/**
 * Quiz 数据模型（从 JSON 加载）
 *
 * 输入：index.json 或 quizzes.json 中的 quiz 条目
 * 输出：题目类型、问题、答案、选项、提示、解释
 */
data class QuizItem(
    val type: String,
    val question: String,
    val answer: String,
    val options: List<String>,
    val hint: String,
    val explanation: String,
    val module: String,
    val slug: String
)
