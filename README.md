# FANDEX-App

FANDEX 知识体系的 Android 客户端 -- 完全离线的技术知识阅读工具。

## 特性

- **离线阅读** -- 内置 49 个模块、1933 篇文档，无需网络即可浏览全部内容
- **分类体系** -- 覆盖人工智能、前端、后端、数据库、计算机科学、数学、云与基础设施、工具链 8 大技术领域
- **原生渲染** -- 基于 commonmark-java 的原生 Compose Markdown 渲染器，无 WebView 依赖
- **代码高亮** -- 支持 16 种编程语言的轻量级语法高亮，代码块一键复制
- **深浅双模式** -- 默认深色主题，支持亮色/深色切换，DataStore 持久化保存
- **三语界面** -- 中文/英文/日语界面切换，文档内容保持中文
- **侧边栏导航** -- 抽屉式侧边栏，分类与模块快速跳转
- **字体缩放** -- 阅读页支持 0.8x - 1.4x 字体大小调节
- **翻页浏览** -- 文档内上一篇/下一篇快速切换，页码显示
- **返回顶部** -- 悬浮按钮一键回到文档顶部

## 下载

前往 [Releases](https://github.com/fanquanpp/FANDEX-App/releases) 下载最新版本。

**移动端用户**：GitHub 网页版可能不显示下载按钮，请复制以下链接到浏览器地址栏打开：

```
https://github.com/fanquanpp/FANDEX-App/releases/download/v1.3.0-beta/FANDEX-v1.3.0-beta.apk
```

或通过 GitHub App 下载。

## 架构

```
UI 层        Jetpack Compose + Material 3
Service 层   内容加载 / 导航调度
Data 层      DataStore 偏好存储 + JSON 内容索引
```

### 页面结构

| 页面 | 路由 | 职责 |
|------|------|------|
| HomeScreen | /home | 分类浏览、模块卡片 |
| ModuleScreen | /module/{id} | 模块文档列表（带编号） |
| ArticleScreen | /article/{module}/{slug}/{title} | 原生 Markdown 文档阅读 |

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
| 文档渲染 | commonmark-java + 原生 Compose 组件 |
| 偏好存储 | DataStore Preferences |
| JSON 解析 | Gson 2.11.0 |
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
