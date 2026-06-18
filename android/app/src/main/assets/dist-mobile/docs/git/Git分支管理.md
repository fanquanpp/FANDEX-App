# Git 分支管理

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 查看分支

**查看本地分支**：列出所有本地分支
`git branch`

```bash
# 列出本地所有分支
git branch;
```

**查看远程分支**：列出所有远程分支
`git branch -r`

```bash
# 列出所有远程分支
git branch -r;
```

**查看所有分支**：同时列出本地和远程分支
`git branch -a`

```bash
# 列出本地和远程所有分支
git branch -a;
```

**查看分支详情**：显示分支及其最后一次提交
`git branch -v`

```bash
# 显示分支名、哈希、提交消息
git branch -v;
```

---

## 创建分支

**创建新分支**：基于当前分支创建新分支
`git branch <分支名>`

```bash
# 创建 feature/login 分支
git branch feature/login;
```

---

## 切换分支

**切换分支**：切换到指定分支
`git checkout <分支名>`

```bash
# 切换到 feature/login 分支
git checkout feature/login;
```

**使用 switch 切换**：Git 2.23+ 推荐写法
`git switch <分支名>`

```bash
# 切换到 develop 分支
git switch develop;
```

---

## 创建并切换分支

**创建并切换**：一步完成创建和切换
`git checkout -b <分支名>`

```bash
# 创建并切换到 feature/login 分支
git checkout -b feature/login;
```

**使用 switch 创建并切换**：Git 2.23+ 推荐写法
`git switch -c <分支名>`

```bash
# 创建并切换到 feature/payment 分支
git switch -c feature/payment;
```

---

## 合并分支

**合并到当前分支**：将指定分支合并到当前分支
`git merge <分支名>`

```bash
# 将 feature/login 合并到当前分支
git merge feature/login;
```

**快速合并（Fast-forward）**：主分支无新提交时直接移动指针
`git merge <分支名>`

```bash
# 切换到 main 后合并 feature/login
git checkout main;
git merge feature/login;
```

**三方合并（3-way merge）**：主分支有新提交时创建合并提交
`git merge <分支名>`

```bash
# 切换到 main 后合并 feature/payment
git checkout main;
git merge feature/payment;
```

---

## 合并策略

**优先对方分支修改**：冲突时采用合并分支内容
`git merge --strategy-option theirs <分支名>`

```bash
# 冲突时优先使用对方分支的修改
git merge --strategy-option theirs feature/branch;
```

**优先当前分支修改**：冲突时采用当前分支内容
`git merge --strategy-option ours <分支名>`

```bash
# 冲突时优先使用当前分支的修改
git merge --strategy-option ours feature/branch;
```

**递归策略**：默认合并策略
`git merge --strategy recursive <分支名>`

```bash
# 显式指定递归策略
git merge --strategy recursive feature/branch;
```

**章鱼策略**：合并多个分支
`git merge --strategy octopus <分支1> <分支2> <分支3>`

```bash
# 同时合并多个分支
git merge --strategy octopus feature1 feature2 feature3;
```

---

## 删除分支

**安全删除**：仅当分支已合并时删除
`git branch -d <分支名>`

```bash
# 删除已合并的 feature/login 分支
git branch -d feature/login;
```

**强制删除**：无论是否合并都删除
`git branch -D <分支名>`

```bash
# 强制删除未合并的 feature/login 分支
git branch -D feature/login;
```

**删除远程分支**：删除远程仓库的分支
`git push <远程仓库名> --delete <分支名>`

```bash
# 删除 origin 上的 feature/login 分支
git push origin --delete feature/login;
```

---

## 重命名分支

**重命名分支**：修改分支名称
`git branch -m <旧分支名> <新分支名>`

```bash
# 将 feature/old 重命名为 feature/new
git branch -m feature/old feature/new;
```

---

## 设置上游分支

**设置已有分支上游**：关联本地分支与远程分支
`git branch --set-upstream-to=<远程仓库名>/<远程分支名> <本地分支名>`

```bash
# 将本地 feature/login 关联到 origin/feature/login
git branch --set-upstream-to=origin/feature/login feature/login;
```

**首次推送时设置上游**：推送同时建立追踪关系
`git push -u <远程仓库名> <本地分支名>`

```bash
# 推送 feature/login 并设置上游
git push -u origin feature/login;
```

---

## 分支命名规范

**命名格式约定**：按分支类型统一命名
`<type>/<描述>`

```text
# 功能分支：feature/login
# 修复分支：bugfix/login-error
# 紧急修复：hotfix/security-patch
# 发布分支：release/v1.0.0
# 开发分支：develop
# 主分支：main / master
```

---

## GitFlow 工作流

**初始化 GitFlow**：交互式配置分支结构
`git flow init`

```bash
# 初始化 GitFlow 工作流
git flow init;
```

**功能分支操作**：管理 feature 类型分支
`git flow feature start <功能名>`

```bash
# 创建功能分支
git flow feature start login;
# 完成功能分支
git flow feature finish login;
```

**发布分支操作**：管理 release 类型分支
`git flow release start <版本号>`

```bash
# 创建发布分支
git flow release start v1.0.0;
# 完成发布分支
git flow release finish v1.0.0;
```

**热修复分支操作**：管理 hotfix 类型分支
`git flow hotfix start <修复名>`

```bash
# 创建热修复分支
git flow hotfix start security-patch;
# 完成热修复分支
git flow hotfix finish security-patch;
```

---

## 解决分支冲突

**查看冲突文件**：列出冲突内容
`git diff`

```bash
# 查看冲突详情
git diff;
```

**手动解决冲突**：编辑文件保留正确内容
`<<<<<<< HEAD ... ======= ... >>>>>>> <分支名>`

```text
# 冲突标记格式
<<<<<<< HEAD
当前分支的内容
=======
要合并的分支的内容
>>>>>>> feature/login
```

**标记冲突已解决**：添加解决后的文件
`git add .`

```bash
# 将解决冲突后的文件加入暂存区
git add .;
```

**完成合并提交**：提交合并结果
`git commit`

```bash
# 提交合并结果
git commit;
```

**放弃合并**：取消合并回到合并前状态
`git merge --abort`

```bash
# 放弃当前合并操作
git merge --abort;
```
