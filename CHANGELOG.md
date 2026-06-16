# 更新日志

所有版本均为测试版。

## v1.3.0-beta

**架构重构**

- 移除 WebView，使用原生 Compose Markdown 渲染器（commonmark-java AST -> Compose 组件）
- 新增 DataStore Preferences 持久化存储（主题、语言、字体大小）
- 新增 ModalNavigationDrawer 侧边栏导航

**功能**

- 默认深色主题
- 主题/语言/字体大小设置持久化，重启后保留
- 侧边栏：分类与模块快速跳转，任何页面均可打开
- 字体缩放：0.8x - 1.4x，阅读页 A-/A+ 按钮调节
- 翻页：文档内上一篇/下一篇 + 页码显示
- 返回顶部：悬浮按钮
- 三语界面：中文/英文/日语循环切换
- 代码块语法高亮：16 种语言，AnnotatedString + SpanStyle
- 代码块复制：ClipboardManager 一键复制

**界面**

- 紧凑底部导航栏：仅图标，无文字标签
- 紧凑顶部栏：精简按钮布局
- 模块卡片：显示分类名、文档数、简介
- 文档列表：编号圆圈 + 标题 + slug
- 分类色短分界线分隔模块和文档

**移除**

- 移除搜索功能
- 移除复习功能（ReviewScreen、SM-2 算法、Room 数据库）
- 移除 WebView 依赖

## v1.2.0-beta

- 移除搜索和复习功能，简化应用
- 新增日语界面语言（中文/英文/日语循环切换）
- 默认语言：中文
- 修复文章阅读页：复制按钮重叠、代码块溢出、内容溢出
- 新增翻页按钮（上一篇/下一篇）
- 新增返回顶部悬浮按钮
- 修复切换文档时深色模式闪屏（WebView 背景色预设）
- 文章 CSS 匹配 Material 3 主页风格
- 复制按钮默认隐藏，悬停时显示

## v1.1.1-beta

- 中英文双语界面支持
- 模块和文档间添加分类色短分界线
- 所有 UI 字符串国际化
- 版本号标记为 beta

## v1.1.0-beta

- 使用 commonmark-java 重写 Markdown 渲染器，支持完整语法
- 代码语法高亮：轻量级 JS 高亮器，支持 20+ 语言
- 代码块复制按钮
- 深色/亮色模式切换
- 文档编号显示
- CSS 移植自 FANDEX-Original-HTML

## v1.0.0-beta1

- 首个测试版本
- Navigation Compose 单 Activity 架构
- Room 数据库 + SM-2 间隔重复算法
- MarkdownRenderer 渲染器
- 搜索和分类筛选
- 49 个模块、1933 篇文档
