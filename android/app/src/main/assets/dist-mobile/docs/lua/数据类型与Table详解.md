# Lua 数据类型与 Table 速查

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 基础数据类型

**8 种基础数据类型**
`nil | boolean | number | string | function | table | thread | userdata`
```lua
-- nil：无效值
local x = nil;
-- boolean：布尔值
local flag = true;
-- number：数值（双精度浮点数）
local num = 10;
local int = 42;
local float = 3.14;
-- string：字符串
local str = "Hello";
-- function：函数
local add = function(a, b) return a + b end;
-- table：表
local t = {1, 2, 3};
-- thread：协程
local co = coroutine.create(function() end);
```

**类型判断**
`type(<value>)`
```lua
print(type(nil));                          -- nil
print(type(true));                         -- boolean
print(type(10));                           -- number
print(type("hello"));                      -- string
print(type(function() end));               -- function
print(type({}));                           -- table
print(type(coroutine.create(function() end)));  -- thread
```

---

## 类型转换

**数值与字符串转换**
`tostring(<value>) | tonumber(<str>)`
```lua
-- 数值转字符串
local num = 10;
local str = tostring(num);  -- "10"
-- 字符串转数值
local str = "123";
local num1 = tonumber(str);     -- 123
local num2 = tonumber("3.14");  -- 3.14
local num3 = tonumber("abc");   -- nil
-- 算术运算自动转换
print("10" + 5);  -- 15
print(10 .. "5"); -- "105"
```

---

## Table 创建

**空表与初始化**
`local <name> = {} | {<items>} | {<key> = <value>}`
```lua
-- 空表
local t1 = {};
-- 数组初始化
local t2 = {1, 2, 3, 4, 5};
-- 字典初始化
local t3 = {name = "Lua", version = 5.4};
-- 混合初始化
local t4 = {
    "apple",
    "banana",
    name = "fruit",
    count = 2
};
-- 显式键
local t5 = {
    ["name"] = "Lua",
    [10] = "ten",
    [{}] = "table"
};
```

---

## Table 作为数组

**数组操作**
`<arr>[<index>] = <value> | #<arr>`
```lua
-- 注意：Lua 数组索引从 1 开始
local arr = {10, 20, 30, 40, 50};
-- 访问元素
print(arr[1]);  -- 10
print(arr[3]);  -- 30
-- 修改元素
arr[2] = 25;
print(arr[2]);  -- 25
-- 添加元素
arr[6] = 60;
print(arr[6]);  -- 60
-- 获取长度
print(#arr);    -- 6
```

---

## Table 作为字典

**字典操作**
`<dict>.<key> | <dict>["<key>"] | <dict>[<key>] = nil`
```lua
local dict = {
    name = "John",
    age = 30,
    city = "New York"
};
-- 点语法访问
print(dict.name);  -- John
-- 方括号访问
print(dict["age"]);  -- 30
-- 变量键访问
local key = "city";
print(dict[key]);  -- New York
-- 添加字段
dict.email = "john@example.com";
print(dict.email);  -- john@example.com
-- 删除字段（设为 nil）
dict.age = nil;
print(dict.age);  -- nil
```

---

## Table 遍历

**ipairs 遍历数组**
`for <i>, <v> in ipairs(<arr>) do <body> end`
```lua
-- ipairs：遍历连续整数键
local arr = {1, 2, 3, 4, 5};
for i = 1, #arr do
    print(i, arr[i]);
end;
for index, value in ipairs(arr) do
    print(index, value);
end;
```

**pairs 遍历字典**
`for <k>, <v> in pairs(<dict>) do <body> end`
```lua
-- pairs：遍历所有键值对
local dict = {name = "John", age = 30, city = "New York"};
for key, value in pairs(dict) do
    print(key, value);
end;
```

---

## Table 操作函数

**table.insert / table.remove / table.concat / table.sort / table.unpack**
`table.<func>(<table>[, <args>])`
```lua
local arr = {10, 30, 20, 50, 40};
-- 排序
table.sort(arr);
print(table.concat(arr, ", "));  -- 10, 20, 30, 40, 50
-- 插入元素到指定位置
table.insert(arr, 3, 25);
print(table.concat(arr, ", "));  -- 10, 20, 25, 30, 40, 50
-- 删除指定位置元素
table.remove(arr, 3);
print(table.concat(arr, ", "));  -- 10, 20, 30, 40, 50
-- 连接字符串
local strs = {"Hello", "Lua", "World"};
print(table.concat(strs, " "));  -- Hello Lua World
-- 解压表
local values = {10, 20, 30};
local a, b, c = table.unpack(values);
print(a, b, c);  -- 10 20 30
```

---

## 嵌套表

**嵌套表结构**
`<table>.<table>.<key>`
```lua
local person = {
    name = "John",
    age = 30,
    address = {
        street = "Main St",
        city = "New York",
        zipcode = 10001
    },
    hobbies = {"reading", "coding", "gaming"}
};
-- 访问嵌套字段
print(person.address.city);    -- New York
print(person.hobbies[2]);     -- coding
-- 修改嵌套字段
person.address.street = "Broadway";
print(person.address.street);  -- Broadway
```

---

## 表的引用与拷贝

