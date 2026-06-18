# PostgreSQL 高级 SQL

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 窗口函数

**排名函数**
`<RANK|DENSE_RANK|ROW_NUMBER>() OVER (PARTITION BY <列名> ORDER BY <列名> [ASC|DESC])`
```sql
-- 部门内薪资排名
SELECT name, dept_id, salary,
    RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rank,
    DENSE_RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS dense_rank,
    ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS row_num
FROM employees;
```

**累计聚合**
`<SUM|AVG|COUNT>(<列名>) OVER (ORDER BY <列名> [ROWS BETWEEN <范围>])`
```sql
-- 累计求和与移动平均
SELECT order_date, amount,
    SUM(amount) OVER (ORDER BY order_date) AS cumulative,
    AVG(amount) OVER (ORDER BY order_date ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS moving_avg
FROM daily_sales;
```

**LAG / LEAD**
`<LAG|LEAD>(<列名>[, <偏移量>[, <默认值>]]) OVER (ORDER BY <列名>)`
```sql
-- 环比变化
SELECT order_date, amount,
    amount - LAG(amount) OVER (ORDER BY order_date) AS day_over_day
FROM daily_sales;
```

**FILTER 子句**
`<聚合函数>(*) FILTER (WHERE <条件>)`
```sql
-- 条件聚合
SELECT dept_id,
    COUNT(*) AS total,
    COUNT(*) FILTER (WHERE salary > 50000) AS high_earners
FROM employees
GROUP BY dept_id;
```

---

## CTE 与递归 CTE

**普通 CTE**
`WITH <CTE 名称> AS (<SELECT 语句>) SELECT ...`
```sql
-- 使用 CTE 简化复杂查询
WITH dept_stats AS (
    SELECT dept_id, AVG(salary) AS avg_salary
    FROM employees GROUP BY dept_id
)
SELECT e.name, e.salary, ds.avg_salary
FROM employees e JOIN dept_stats ds ON e.dept_id = ds.dept_id;
```

**递归 CTE**
`WITH RECURSIVE <CTE 名称> AS (<基础查询> UNION ALL <递归查询>) SELECT ...`
```sql
-- 递归查询组织树
WITH RECURSIVE org_tree AS (
    SELECT emp_id, name, manager_id, 1 AS level
    FROM employees WHERE manager_id IS NULL
    UNION ALL
    SELECT e.emp_id, e.name, e.manager_id, ot.level + 1
    FROM employees e JOIN org_tree ot ON e.manager_id = ot.emp_id
)
SELECT * FROM org_tree;
```

---

## 横向连接

**LATERAL**
`SELECT <列名> FROM <表1>, LATERAL (<子查询>) AS <别名>`
```sql
-- 每行执行子查询
SELECT d.dept_name, top3.name, top3.salary
FROM departments d,
LATERAL (
    SELECT name, salary FROM employees
    WHERE dept_id = d.id
    ORDER BY salary DESC LIMIT 3
) top3;
```

---

## 分组集

**ROLLUP**
`GROUP BY ROLLUP (<列名>[, <列名>...])`
```sql
-- 层次汇总
SELECT dept_id, job_title, SUM(salary)
FROM employees
GROUP BY ROLLUP (dept_id, job_title);
```

**CUBE**
`GROUP BY CUBE (<列名>[, <列名>...])`
```sql
-- 多维汇总
SELECT dept_id, job_title, SUM(salary)
FROM employees
GROUP BY CUBE (dept_id, job_title);
```

**GROUPING SETS**
`GROUP BY GROUPING SETS ((<列组合1>), (<列组合2>), ...)`
```sql
-- 自定义分组集
SELECT dept_id, job_title, SUM(salary)
FROM employees
GROUP BY GROUPING SETS ((dept_id, job_title), (dept_id), ());
```
