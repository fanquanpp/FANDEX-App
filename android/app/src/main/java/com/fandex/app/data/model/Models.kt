package com.fandex.app.data.model

/**
 * 索引清单数据类
 * 对应 assets/index.json 的顶层结构
 */
data class Manifest(
    val version: String,
    val generatedAt: String,
    val modules: List<Module>
)

/**
 * 模块数据类
 * 每个模块包含名称和文档列表
 */
data class Module(
    val name: String,
    val docs: List<Doc>
)

/**
 * 文档数据类
 * path 字段相对于 assets/content/ 目录
 */
data class Doc(
    val title: String,
    val path: String
)
