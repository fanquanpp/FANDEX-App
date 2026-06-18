# MySQL SQL 函数与高级查询

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 字符串函数

**CONCAT**
`CONCAT(<字符串1>[, <字符串2>...])`
```sql
-- 连接字符串
SELECT CONCAT(username, ' (', email, ')') AS user_info FROM users;
```

**CONCAT_WS**
`CONCAT_WS('<分隔符>', <字符串1>[, <字符串2>...])`
```sql
-- 带分隔符连接
SELECT CONCAT_WS('-', province, city, district) AS full_address FROM addresses;

-- 空字符串分隔
SELECT CONCAT_WS('', province, city, district, detail_address) AS full_address FROM addresses;
```

**LENGTH / CHAR_LENGTH**
`LENGTH(<字符串>)` / `CHAR_LENGTH(<字符串>)`
```sql
-- 字节长度与字符长度
SELECT
  username,
  LENGTH(username) AS name_bytes,
  CHAR_LENGTH(username) AS name_chars
FROM users;
```

**SUBSTRING**
`SUBSTRING(<字符串>, <起始位置>[, <长度>])`
```sql
-- 截取字符串
SELECT SUBSTRING(phone, 1, 3) AS phone_prefix FROM users;
```

**LEFT / RIGHT**
`LEFT(<字符串>, <长度>)` / `RIGHT(<字符串>, <长度>)`
```sql
-- 从左/右截取
SELECT LEFT(username, 2) FROM users;
```

**TRIM**
`TRIM(<字符串>)`
```sql
-- 去除首尾空格
SELECT TRIM(' Hello ');
```

**LOWER / UPPER**
`LOWER(<字符串>)` / `UPPER(<字符串>)`
```sql
-- 转小写/大写
SELECT LOWER(email) AS email_lower, UPPER(username) AS name_upper FROM users;
```

**REPLACE**
`REPLACE(<字符串>, '<旧子串>', '<新子串>')`
```sql
-- 替换字符串
SELECT REPLACE('Hello', 'l', 'w');
```

**REVERSE**
`REVERSE(<字符串>)`
```sql
-- 反转字符串
SELECT REVERSE('Hello');
```

**LPAD / RPAD**
`LPAD(<字符串>, <长度>, '<填充字符>')` / `RPAD(<字符串>, <长度>, '<填充字符>')`
```sql
-- 左填充
SELECT LPAD('5', 3, '0');
```

**INSTR**
`INSTR(<字符串>, '<子串>')`
```sql
-- 查找子串位置
SELECT INSTR('Hello', 'll');
```

---

## 日期时间函数

**NOW / CURDATE / CURTIME**
`NOW()` / `CURDATE()` / `CURTIME()`
```sql
-- 当前日期时间
SELECT
  NOW() AS now,
  CURDATE() AS today,
  CURTIME() AS current_time;
```

**DATE / TIME**
`DATE(<日期时间>)` / `TIME(<日期时间>)`
```sql
-- 提取日期/时间部分
SELECT DATE('2024-01-15 10:30:00'), TIME('2024-01-15 10:30:00');
```

**YEAR / MONTH / DAY**
`YEAR(<日期>)` / `MONTH(<日期>)` / `DAY(<日期>)`
```sql
-- 提取年月日
SELECT YEAR(NOW()), MONTH(NOW()), DAY(NOW());
```

**HOUR / MINUTE / SECOND**
`HOUR(<时间>)` / `MINUTE(<时间>)` / `SECOND(<时间>)`
```sql
-- 提取时分秒
SELECT HOUR(NOW()), MINUTE(NOW()), SECOND(NOW());
```

**DATE_FORMAT**
`DATE_FORMAT(<日期>, '<格式>')`
```sql
-- 格式化日期
SELECT DATE_FORMAT(NOW(), '%Y年%m月%d日 %H:%i:%s') AS formatted;
```

**DATE_ADD / DATE_SUB**
`DATE_ADD(<日期>, INTERVAL <值> <单位>)` / `DATE_SUB(<日期>, INTERVAL <值> <单位>)`
```sql
-- 日期加减
SELECT
  DATE_ADD(NOW(), INTERVAL 7 DAY) AS next_week,
  DATE_SUB(NOW(), INTERVAL 1 MONTH) AS last_month;
```

**DATEDIFF**
`DATEDIFF(<日期1>, <日期2>)`
```sql
-- 日期差（天数）
SELECT DATEDIFF(NOW(), created_at) AS days_since_join FROM users;
```

**TIMESTAMPDIFF**
`TIMESTAMPDIFF(<单位>, <开始>, <结束>)`
```sql
-- 时间差
SELECT TIMESTAMPDIFF(YEAR, created_at, NOW()) AS years_since_join FROM users;

-- 计算年龄
SELECT TIMESTAMPDIFF(YEAR, birthday, NOW()) AS age FROM users;
```

