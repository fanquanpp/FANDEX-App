# FANDEX-App 文档格式模板

> 本模板定义 FANDEX-App 内所有语法速查文档的编写规范。FANDEX-App 仅收录编程语言/查询语言/标记语言的语法格式文档，不包含教学讲解、概念阐述、项目实战等内容。

---

## 编写规则

### 一、文档定位

- 文档用途：即查即用的语法参考，聚焦语法格式、函数签名、控制结构、类型定义
- 禁止内容：概述、发展历史、概念阐述、环境配置、项目实战、理论知识、最佳实践
- 语言要求：中文注释，代码示例使用对应语言的代码块

### 二、符号约定

| 符号 | 含义 | 示例 |
| :--- | :--- | :--- |
| `< >` | 必填参数，需替换为实际值 | `<表名>`、`<列名>` |
| `[ ]` | 可选参数，可省略 | `[WHERE <条件>]` |
| `{ }` | 分组参数，多选一 | `{ASC \| DESC}` |
| `\|` | 或，分隔可选项 | `ASC \| DESC` |
| `...` | 重复前一项 | `<值 1>, <值 2>, ...` |

### 三、书写格式

每个语法点须包含以下两部分：

1. **语法签名**：使用行内代码（反引号）标注完整语法格式，参数使用符号约定
2. **代码示例**：使用带语言标签的代码块，注释说明用途

对于存在单行与换行两种写法的语法（如 SQL 多字段场景），须分别给出：

- **单行写法**：适用于字段较少的简单场景，一行书写完成
- **换行写法**：适用于字段较多的复杂场景，每个字段独占一行

### 四、文档结构

```
# <模块名> 语法速查手册

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## <语法分类 1>

**<写法类型>：<语法用途>**
`<语法签名>`
```<语言>
-- 注释说明
<代码示例>;
```

---

## <语法分类 2>
...
```

### 五、分类组织

按语法功能分类，使用二级标题（`##`）分隔。每个分类下按语法用途排列，相关语法聚集在一起。分类之间使用 `---` 分隔线隔开。

---

## 模板示例（SQL 语法速查手册）

以下为完整模板示例，展示语法速查文档的标准格式：

---

# SQL 语法速查手册

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## SELECT 查询

**单行写法：查询指定列**
`SELECT <列名 1>, <列名 2> FROM <表名>;`
```sql
-- 查询用户表中的 id 和 name 字段
SELECT id, name FROM users;
```

**单行写法：查询所有列**
`SELECT * FROM <表名>;`
```sql
-- 查询用户表中的所有字段
SELECT * FROM users;
```

**单行写法：去重查询**
`SELECT DISTINCT <列名> FROM <表名>;`
```sql
-- 查询用户表中不重复的城市列表
SELECT DISTINCT city FROM users;
```

**单行写法：多条件过滤 (AND)**
`SELECT <列名 1>, <列名 2> FROM <表名> WHERE <条件 1> AND <条件 2>;`
```sql
-- 查询年龄大于 18 且状态为激活的用户
SELECT id, name FROM users WHERE age > 18 AND status = 'active';
```

**单行写法：多条件过滤 (OR)**
`SELECT <列名 1>, <列名 2> FROM <表名> WHERE <条件 1> OR <条件 2>;`
```sql
-- 查询年龄小于 18 或状态为封禁的用户
SELECT id, name FROM users WHERE age < 18 OR status = 'banned';
```

**单行写法：模糊查询 (LIKE)**
`SELECT <列名 1>, <列名 2> FROM <表名> WHERE <列名> LIKE '<匹配模式>';`
```sql
-- 查询姓名以字母 A 开头的用户
SELECT id, name FROM users WHERE name LIKE 'A%';
```

**单行写法：范围查询 (BETWEEN)**
`SELECT <列名 1>, <列名 2> FROM <表名> WHERE <列名> BETWEEN <最小值> AND <最大值>;`
```sql
-- 查询年龄在 20 到 30 岁之间的用户
SELECT id, name FROM users WHERE age BETWEEN 20 AND 30;
```

**单行写法：集合查询 (IN)**
`SELECT <列名 1>, <列名 2> FROM <表名> WHERE <列名> IN (<值 1>, <值 2>, <值 3>);`
```sql
-- 查询城市为纽约、洛杉矶或旧金山的用户
SELECT id, name FROM users WHERE city IN ('NY', 'LA', 'SF');
```

**单行写法：分组与聚合**
`SELECT <分组列名>, COUNT(*) FROM <表名> GROUP BY <分组列名>;`
```sql
-- 按城市分组，统计每个城市的用户数量（单行书写）
SELECT city, COUNT(*) FROM users GROUP BY city;
```

**换行写法：分组与聚合**
`SELECT <分组列名>, COUNT(*) FROM <表名> GROUP BY <分组列名>;`
```sql
-- 按城市分组，统计每个城市的用户数量（换行书写）
SELECT city, COUNT(*)
FROM users
GROUP BY city;
```

