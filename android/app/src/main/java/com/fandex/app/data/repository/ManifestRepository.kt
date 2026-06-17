package com.fandex.app.data.repository

import android.content.Context
import com.fandex.app.data.model.Doc
import com.fandex.app.data.model.Manifest
import com.fandex.app.data.model.Module
import org.json.JSONObject

/**
 * 索引仓库：从 assets 加载 index.json 并解析为 Manifest 对象
 * 使用 org.json 手写解析，避免引入 Gson 增加体积
 */
class ManifestRepository(private val context: Context) {

    /**
     * 加载索引清单
     * 输入：无（从 assets/index.json 读取）
     * 输出：Manifest 对象，失败返回 null
     */
    fun loadManifest(): Manifest? {
        return try {
            val json = context.assets.open("index.json")
                .bufferedReader().use { it.readText() }
            parseJsonToManifest(json)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * 读取文档内容
     * 输入：文档路径（相对于 assets/content/ 目录）
     * 输出：文档原始文本
     */
    fun loadDocument(path: String): String {
        return context.assets.open("content/$path")
            .bufferedReader().use { it.readText() }
    }

    /**
     * 手写 JSON 解析（避免 Gson 依赖）
     */
    private fun parseJsonToManifest(json: String): Manifest {
        val obj = JSONObject(json)
        val modules = mutableListOf<Module>()
        val arr = obj.getJSONArray("modules")
        for (i in 0 until arr.length()) {
            val m = arr.getJSONObject(i)
            val docs = mutableListOf<Doc>()
            val docArr = m.getJSONArray("docs")
            for (j in 0 until docArr.length()) {
                val d = docArr.getJSONObject(j)
                docs.add(Doc(d.getString("title"), d.getString("path")))
            }
            modules.add(Module(m.getString("name"), docs))
        }
        return Manifest(obj.getString("version"), obj.getString("generatedAt"), modules)
    }
}
