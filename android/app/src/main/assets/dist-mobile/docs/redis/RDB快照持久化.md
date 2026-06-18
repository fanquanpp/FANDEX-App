# RDB 快照持久化

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## save 命令

**SAVE：阻塞方式生成 RDB 文件**
`SAVE`
```bash
# 阻塞方式生成 RDB 文件
SAVE
# 执行流程:
# 1. 主进程直接调用 rdbSave() 函数
# 2. 主进程被阻塞，无法响应任何客户端请求
# 3. RDB 文件写入完成后，主进程恢复服务
```

---

## bgsave 命令

**BGSAVE：后台子进程生成 RDB 文件**
`BGSAVE`
```bash
# 后台方式生成 RDB 文件（生产推荐）
BGSAVE
# 执行流程:
# 1. 主进程调用 fork() 创建子进程
# 2. 子进程调用 rdbSave() 将数据写入临时 RDB 文件
# 3. 写入完成后，子进程将临时文件原子性地重命名为正式 RDB 文件
# 4. 子进程退出，主进程收到信号
```

**LASTSAVE：查看最近一次保存时间**
`LASTSAVE`
```bash
# 返回最近一次成功保存的 Unix 时间戳
LASTSAVE
```

**INFO Persistence：查看 RDB 统计信息**
`INFO Persistence`
```bash
# 查看 rdb 相关统计信息
INFO Persistence
```

---

## 写时复制（COW）

**fork 与内存共享：父子进程共享物理内存页**
`fork()`
```c
// fork() 创建子进程后，父子进程共享同一块物理内存页
// 此时内存消耗并不会翻倍，因为页表指向相同的物理帧
//
// fork() 后：
//   父进程页表 → 物理页 A (只读)
//   子进程页表 → 物理页 A (只读)
//
// 父进程修改页 A：
//   父进程页表 → 物理页 A' (可写) ← 新副本
//   子进程页表 → 物理页 A  (只读) ← 原始数据
```

**COW 内存开销估算：额外内存计算**
`COW 额外内存 ≈ 修改页数 × 页大小`
```c
// Linux 默认页大小为 4KB
// 假设 bgsave 耗时 5 秒，每秒写入 10 万个 key，每个 key 平均 100 字节:
//
// 修改页数 ≈ (5 × 100000 × 100) / 4096 ≈ 12207 页
// COW 额外内存 ≈ 12207 × 4KB ≈ 48MB
//
// 生产建议：预留 Redis 实例内存的 20%~50% 作为 COW 缓冲
```

---

## 自动触发配置

**save 配置：自动触发 RDB 快照**
`save <seconds> <changes>`
```bash
# 在 redis.conf 中配置
save 900 1      # 900秒内有至少1个key被修改
save 300 10     # 300秒内有至少10个key被修改
save 60 10000   # 60秒内有至少10000个key被修改
# 触发逻辑：满足任意一个条件即触发 bgsave
```

**禁用自动 RDB**
`save ""`
```bash
# 禁用自动 RDB
save ""
```

**运行时修改 save 配置**
`CONFIG SET save "<config>"`
```bash
# 运行时修改
CONFIG SET save "900 1 300 10 60 10000"
```

---

## RDB 文件格式

**RDB 文件结构：二进制格式**
`[REDIS][version][databases][EOF][checksum]`
```c
// RDB 文件采用二进制格式，结构如下:
// ┌──────────┬──────────┬───────────┬──────────┬──────────┐
// │  REDIS   │ version  │ databases │  EOF     │ checksum │
// │  魔数    │ 版本号   │ 数据库数据 │ 结束标记 │  校验和  │
// └──────────┴──────────┴───────────┴──────────┴──────────┘
```

**数据库区域结构：带过期时间的 key-value**
`[SELECTDB][key-value对(带过期时间)][key-value对(无过期时间)]`
```c
// 各数据库区域结构:
// ┌─────────────┬──────────────────┬──────────────────┐
// │ SELECTDB    │   key-value对    │   key-value对    │
// │ 数据库编号   │  (带过期时间)     │  (无过期时间)     │
// └─────────────┴──────────────────┴──────────────────┘
```

---

## RDB 文件恢复

**自动恢复：启动时加载 RDB**
`dir <path> | dbfilename <name>`
```bash
# 配置 RDB 文件路径
dir /var/lib/redis
dbfilename dump.rdb

# Redis 启动时自动检测 RDB 文件:
# 1. 读取 dir 和 dbfilename 配置
# 2. 打开 RDB 文件，校验 checksum
# 3. 逐个加载 key-value 到内存
# 4. 加载完成后开始接受客户端请求
```

**redis-check-rdb：校验 RDB 文件**
`redis-check-rdb <file>`
```bash
# 使用 redis-check-rdb 工具
redis-check-rdb dump.rdb
```

---

## 配置优化

**关键配置项：RDB 完整配置**
`dbfilename <name> | dir <path> | rdbcompression <yes|no>`
```bash
# RDB 文件名
dbfilename dump.rdb

# 存储目录
dir /var/lib/redis

# 是否压缩（LZF）
rdbcompression yes

# 是否使用 CRC64 校验
rdbchecksum yes

# bgsave 失败时是否停止写入
stop-writes-on-bgsave-error yes

# 自动触发条件
save 900 1
save 300 10
save 60 10000
```