**浅拷贝**
`local function shallow_copy(<table>)`
```lua
-- 表是引用类型
local t1 = {1, 2, 3};
local t2 = t1;  -- t2 引用 t1
t2[1] = 10;
print(t1[1]);  -- 10
-- 浅拷贝
local function shallow_copy(t)
    local copy = {};
    for k, v in pairs(t) do
        copy[k] = v;
    end;
    return copy;
end;
local t3 = shallow_copy(t1);
t3[1] = 20;
print(t1[1]);  -- 10（t1 不受影响）
print(t3[1]);  -- 20
```

**深拷贝**
`local function deep_copy(<table>)`
```lua
-- 深拷贝（递归复制嵌套表）
local function deep_copy(t)
    if type(t) ~= "table" then
        return t;
    end;
    local copy = {};
    for k, v in pairs(t) do
        copy[k] = deep_copy(v);
    end;
    return copy;
end;
```

---

## 元表基础

**setmetatable 设置元表**
`setmetatable(<table>, <metatable>)`
```lua
-- 元表允许修改表的行为
local mt = {
    -- __add 元方法：定义加法操作
    __add = function(a, b)
        local result = {};
        for i, v in ipairs(a) do
            result[i] = v;
        end;
        for i, v in ipairs(b) do
            result[#result + 1] = v;
        end;
        return result;
    end,
    -- __tostring 元方法：定义字符串表示
    __tostring = function(t)
        return "[" .. table.concat(t, ", ") .. "]";
    end
};
local t1 = {1, 2, 3};
local t2 = {4, 5, 6};
setmetatable(t1, mt);
setmetatable(t2, mt);
-- 使用 + 运算符
local t3 = t1 + t2;
print(t3);  -- [1, 2, 3, 4, 5, 6]
print(t1);  -- [1, 2, 3]
```

---

## Table 实现面向对象

**类定义**
`local <Class> = {} <Class>.__index = <Class> function <Class>:new(<args>) ... end`
```lua
-- 类定义
local Person = {};
Person.__index = Person;
-- 构造函数
function Person:new(name, age)
    local self = setmetatable({}, self);
    self.name = name;
    self.age = age;
    return self;
end;
-- 实例方法
function Person:greet()
    print("Hello, my name is " .. self.name);
end;
function Person:get_age()
    return self.age;
end;
-- 使用
local john = Person:new("John", 30);
john:greet();        -- Hello, my name is John
print(john:get_age());  -- 30
```

**继承**
`local <SubClass> = setmetatable({}, {__index = <BaseClass>})`
```lua
-- 子类继承
local Student = setmetatable({}, {__index = Person});
Student.__index = Student;
function Student:new(name, age, grade)
    local self = Person:new(name, age);  -- 调用父类构造函数
    setmetatable(self, Student);
    self.grade = grade;
    return self;
end;
-- 重写方法
function Student:greet()
    print("Hello, my name is " .. self.name .. " and I'm in grade " .. self.grade);
end;
-- 使用
local alice = Student:new("Alice", 15, 9);
alice:greet();  -- Hello, my name is Alice and I'm in grade 9
print(alice:get_age());  -- 15（继承自 Person）
```

---

## 数据结构实现

**栈**
`local <Stack> = {} <Stack>.__index = <Stack>`
```lua
-- 栈数据结构
local Stack = {};
Stack.__index = Stack;
function Stack:new()
    return setmetatable({items = {}}, self);
end;
function Stack:push(item)
    table.insert(self.items, item);
end;
function Stack:pop()
    return table.remove(self.items);
end;
function Stack:peek()
    return self.items[#self.items];
end;
function Stack:isEmpty()
    return #self.items == 0;
end;
-- 使用
local stack = Stack:new();
stack:push(1);
stack:push(2);
print(stack:pop());   -- 2
print(stack:peek());  -- 1
```

**队列**
`local <Queue> = {} <Queue>.__index = <Queue>`
```lua
-- 队列数据结构
local Queue = {};
Queue.__index = Queue;
function Queue:new()
    return setmetatable({items = {}}, self);
end;
function Queue:enqueue(item)
    table.insert(self.items, item);
end;
function Queue:dequeue()
    return table.remove(self.items, 1);
end;
function Queue:front()
    return self.items[1];
end;
-- 使用
local queue = Queue:new();
queue:enqueue(1);
queue:enqueue(2);
print(queue:dequeue());  -- 1
```

**集合**
`local <Set> = {} <Set>.__index = <Set>`
```lua
-- 集合数据结构
local Set = {};
Set.__index = Set;
function Set:new(values)
    local self = setmetatable({elements = {}}, self);
    if values then
        for _, v in ipairs(values) do
            self:add(v);
        end;
    end;
    return self;
end;
function Set:add(value)
    self.elements[value] = true;
end;
function Set:remove(value)
    self.elements[value] = nil;
end;
function Set:contains(value)
    return self.elements[value] == true;
end;
function Set:size()
    local count = 0;
    for _ in pairs(self.elements) do
        count = count + 1;
    end;
    return count;
end;
-- 使用
local set = Set:new({1, 2, 3});
set:add(4);
print(set:contains(3));  -- true
print(set:contains(5));  -- false
```