**DAYOFWEEK / LAST_DAY**
`DAYOFWEEK(<日期>)` / `LAST_DAY(<日期>)`
```sql
-- 星期几 / 月份最后一天
SELECT DAYOFWEEK(NOW()), LAST_DAY('2024-01-15');
```

---

## 数值函数

**ABS**
`ABS(<数值>)`
```sql
-- 绝对值
SELECT ABS(-10);
```

**ROUND**
`ROUND(<数值>[, <小数位>])`
```sql
-- 四舍五入
SELECT ROUND(price, 2) AS rounded FROM products;
```

**CEIL / FLOOR**
`CEIL(<数值>)` / `FLOOR(<数值>)`
```sql
-- 向上/向下取整
SELECT CEIL(price) AS ceil_price, FLOOR(price) AS floor_price FROM products;
```

**MOD**
`MOD(<数值1>, <数值2>)`
```sql
-- 取模
SELECT MOD(10, 3);
```

**POW / SQRT**
`POW(<底数>, <指数>)` / `SQRT(<数值>)`
```sql
-- 幂运算 / 平方根
SELECT POW(2, 3), SQRT(16);
```

**RAND**
`RAND()`
```sql
-- 随机数
SELECT * FROM users ORDER BY RAND() LIMIT 5;
```

**TRUNCATE**
`TRUNCATE(<数值>, <小数位>)`
```sql
-- 截断
SELECT TRUNCATE(3.14159, 3);
```

**SIGN**
`SIGN(<数值>)`
```sql
-- 符号
SELECT SIGN(-10);
```

---

## 条件函数

**IF**
`IF(<条件>, <真值>, <假值>)`
```sql
-- 条件判断
SELECT
  username,
  age,
  IF(age >= 18, '成人', '未成年') AS age_desc,
  IF(status = 1, '正常', '禁用') AS status_desc
FROM users;
```

**IFNULL**
`IFNULL(<值>, <默认值>)`
```sql
-- NULL 替换
SELECT
  username,
  IFNULL(email, '未填写') AS email,
  IFNULL(phone, IFNULL(telephone, '无')) AS contact
FROM users;
```

**NULLIF**
`NULLIF(<值1>, <值2>)`
```sql
-- 相等返回 NULL
SELECT NULLIF(a, b);
```

**CASE WHEN**
`CASE WHEN <条件> THEN <值> [WHEN ...] [ELSE <值>] END`
```sql
-- 多条件判断
SELECT
  username,
  age,
  CASE
    WHEN age < 18 THEN '未成年'
    WHEN age < 30 THEN '青年'
    WHEN age < 60 THEN '中年'
    ELSE '老年'
  END AS age_group
FROM users;
```

**CASE 表达式**
`CASE <表达式> WHEN <值> THEN <结果> [WHEN ...] [ELSE <结果>] END`
```sql
-- 等值匹配
SELECT
  username,
  CASE status
    WHEN 1 THEN '正常'
    WHEN 2 THEN '冻结'
    WHEN 0 THEN '禁用'
    ELSE '未知'
  END AS status_desc
FROM users;
```

**CASE 聚合**
`SUM(CASE WHEN <条件> THEN 1 ELSE 0 END)`
```sql
-- 条件计数
SELECT
  SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS active_count,
  SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS inactive_count,
  SUM(CASE WHEN gender = '男' THEN 1 ELSE 0 END) AS male_count,
  SUM(CASE WHEN gender = '女' THEN 1 ELSE 0 END) AS female_count
FROM users;
```

---

## 类型转换与系统函数

**CAST**
`CAST(<值> AS <类型>)`
```sql
-- 类型转换
SELECT CAST(price AS CHAR) FROM products;
SELECT CONVERT(price, DECIMAL(10,2)) FROM products;
```

**FORMAT**
`FORMAT(<数值>, <小数位>)`
```sql
-- 格式化数字（千位分隔符）
SELECT FORMAT(price, 2) AS price_formatted FROM products;
```

**MD5 / SHA1 / SHA2**
`MD5('<字符串>')` / `SHA1('<字符串>')` / `SHA2('<字符串>', <长度>)`
```sql
-- 哈希函数
SELECT
  MD5('password') AS md5_hash,
  SHA1('password') AS sha1_hash,
  SHA2('password', 256) AS sha256_hash;
```

**UUID**
`UUID()`
```sql
-- 生成 UUID
SELECT UUID() AS uuid;
```

**用户变量**
`SET @<变量名> = <值>` / `SELECT @<变量名> := <表达式>`
```sql
-- 用户变量累加
SET @total = 0;
SELECT @total := @total + price FROM products;
```

---

## 子查询

**标量子查询**
`SELECT * FROM <表名> WHERE <列名> = (SELECT <聚合> FROM <表名>)`
```sql
-- 查询年龄最大的用户
SELECT * FROM users WHERE age = (SELECT MAX(age) FROM users);

-- 查询年龄大于平均年龄的用户
SELECT * FROM users WHERE age > (SELECT AVG(age) FROM users);

-- 用子查询更新
UPDATE users SET age = (SELECT MAX(age) FROM users) + 1 WHERE id = 1;
```

