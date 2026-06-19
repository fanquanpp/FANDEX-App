<div align="center">

# FANDEX-App

**零基础学习者的离线语法速查伴侣** · Android 原生应用

一款纯粹的代码语法速查工具，为移动场景而生的离线学习伴侣。无需网络依赖，内置 17 个编程语言模块、207 篇语法速查文档，覆盖 C/C++/Java/Go/Python/JavaScript/TypeScript/Kotlin/C#/CSS/SQL/MySQL/PostgreSQL/Redis/Git/Markdown/Lua，基于 Kotlin + Jetpack Compose 原生渲染，为自学者提供即查即用的语法参考，随时随地巩固所学。

[![下载 APK](https://img.shields.io/badge/下载_APK-fanquanpp.github.io%2FFANDEX--App-2563eb?style=for-the-badge&logo=android&logoColor=white)](https://fanquanpp.github.io/FANDEX-App/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![文档数](https://img.shields.io/badge/文档-207-0ea5e9?style=flat-square)](https://fanquanpp.github.io/FANDEX-App/)
[![模块数](https://img.shields.io/badge/模块-17-8b5cf6?style=flat-square)](https://fanquanpp.github.io/FANDEX-App/)
[![完全开源](https://img.shields.io/badge/开源-完全共享-22c55e?style=flat-square)](https://github.com/fanquanpp/FANDEX-App)

</div>

---

## 这是什么

FANDEX-App 是 FANDEX 知识体系的**离线移动速查应用**。它聚焦于编程语言的语法格式、函数签名、控制结构、类型定义等即时查阅场景，不包含教学讲解、概念阐述、项目实战等内容，仅提供即查即用的语法参考。

作为零基础学习者在移动场景下的离线伴侣，FANDEX-App 将 FANDEX-Web 平台中最核心的语法速查文档抽取为移动端产物，通过 Kotlin + Jetpack Compose 原生渲染，实现完全离线、即开即查的体验。当学习者在 FANDEX-Web 上完成概念学习后，可随时在 App 中查阅对应语言的语法格式，巩固所学。无论是在通勤途中、午休间隙，还是在没有网络的环境中，都能随时复习语法。

## 仓库特色

本仓库是 FANDEX 知识体系的**离线移动消费端**，承担以下独特角色：

| 特色维度 | 说明 |
| :--- | :--- |
| 完全离线 | 无需任何网络依赖，所有文档内置，飞行模式下正常使用 |
| 原生渲染 | 基于 commonmark-java 的原生 Compose Markdown 渲染器，无 WebView 依赖，性能优异 |
| 即查即用 | 聚焦语法速查场景，不包含教学讲解，仅提供即时语法参考 |
| 三语界面 | 中文/英文/日语界面切换，文档内容保持中文，适配多语言学习者 |
| 深浅双模式 | 默认深色主题，支持亮色/深色切换，DataStore 持久化保存 |
| 移动优化 | 抽屉式侧边栏、字体缩放、翻页浏览、返回顶部，为移动阅读深度优化 |
| 完全开源 | 所有内容完全开源共享，不主张知识产权保护，可自由获取、使用、修改和分发 |

## 内容分类

| 分类 | 标签 | 模块 |
| :--- | :--- | :--- |
| 编程语言 | 10 种 | C、C++、C#、Go、Java、JavaScript、Kotlin、Lua、Python、TypeScript |
| 前端技术 | 1 种 | CSS |
| 数据库 | 4 种 | MySQL、PostgreSQL、Redis、SQL |
| 工具链 | 1 种 | Git |
| 标记语言 | 1 种 | Markdown |

> 17 个模块 · 207 篇语法文档 · 5 大分类 · 完全离线

## 功能特性

| 特性 | 说明 |
| :--- | :--- |
| 离线速查 | 内置 17 个模块、207 篇语法文档，无需网络即可查阅 |
| 原生渲染 | 基于 commonmark-java 的原生 Compose Markdown 渲染器，无 WebView 依赖 |
| 代码高亮 | 支持 20+ 种编程语言的语法高亮，代码块一键复制 |
| 深浅双模式 | 默认深色主题，支持亮色/深色切换，DataStore 持久化保存 |
| 三语界面 | 中文/英文/日语界面切换，文档内容保持中文 |
| 启动页 | 1.8 秒品牌展示页 + 欢迎语，侧边栏可关闭 |
| 侧边栏导航 | 抽屉式侧边栏，分类与模块快速跳转 |
| 字体缩放 | 阅读页支持 0.8x - 1.4x 字体大小调节 |
| 翻页浏览 | 文档内上一篇/下一篇快速切换，页码显示 |
| 返回顶部 | 悬浮按钮一键回到文档顶部 |

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

## 内容来源

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

## 关联项目

FANDEX-App 是 FANDEX 知识体系的一部分，以下为关联仓库：

| 仓库 | 定位 | 特色 |
| :--- | :--- | :--- |
| [FANDEX](https://github.com/fanquanpp/FANDEX) | 内容基准仓库 | 原始 Astro 版本，FANDEX-Web 的前身，面向零基础学习者的完整自学教程，51 模块 6 分类，从环境搭建到 AI 前沿，文档体系最为完善 |
| [FANDEX-Web](https://github.com/fanquanpp/FANDEX-Web) | 学习平台 | Astro 5 SSG 知识学习平台，面向零基础自学者，monorepo 四层分离架构，鼓励学习者运用外部 AI 工具辅助学习，同时保留完整的非 AI 学习路径，提供 Web/桌面/离线三种交付形态 |

## 更新日志规则

| 版本变更 | 类型 | 记录要求 |
| :--- | :--- | :--- |
| 1.x.x -> 2.x.x | 大版本更新 | 新模块、新功能、新页面增加及重构，须详细说明更新内容 |
| 1.0.x -> 1.1.x | 小更新 | 小 BUG 修复，简要书写 |
| 1.x.0 -> 1.x.1 | 补丁修复 | 统一书写"修复了一些 BUG" |

### 版本留存规则

- 仅留存每个大版本的最终安装包
- 大版本初始安装包须单独留存

## 开源共享声明

本仓库所有内容以开放共享为目的，不主张知识产权保护。任何个人或机构均可自由获取、使用、修改和分发，包括但不限于学习、研究、修改、分发及商业用途。因使用本仓库内容所产生的一切后果，由使用者自行承担，与本仓库及其作者、维护者无关。

本仓库所有内容均由人工与 AI 共同编写、搜集、整理与编排，可能存在遗漏、过时或错误之处，使用者应结合官方文档与权威资料进行独立验证。
