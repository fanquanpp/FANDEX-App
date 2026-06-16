package com.fandex.app.data

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

/**
 * 内容索引加载服务
 *
 * 功能：从 assets/dist-mobile/index.json 加载并解析内容索引
 * 输入：Context（用于访问 assets）
 * 输出：ContentIndex 数据对象
 * 流程：打开 assets 文件 -> 读取 JSON -> Gson 解析 -> 返回数据对象
 */
object ContentLoader {

    private const val INDEX_PATH = "dist-mobile/index.json"

    /**
     * 加载内容索引
     *
     * 输入：Context
     * 输出：ContentIndex 或 null（加载失败时）
     * 流程：打开文件流 -> InputStreamReader 读取 -> Gson 反序列化
     */
    fun loadIndex(context: Context): ContentIndex? {
        return try {
            val inputStream = context.assets.open(INDEX_PATH)
            val reader = InputStreamReader(inputStream, "UTF-8")
            val index = Gson().fromJson(reader, ContentIndex::class.java)
            reader.close()
            inputStream.close()
            index
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 加载指定文档的 HTML 内容
     *
     * 输入：Context、分类 ID、文档 slug
     * 输出：HTML 字符串或 null
     * 流程：拼接路径 -> 打开文件流 -> 读取为字符串
     */
    fun loadDocumentHtml(context: Context, category: String, slug: String): String? {
        return try {
            val path = "dist-mobile/docs/$category/$slug.html"
            val inputStream = context.assets.open(path)
            val reader = InputStreamReader(inputStream, "UTF-8")
            val content = reader.readText()
            reader.close()
            inputStream.close()
            content
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 加载指定文档的 Markdown 内容
     *
     * 输入：Context、分类 ID、文档 slug
     * 输出：Markdown 字符串或 null
     * 流程：拼接路径 -> 尝试加载 .md 文件 -> 读取为字符串
     */
    fun loadDocumentMarkdown(context: Context, category: String, slug: String): String? {
        return try {
            val path = "dist-mobile/docs/$category/$slug.md"
            val inputStream = context.assets.open(path)
            val reader = InputStreamReader(inputStream, "UTF-8")
            val content = reader.readText()
            reader.close()
            inputStream.close()
            content
        } catch (e: Exception) {
            null
        }
    }
}
