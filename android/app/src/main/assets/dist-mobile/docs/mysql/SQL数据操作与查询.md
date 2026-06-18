# MySQL SQL 数据操作与查询

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 插入数据

**基本插入**
`INSERT INTO <表名> (<列名>[, <列名>...]) VALUES (<值>[, <值>...])`
```sql
-- 指定列插入
INSERT INTO users (id, username, email, password, age)
VALUES (1, '张三', 'zhangsan@example.com', 'encrypted_pass', 25);

-- 省略自增列
INSERT INTO users (username, email, password, age)
VALUES ('张三', 'zhangsan@example.com', 'encrypted_pass', 25);
```

**SET 语法插入**
`INSERT INTO <表名> SET <列名> = <值>[, <列名> = <值>...]`
```sql
-- SET 形式插入
INSERT INTO users SET
  username = '李四',
  email = 'lisi@example.com',
  password = 'encrypted_pass',
  age = 30;
```

**批量插入**
`INSERT INTO <表名> (<列名>) VALUES (<值1>), (<值2>)[, ...]`
```sql
-- 批量插入多行
INSERT INTO users (username, email, password, age) VALUES
('王五', 'wangwu@example.com', 'pass1', 28),
('赵六', 'zhaoliu@example.com', 'pass2', 32),
('钱七', 'qianqi@example.com', 'pass3', 27);
```

**插入查询结果**
`INSERT INTO <表名> (<列名>) SELECT <列名> FROM <源表> [WHERE <条件>]`
```sql
-- 从旧表迁移数据
INSERT INTO users (username, email, password, age)
SELECT username, email, password, age FROM old_users WHERE status = 1;
```

**插入或更新**
`INSERT INTO <表名> (<列名>) VALUES (<值>) ON DUPLICATE KEY UPDATE <列名> = <值>`
```sql
-- 主键冲突时更新
INSERT INTO users (id, username, email) VALUES (1, '张三', 'new@example.com')
ON DUPLICATE KEY UPDATE email = 'new@example.com', updated_at = NOW();
```

**忽略冲突插入**
`INSERT IGNORE INTO <表名> (<列名>) VALUES (<值>)`
```sql
-- 主键冲突时跳过
INSERT IGNORE INTO users (username, email) VALUES ('张三', 'test@example.com');
```

**替换插入**
`REPLACE INTO <表名> (<列名>) VALUES (<值>)`
```sql
-- 主键冲突时删除原行再插入
REPLACE INTO users (id, username, email) VALUES (1, '张三', 'new@example.com');
```

**获取自增 ID**
`SELECT LAST_INSERT_ID();`
```sql
-- 插入后获取自增主键
INSERT INTO users (username, email) VALUES ('测试', 'test@example.com');
SELECT LAST_INSERT_ID();
```

---

## 更新数据

**基本更新**
`UPDATE <表名> SET <列名> = <值>[, <列名> = <值>...] [WHERE <条件>]`
```sql
-- 更新单行
UPDATE users SET age = 26 WHERE id = 1;

-- 基于原值更新
UPDATE users SET age = age + 1 WHERE age < 30;

-- 多列更新
UPDATE users
SET age = 27, email = 'new@example.com', updated_at = NOW()
WHERE id = 1;
```

**JOIN 更新**
`UPDATE <表1> [AS <别名>] JOIN <表2> [AS <别名>] ON <条件> SET <列名> = <值>`
```sql
-- 关联更新
UPDATE users u
JOIN user_profiles p ON u.id = p.user_id
SET u.avatar = p.avatar_url, u.status = p.status
WHERE u.id = 1;
```

**子查询更新**
`UPDATE <表名> SET <列名> = (SELECT <聚合> FROM <表> WHERE <条件>)`
```sql
-- 用子查询结果更新
UPDATE users
SET balance = (SELECT SUM(amount) FROM orders WHERE user_id = users.id)
WHERE id = 1;
```

---

## 删除数据

**基本删除**
`DELETE FROM <表名> [WHERE <条件>] [ORDER BY <列名>] [LIMIT <行数>]`
```sql
-- 条件删除
DELETE FROM users WHERE id = 1;

-- 范围删除
DELETE FROM users WHERE status = 0 AND created_at < '2024-01-01';

-- 删除前 N 行
DELETE FROM users ORDER BY created_at DESC LIMIT 10;
```

**JOIN 删除**
`DELETE <别名> FROM <表1> [AS <别名>] JOIN <表2> [AS <别名>] ON <条件> WHERE <条件>`
```sql
-- 关联删除
DELETE u FROM users u
JOIN inactive_users i ON u.email = i.email
WHERE u.status = 0;
```

**子查询删除**
`DELETE FROM <表名> WHERE <列名> IN (SELECT <列名> FROM <表> WHERE <条件>)`
```sql
-- 用子查询删除
DELETE FROM users WHERE id IN (SELECT user_id FROM old_users WHERE created_at < '2023-01-01');
```

**清空表**
`TRUNCATE TABLE <表名>`
```sql
-- 清空表数据并重置自增
TRUNCATE TABLE users;
```

---

## 基础查询

**查询所有列**
`SELECT * FROM <表名>`
```sql
-- 查询全部
SELECT * FROM users;
```

**查询指定列**
`SELECT <列名>[, <列名>...] FROM <表名>`
```sql
-- 查询指定列
SELECT id, username, email FROM users;
```

**列别名**
`SELECT <列名> [AS] <别名>`
```sql
-- 使用别名
SELECT username AS name, email AS "邮箱地址" FROM users;

-- 计算列别名
SELECT price, quantity, price * quantity AS total FROM order_items;
```

