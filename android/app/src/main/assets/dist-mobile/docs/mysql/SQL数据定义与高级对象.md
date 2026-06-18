# MySQL SQL 数据定义与高级对象

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 数据库操作

**创建数据库**
`CREATE DATABASE [IF NOT EXISTS] <库名> [CHARACTER SET <字符集>] [COLLATE <排序规则>]`
```sql
-- 基本创建
CREATE DATABASE mydb;

-- 指定字符集
CREATE DATABASE mydb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 不存在时创建
CREATE DATABASE IF NOT EXISTS mydb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

**查看数据库**
`SHOW DATABASES` / `SHOW CREATE DATABASE <库名>` / `SELECT DATABASE()`
```sql
-- 查看所有库
SHOW DATABASES;

-- 查看建库语句
SHOW CREATE DATABASE mydb;

-- 查看当前库
SELECT DATABASE();
```

**选择数据库**
`USE <库名>`
```sql
-- 切换数据库
USE mydb;
```

**删除数据库**
`DROP DATABASE [IF EXISTS] <库名>`
```sql
-- 删除数据库
DROP DATABASE mydb;

-- 存在时删除
DROP DATABASE IF EXISTS mydb;
```

**修改数据库**
`ALTER DATABASE <库名> CHARACTER SET <字符集> COLLATE <排序规则>`
```sql
-- 修改字符集
ALTER DATABASE mydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 表操作

**创建表**
`CREATE TABLE [IF NOT EXISTS] <表名> (<列定义> [, <表约束>...])`
```sql
-- 完整建表示例
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  email VARCHAR(100) NOT NULL COMMENT '邮箱',
  password VARCHAR(255) NOT NULL COMMENT '密码',
  phone VARCHAR(20) COMMENT '手机号',
  age INT UNSIGNED COMMENT '年龄',
  gender ENUM('男', '女', '保密') DEFAULT '保密' COMMENT '性别',
  status TINYINT DEFAULT 1 COMMENT '状态',
  balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_username (username),
  INDEX idx_email (email),
  INDEX idx_status (status)
);
```

**查看表结构**
`DESC <表名>` / `SHOW COLUMNS FROM <表名>` / `SHOW CREATE TABLE <表名>`
```sql
-- 查看表字段
DESC users;

-- 查看列信息
SHOW COLUMNS FROM users;

-- 查看建表语句
SHOW CREATE TABLE users;

-- 查看所有表
SHOW TABLES;

-- 模糊查表
SHOW TABLES LIKE '%user%';
```

**修改表结构**
`ALTER TABLE <表名> <修改操作>`

**添加列**
`ALTER TABLE <表名> ADD COLUMN <列定义> [AFTER <列名>]`
```sql
-- 添加列
ALTER TABLE users ADD COLUMN address VARCHAR(255) AFTER email;
ALTER TABLE users ADD COLUMN is_verified TINYINT DEFAULT 0 AFTER status;
```

**修改列类型**
`ALTER TABLE <表名> MODIFY COLUMN <列名> <新类型> [<约束>]`
```sql
-- 修改列定义
ALTER TABLE users MODIFY COLUMN phone VARCHAR(20) NOT NULL;
```

**重命名列**
`ALTER TABLE <表名> CHANGE COLUMN <旧列名> <新列名> <类型> [<约束>]`
```sql
-- 重命名列
ALTER TABLE users CHANGE COLUMN phone telephone VARCHAR(20) NOT NULL;
```

**删除列**
`ALTER TABLE <表名> DROP COLUMN <列名>`
```sql
-- 删除列
ALTER TABLE users DROP COLUMN address;
```

**添加索引**
`ALTER TABLE <表名> ADD [UNIQUE] INDEX <索引名> (<列名>[, <列名>...])`
```sql
-- 添加普通索引
ALTER TABLE users ADD INDEX idx_age (age);

-- 添加唯一索引
ALTER TABLE users ADD UNIQUE INDEX idx_phone (phone);

-- 添加复合索引
ALTER TABLE users ADD INDEX idx_age_gender (age, gender);
```

**添加外键**
`ALTER TABLE <表名> ADD CONSTRAINT <约束名> FOREIGN KEY (<列名>) REFERENCES <父表>(<父列>)`
```sql
-- 添加外键
ALTER TABLE orders ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id);

-- 删除外键
ALTER TABLE orders DROP FOREIGN KEY fk_user_id;
```

**重命名表**
`ALTER TABLE <旧表名> RENAME TO <新表名>` / `RENAME TABLE <旧表名> TO <新表名>`
```sql
-- 重命名表
ALTER TABLE users RENAME TO user_info;

-- 多表重命名
RENAME TABLE users TO user_info, orders TO order_info;
```

**删除表**
`DROP TABLE [IF EXISTS] <表名>[, <表名>...]`
```sql
-- 删除表
DROP TABLE users;

-- 存在时删除
DROP TABLE IF EXISTS users;

-- 删除多表
DROP TABLE IF EXISTS users, orders, products;
```

**清空表**
`TRUNCATE TABLE <表名>`
```sql
-- 清空表数据
TRUNCATE TABLE users;
```

**复制表**
`CREATE TABLE <新表> LIKE <源表>` / `CREATE TABLE <新表> AS SELECT ...`
```sql
-- 仅复制结构
CREATE TABLE users_copy LIKE users;

-- 复制结构和数据
CREATE TABLE users_copy AS SELECT * FROM users;

-- 复制部分数据
CREATE TABLE users_copy AS SELECT * FROM users WHERE status = 1;
```

---

## 索引操作

