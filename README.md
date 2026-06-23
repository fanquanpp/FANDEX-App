<div align="center">

# FANDEX-App

**代码语法速查伴侣** · Android 原生应用

为已在 FANDEX-web 完成基础学习的学习者打造的离线代码速查工具。当学习者在实践中忘记语法、需要确认函数签名或查阅使用公式时，打开 App 即可即查即用。内置 17 个编程语言模块、207 篇语法速查文档，所有文档严格遵守统一的格式模板，提供语法签名与代码示例，而非冗长的教学讲解。

[![下载 APK](https://img.shields.io/badge/下载_APK-fanquanpp.github.io%2FFANDEX--App-2563eb?style=for-the-badge&logo=android&logoColor=white)](https://fanquanpp.github.io/FANDEX-App/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![文档数](https://img.shields.io/badge/文档-207-0ea5e9?style=flat-square)](https://fanquanpp.github.io/FANDEX-App/)
[![模块数](https://img.shields.io/badge/模块-17-8b5cf6?style=flat-square)](https://fanquanpp.github.io/FANDEX-App/)
[![完全开源](https://img.shields.io/badge/开源-完全共享-22c55e?style=flat-square)](https://github.com/fanquanpp/FANDEX-App)

</div>

---

## 项目背景

FANDEX 项目的创立初衷在于：**协助零基础学习者迈出计算机科学学习的第一步**。

当学习者在 FANDEX-web 上完成概念学习后，进入自主实践阶段时，常面临以下场景：

- 遗忘某个函数的语法写法
- 不确定某个控制结构的格式
- 需要快速确认某个 API 的参数顺序
- 回忆不起某个语法的使用公式

FANDEX-App 正是为上述场景而设计。其不包含教学讲解，仅提供**即查即用的语法参考**——语法签名、代码示例、使用公式。所有文档严格遵守统一的格式模板，确保查阅体验一致。

**核心理念：**

- 实践导向：为已具备基础的学习者提供实践中的语法速查
- 公式化呈现：每个语法点提供语法签名（公式）与代码示例，而非冗长讲解
- 完全离线：无需网络，随时随地查阅
- 统一模板：所有文档严格遵守格式模板，确保查阅体验一致

## 项目概述

FANDEX-App 是 FANDEX 知识体系的**离线移动速查应用**。其聚焦于编程语言的语法格式、函数签名、控制结构、类型定义等即时查阅场景，不包含教学讲解、概念阐述、项目实战等内容，仅提供即查即用的语法参考。

作为学习者在移动场景下的离线伴侣，FANDEX-App 采用 Kotlin + Jetpack Compose 原生渲染，实现完全离线、即开即查的体验。无论是在通勤途中、午休间隙，还是在无网络环境中，均可随时查阅语法。

## 文档格式规范

FANDEX-App 内所有文档严格遵守统一的格式模板（详见 `FANDEXAPP文档格式模板（不可删除）.md`）。

**模板核心要素：**

| 要素       | 说明                                                         |
| :--------- | :----------------------------------------------------------- |
| 语法签名   | 使用行内代码标注完整语法格式，参数使用符号约定（`< >` 必填、`[ ]` 可选） |
| 代码示例   | 使用带语言标签的代码块，注释说明用途                          |
| 写法类型   | 标注单行写法 / 换行写法，适配不同场景                        |
| 分类组织   | 按语法功能分类，使用二级标题分隔                             |

**文档结构示例：**

```
# <模块名> 语法速查手册

> 符号约定：< > 必填参数 | [ ] 可选参数

## <语法分类>

**<写法类型>：<语法用途>**
`<语法签名>`
```<语言>
-- 注释说明
<代码示例>;
```
```

> 每个语法点必须包含语法签名和代码示例两部分，缺一不可。

## 仓库特色

| 特色维度 | 说明                                                                 |
| :--- | :--- |
| 完全离线 | 无需任何网络依赖，所有文档内置，飞行模式下正常使用                  |
| 原生渲染 | 基于 commonmark-java 的原生 Compose Markdown 渲染器，无 WebView 依赖 |
| 公式化   | 每个语法点提供语法签名（公式）+ 代码示例，即查即用                  |
| 统一模板 | 所有文档严格遵守格式模板，查阅体验一致                              |
| 三语界面 | 中文/英文/日语界面切换，文档内容保持中文                             |
| 深浅双模式 | 默认深色主题，支持亮色/深色切换，DataStore 持久化保存              |
| 移动优化 | 抽屉式侧边栏、字体缩放、翻页浏览、返回顶部                          |
| 完全开源 | 所有内容完全开源，允许任何用途（含商业用途），作者不承担任何责任     |

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

FANDEX-App 的内容来源于 FANDEX-web 仓库的文档，通过构建脚本生成移动端 JSON 索引。

```powershell
# 重新生成内容索引
powershell -ExecutionPolicy Bypass -File scripts/process-content.ps1
```

## 关联项目

FANDEX-App 是 FANDEX 知识体系的一部分，以下为关联仓库：

| 仓库                                                  | 定位             | 用处                                                                 | 特色                                                                                              |
| :---------------------------------------------------- | :--------------- | :------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------ |
| [FANDEX-web](https://github.com/fanquanpp/FANDEX-web) | 线上学习平台     | 零基础到本科毕业的完整在线自学，概念讲解与代码示例的系统化学习       | 内容基准仓库，51 模块 1993 篇文档，浏览器直接访问，交互测验与知识地图                            |
| [FANDEX-exe](https://github.com/fanquanpp/FANDEX-exe) | Windows 桌面端   | 无网络环境下的离线桌面学习，桌面级系统集成与本地文件操作             | Electron 桌面应用，完全离线访问，内置静态服务器，系统菜单与多窗口，离线 ZIP 包与移动端导出产物    |

## 下载

| 平台 | 地址 | 说明 |
| :--- | :--- | :--- |
| 移动端 | [fanquanpp.github.io/FANDEX-App](https://fanquanpp.github.io/FANDEX-App/) | 下载页直接下载 APK |
| 桌面端 | [GitHub Releases](https://github.com/fanquanpp/FANDEX-App/releases) | 版本归档 |

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

## 内容说明

本仓库为 FANDEX 知识体系的离线移动速查应用，内容来源于 FANDEX-web 仓库，通过构建脚本生成移动端 JSON 索引。FANDEX-web 仓库为全部项目的根本内容源，本仓库仅允许只读引用，如需变更内容须从 FANDEX-web 仓库复制后在本仓库内进行适配与优化。

本仓库所有内容均由人工与人工智能（AI）共同编写、搜集、整理与编排。受限于编撰方式与知识更新速度，内容可能存在遗漏、过时或错误之处，使用者应结合官方文档与权威资料进行独立验证。

## 开源共享与免责声明

### 开源共享

本仓库所有内容均完全开源。任何个人或机构均可自由获取、使用、修改和分发本仓库的全部内容，包括但不限于学习、研究、修改、分发及商业用途，无需获得作者授权。

### 免责声明

- 本仓库所有内容均由人工与人工智能技术协同编撰、搜集、整理与编排。受限于编撰方式及知识更新周期，内容可能存在遗漏、过时或错误之处。使用者应结合官方文档与权威资料进行独立验证与核实，切勿将本仓库内容作为唯一依据
- 因使用或引用本仓库内容所产生的一切直接或间接后果，均由使用者自行承担。本仓库作者及维护者不对使用后果承担任何形式的法律责任或连带责任
- 本仓库不保证内容的准确性、完整性、时效性或适用性。在任何情况下，本仓库作者及维护者均不对因使用本仓库内容而导致的任何损失或损害承担责任