**去重查询**
`SELECT DISTINCT <列名>[, <列名>...] FROM <表名>`
```sql
-- 单列去重
SELECT DISTINCT status FROM users;

-- 多列去重
SELECT DISTINCT province, city FROM addresses;
```

---

## 条件查询

**比较运算符**
`WHERE <列名> <比较运算符> <值>`
```sql
-- 大于、小于、等于
SELECT * FROM users WHERE age > 25;
SELECT * FROM users WHERE age >= 25;
SELECT * FROM users WHERE age < 30;
SELECT * FROM users WHERE age != 25;
SELECT * FROM users WHERE age <> 25;
```

**逻辑运算符**
`WHERE <条件1> <AND|OR> <条件2> [AND|OR ...]`
```sql
-- AND 且
SELECT * FROM users WHERE age > 25 AND status = 1;

-- OR 或
SELECT * FROM users WHERE status = 1 OR status = 2;

-- NOT 非
SELECT * FROM users WHERE NOT status = 0;

-- 括号组合
SELECT * FROM users
WHERE (age > 25 AND status = 1) OR (age < 20 AND status = 2);
```

**范围查询**
`WHERE <列名> [NOT] BETWEEN <起始> AND <结束>`
```sql
-- 数值范围
SELECT * FROM users WHERE age BETWEEN 20 AND 30;

-- 日期范围
SELECT * FROM users WHERE created_at BETWEEN '2024-01-01' AND '2024-12-31';
```

**IN 查询**
`WHERE <列名> [NOT] IN (<值1>, <值2>[, ...])`
```sql
-- 多值匹配
SELECT * FROM users WHERE status IN (1, 2, 3);

-- 子查询 IN
SELECT * FROM users WHERE id IN (SELECT user_id FROM vip_users);
```

**模糊查询**
`WHERE <列名> [NOT] LIKE '<模式>' [ESCAPE '<转义符>']`
```sql
-- 以指定字符开头
SELECT * FROM users WHERE username LIKE '张%';

-- 包含指定字符
SELECT * FROM users WHERE username LIKE '%张%';

-- 单字符匹配
SELECT * FROM users WHERE username LIKE '张_';

-- 指定转义符
SELECT * FROM users WHERE username LIKE '%100\%%' ESCAPE '\';
```

**NULL 查询**
`WHERE <列名> IS [NOT] NULL`
```sql
-- 查询空值
SELECT * FROM users WHERE email IS NULL;

-- 查询非空
SELECT * FROM users WHERE deleted_at IS NOT NULL;
```

---

## 排序与分页

**排序**
`ORDER BY <列名> [ASC|DESC][, <列名> [ASC|DESC]...]`
```sql
-- 升序
SELECT * FROM users ORDER BY age ASC;

-- 降序
SELECT * FROM users ORDER BY created_at DESC;

-- 多列排序
SELECT * FROM users ORDER BY status ASC, age DESC;

-- 按列位置排序
SELECT id, username, email FROM users ORDER BY 3;
```

**分页**
`LIMIT <行数> [OFFSET <偏移>]` 或 `LIMIT <偏移>, <行数>`
```sql
-- 取前 10 行
SELECT * FROM users LIMIT 10;

-- 第 2 页（每页 10 行）
SELECT * FROM users LIMIT 10 OFFSET 10;

-- 简写形式
SELECT * FROM users LIMIT 10, 10;

-- 排序后取前 5 行
SELECT * FROM users ORDER BY id DESC LIMIT 5;
```

---

## 分组查询

**分组统计**
`SELECT <分组列>, <聚合函数>(<列名>) FROM <表名> GROUP BY <分组列>`
```sql
-- 按状态分组计数
SELECT status, COUNT(*) AS count FROM users GROUP BY status;

-- 多列分组
SELECT province, city, COUNT(*) AS count FROM users GROUP BY province, city;

-- 分组求平均值
SELECT status, AVG(age) AS avg_age FROM users GROUP BY status;
```

**分组过滤**
`SELECT <列> FROM <表> [WHERE <条件>] GROUP BY <列> HAVING <过滤条件>`
```sql
-- 过滤分组结果
SELECT status, COUNT(*) AS count
FROM users
GROUP BY status
HAVING count > 10;

-- WHERE 与 HAVING 组合
SELECT status, AVG(age) AS avg_age, COUNT(*) AS count
FROM users
WHERE age > 0
GROUP BY status
HAVING count > 5 AND avg_age > 25;
```

---

## 聚合函数

**计数**
`COUNT(*|<列名>|DISTINCT <列名>)`
```sql
-- 总行数
SELECT COUNT(*) FROM users;

-- 非空计数
SELECT COUNT(email) FROM users;

-- 去重计数
SELECT COUNT(DISTINCT status) FROM users;
```

**求和**
`SUM(<列名>)`
```sql
-- 求总和
SELECT SUM(balance) AS total_balance FROM users;
```

**平均值**
`AVG(<列名>)`
```sql
-- 求平均
SELECT AVG(age) AS avg_age FROM users;
```

**最大最小值**
`MAX(<列名>)` / `MIN(<列名>)`
```sql
-- 求最大最小
SELECT MAX(price) AS max_price, MIN(price) AS min_price FROM products;
```

**字符串拼接**
`GROUP_CONCAT(<列名> [SEPARATOR '<分隔符>'])`
```sql
-- 分组拼接
SELECT status, GROUP_CONCAT(username SEPARATOR ',') AS names
FROM users GROUP BY status;
```
