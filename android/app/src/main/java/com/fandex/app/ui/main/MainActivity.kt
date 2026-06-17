package com.fandex.app.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.fandex.app.R
import com.fandex.app.data.model.Module
import com.fandex.app.data.repository.ManifestRepository
import com.fandex.app.ui.reader.ReaderActivity

/**
 * 主界面 Activity
 * 布局：DrawerLayout（抽屉 + 主内容区）
 * 功能：抽屉显示模块列表，主内容区显示文档列表
 */
class MainActivity : Activity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var repository: ManifestRepository
    private var modules: List<Module> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = ManifestRepository(this)
        drawerLayout = findViewById(R.id.drawer_layout)

        /* 设置 Toolbar */
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setActionBar(toolbar)
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size)
            title = "FANDEX"
        }
        toolbar.setNavigationOnClickListener { drawerLayout.open() }

        /* 加载索引 */
        val manifest = repository.loadManifest()
        if (manifest == null) {
            findViewById<TextView>(R.id.empty_text).text = "索引加载失败"
            return
        }
        modules = manifest.modules

        /* 抽屉模块列表 */
        val drawerList = findViewById<ListView>(R.id.drawer_list)
        val moduleNames = modules.map { it.name }
        drawerList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, moduleNames)
        drawerList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            showModuleDocs(modules[position])
            drawerLayout.closeDrawers()
        }

        /* 默认显示第一个模块 */
        if (modules.isNotEmpty()) {
            showModuleDocs(modules[0])
        }
    }

    /**
     * 显示指定模块的文档列表
     */
    private fun showModuleDocs(module: Module) {
        actionBar?.title = module.name
        val docList = findViewById<ListView>(R.id.doc_list)
        val docTitles = module.docs.map { it.title }
        docList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, docTitles)
        docList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val doc = module.docs[position]
            val intent = Intent(this, ReaderActivity::class.java)
            intent.putExtra("path", doc.path)
            intent.putExtra("title", doc.title)
            startActivity(intent)
        }
    }
}
