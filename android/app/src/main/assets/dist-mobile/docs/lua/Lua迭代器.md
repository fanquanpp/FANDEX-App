# Lua 迭代器速查

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 泛型 for 循环

**泛型 for 语法**
`for <var1>, <var2>, ... in <exp1>, <exp2>, <exp3> do <body> end`
```lua
-- 泛型 for 循环的工作机制
-- 1. 调用迭代器函数（exp1）产生第一个值
-- 2. 将不可变状态（exp2）和控制变量（exp3）传入迭代器
-- 3. 将返回值赋给变量列表
-- 4. 如果第一个返回值为 nil，循环结束
-- 5. 否则执行循环体，回到步骤 2
-- 等价于
do
    local iter, state, control = exp1, exp2, exp3;
    while true do
        local var1, var2 = iter(state, control);
        if var1 == nil then break end;
        control = var1;
        -- 循环体
    end;
end;
```

---

## 无状态迭代器

**无状态迭代器**
`function <iter>(<state>, <control>) <body> return <next> end`
```lua
-- 无状态迭代器：不保存任何状态
local function iter(state, i)
    i = i + 1;
    if i > #state then return nil end;
    return i, state[i];
end;
function ipairs(t)
    return iter, t, 0;
end;
-- 使用
local arr = {10, 20, 30};
for i, v in ipairs(arr) do
    print(i, v);  -- 1 10, 2 20, 3 30
end;
```

**数字迭代器**
`function <range>(<from>, <to>, <step>) <body> end`
```lua
-- 数字迭代器
local function rangeIter(state, current)
    current = current + state.step;
    if current > state.to then return nil end;
    return current;
end;
function range(from, to, step)
    step = step or 1;
    return rangeIter, {from = from, to = to, step = step}, from - step;
end;
-- 使用
for i in range(1, 10, 2) do
    print(i);  -- 1, 3, 5, 7, 9
end;
```

---

## 多值迭代器

**返回键值对的迭代器**
`function <pairsIter>(<state>, <control>) <body> return <key>, <value> end`
```lua
-- 返回键值对的迭代器
local function pairsIter(t, k)
    local v;
    k, v = next(t, k);
    if k == nil then return nil end;
    return k, v;
end;
function pairs(t)
    return pairsIter, t, nil;
end;
-- 使用
local dict = {name = "Alice", age = 30, city = "Beijing"};
for k, v in pairs(dict) do
    print(k, v);
end;
```

---

## 有状态迭代器

**闭包迭代器**
`function <iter>(<collection>) local <state> return function() <body> end end`
```lua
-- 有状态迭代器：使用闭包保存状态
function reversedIterator(arr)
    local i = #arr + 1;
    return function()
        i = i - 1;
        if i >= 1 then
            return i, arr[i];
        end;
        return nil;
    end;
end;
-- 使用
local arr = {"a", "b", "c", "d"};
for i, v in reversedIterator(arr) do
    print(i, v);  -- 4 d, 3 c, 2 b, 1 a
end;
```

---

## 复杂迭代器

**文件行迭代器**
`function <lines>(<file>) <body> return function() <body> end end`
```lua
-- 文件行迭代器
function lines(filename)
    local file = io.open(filename, "r");
    if not file then return function() return nil end end;
    return function()
        local line = file:read("*l");
        if line == nil then file:close() end;
        return line;
    end;
end;
-- 使用
for line in lines("test.txt") do
    print(line);
end;
```

**斐波那契数列迭代器**
`function <fibonacci>(<limit>) <body> return function() <body> end end`
```lua
-- 斐波那契数列迭代器
function fibonacci(limit)
    local a, b = 0, 1;
    local count = 0;
    return function()
        if count >= limit then return nil end;
        local result = a;
        a, b = b, a + b;
        count = count + 1;
        return count, result;
    end;
end;
-- 使用
for i, fib in fibonacci(10) do
    print(i, fib);  -- 1 0, 2 1, 3 1, 4 2, 5 3, 6 5, 7 8, 8 13, 9 21, 10 34
end;
```

**树遍历迭代器**
`function <traverse>(<tree>) <body> return function() <body> end end`
```lua
-- 树遍历迭代器（深度优先）
function traverseTree(node)
    local stack = {node};
    return function()
        while #stack > 0 do
            local current = table.remove(stack);
            if current then
                if current.right then table.insert(stack, current.right) end;
                if current.left then table.insert(stack, current.left) end;
                return current.value;
            end;
        end;
        return nil;
    end;
end;
-- 使用
local tree = {
    value = 1,
    left = {
        value = 2,
        left = {value = 4},
        right = {value = 5}
    },
    right = {
        value = 3,
        left = {value = 6},
        right = {value = 7}
    }
};
for v in traverseTree(tree) do
    print(v);  -- 1, 2, 4, 5, 3, 6, 7
end;
```

---

## 协程迭代器

**协程生成迭代器**
`function <gen>(<args>) return coroutine.wrap(function() <body> end) end`
```lua
-- 使用协程生成迭代器
function filter(arr, predicate)
    return coroutine.wrap(function()
        for i, v in ipairs(arr) do
            if predicate(v) then
                coroutine.yield(i, v);
            end;
        end;
    end);
end;
-- 使用
local arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
for i, v in filter(arr, function(x) return x % 2 == 0 end) do
    print(i, v);  -- 2 2, 4 4, 6 6, 8 8, 10 10
end;
```

**生成器**
`function <generator>(<args>) return coroutine.wrap(function() <body> end) end`
```lua
-- 无限序列生成器
function naturalNumbers()
    return coroutine.wrap(function()
        local i = 1;
        while true do
            coroutine.yield(i);
            i = i + 1;
        end;
    end);
end;
-- 使用（需要手动停止）
local gen = naturalNumbers();
for i = 1, 5 do
    print(gen());  -- 1, 2, 3, 4, 5
end;
```

---

## 迭代器组合

**map 迭代器**
`function <map>(<iter>, <transform>) return coroutine.wrap(function() <body> end) end`
```lua
-- map 迭代器
function map(iter, transform)
    return coroutine.wrap(function()
        for k, v in iter do
            coroutine.yield(k, transform(v));
        end;
    end);
end;
-- 使用
local arr = {1, 2, 3, 4, 5};
for i, v in map(ipairs(arr), function(x) return x * 2 end) do
    print(i, v);  -- 1 2, 2 4, 3 6, 4 8, 5 10
end;
```

**链式组合**
`for <k>, <v> in <iter1> |> <iter2> do <body> end`
```lua
-- 链式组合多个迭代器
function take(iter, n)
    return coroutine.wrap(function()
        local count = 0;
        for k, v in iter do
            if count >= n then break end;
            coroutine.yield(k, v);
            count = count + 1;
        end;
    end);
end;
-- 使用
local arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
for i, v in take(map(ipairs(arr), function(x) return x * 2 end), 3) do
    print(i, v);  -- 1 2, 2 4, 3 6
end;
```
