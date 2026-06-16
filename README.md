# FANDEX-App

FANDEX 知识体系的 Android 客户端 -- 离线优先的技术知识阅读与间隔重复复习工具。

## 特性

- **离线阅读** -- 内置 Markdown 文档索引，无需网络即可浏览全部内容
- **分类体系** -- 覆盖 AI、后端、云原生等 8 大技术领域，层级清晰
- **间隔复习** -- 基于间隔重复算法的知识巩固模式（开发中）
- **WebView 渲染** -- 文档以 HTML 形式呈现，支持代码高亮与公式排版
- **Material Design 3** -- 遵循 MD3 设计规范，支持亮色/暗色主题

## 架构

```
UI 层        Jetpack Compose + WebView 混合渲染
Service 层   内容加载 / 离线缓存 / 复习调度
Data 层      Room 本地数据库 + JSON 内容索引
```

### 页面结构

| 页面 | 入口 | 职责 |
|------|------|------|
| HomeActivity | LAUNCHER | 分类浏览与模块检索 |
| ArticleActivity | HomeActivity | WebView 文档阅读 |
| ReviewActivity | HomeActivity | 间隔重复复习 |

### 内容分类

| 分类 | 标签 | 覆盖范围 |
|------|------|----------|
| ai | 人工智能 | Agent、LLM、NLP、深度学习、生成式 AI 等 |
| backend | 后端技术 | Go、Java、Kotlin、C#、Lua |
| cloud | 云与基础设施 | DevOps、IoT |
| tools | 工具链 | -- |
| frontend | 前端技术 | -- |
| database | 数据库 | -- |
| cs | 计算机科学 | -- |
| math | 数学 | -- |

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin 2.0.21 |
| UI 框架 | Jetpack Compose + Material 3 |
| 文档渲染 | WebView (JavaScript 禁用) |
| 本地存储 | Room 2.6.1 |
| JSON 解析 | Gson 2.11.0 |
| 导航 | Navigation Compose 2.8.5 |
| 最低 SDK | 26 (Android 8.0) |
| 目标 SDK | 35 (Android 15) |

## 构建

```bash
# Debug 构建
cd android && ./gradlew assembleDebug

# Release 构建（启用混淆）
cd android && ./gradlew assembleRelease
```

APK 输出路径：

- Debug: `android/app/build/outputs/apk/debug/`
- Release: `android/app/build/outputs/apk/release/`

## 内容更新

内容通过 `content/` 目录管理，与 FANDEX-Web 仓库共享同一套 Markdown 文档。构建时由 `scripts/export-content.sh` 将 Markdown 转换为移动端 JSON 索引，输出至 `assets/dist-mobile/`。

## CI/CD

推送至 `main` 分支时自动触发 GitHub Actions 构建 Debug APK 并上传为 Artifact。

## 许可

私有仓库，未授权禁止使用。
