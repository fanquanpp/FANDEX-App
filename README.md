# FANDEX-App

FANDEX 知识体系的 Android 客户端 -- 离线优先的技术知识阅读与间隔重复复习工具。

## 特性

- **离线阅读** -- 内置 49 个模块、1933 篇 Markdown 文档，无需网络即可浏览全部内容
- **分类体系** -- 覆盖 AI、前端、后端、数据库、计算机科学、数学、云与基础设施、工具链 8 大技术领域
- **间隔复习** -- 基于 SM-2 间隔重复算法的知识巩固模式，6 级自评反馈
- **搜索筛选** -- 首页支持关键词搜索和分类筛选，快速定位目标内容
- **Markdown 渲染** -- 文档以 HTML 形式呈现，支持代码块、引用、表格、列表等元素
- **Material Design 3** -- 遵循 MD3 设计规范，支持亮色/暗色主题与动态取色

## 下载

前往 [Releases](https://github.com/fanquanpp/FANDEX-App/releases/latest) 下载最新版本。

> 下载 `.zip` 文件后，将后缀名改为 `.apk` 即可安装。

## 架构

```
UI 层        Jetpack Compose + Material 3
Service 层   内容加载 / 复习调度 / SM-2 算法
Data 层      Room 本地数据库 + JSON 内容索引
```

### 页面结构

| 页面 | 路由 | 职责 |
|------|------|------|
| HomeScreen | /home | 分类浏览、模块检索、搜索筛选 |
| ModuleScreen | /module/{id} | 模块文档列表 |
| ArticleScreen | /article/{module}/{slug}/{title} | WebView 文档阅读 |
| ReviewScreen | /review | 间隔重复复习与自评 |

### 内容分类

| 分类 | 标签 | 覆盖范围 |
|------|------|----------|
| ai | 人工智能 | Agent、LLM、NLP、深度学习、生成式 AI、AI 伦理等 |
| frontend | 前端技术 | HTML5、CSS、JavaScript、TypeScript、React、Vue 3 |
| backend | 后端技术 | Go、Java、Kotlin、C#、Lua |
| database | 数据库 | MySQL、PostgreSQL、Redis、SQL、大数据 |
| cs | 计算机科学 | C、C++、算法、网络、安全、软件架构与工程 |
| math | 数学 | 微积分、线性代数、概率论、离散数学、数据分析 |
| cloud | 云与基础设施 | 云计算、IoT、鸿蒙开发 |
| tools | 工具链 | Git、GitHub、DevOps、Markdown、技术英语 |

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin 2.0.21 |
| UI 框架 | Jetpack Compose + Material 3 |
| 导航 | Navigation Compose 2.8.5 |
| 文档渲染 | WebView (JavaScript 禁用) + MarkdownRenderer |
| 本地存储 | Room 2.6.1 (KSP) |
| JSON 解析 | Gson 2.11.0 |
| 复习算法 | SM-2 间隔重复 |
| 最低 SDK | 26 (Android 8.0) |
| 目标 SDK | 35 (Android 15) |

## 构建

```bash
# 前置条件：JDK 17 + Android SDK (platforms;android-35)

# Debug 构建
cd android && ./gradlew assembleDebug

# Release 构建（启用混淆 + 签名）
cd android && ./gradlew assembleRelease
```

APK 输出路径：

- Debug: `android/app/build/outputs/apk/debug/`
- Release: `android/app/build/outputs/apk/release/`

## 内容更新

内容通过 `scripts/process-content.ps1` 从 FANDEX-Web 仓库的 Markdown 文档生成移动端 JSON 索引，输出至 `assets/dist-mobile/`。

```powershell
# 重新生成内容索引
powershell -ExecutionPolicy Bypass -File scripts/process-content.ps1
```

## 许可

私有仓库，未授权禁止使用。
