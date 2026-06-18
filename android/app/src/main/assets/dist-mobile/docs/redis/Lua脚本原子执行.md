# Lua 脚本原子执行

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## EVAL 命令

**EVAL：执行 Lua 脚本**
`EVAL <script> <numkeys> <key> [key ...] <arg> [arg ...]`
```bash
# script: Lua 脚本
# numkeys: 键的数量
# key: 键名列表（通过 KEYS[1], KEYS[2]... 访问）
# arg: 参数列表（通过 ARGV[1], ARGV[2]... 访问）

# 简单的 GET + SET
EVAL "redis.call('SET', KEYS[1], ARGV[1]); return 'OK'" 1 mykey myvalue
```

**EVAL 限流器：原子性限流**
`EVAL <script> 1 <key> <expire> <limit>`
```bash
# 限流器
EVAL "
  local count = redis.call('INCR', KEYS[1])
  if count == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[1])
  end
  if count > tonumber(ARGV[2]) then
    return 0
  end
  return 1
" 1 rate_limit:user1 60 100
```

---

## redis.call 与 redis.pcall

**redis.call：错误时脚本终止**
`redis.call(<command>, <args>)`
```lua
-- redis.call: 错误时脚本终止
local val = redis.call('INCR', 'non_numeric_key')  -- 如果key不是整数，报错终止
```

**redis.pcall：错误时返回错误表**
`redis.pcall(<command>, <args>)`
```lua
-- redis.pcall: 错误时返回错误表
local result = redis.pcall('INCR', 'non_numeric_key')
if type(result) == 'table' and result.err then
    -- 处理错误
    redis.call('SET', 'error_log', result.err)
end
```

---

## EVALSHA 与脚本缓存

**SCRIPT LOAD：加载脚本到缓存**
`SCRIPT LOAD <script>`
```bash
# 加载脚本到缓存
SCRIPT LOAD "return redis.call('GET', KEYS[1])"
# 返回: "a1b2c3d4..." (SHA1)
```

**EVALSHA：使用 SHA1 执行缓存脚本**
`EVALSHA <sha1> <numkeys> <key> [key ...] <arg> [arg ...]`
```bash
# 使用 SHA1 执行
EVALSHA a1b2c3d4... 1 mykey
```

**SCRIPT EXISTS：检查脚本是否在缓存中**
`SCRIPT EXISTS <sha1> [sha1 ...]`
```bash
# 检查脚本是否在缓存中
SCRIPT EXISTS a1b2c3d4... e5f6g7h8...
# 返回: 1 0 (第一个存在，第二个不存在)
```

**SCRIPT FLUSH：清空脚本缓存**
`SCRIPT FLUSH [SYNC|ASYNC]`
```bash
# 清空所有脚本缓存
SCRIPT FLUSH

# 清空并同步（Redis 7.0+）
SCRIPT FLUSH SYNC
SCRIPT FLUSH ASYNC
```

**Python EVALSHA 降级：自动处理 NOSCRIPT**
`r.register_script(<script>)`
```python
# Python: 自动 EVAL → EVALSHA 降级
r = redis.Redis()

# redis-py 内部自动处理:
# 1. 计算 script 的 SHA1
# 2. 尝试 EVALSHA
# 3. 如果 NOSCRIPT 错误 → 降级为 EVAL
script = r.register_script("""
    local stock = tonumber(redis.call('GET', KEYS[1]))
    if stock and stock > 0 then
        redis.call('DECR', KEYS[1])
        return 1
    end
    return 0
""")
result = script(keys=['stock:item1'])
```

---

## Lua 沙箱限制

**允许的函数：沙箱内可用 API**
`redis.call() | redis.pcall() | redis.log() | redis.sha1hex()`
```lua
-- 允许的函数:
redis.call()              -- 调用 Redis 命令
redis.pcall()             -- 调用 Redis 命令（安全模式）
redis.log()               -- 写日志
redis.sha1hex()           -- SHA1 计算
redis.status_reply()      -- 构造状态回复
redis.error_reply()       -- 构造错误回复
cjson.encode()            -- JSON 编码
cjson.decode()            -- JSON 解码
cmsgpack.pack()           -- MessagePack 编码
cmsgpack.unpack()         -- MessagePack 解码
```

**禁止的操作：沙箱限制**
`os.execute() | io.open() | require()`
```lua
-- 禁止的操作:
os.execute('rm -rf /')   -- 禁止系统调用
io.open('/etc/passwd')    -- 禁止文件操作
require('socket')         -- 禁止加载模块
```

**lua-time-limit：脚本最大执行时间**
`lua-time-limit <ms>`
```bash
# Lua 脚本最大执行时间（毫秒），0=无限制
lua-time-limit 5000

# 超时后:
# 1. 其他客户端收到 BUSY 错误
# 2. 可执行 SCRIPT KILL 终止脚本
# 3. 如果脚本正在写入，只能 shutdown nosave
```

**redis.set_repl：控制复制行为（Redis 7.0+）**
`redis.set_repl(<repl_flag>)`
```lua
-- 如果需要非确定性，使用 redis.set_repl()
redis.set_repl(redis.REPL_ALL)  -- 默认，复制所有写命令
redis.set_repl(redis.REPL_NONE) -- 不复制
```

---

## 实战模式

**分布式锁释放：原子性检查并释放**
`EVAL <script> 1 <lock_key> <lock_value>`
```lua
-- 原子性检查并释放锁
if redis.call('GET', KEYS[1]) == ARGV[1] then
    return redis.call('DEL', KEYS[1])
end
return 0
```

**限流器（滑动窗口）：基于 ZSET 实现**
`EVAL <script> 1 <key> <limit> <window> <now>`
```lua
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

redis.call('ZREMRANGEBYSCORE', key, 0, now - window)
local count = redis.call('ZCARD', key)
if count < limit then
    redis.call('ZADD', key, now, now .. '-' .. math.random(1000000))
    redis.call('PEXPIRE', key, window)
    return 1
end
return 0
```

**库存扣减：原子性检查并扣减**
`EVAL <script> 2 <stock_key> <user_key> <user_id> <quantity>`
```lua
local stock_key = KEYS[1]
local user_key = KEYS[2]
local user_id = ARGV[1]
local quantity = tonumber(ARGV[2])

-- 检查是否已购买
if redis.call('SISMEMBER', user_key, user_id) == 1 then
    return -1  -- 已购买
end

-- 检查库存
local stock = tonumber(redis.call('GET', stock_key))
if not stock or stock < quantity then
    return 0  -- 库存不足
end

-- 扣减库存 + 记录用户
redis.call('DECRBY', stock_key, quantity)
redis.call('SADD', user_key, user_id)
return 1  -- 成功
```
