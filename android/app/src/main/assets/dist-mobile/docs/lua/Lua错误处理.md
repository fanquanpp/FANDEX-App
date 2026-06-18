# Lua 错误处理速查

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## error 抛出错误

**error 函数**
`error(<message>[, <level>])`
```lua
-- error 抛出错误
error("发生错误");
-- 带错误级别
error("发生错误", 1);  -- 1：调用 error 的位置（默认）
error("发生错误", 2);  -- 2：调用包含 error 的函数的位置
error("发生错误", 0);  -- 0：不添加位置信息
```

**条件抛出**
`if not <cond> then error(<message>) end`
```lua
-- 条件抛出错误
function divide(a, b)
    if b == 0 then
        error("除数不能为零");
    end;
    return a / b;
end;
```

**assert 断言**
`assert(<cond>[, <message>])`
```lua
-- assert 断言
local value = nil;
local result, err = pcall(function()
    assert(value ~= nil, "value 不能为 nil");
end);
print(result, err);  -- false  value 不能为 nil
-- assert 返回值
local file = assert(io.open("test.txt", "r"), "无法打开文件");
```

---

## pcall 保护调用

**pcall 捕获错误**
`pcall(<func>[, <args>...])`
```lua
-- pcall 捕获错误
local function riskyOperation()
    error("操作失败");
end;
local success, result = pcall(riskyOperation);
if success then
    print("成功:", result);
else
    print("失败:", result);  -- 失败: test.lua:2: 操作失败
end;
```

**带参数的 pcall**
`pcall(<func>, <arg1>, <arg2>, ...)`
```lua
-- 带参数的 pcall
local function add(a, b)
    if type(a) ~= "number" or type(b) ~= "number" then
        error("参数必须是数字");
    end;
    return a + b;
end;
local success, result = pcall(add, 1, 2);
print(success, result);  -- true 3
local success, result = pcall(add, "a", 2);
print(success, result);  -- false ...参数必须是数字
```

---

## xpcall 带错误处理

**xpcall 带错误处理函数**
`xpcall(<func>, <handler>[, <args>...])`
```lua
-- xpcall 带错误处理函数
local function errorHandler(err)
    print("错误处理函数被调用");
    return "处理后的错误: " .. tostring(err);
end;
local function riskyOperation()
    error("操作失败");
end;
local success, result = xpcall(riskyOperation, errorHandler);
print(success, result);  -- false 处理后的错误: ...操作失败
```

**带参数的 xpcall**
`xpcall(<func>, <handler>, <arg1>, <arg2>, ...)`
```lua
-- 带参数的 xpcall（Lua 5.2+）
local function divide(a, b)
    if b == 0 then error("除数不能为零") end;
    return a / b;
end;
local function handler(err)
    return "错误: " .. tostring(err);
end;
local success, result = xpcall(divide, handler, 10, 0);
print(success, result);  -- false 错误: ...除数不能为零
```

---

## 错误对象

**错误对象**
`error({ code = <code>, message = <msg> })`
```lua
-- 错误对象
local function createError(code, message)
    return {code = code, message = message};
end;
local function riskyOperation()
    error(createError(500, "服务器内部错误"));
end;
local success, result = pcall(riskyOperation);
if not success and type(result) == "table" then
    print("错误代码:", result.code);
    print("错误信息:", result.message);
end;
```

---

## finally 模式

**finally 模式**
`local function <name>(<args>) local <finally> = function() <body> end local <success>, <err> = pcall(<func>) <finally>() if not <success> then error(<err>) end end`
```lua
-- finally 模式
local function withFinally(func)
    local finally = function() end;
    local success, err = pcall(function()
        finally = func() or function() end;
    end);
    finally();
    if not success then
        error(err);
    end;
end;
-- 使用
withFinally(function()
    print("执行操作");
    return function()
        print("清理资源");
    end;
end);
```

---

## 自定义错误处理

**try-catch-finally 模式**
`local function try(<func>) return { catch = ..., finally = ... } end`
```lua
-- try-catch-finally 模式
local function try(block)
    local body = block[1];
    local handler = block.catch;
    local finalizer = block.finally;
    local success, err = pcall(body);
    if handler and not success then
        local handlerSuccess, handlerResult = pcall(handler, err);
        if not handlerSuccess then
            if finalizer then finalizer() end;
            error(handlerResult);
        end;
        if finalizer then finalizer() end;
        return handlerResult;
    end;
    if finalizer then finalizer() end;
    if not success then
        error(err);
    end;
    return success;
end;
-- 使用
try {
    function()
        error("发生错误");
    end,
    catch = function(err)
        print("捕获到错误:", err);
        return "已处理";
    end,
    finally = function()
        print("执行清理");
    end
};
```

---

## 资源管理

**withResource 模式**
`local function withResource(<acquire>, <func>) <body> end`
```lua
-- withResource 模式
local function withResource(acquire, release, func)
    local resource = acquire();
    local success, err = pcall(func, resource);
    release(resource);
    if not success then
        error(err);
    end;
end;
-- 使用
withResource(
    function() return io.open("file.txt", "w") end,
    function(file) if file then file:close() end end,
    function(file)
        file:write("Hello, World!");
    end
);
```

---

## 日志记录

**错误日志**
`local function logError(<level>, <message>, <trace>) <body> end`
```lua
-- 错误日志记录
local function logError(level, message, trace)
    local logEntry = string.format(
        "[%s] %s: %s\n%s\n",
        os.date("%Y-%m-%d %H:%M:%S"),
        level,
        message,
        trace or ""
    );
    local logFile = io.open("error.log", "a");
    if logFile then
        logFile:write(logEntry);
        logFile:close();
    end;
end;
-- 使用 xpcall 记录错误
local function safeCall(func, ...)
    local function handler(err)
        logError("ERROR", tostring(err), debug.traceback());
        return err;
    end;
    return xpcall(func, handler, ...);
end;
```

---

## 调试信息

**debug.traceback**
`debug.traceback([<message>[, <level>]])`
```lua
-- 获取调用栈
local function funcA()
    error("发生错误");
end;
local function funcB()
    funcA();
end;
local function funcC()
    funcB();
end;
local success, err = xpcall(funcC, function(e)
    print("错误:", e);
    print("调用栈:", debug.traceback());
end);
```

**debug.getinfo**
`debug.getinfo([<thread>, ]<level>[, <fields>])`
```lua
-- 获取函数信息
local function test() print("test") end;
local info = debug.getinfo(test);
print("函数名:", info.name);
print("来源:", info.source);
print("行号:", info.linedefined);
print("参数个数:", info.nparams);
```
