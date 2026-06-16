<div align="center">

# FANDEX-App

**离线知识，随时可达** · Android 原生阅读器

FANDEX 知识体系的 Android 平台完全离线查阅应用。无需网络依赖，内置 49 个模块、1933 篇文档，支持中英日三语界面与深浅色双模式，基于 Kotlin + 原生 Compose 渲染，为移动端提供流畅的技术知识阅读体验。

[![下载 APK](https://img.shields.io/badge/下载_APK-fanquanpp.github.io%2FFANDEX--App-2563eb?style=for-the-badge&logo=android&logoColor=white)](https://fanquanpp.github.io/FANDEX-App/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![文档数](https://img.shields.io/badge/文档-1933-0ea5e9?style=flat-square)](https://fanquanpp.github.io/FANDEX-App/)
[![模块数](https://img.shields.io/badge/模块-49-8b5cf6?style=flat-square)](https://fanquanpp.github.io/FANDEX-App/)

</div>

---

## 关于

FANDEX-App 是 FANDEX 知识体系在 Android 平台的离线查阅应用。所有内容打包于应用内部，无需网络连接即可浏览全部文档，适用于通勤、旅行等无网场景下的技术学习与查阅。

应用采用 Kotlin 编写，基于 Jetpack Compose + Material 3 构建原生界面，通过 commonmark-java 实现纯原生 Markdown 渲染，不依赖 WebView，确保阅读体验流畅且一致。

## 功能特性

| 特性 | 说明 |
| :--- | :--- |
| 离线阅读 | 内置 49 个模块、1933 篇文档，无需网络即可浏览全部内容 |
| 分类体系 | 覆盖人工智能、前端、后端、数据库、计算机科学、数学、云与基础设施、工具链 8 大技术领域 |
| 原生渲染 | 基于 commonmark-java 的原生 Compose Markdown 渲染器，无 WebView 依赖 |
| 代码高亮 | 支持 16 种编程语言的轻量级语法高亮，代码块一键复制 |
| 深浅双模式 | 默认深色主题，支持亮色/深色切换，DataStore 持久化保存 |
| 三语界面 | 中文/英文/日语界面切换，文档内容保持中文 |
| 启动页 | 1.8 秒品牌展示页 + 欢迎语，侧边栏可关闭 |
| 侧边栏导航 | 抽屉式侧边栏，分类与模块快速跳转 |
| 字体缩放 | 阅读页支持 0.8x - 1.4x 字体大小调节 |
| 翻页浏览 | 文档内上一篇/下一篇快速切换，页码显示 |
| 返回顶部 | 悬浮按钮一键回到文档顶部 |

## 内容分类

| 分类 | 标签 | 覆盖范围 |
| :--- | :--- | :--- |
| ai | 人工智能 | Agent、LLM、NLP、深度学习、生成式 AI、AI 伦理等 |
| frontend | 前端技术 | HTML5、CSS、JavaScript、TypeScript、React、Vue 3 |
| backend | 后端技术 | Go、Java、Kotlin、C#、Lua |
| database | 数据库 | MySQL、PostgreSQL、Redis、SQL、大数据 |
| cs | 计算机科学 | C、C++、算法、网络、安全、软件架构与工程 |
| math | 数学 | 微积分、线性代数、概率论、离散数学、数据分析 |
| cloud | 云与基础设施 | 云计算、IoT、鸿蒙开发 |
| tools | 工具链 | Git、GitHub、DevOps、Markdown、技术英语 |

> 49 个模块 · 1933 篇文档 · 8 大技术领域 · 完全离线

## 技术栈

| 层级 | 技术 | 说明 |
| :--- | :--- | :--- |
| 语言 | Kotlin 2.0.21 | 主开发语言 |
| UI 框架 | Jetpack Compose + Material 3 | 声明式 UI |
| 导航 | Navigation Compose 2.8.5 | 页面路由 |
| 文档渲染 | commonmark-java + 原生 Compose 组件 | 无 WebView 依赖 |
| 偏好存储 | DataStore Preferences | 主题/语言/字体/启动页持久化 |
| JSON 解析 | Gson 2.11.0 | 内容索引解析 |
| 最低 SDK | 26 (Android 8.0) | 兼容性下限 |
| 目标 SDK | 35 (Android 15) | 目标平台 |

## 架构

```
UI 层        Jetpack Compose + Material 3
Service 层   内容加载 / 导航调度
Data 层      DataStore 偏好存储 + JSON 内容索引
```

### 页面结构

| 页面 | 路由 | 职责 |
| :--- | :--- | :--- |
| HomeScreen | /home | 分类浏览、模块卡片 |
| ModuleScreen | /module/{id} | 模块文档列表（带编号） |
| ArticleScreen | /article/{module}/{slug}/{title} | 原生 Markdown 文档阅读 |

## 内容源约束

FANDEX 仓库（[https://github.com/fanquanpp/FANDEX](https://github.com/fanquanpp/FANDEX)）为全部项目的根本内容源，FANDEX-App 仅对其保持只读引用，不修改、不覆盖任何原始内容。

FANDEX-App 的内容来源于 FANDEX-Web 仓库的 `dist-mobile.zip` 导出产物。内容更新流程：

1. FANDEX 仓库维护原始 Markdown 文档
2. FANDEX-Web 构建时生成 `dist-mobile.zip` 导出产物
3. FANDEX-App 通过 `scripts/process-content.ps1` 从 FANDEX-Web 的 Markdown 文档生成移动端 JSON 索引
4. 输出至 `assets/dist-mobile/` 目录

```powershell
# 重新生成内容索引
powershell -ExecutionPolicy Bypass -File scripts/process-content.ps1
```

## 下载

| 平台 | 地址 | 说明 |
| :--- | :--- | :--- |
| 移动端 | [fanquanpp.github.io/FANDEX-App](https://fanquanpp.github.io/FANDEX-App/) | 直接下载 APK |
| 桌面端 | [GitHub Releases](https://github.com/fanquanpp/FANDEX-App/releases) | 历史版本归档 |

> 安装前请在系统设置中允许"安装未知来源应用"权限。

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

## 更新日志规则

| 版本变更 | 类型 | 记录要求 |
| :--- | :--- | :--- |
| 1.x.x -> 2.x.x | 大版本更新 | 新模块、新功能、新页面增加及重构，须详细说明更新内容 |
| 1.0.x -> 1.1.x | 小更新 | 小 BUG 修复，简要书写 |
| 1.x.0 -> 1.x.1 | 补丁修复 | 统一书写"修复了一些 BUG" |

### 版本留存规则

- 仅留存每个大版本的最终安装包
- 大版本初始安装包须单独留存

## 关联项目

FANDEX-App 是 FANDEX 知识体系的一部分，以下为关联仓库：

| 仓库 | 说明 |
| :--- | :--- |
| [FANDEX](https://github.com/fanquanpp/FANDEX) | 基于 GitHub Pages 构建的云端在线查阅平台，为最早建立的内容仓库，文档体系最为完善 |
| [FANDEX-Web](https://github.com/fanquanpp/FANDEX-Web) | Astro 5 SSG 知识学习平台，四层分离架构，具备 AI 能力、三种阅读模式、知识图谱、复习卡片系统 |

## 免责声明

本仓库所有内容均以开放共享为宗旨，不主张知识产权保护。任何个人或机构均可自由获取、使用、修改和分发本仓库内容，对本仓库内容的使用不设任何限制，包括但不限于学习、研究、修改、分发及商业用途。因使用本仓库内容所产生的一切后果，均由使用者自行承担，本仓库及其作者、维护者不对使用后果承担任何形式的责任。

本仓库所有内容均由人工与 AI 共同编写、搜集、整理与编排，可能存在遗漏、过时或错误之处，使用者应结合官方文档与权威资料进行独立验证。