**单行写法：分组后过滤**
`SELECT <分组列名>, COUNT(*) FROM <表名> GROUP BY <分组列名> HAVING COUNT(*) > <阈值>;`
```sql
-- 筛选出用户数量大于 10 的城市（单行书写）
SELECT city, COUNT(*) FROM users GROUP BY city HAVING COUNT(*) > 10;
```

**换行写法：分组后过滤**
`SELECT <分组列名>, COUNT(*) FROM <表名> GROUP BY <分组列名> HAVING COUNT(*) > <阈值>;`
```sql
-- 筛选出用户数量大于 10 的城市（换行书写）
SELECT city, COUNT(*)
FROM users
GROUP BY city
HAVING COUNT(*) > 10;
```

**单行写法：升序排序**
`SELECT <列名 1>, <列名 2> FROM <表名> ORDER BY <排序列名> ASC;`
```sql
-- 按创建时间升序排列用户
SELECT id, name FROM users ORDER BY created_at ASC;
```

**单行写法：降序排序**
`SELECT <列名 1>, <列名 2> FROM <表名> ORDER BY <排序列名> DESC;`
```sql
-- 按创建时间降序排列用户
SELECT id, name FROM users ORDER BY created_at DESC;
```

**单行写法：限制行数**
`SELECT <列名 1>, <列名 2> FROM <表名> LIMIT <返回行数>;`
```sql
-- 仅查询前 10 条用户数据
SELECT id, name FROM users LIMIT 10;
```

**单行写法：分页查询**
`SELECT <列名 1>, <列名 2> FROM <表名> LIMIT <返回行数> OFFSET <跳过行数>;`
```sql
-- 跳过前 20 条数据，返回接下来的 10 条数据
SELECT id, name FROM users LIMIT 10 OFFSET 20;
```

---

## INSERT 插入

**单值插入：插入完整行**
`INSERT INTO <表名> VALUES (<值 1>, <值 2>, <值 3>);`
```sql
-- 向用户表插入一条包含所有字段值的完整记录
INSERT INTO users VALUES (1, 'Tom', 25, 'NY');
```

**单值插入：插入指定列 (单行写法)**
`INSERT INTO <表名> (<列名 1>, <列名 2>) VALUES (<值 1>, <值 2>);`
```sql
-- 单行书写插入指定列，适用于字段较少的场景
INSERT INTO users (id, name) VALUES (2, 'Jerry');
```

**单值插入：插入指定列 (换行写法)**
`INSERT INTO <表名> (<列名 1>, <列名 2>) VALUES (<值 1>, <值 2>);`
```sql
-- 换行书写插入指定列，适用于字段较多的场景
INSERT INTO users (
    id,
    name,
    age,
    city
) VALUES (
    3,
    'Alice',
    28,
    'LA'
);
```

**批量插入：多行插入 (单行写法)**
`INSERT INTO <表名> (<列名 1>, <列名 2>) VALUES (<值 1_1>, <值 1_2>), (<值 2_1>, <值 2_2>);`
```sql
-- 单行书写批量插入，适用于数据量极少的场景
INSERT INTO users (id, name) VALUES (4, 'Bob'), (5, 'Charlie');
```

**批量插入：多行插入 (换行写法)**
`INSERT INTO <表名> (<列名 1>, <列名 2>) VALUES (<值 1_1>, <值 1_2>), (<值 2_1>, <值 2_2>);`
```sql
-- 换行书写批量插入，适用于数据量较多的场景，便于核对数据
INSERT INTO users (id, name)
VALUES
    (6, 'Dave'),
    (7, 'Eve'),
    (8, 'Frank');
```

**单行写法：插入查询结果**
`INSERT INTO <目标表名> (<目标列名 1>, <目标列名 2>) SELECT <源列名 1>, <源列名 2> FROM <源表名> [WHERE <过滤条件>];`
```sql
-- 将状态为激活的用户数据备份到备份表中（单行书写）
INSERT INTO users_backup (id, name) SELECT id, name FROM users WHERE status = 'active';
```

**换行写法：插入查询结果**
`INSERT INTO <目标表名> (<目标列名 1>, <目标列名 2>) SELECT <源列名 1>, <源列名 2> FROM <源表名> [WHERE <过滤条件>];`
```sql
-- 将状态为激活的用户数据备份到备份表中（换行书写）
INSERT INTO users_backup (id, name)
SELECT id, name
FROM users
WHERE status = 'active';
```

---

## UPDATE 更新

**单行写法：更新单列**
`UPDATE <表名> SET <列名 1> = <新值 1> WHERE <过滤条件>;`
```sql
-- 单行更新单个字段
UPDATE users SET age = 26 WHERE id = 1;
```

**单行写法：更新多列**
`UPDATE <表名> SET <列名 1> = <新值 1>, <列名 2> = <新值 2> WHERE <过滤条件>;`
```sql
-- 单行更新多个字段，适用于字段较少的场景
UPDATE users SET age = 26, city = 'LA' WHERE id = 1;
```