**创建索引**
`CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX <索引名> ON <表名>(<列名>[(<长度>)][, ...])`
```sql
-- 普通索引
CREATE INDEX idx_username ON users(username);

-- 唯一索引
CREATE UNIQUE INDEX idx_email ON users(email);

-- 复合索引
CREATE INDEX idx_name_status ON users(username, status);

-- 前缀索引
CREATE INDEX idx_email_prefix ON users(email(10));

-- 全文索引
ALTER TABLE articles ADD FULLTEXT INDEX ft_title_content (title, content);
```

**查看索引**
`SHOW INDEX FROM <表名>`
```sql
-- 查看表索引
SHOW INDEX FROM users;

-- 竖向显示
SHOW INDEX FROM users\G
```

**删除索引**
`DROP INDEX <索引名> ON <表名>`
```sql
-- 删除索引
DROP INDEX idx_username ON users;
```

---

## 约束

**约束示例**
`CREATE TABLE <表名> (<列定义>, <约束定义>...)`
```sql
-- 综合约束示例
CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
  user_id INT NOT NULL COMMENT '用户ID',
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总额',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CHECK (total_amount >= 0),
  CHECK (status IN (1, 2, 3, 4, 5))
);
```

---

## 事务

**开启事务**
`START TRANSACTION` / `BEGIN`
```sql
-- 开启事务
START TRANSACTION;

-- 或
BEGIN;
```

**提交事务**
`COMMIT`
```sql
-- 提交
START TRANSACTION;
INSERT INTO users (username, email) VALUES ('张三', 'zhangsan@example.com');
UPDATE accounts SET balance = balance - 100 WHERE user_id = 1;
COMMIT;
```

**回滚事务**
`ROLLBACK`
```sql
-- 回滚
ROLLBACK;
```

**保存点**
`SAVEPOINT <保存点名>` / `ROLLBACK TO <保存点名>`
```sql
-- 使用保存点
START TRANSACTION;
INSERT INTO users (username) VALUES ('张三');
SAVEPOINT sp1;
INSERT INTO users (username) VALUES ('李四');
ROLLBACK TO sp1;
COMMIT;
```

**设置隔离级别**
`SET [SESSION|GLOBAL] TRANSACTION ISOLATION LEVEL <级别>`
```sql
-- 查看隔离级别
SELECT @@transaction_isolation;

-- 设置会话隔离级别
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- 设置全局隔离级别
SET GLOBAL TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

---

## 视图

**创建视图**
`CREATE VIEW <视图名> AS <SELECT 语句>`
```sql
-- 简单视图
CREATE VIEW active_users AS
SELECT id, username, email, status
FROM users
WHERE status = 1;

-- 多表视图
CREATE VIEW order_details AS
SELECT
  o.id AS order_id,
  o.order_no,
  u.username,
  u.email,
  o.total_amount,
  o.status
FROM orders o
INNER JOIN users u ON o.user_id = u.id;
```

**查询视图**
`SELECT <列名> FROM <视图名> [WHERE <条件>]`
```sql
-- 查询视图
SELECT * FROM active_users WHERE username LIKE '张%';
```

**修改视图**
`CREATE OR REPLACE VIEW <视图名> AS <SELECT 语句>`
```sql
-- 替换视图定义
CREATE OR REPLACE VIEW active_users AS
SELECT id, username, email, status, created_at
FROM users
WHERE status = 1;
```

**删除视图**
`DROP VIEW [IF EXISTS] <视图名>`
```sql
-- 删除视图
DROP VIEW IF EXISTS active_users;
```

**查看视图定义**
`SHOW CREATE VIEW <视图名>`
```sql
-- 查看视图建语句
SHOW CREATE VIEW order_details;
```

---

## 存储过程

**创建存储过程**
`CREATE PROCEDURE <过程名>([<参数>]) BEGIN <过程体> END`
```sql
-- 带 IN 参数
DELIMITER //
CREATE PROCEDURE get_user_by_age(IN min_age INT, IN max_age INT)
BEGIN
  SELECT * FROM users
  WHERE age BETWEEN min_age AND max_age
  ORDER BY age;
END //

-- 带 OUT 参数
CREATE PROCEDURE count_users_by_status(OUT active_count INT, OUT inactive_count INT)
BEGIN
  SELECT COUNT(*) INTO active_count FROM users WHERE status = 1;
  SELECT COUNT(*) INTO inactive_count FROM users WHERE status = 0;
END //
DELIMITER ;
```

**调用存储过程**
`CALL <过程名>([<参数>...])`
```sql
-- 调用无参
CALL get_all_users();

-- 调用 IN 参数
CALL get_user_by_age(20, 30);

-- 调用 OUT 参数
CALL count_users_by_status(@active, @inactive);
SELECT @active AS active_users, @inactive AS inactive_users;
```

**删除存储过程**
`DROP PROCEDURE [IF EXISTS] <过程名>`
```sql
-- 删除存储过程
DROP PROCEDURE IF EXISTS get_user_by_age;
```

---

## 触发器

**创建触发器**
`CREATE TRIGGER <触发器名> {BEFORE|AFTER} {INSERT|UPDATE|DELETE} ON <表名> FOR EACH ROW BEGIN <触发体> END`
```sql
-- 插入前触发
DELIMITER //
CREATE TRIGGER before_user_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
  SET NEW.created_at = NOW();
  SET NEW.updated_at = NOW();
  IF NEW.status IS NULL THEN
    SET NEW.status = 1;
  END IF;
END //

-- 更新后触发
CREATE TRIGGER after_order_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
  IF OLD.status != NEW.status THEN
    INSERT INTO order_status_log (order_id, old_status, new_status, changed_at)
    VALUES (OLD.id, OLD.status, NEW.status, NOW());
  END IF;
END //
DELIMITER ;
```

**删除触发器**
`DROP TRIGGER [IF EXISTS] <触发器名>`
```sql
-- 删除触发器
DROP TRIGGER IF EXISTS before_user_insert;
```
