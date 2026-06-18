package com.fandex.app.data

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

/**
 * 内容索引加载服务
 *
 * 功能：从 assets/dist-mobile/ 加载并解析内容索引和文档
 * 输入：Context（用于访问 assets）
 * 输出：ContentIndex 数据对象、文档内容字符串
 * 流程：打开 assets 文件 -> 读取 JSON/文本 -> 解析/返回
 */
object ContentLoader {

    /** 索引文件路径 */
    private const val INDEX_PATH = "dist-mobile/index.json"

    /** JSON 解析器（线程安全，全局复用） */
    private val gson = Gson()

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
            val index = gson.fromJson(reader, ContentIndex::class.java)
            reader.close()
            inputStream.close()
            index
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 加载指定文档的 Markdown 内容
     *
     * 输入：Context、模块 ID、文档 slug
     * 输出：Markdown 字符串或 null
     * 流程：拼接路径 -> 打开文件流 -> 读取为字符串
     */
    fun loadDocumentMarkdown(context: Context, module: String, slug: String): String? {
        return try {
            val path = "dist-mobile/docs/$module/$slug.md"
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
