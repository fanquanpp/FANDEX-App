package com.fandex.app.ui.about

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.fandex.app.BuildConfig
import com.fandex.app.R
import com.fandex.app.data.repository.ManifestRepository

/**
 * 关于界面 Activity
 * 功能：显示应用版本、内容版本、文档/模块统计
 */
class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        actionBar?.apply {
            title = "关于"
            setDisplayHomeAsUpEnabled(true)
        }

        val repository = ManifestRepository(this)
        val manifest = repository.loadManifest()

        val totalDocs = manifest?.modules?.sumOf { it.docs.size } ?: 0
        val totalModules = manifest?.modules?.size ?: 0

        findViewById<TextView>(R.id.app_version).text = "应用版本：${BuildConfig.VERSION_NAME}"
        findViewById<TextView>(R.id.content_version).text = "内容版本：${manifest?.version ?: "未知"}"
        findViewById<TextView>(R.id.doc_count).text = "文档总数：$totalDocs"
        findViewById<TextView>(R.id.module_count).text = "模块总数：$totalModules"
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}
