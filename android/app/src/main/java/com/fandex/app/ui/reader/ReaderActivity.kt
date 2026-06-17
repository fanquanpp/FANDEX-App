package com.fandex.app.ui.reader

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.fandex.app.R
import com.fandex.app.data.repository.ManifestRepository
import com.fandex.app.render.MarkdownRenderer

/**
 * 阅读界面 Activity
 * 功能：加载并渲染 Markdown 文档
 */
class ReaderActivity : Activity() {

    private lateinit var repository: ManifestRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        repository = ManifestRepository(this)

        val path = intent.getStringExtra("path") ?: return finish()
        val title = intent.getStringExtra("title") ?: ""

        actionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(true)
        }

        /* 加载并渲染文档 */
        try {
            val raw = repository.loadDocument(path)
            val rendered = MarkdownRenderer.render(raw)
            val textView = findViewById<TextView>(R.id.reader_text)
            textView.text = rendered
        } catch (_: Exception) {
            findViewById<TextView>(R.id.reader_text).text = "文档加载失败：$path"
        }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}
