# MySQL JSON 类型与 JSON_TABLE

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## JSON 数据类型

**创建 JSON 类型列**
`<列名> JSON`
```sql
-- 原生 JSON 类型（自动校验、二进制存储）
CREATE TABLE users (
    id       INT PRIMARY KEY,
    profile  JSON
);

-- 传统文本存储（无校验）
CREATE TABLE users_old (
    id       INT PRIMARY KEY,
    profile  TEXT
);
```

**插入 JSON 数据**
`INSERT INTO <表名> VALUES (<值>, '<JSON 字符串>')`
```sql
-- 有效 JSON
INSERT INTO users VALUES (1, '{"name": "Alice", "age": 30, "tags": ["dev", "go"]}');

-- JSON 数组
INSERT INTO users VALUES (3, '[1, 2, 3, "hello", null, true]');

-- 嵌套 JSON
INSERT INTO users VALUES (4, '{
    "name": "Bob",
    "address": {
        "city": "Beijing",
        "zip": "100000"
    },
    "orders": [
        {"id": 1, "amount": 99.9},
        {"id": 2, "amount": 199.9}
    ]
}');

-- 无效 JSON 会报错
-- INSERT INTO users VALUES (2, '{name: Alice}');
```

---

## JSON 提取函数

**JSON_EXTRACT**
`JSON_EXTRACT(<JSON 列>, '<路径>')`
```sql
-- 提取值（返回 JSON 类型）
SELECT JSON_EXTRACT(profile, '$.name') FROM users WHERE id = 1;
```

**-> 运算符**
`<JSON 列>->'<路径>'`
```sql
-- JSON_EXTRACT 的简写
SELECT profile->'$.name' FROM users WHERE id = 1;
```

**->> 运算符**
`<JSON 列>->>'<路径>'`
```sql
-- 提取并取消引号
SELECT profile->>'$.name' FROM users WHERE id = 1;

-- 提取嵌套值
SELECT profile->>'$.address.city' FROM users WHERE id = 4;

-- 提取数组元素
SELECT profile->'$.orders[0].amount' FROM users WHERE id = 4;

-- 提取数组所有元素
SELECT profile->'$.tags[*]' FROM users WHERE id = 1;
```

---

## JSON_PATH 语法

**路径表达式**
`$ | .key | [num] | [*] | .. | [last] | [last-1] | [to last]`
```
$           根元素
.key        对象的 key
[num]       数组的第 num 个元素
[*]         数组所有元素
..          递归下降（MySQL 8.0+）
[last]      数组最后一个元素
[last-1]    数组倒数第二个元素
[to last]   从某位置到末尾
```

---

## JSON 修改函数

**JSON_SET**
`JSON_SET(<JSON 列>, '<路径>', <值>[, '<路径>', <值>...])`
```sql
-- 设置值（存在则更新，不存在则创建）
UPDATE users SET profile = JSON_SET(profile, '$.age', 31) WHERE id = 1;
```

**JSON_INSERT**
`JSON_INSERT(<JSON 列>, '<路径>', <值>)`
```sql
-- 插入值（仅不存在时创建）
UPDATE users SET profile = JSON_INSERT(profile, '$.email', 'alice@example.com') WHERE id = 1;
```

**JSON_REPLACE**
`JSON_REPLACE(<JSON 列>, '<路径>', <值>)`
```sql
-- 替换值（仅存在时更新）
UPDATE users SET profile = JSON_REPLACE(profile, '$.age', 32) WHERE id = 1;
```

**JSON_REMOVE**
`JSON_REMOVE(<JSON 列>, '<路径>')`
```sql
-- 删除值
UPDATE users SET profile = JSON_REMOVE(profile, '$.tags') WHERE id = 1;
```

**JSON_ARRAY_APPEND**
`JSON_ARRAY_APPEND(<JSON 列>, '<路径>', <值>)`
```sql
-- 追加数组元素
UPDATE users SET profile = JSON_ARRAY_APPEND(profile, '$.tags', 'java') WHERE id = 1;
```

**JSON_MERGE_PATCH**
`JSON_MERGE_PATCH(<JSON 列>, '<JSON 对象>')`
```sql
-- 合并（覆盖同 key）
UPDATE users SET profile = JSON_MERGE_PATCH(profile, '{"age": 33, "level": "senior"}') WHERE id = 1;
```

---

## JSON 查询与搜索函数

**JSON_CONTAINS**
`JSON_CONTAINS(<JSON 列>, '<JSON 值>'[, '<路径>'])`
```sql
-- 是否包含指定值
SELECT * FROM users WHERE JSON_CONTAINS(profile->'$.tags', '"dev"');
```

**JSON_CONTAINS_PATH**
`JSON_CONTAINS_PATH(<JSON 列>, 'one|all', '<路径>'[, '<路径>'...])`
```sql
-- 是否包含指定路径
SELECT * FROM users WHERE JSON_CONTAINS_PATH(profile, 'one', '$.email');
```

