# Git 基本操作

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 状态查看

**查看仓库状态**：显示工作区、暂存区文件状态
`git status`

```bash
# 显示完整状态信息
git status;
```

**简略格式状态**：以短格式显示状态标记
`git status -s`

```bash
# 输出 ?? 未追踪 / A 新增暂存 / M 修改 / D 删除
git status -s;
```

---

## 暂存操作

**暂存单个文件**：将指定文件加入暂存区
`git add <file>`

```bash
# 暂存指定文件
git add src/index.js;
```

**暂存所有变更**：将工作区所有变更加入暂存区
`git add .`

```bash
# 暂存当前目录下所有文件
git add .;
```

**暂存已追踪文件**：仅暂存已追踪文件的修改（不含新文件）
`git add -u`

```bash
# 暂存所有已追踪文件的修改
git add -u;
```

**交互式暂存**：按代码块选择暂存内容
`git add -p`

```bash
# 逐代码块确认是否暂存
git add -p;
```

---

## 提交操作

**标准提交**：提交暂存区内容
`git commit -m "<message>"`

```bash
# 提交并附带消息
git commit -m "feat: add login module";
```

**跳过暂存提交**：自动暂存已追踪文件并提交
`git commit -a -m "<message>"`

```bash
# 等价于 git add -u 后再提交
git commit -a -m "fix: resolve crash";
```

**修改最后一次提交**：补充或修改最近一次提交
`git commit --amend -m "<message>"`

```bash
# 修改最近一次提交消息
git commit --amend -m "feat: add login module v2";
```

---

## 提交信息规范

**约定式提交格式**：统一提交消息结构
`<type>: <subject>`

```text
# type 取值：feat / fix / docs / style / refactor / test / chore
feat: add user authentication
```

---

## 查看历史

**查看完整历史**：列出所有提交记录
`git log`

```bash
# 查看完整提交历史
git log;
```

**简洁历史**：单行显示提交
`git log --oneline`

```bash
# 每条提交一行显示
git log --oneline;
```

**限制条数**：查看最近 N 次提交
`git log -n <count>`

```bash
# 查看最近 5 次提交
git log -n 5;
```

**图形化分支历史**：显示分支合并图
`git log --graph --oneline --all`

```bash
# 查看所有分支的合并图
git log --graph --oneline --all;
```

**查看文件历史**：查看指定文件的提交记录
`git log <file>`

```bash
# 查看 src/index.js 的修改历史
git log src/index.js;
```

**查看提交详情**：显示某次提交的完整内容
`git show <commit-hash>`

```bash
# 查看指定提交的详情
git show abc1234;
```

---

## 查看差异

**工作区与暂存区差异**：查看未暂存的修改
`git diff`

```bash
# 查看未暂存的修改
git diff;
```

**暂存区与上次提交差异**：查看已暂存的修改
`git diff --cached`

```bash
# 查看已暂存但未提交的修改
git diff --cached;
```

**分支间差异**：比较两个分支
`git diff <branch1>..<branch2>`

```bash
# 比较 main 与 feature 分支差异
git diff main..feature;
```

**文件差异**：查看指定文件的修改
`git diff <file>`

```bash
# 查看 src/index.js 的修改
git diff src/index.js;
```

---

## 撤销工作区修改

**撤销单个文件修改**：恢复到暂存区或上次提交状态
`git checkout -- <file>`

```bash
# 撤销 src/index.js 的工作区修改
git checkout -- src/index.js;
```

**撤销所有文件修改**：恢复整个工作区
`git checkout -- .`

```bash
# 撤销所有工作区修改
git checkout -- .;
```

**使用 restore 撤销**：Git 2.23+ 推荐写法
`git restore <file>`

```bash
# 撤销指定文件修改
git restore src/index.js;
```

---

## 撤销暂存

**取消暂存（保留修改）**：将文件移出暂存区
`git reset HEAD <file>`

```bash
# 将 src/index.js 移出暂存区
git reset HEAD src/index.js;
```

**使用 restore 撤销暂存**：Git 2.23+ 推荐写法
`git restore --staged <file>`

```bash
# 取消暂存但保留工作区修改
git restore --staged src/index.js;
```

---

## 撤销提交

**软回退**：撤销提交，保留修改在暂存区
`git reset --soft HEAD~<n>`

```bash
# 撤销最近一次提交，修改保留在暂存区
git reset --soft HEAD~1;
```

**混合回退**：撤销提交，保留修改在工作区
`git reset --mixed HEAD~<n>`

```bash
# 撤销最近一次提交，修改保留在工作区
git reset --mixed HEAD~1;
```

**硬回退**：撤销提交并丢弃所有修改
`git reset --hard HEAD~<n>`

```bash
# 撤销最近一次提交并丢弃修改
git reset --hard HEAD~1;
```

**安全撤销（revert）**：创建新提交来撤销指定提交
`git revert <commit-hash>`

```bash
# 创建反向提交撤销 abc1234
git revert abc1234;
```

**撤销多个提交**：批量撤销不连续提交
`git revert <hash1> <hash2>`

```bash
# 撤销多个不连续的提交
git revert abc1234 def5678;
```

---

## 远程仓库基础

**添加远程仓库**：关联远程仓库地址
`git remote add <name> <url>`

```bash
# 添加名为 origin 的远程仓库
git remote add origin https://github.com/user/repo.git;
```

**查看远程仓库**：列出已配置的远程仓库
`git remote -v`

```bash
# 显示远程仓库名称和地址
git remote -v;
```

**修改远程仓库 URL**：更新远程仓库地址
`git remote set-url <name> <new-url>`

```bash
# 更新 origin 的 URL
git remote set-url origin https://github.com/user/new-repo.git;
```

**删除远程仓库**：解除远程仓库关联
`git remote remove <name>`

```bash
# 删除名为 origin 的远程仓库
git remote remove origin;
```

---

## 推送与拉取

**推送到远程**：将本地分支推送到远程
`git push <remote> <branch>`

```bash
# 推送 main 分支到 origin
git push origin main;
```

**拉取远程更新**：拉取并合并远程分支
`git pull <remote> <branch>`

```bash
# 拉取 origin 的 main 分支并合并
git pull origin main;
```

**获取远程更新（不合并）**：仅下载远程更新
`git fetch <remote>`

```bash
# 获取 origin 的更新但不合并
git fetch origin;
```

---

## 暂存修改（stash）

**暂存当前修改**：保存工作区状态
`git stash`

```bash
# 暂存当前所有修改
git stash;
```

**恢复暂存修改**：应用最近一次暂存
`git stash pop`

```bash
# 恢复最近一次暂存的修改并删除该暂存
git stash pop;
```

**查看暂存列表**：列出所有暂存记录
`git stash list`

```bash
# 查看所有暂存记录
git stash list;
```

---

## 选择性合并

**挑选提交合并**：将指定提交应用到当前分支
`git cherry-pick <commit-hash>`

```bash
# 将 abc1234 提交应用到当前分支
git cherry-pick abc1234;
```
