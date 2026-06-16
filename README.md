# FANDEX-App

FANDEX Android 客户端，基于 Kotlin + WebView 架构。

## 架构

- UI层：Jetpack Compose + WebView 混合渲染
- Service层：内容加载、离线缓存、复习调度
- Data层：Room 本地数据库 + JSON 内容索引

## 构建

```bash
./gradlew assembleDebug
```

## 内容更新

内容通过 `content/` 目录管理，与 FANDEX-Web 仓库共享同一套 Markdown 文档。
构建时由 `scripts/export-content.sh` 将 Markdown 转换为移动端 JSON 索引。