**JSON_SEARCH**
`JSON_SEARCH(<JSON 列>, 'one|all', '<值>')`
```sql
-- 搜索值返回路径
SELECT JSON_SEARCH(profile, 'one', 'Alice') FROM users;
```

**JSON_KEYS**
`JSON_KEYS(<JSON 列>[, '<路径>'])`
```sql
-- 获取所有 key
SELECT JSON_KEYS(profile) FROM users WHERE id = 1;
```

**JSON_LENGTH**
`JSON_LENGTH(<JSON 列>[, '<路径>'])`
```sql
-- 获取长度
SELECT JSON_LENGTH(profile->'$.tags') FROM users WHERE id = 1;
```

**JSON_TYPE**
`JSON_TYPE(<JSON 值>)`
```sql
-- 获取类型
SELECT JSON_TYPE(profile->'$.name') FROM users WHERE id = 1;
```

**JSON_VALID**
`JSON_VALID('<JSON 字符串>')`
```sql
-- 是否有效 JSON
SELECT JSON_VALID('{"a":1}');
```

---

## JSON_TABLE

**基本语法**
`JSON_TABLE(<JSON 文档>, <路径> COLUMNS (<列定义>) ) [AS] <别名>`
```sql
-- 将 JSON 数组展开为关系表
SELECT
    o.id AS order_id,
    jt.product_id,
    jt.name,
    jt.qty,
    jt.price,
    jt.qty * jt.price AS subtotal
FROM orders o,
JSON_TABLE(
    o.items,
    '$[*]' COLUMNS (
        product_id INT PATH '$.product_id',
        name       VARCHAR(50) PATH '$.name',
        qty        INT PATH '$.qty',
        price      DECIMAL(10,2) PATH '$.price'
    )
) AS jt;
```

**嵌套展开 NESTED PATH**
`NESTED PATH '<路径>' COLUMNS (<列定义>)`
```sql
-- 嵌套 JSON 结构展开
SELECT
    o.id,
    jt.product_id,
    jt.name,
    nt.color,
    nt.stock
FROM orders o,
JSON_TABLE(
    o.items,
    '$[*]' COLUMNS (
        product_id INT PATH '$.product_id',
        name       VARCHAR(50) PATH '$.name',
        NESTED PATH '$.variants[*]' COLUMNS (
            color VARCHAR(20) PATH '$.color',
            stock INT PATH '$.stock'
        )
    )
) AS jt;
```

**ORDINALITY 列**
`<列名> FOR ORDINALITY`
```sql
-- 自动生成行号
SELECT
    jt.row_num,
    jt.name
FROM orders o,
JSON_TABLE(
    o.items,
    '$[*]' COLUMNS (
        row_num FOR ORDINALITY,
        name VARCHAR(50) PATH '$.name'
    )
) AS jt
WHERE o.id = 1;
```

**处理缺失值**
`<列名> <类型> PATH '<路径>' DEFAULT '<默认值>' ON EMPTY`
```sql
-- 缺失值使用默认值
SELECT * FROM orders o,
JSON_TABLE(
    o.items,
    '$[*]' COLUMNS (
        name VARCHAR(50) PATH '$.name',
        discount DECIMAL(5,2) PATH '$.discount' DEFAULT '0.00' ON EMPTY
    )
) AS jt;
```

---

## JSON 索引优化

**虚拟列索引**
`ALTER TABLE <表名> ADD COLUMN <列名> <类型> GENERATED ALWAYS AS (<表达式>) VIRTUAL, ADD INDEX <索引名> (<列名>)`
```sql
-- 为 JSON 字段创建虚拟列 + 索引
ALTER TABLE users
ADD COLUMN name_virtual VARCHAR(50)
    GENERATED ALWAYS AS (JSON_UNQUOTE(profile->'$.name')) VIRTUAL,
ADD INDEX idx_name (name_virtual);

-- 查询走索引
SELECT * FROM users WHERE name_virtual = 'Alice';
```

**函数索引**
`ALTER TABLE <表名> ADD INDEX <索引名> ((<表达式>))`
```sql
-- 直接创建函数索引（MySQL 8.0+）
ALTER TABLE users
ADD INDEX idx_json_name ((CAST(profile->>'$.name' AS CHAR(50))));
```

**多值索引**
`ALTER TABLE <表名> ADD INDEX <索引名> ((CAST(<JSON 列> AS <类型> ARRAY)))`
```sql
-- 为 JSON 数组创建多值索引（MySQL 8.0.17+）
ALTER TABLE users
ADD INDEX idx_tags ((CAST(profile->'$.tags' AS CHAR(50) ARRAY)));

-- 使用 MEMBER OF 查询
SELECT * FROM users WHERE 'dev' MEMBER OF(profile->'$.tags');

-- 使用 JSON_OVERLAPS
SELECT * FROM users WHERE JSON_OVERLAPS(profile->'$.tags', '["dev", "java"]');
```