**换行写法：更新多列**
`UPDATE <表名> SET <列名 1> = <新值 1>, <列名 2> = <新值 2> WHERE <过滤条件>;`
```sql
-- 换行更新多个字段，适用于字段较多的场景
UPDATE users
SET age = 26,
    city = 'LA',
    status = 'active'
WHERE id = 1;
```

**单行写法：使用算术表达式更新**
`UPDATE <表名> SET <列名> = <列名> <运算符> <运算值> WHERE <过滤条件>;`
```sql
-- 将 id 为 1 的用户年龄增加 1 岁（运算符支持 +, -, *, /）
UPDATE users SET age = age + 1 WHERE id = 1;
```

---

## DELETE 删除

**单行写法：删除指定行**
`DELETE FROM <表名> WHERE <过滤条件>;`
```sql
-- 删除状态为已删除的用户记录
DELETE FROM users WHERE status = 'deleted';
```

**单行写法：清空表数据 (DELETE)**
`DELETE FROM <表名>;`
```sql
-- 删除用户表中的所有数据，但保留表结构
DELETE FROM users;
```

**单行写法：清空表数据 (TRUNCATE)**
`TRUNCATE TABLE <表名>;`
```sql
-- 快速清空表数据并重置自增主键，此操作不可回滚
TRUNCATE TABLE users;
```

---

## JOIN 多表连接

**单行写法：内连接 (INNER JOIN)**
`SELECT <表 1 别名>.<列名 1>, <表 2 别名>.<列名 2> FROM <表 1> <表 1 别名> INNER JOIN <表 2> <表 2 别名> ON <表 1 别名>.<关联键> = <表 2 别名>.<关联键>;`
```sql
-- 单行书写内连接，适用于简单的两表关联
SELECT u.name, o.order_id FROM users u INNER JOIN orders o ON u.id = o.user_id;
```

**换行写法：内连接 (INNER JOIN)**
`SELECT <表 1 别名>.<列名 1>, <表 2 别名>.<列名 2> FROM <表 1> <表 1 别名> INNER JOIN <表 2> <表 2 别名> ON <表 1 别名>.<关联键> = <表 2 别名>.<关联键>;`
```sql
-- 换行书写内连接，适用于复杂的多表关联
SELECT u.name, o.order_id
FROM users u
INNER JOIN orders o ON u.id = o.user_id;
```

**单行写法：左外连接 (LEFT JOIN)**
`SELECT <表 1 别名>.<列名 1>, <表 2 别名>.<列名 2> FROM <表 1> <表 1 别名> LEFT JOIN <表 2> <表 2 别名> ON <表 1 别名>.<关联键> = <表 2 别名>.<关联键>;`
```sql
-- 单行书写左外连接
SELECT u.name, o.order_id FROM users u LEFT JOIN orders o ON u.id = o.user_id;
```

**换行写法：左外连接 (LEFT JOIN)**
`SELECT <表 1 别名>.<列名 1>, <表 2 别名>.<列名 2> FROM <表 1> <表 1 别名> LEFT JOIN <表 2> <表 2 别名> ON <表 1 别名>.<关联键> = <表 2 别名>.<关联键>;`
```sql
-- 换行书写左外连接，保留左表所有行，右表无匹配则为 NULL
SELECT u.name, o.order_id
FROM users u
LEFT JOIN orders o ON u.id = o.user_id;
```

**单行写法：右外连接 (RIGHT JOIN)**
`SELECT <表 1 别名>.<列名 1>, <表 2 别名>.<列名 2> FROM <表 1> <表 1 别名> RIGHT JOIN <表 2> <表 2 别名> ON <表 1 别名>.<关联键> = <表 2 别名>.<关联键>;`
```sql
-- 单行书写右外连接
SELECT u.name, o.order_id FROM users u RIGHT JOIN orders o ON u.id = o.user_id;
```

**换行写法：右外连接 (RIGHT JOIN)**
`SELECT <表 1 别名>.<列名 1>, <表 2 别名>.<列名 2> FROM <表 1> <表 1 别名> RIGHT JOIN <表 2> <表 2 别名> ON <表 1 别名>.<关联键> = <表 2 别名>.<关联键>;`
```sql
-- 换行书写右外连接，保留右表所有行，左表无匹配则为 NULL
SELECT u.name, o.order_id
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id;
```

**单行写法：自连接 (SELF JOIN)**
`SELECT <别名 1>.<列名 1>, <别名 2>.<列名 2> FROM <表名> <别名 1> JOIN <表名> <别名 2> ON <别名 1>.<关联键> = <别名 2>.<关联键>;`
```sql
-- 单行书写自连接
SELECT e.name AS employee, m.name AS manager FROM employees e JOIN employees m ON e.manager_id = m.id;
```

**换行写法：自连接 (SELF JOIN)**
`SELECT <别名 1>.<列名 1>, <别名 2>.<列名 2> FROM <表名> <别名 1> JOIN <表名> <别名 2> ON <别名 1>.<关联键> = <别名 2>.<关联键>;`
```sql
-- 换行书写自连接，查询员工及其对应主管的名字
SELECT e.name AS employee, m.name AS manager
FROM employees e
JOIN employees m ON e.manager_id = m.id;
```
