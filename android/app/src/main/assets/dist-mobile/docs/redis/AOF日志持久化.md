# AOF 日志持久化

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 工作流程

**feedAppendOnlyFile：命令追加到 AOF 缓冲区**
`void feedAppendOnlyFile(struct redisCommand *cmd, int argc, robj **argv)`
```c
// 伪代码
void feedAppendOnlyFile(struct redisCommand *cmd, int argc, robj **argv) {
    // 将命令转换为 RESP 协议格式
    buf = catAppendOnlyGenericCommand(argc, argv);
    // 追加到 aof_buf
    aof_buf = sdscatlen(aof_buf, buf, sdslen(buf));
}
```

**flushAppendOnlyFile：写入文件并根据策略刷盘**
`void flushAppendOnlyFile(int force)`
```c
// 伪代码
void flushAppendOnlyFile(int force) {
    // 将 aof_buf 写入 AOF 文件
    nwritten = write(server.aof_fd, server.aof_buf, sdslen(server.aof_buf));
    // 根据 appendfsync 策略决定是否 fsync
    if (server.aof_fsync == APPENDFSYNC_ALWAYS) {
        fsync(server.aof_fd);
    }
}
```

---

## appendfsync 策略

**appendfsync always：每条命令都 fsync**
`appendfsync always`
```bash
# 每条命令都 fsync
appendfsync always
# 行为：每条写命令执行后立即调用 fsync()
# 数据安全：最高，最多丢失一条命令
# 性能影响：最严重，每秒只能处理数百到数千次写入
```

**appendfsync everysec：每秒 fsync（默认推荐）**
`appendfsync everysec`
```bash
# 每秒 fsync（默认推荐）
appendfsync everysec
# 行为：每秒调用一次 fsync()
# 数据安全：较高，最多丢失 1 秒数据
# 性能影响：可接受，与 no 策略性能差距不大
```

**appendfsync no：由操作系统决定刷盘**
`appendfsync no`
```bash
# 由操作系统决定刷盘时机
appendfsync no
# 行为：不主动 fsync()，由操作系统决定
# 数据安全：最低，可能丢失最近数秒数据
# 性能影响：最好，依赖 OS 缓冲区刷盘
```

---

## AOF 重写

**重写原理：读取当前数据库状态生成最小命令**
`BGREWRITEAOF`
```bash
# 旧 AOF 文件中可能有以下 6 条命令
SET counter 1
INCR counter
INCR counter
INCR counter
DEL counter
SET counter 100

# 重写后只需 1 条命令
SET counter 100
```

**自动触发条件：配置重写阈值**
`auto-aof-rewrite-percentage <percentage> | auto-aof-rewrite-min-size <size>`
```bash
# 自动触发条件
auto-aof-rewrite-percentage 100   # AOF 文件大小比上次重写后增长 100%
auto-aof-rewrite-min-size 64mb    # AOF 文件最小 64MB 才触发重写

# 手动触发
BGREWRITEAOF
```

**列表合并示例：重写时合并列表操作**
`RPUSH <key> <values>`
```bash
# 旧 AOF 文件
RPUSH list a b c
RPUSH list d e
LPOP list
RPUSH list f

# 重写后
RPUSH list b c d e f
```

---

## AOF 文件格式

**RESP 协议格式：AOF 文件内容**
`*<count>\r\n$<len>\r\n<command>\r\n...`
```bash
# AOF 文件使用 Redis 序列化协议（RESP）格式
*2\r\n
$6\r\n
SELECT\r\n
$1\r\n
0\r\n
*3\r\n
$3\r\n
SET\r\n
$4\r\n
key1\r\n
$6\r\n
value1\r\n
```

**MULTI/EXEC 合并：Redis 4.0+ 减少文件体积**
`*1\r\n$5\r\nMULTI\r\n...*1\r\n$4\r\nEXEC\r\n`
```bash
# Redis 4.0+ 支持 AOF 使用 MULTI/EXEC 包裹命令
*1\r\n
$5\r\n
MULTI\r\n
*3\r\n
$3\r\n
SET\r\n
$3\r\n
key\r\n
$5\r\n
value\r\n
*3\r\n
$3\r\n
SET\r\n
$4\r\n
key2\r\n
$6\r\n
value2\r\n
*1\r\n
$4\r\n
EXEC\r\n
```

---

## AOF 文件恢复

**自动恢复：启动时加载 AOF**
`appendonly yes`
```bash
# Redis 启动时自动加载 AOF 文件（AOF 优先级高于 RDB）
# 1. 检查 AOF 是否开启（appendonly yes）
# 2. 加载 AOF 文件，逐条执行命令
# 3. 如果 AOF 文件损坏，拒绝启动
```

**redis-check-aof：检查并修复 AOF 文件**
`redis-check-aof [--fix] <file>`
```bash
# 检查 AOF 文件
redis-check-aof appendonly.aof

# 修复 AOF 文件（截断损坏部分）
redis-check-aof --fix appendonly.aof
```

---

## 配置优化

**关键配置项：AOF 完整配置**
`appendonly yes | appendfilename <name> | appendfsync <strategy>`
```bash
# 开启 AOF
appendonly yes

# AOF 文件名
appendfilename "appendonly.aof"

# 刷盘策略
appendfsync everysec

# 自动重写条件
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

# 重写期间是否禁止 fsync
no-appendfsync-on-rewrite no

# 加载时忽略最后一条不完整命令
aof-load-truncated yes

# AOF 文件存储目录
dir /var/lib/redis
```

**no-appendfsync-on-rewrite：重写期间禁止 fsync**
`no-appendfsync-on-rewrite <yes|no>`
```bash
# 当设为 yes 时，AOF 重写期间不执行 fsync()
no-appendfsync-on-rewrite yes
# 优点：避免重写期间主线程因 fsync 阻塞
# 风险：重写期间如果宕机，可能丢失更多数据
```