**IN 子查询**
`WHERE <列名> [NOT] IN (SELECT <列名> FROM <表名>)`
```sql
-- IN 子查询
SELECT * FROM users WHERE id IN (SELECT user_id FROM vip_users);

-- NOT IN 子查询
SELECT * FROM users WHERE id NOT IN (SELECT user_id FROM blocked_users);
```

**ANY / ALL**
`WHERE <列名> <操作符> ANY (SELECT ...)` / `WHERE <列名> <操作符> ALL (SELECT ...)`
```sql
-- ANY：满足子查询中任意一个
SELECT * FROM products WHERE price > ANY (SELECT price FROM products WHERE category_id = 1);

-- ALL：满足子查询中所有
SELECT * FROM products WHERE price > ALL (SELECT price FROM products WHERE status = 0);
```

**EXISTS**
`WHERE [NOT] EXISTS (SELECT 1 FROM <表名> WHERE <条件>)`
```sql
-- EXISTS：存在相关记录
SELECT * FROM users u WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);

-- NOT EXISTS：不存在相关记录
SELECT * FROM users u WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

**FROM 子查询**
`SELECT * FROM (SELECT ...) AS <别名>`
```sql
-- 子查询作为临时表
SELECT * FROM (SELECT * FROM users WHERE status = 1) AS active_users;

-- 分组统计子查询
SELECT * FROM (
  SELECT status, COUNT(*) AS count, AVG(age) AS avg_age
  FROM users
  GROUP BY status
) AS stats;
```

**SELECT 子查询**
`SELECT <列名>, (SELECT ...) AS <别名>`
```sql
-- 列子查询
SELECT
  u.id,
  u.username,
  (SELECT COUNT(*) FROM orders WHERE user_id = u.id) AS order_count
FROM users u;

-- 最新订单时间
SELECT
  u.id,
  u.username,
  (SELECT MAX(created_at) FROM orders WHERE user_id = u.id) AS last_order_time
FROM users u;
```

---

## 多表查询

**内连接**
`SELECT <列名> FROM <表1> [AS <别名>] INNER JOIN <表2> [AS <别名>] ON <条件>`
```sql
-- 两表内连接
SELECT u.username, o.order_no, o.total_amount
FROM users u
INNER JOIN orders o ON u.id = o.user_id;

-- 多表内连接
SELECT u.username, o.order_no, p.product_name, oi.quantity
FROM users u
INNER JOIN orders o ON u.id = o.user_id
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id;

-- USING 简写
SELECT u.username, o.order_no
FROM users u
INNER JOIN orders o USING (user_id);
```

**左连接**
`SELECT <列名> FROM <表1> LEFT JOIN <表2> ON <条件>`
```sql
-- 左连接（左表全部）
SELECT u.username, o.order_no, o.total_amount
FROM users u
LEFT JOIN orders o ON u.id = o.user_id;

-- 左连接分组统计
SELECT u.username, COUNT(o.id) AS order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username;

-- 找出没有订单的用户
SELECT u.*
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE o.id IS NULL;
```

**右连接**
`SELECT <列名> FROM <表1> RIGHT JOIN <表2> ON <条件>`
```sql
-- 右连接（右表全部）
SELECT u.username, o.order_no
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id;

-- 找出没有员工的部门
SELECT e.*
FROM employees e
RIGHT JOIN departments d ON e.dept_id = d.id
WHERE e.id IS NULL;
```

**自连接**
`SELECT <列名> FROM <表> [AS <别名1>] JOIN <表> [AS <别名2>] ON <条件>`
```sql
-- 员工与同事
SELECT e1.name AS employee, e2.name AS colleague, d.name AS dept
FROM employees e1
JOIN employees e2 ON e1.dept_id = e2.dept_id AND e1.id != e2.id
JOIN departments d ON e1.dept_id = d.id
WHERE e1.name = '张三';

-- 员工与经理
SELECT e.name AS employee, m.name AS manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id;
```

**全连接（UNION 实现）**
`SELECT ... LEFT JOIN ... UNION SELECT ... RIGHT JOIN ...`
```sql
-- MySQL 用 UNION 实现 FULL OUTER JOIN
SELECT u.username, o.order_no
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
UNION
SELECT u.username, o.order_no
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id;
```

**交叉连接**
`SELECT <列名> FROM <表1> CROSS JOIN <表2>`
```sql
-- 笛卡尔积
SELECT u.username, p.product_name
FROM users u
CROSS JOIN products p;
```

---

## UNION 合并查询

**UNION**
`SELECT ... UNION [ALL] SELECT ...`
```sql
-- 合并结果集（去重）
SELECT username FROM users WHERE status = 1
UNION
SELECT username FROM users WHERE age > 30;

-- 合并结果集（保留重复）
SELECT username FROM users WHERE status = 1
UNION ALL
SELECT username FROM users WHERE age > 30;
```
