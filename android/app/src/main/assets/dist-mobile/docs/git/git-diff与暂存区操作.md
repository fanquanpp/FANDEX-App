# git-diff 与暂存区操作

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## diff 三种模式

**工作区与暂存区差异**：查看未暂存的修改
`git diff`

```bash
# 查看工作区与暂存区的差异
git diff;
```

**暂存区与最新提交差异**：查看已暂存的修改
`git diff --staged`

```bash
# 查看暂存区与 HEAD 的差异
git diff --staged;
```

**工作区与指定提交差异**：查看与指定提交的差异
`git diff <commit>`

```bash
# 查看工作区与 abc1234 提交的差异
git diff abc1234;
```

---

## 统计输出

**统计差异**：显示文件变更统计
`git diff --stat`

```bash
# 显示每个文件的增删行数统计
git diff --stat;
```

**数字统计**：以数字形式显示增删行数
`git diff --numstat`

```bash
# 输出格式：新增行数 删除行数 文件名
git diff --numstat;
```

---

## 过滤选项

**只看文件名**：仅显示变更的文件名
`git diff --name-only`

```bash
# 列出所有变更的文件名
git diff --name-only;
```

**只看文件名和状态**：显示文件名和变更类型
`git diff --name-status`

```bash
# M 修改 / A 新增 / D 删除
git diff --name-status;
```

**按路径过滤**：仅查看指定路径的差异
`git diff -- <路径>`

```bash
# 查看 src/ 目录的差异
git diff -- src/;
# 查看 JavaScript 文件的差异
git diff -- '*.js';
```

**排除路径**：排除指定文件
`git diff -- ':(exclude)<模式>'`

```bash
# 排除测试文件
git diff -- ':(exclude)*.test.js';
```

---

## 显示选项

**增加上下文行数**：调整 diff 显示的上下文
`git diff -U<行数>`

```bash
# 显示 5 行上下文（默认 3 行）
git diff -U5;
```

**忽略空白**：不显示空白变化
`git diff -w`

```bash
# 忽略所有空白变化
git diff -w;
```

**忽略行尾空白**：只忽略行尾空白
`git diff --ignore-space-at-eol`

```bash
# 只忽略行尾空白
git diff --ignore-space-at-eol;
```

**词语级别差异**：高亮词语级别的变化
`git diff --color-words`

```bash
# 词语级别的差异高亮
git diff --color-words;
```

**词语差异标记**：标记词语级别的差异
`git diff --word-diff`

```bash
# 词语级别的差异标记
git diff --word-diff;
```

**函数上下文**：显示完整函数
`git diff -W`

```bash
# 显示完整函数的差异
git diff -W;
```

---

## 比较选项

**比较两个分支**：显示分支间的差异
`git diff <分支1>..<分支2>`

```bash
# 比较 main 与 feature 分支
git diff main..feature;
```

**比较两个提交**：显示提交间的差异
`git diff <提交1>..<提交2>`

```bash
# 比较 abc1234 与 def5678 提交
git diff abc1234..def5678;
```

**比较分叉点以来的变化**：显示相对于基础分支的变更
`git diff <基础分支>...<目标分支>`

```bash
# feature 相对于 main 的变更
git diff main...feature;
```

**暂存区与 HEAD 差异**：查看已暂存但未提交的修改
`git diff --cached`

```bash
# 查看暂存区与 HEAD 的差异
git diff --cached;
```

---

## 比较特定文件

**比较特定文件在不同提交间的差异**：指定文件比较
`git diff <提交> -- <文件>`

```bash
# 查看 src/index.js 在最近 3 次提交的差异
git diff HEAD~3 -- src/index.js;
```

**比较两个分支的特定文件**：指定文件比较分支
`git diff <分支1> <分支2> -- <文件>`

```bash
# 比较 main 和 feature 分支的 package.json
git diff main feature -- package.json;
```

---

## 交互式 diff

**使用 difftool**：启动配置的 diff 工具
`git difftool`

```bash
# 使用默认 diff 工具
git difftool;
```

**指定 diff 工具**：使用特定工具查看差异
`git difftool --tool=<工具名>`

```bash
# 使用 vimdiff 查看差异
git difftool --tool=vimdiff;
```

---

## 查看合并冲突差异

**检查冲突标记**：检测空白错误和冲突标记
`git diff --check`

```bash
# 检查冲突标记和空白错误
git diff --check;
```

**合并冲突的三方差异**：查看合并冲突详情
`git diff HEAD...MERGE_HEAD`

```bash
# 查看合并冲突的三方差异
git diff HEAD...MERGE_HEAD;
```

---

## diff 算法

**默认算法（Myers）**：快速通用算法
`git diff`

```bash
# 使用默认 Myers 算法
git diff;
```

**耐心算法**：关注唯一行匹配
`git diff --patience`

```bash
# 使用耐心算法，适合代码重构
git diff --patience;
```

**直方图算法**：Patience 的改进版
`git diff --histogram`

```bash
# 使用直方图算法，适合复杂变更
git diff --histogram;
```

---

## 重命名检测

**检测文件重命名**：识别重命名的文件
`git diff -M`

```bash
# 检测重命名（默认 50% 相似度）
git diff -M;
```

**指定相似度阈值**：调整重命名检测的严格度
`git diff -M<百分比>`

```bash
# 90% 相似度阈值（更严格）
git diff -M90%;
```

**检测文件复制**：识别复制的文件
`git diff -C`

```bash
# 检测文件复制
git diff -C;
```

**同时检测重命名和复制**：组合使用
`git diff -C -M`

```bash
# 同时检测重命名和复制
git diff -C -M;
```

---

## 实用别名

**配置 diff 别名**：简化常用 diff 命令
`git config --global alias.<别名> "<命令>"`

```bash
# 配置常用 diff 别名
git config --global alias.d "diff";
git config --global alias.ds "diff --staged";
git config --global alias.dn "diff --name-only";
git config --global alias.dns "diff --staged --name-only";
git config --global alias.dw "diff --color-words";
git config --global alias.dws "diff --staged --color-words";
git config --global alias.dst "diff --stat";
git config --global alias.dsts "diff --staged --stat";
```
