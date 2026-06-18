# CSS Flexbox 弹性布局

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 弹性容器创建

**块级弹性容器**：创建块级 flex 容器
`display: flex;`
```css
/* 块级弹性容器 */
.flex-container {
  display: flex;
}
```

---

**内联弹性容器**：创建内联 flex 容器
`display: inline-flex;`
```css
/* 内联弹性容器 */
.inline-flex-container {
  display: inline-flex;
}
```

---

## 容器属性

**flex-direction**：定义主轴方向
`flex-direction: row | row-reverse | column | column-reverse;`
```css
/* 水平方向（默认） */
.container { display: flex; flex-direction: row; }

/* 垂直方向 */
.container { display: flex; flex-direction: column; }

/* 水平反向 */
.container { display: flex; flex-direction: row-reverse; }

/* 垂直反向 */
.container { display: flex; flex-direction: column-reverse; }
```

---

**justify-content**：主轴对齐方式
`justify-content: flex-start | flex-end | center | space-between | space-around | space-evenly;`
```css
/* 主轴起始对齐 */
.container { display: flex; justify-content: flex-start; }

/* 主轴居中对齐 */
.container { display: flex; justify-content: center; }

/* 项目均匀分布，两端对齐 */
.container { display: flex; justify-content: space-between; }

/* 项目均匀分布，两端有间距 */
.container { display: flex; justify-content: space-around; }

/* 项目均匀分布，两端和中间间距相等 */
.container { display: flex; justify-content: space-evenly; }
```

---

**align-items**：侧轴对齐方式
`align-items: stretch | flex-start | flex-end | center | baseline;`
```css
/* 侧轴拉伸对齐（默认） */
.container { display: flex; align-items: stretch; }

/* 侧轴起始对齐 */
.container { display: flex; align-items: flex-start; }

/* 侧轴居中对齐 */
.container { display: flex; align-items: center; }

/* 侧轴结束对齐 */
.container { display: flex; align-items: flex-end; }

/* 基线对齐 */
.container { display: flex; align-items: baseline; }
```

---

**flex-wrap**：是否换行
`flex-wrap: nowrap | wrap | wrap-reverse;`
```css
/* 不换行（默认） */
.container { display: flex; flex-wrap: nowrap; }

/* 换行 */
.container { display: flex; flex-wrap: wrap; }

/* 反向换行 */
.container { display: flex; flex-wrap: wrap-reverse; }
```

---

**flex-flow**：flex-direction 和 flex-wrap 的复合属性
`flex-flow: <direction> <wrap>;`
```css
/* 水平方向，不换行（默认） */
.container { display: flex; flex-flow: row nowrap; }

/* 垂直方向，换行 */
.container { display: flex; flex-flow: column wrap; }
```

---

**align-content**：多行侧轴对齐方式
`align-content: stretch | flex-start | flex-end | center | space-between | space-around;`
```css
/* 多行拉伸对齐（默认） */
.container { display: flex; flex-wrap: wrap; align-content: stretch; }

/* 多行居中对齐 */
.container { display: flex; flex-wrap: wrap; align-content: center; }

/* 多行均匀分布 */
.container { display: flex; flex-wrap: wrap; align-content: space-between; }
```

---

**gap**：项目间距
`gap: <行间距> <列间距>;`
```css
/* 统一间距 */
.container {
  display: flex;
  gap: 20px;
}

/* 不同行列间距 */
.container {
  display: flex;
  gap: 10px 20px;
}
```

---

## 项目属性

**flex-grow**：放大比例
`flex-grow: <数值>;`
```css
/* 项目不放大（默认） */
.item { flex-grow: 0; }

/* 项目放大比例为 1 */
.item { flex-grow: 1; }

/* 不同项目的放大比例 */
.item-1 { flex-grow: 1; }
.item-2 { flex-grow: 2; }
.item-3 { flex-grow: 1; }
```

---

**flex-shrink**：缩小比例
`flex-shrink: <数值>;`
```css
/* 项目可以缩小（默认） */
.item { flex-shrink: 1; }

/* 项目不可缩小 */
.item { flex-shrink: 0; }
```

---

**flex-basis**：初始大小
`flex-basis: auto | <长度> | <百分比>;`
```css
/* 初始大小为 auto（默认） */
.item { flex-basis: auto; }

/* 初始大小为 200px */
.item { flex-basis: 200px; }

/* 初始大小为 50% */
.item { flex-basis: 50%; }
```

---

**flex 复合属性**：grow、shrink、basis 的简写
`flex: <grow> <shrink> <basis>;`
```css
/* 默认值：0 1 auto */
.item { flex: 0 1 auto; }

/* 等比分配空间 */
.item { flex: 1; }

/* 不缩小，初始大小为 200px */
.item { flex: 0 0 200px; }

/* 放大比例为 2，缩小比例为 1，初始大小为 100px */
.item { flex: 2 1 100px; }
```

---

**align-self**：单个项目侧轴对齐
`align-self: auto | stretch | flex-start | flex-end | center | baseline;`
```css
/* 继承容器的 align-items（默认） */
.item { align-self: auto; }

/* 单个项目居中对齐 */
.item { align-self: center; }

/* 单个项目起始对齐 */
.item { align-self: flex-start; }

/* 单个项目结束对齐 */
.item { align-self: flex-end; }
```

---

**order**：排列顺序
`order: <数值>;`
```css
/* 默认顺序 */
.item { order: 0; }

/* 不同项目的顺序 */
.item-1 { order: 3; }
.item-2 { order: 1; }
.item-3 { order: 2; }
```

---

## 常见布局

**垂直水平居中**：使用 flex 实现居中
`display: flex; justify-content: center; align-items: center;`
```css
/* 垂直水平居中 */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
.item {
  width: 100px;
  height: 100px;
}
```

---

**等高布局**：项目等高
`display: flex;`
```css
/* 等高布局 */
.container {
  display: flex;
}
.item {
  flex: 1;
  padding: 20px;
  border: 1px solid #ddd;
}
```

---

**导航菜单**：两端对齐的导航
`display: flex; justify-content: space-between;`
```css
.nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  padding: 10px 20px;
}
.nav__menu {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 20px;
}
```

---

**卡片布局**：响应式卡片网格
`display: flex; flex-wrap: wrap; gap: <间距>;`
```css
.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
}
.card {
  flex: 1 1 300px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}
```

---

**表单布局**：垂直排列的表单
`display: flex; flex-direction: column; gap: <间距>;`
```css
.form {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-width: 500px;
  margin: 0 auto;
}
.form__group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
```

---

## 响应式 Flexbox

**媒体查询调整**：响应式布局
`@media (max-width: <宽度>) { <选择器> { <样式> } }`
```css
/* 基础布局 */
.container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.item {
  flex: 1 1 300px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .item {
    flex: 1 1 100%;
  }
}

@media (max-width: 480px) {
  .container {
    flex-direction: column;
  }
  .item {
    flex: 1 1 auto;
  }
}
```

---

## 性能优化

**使用 transform 代替 top/left**：避免重排
`transition: transform <时间>;`
```css
/* 优化前 */
.item {
  position: relative;
  left: 0;
  transition: left 0.3s ease;
}
.item:hover {
  left: 10px;
}

/* 优化后 */
.item {
  will-change: transform;
  transition: transform 0.3s ease;
}
.item:hover {
  transform: translateX(10px);
}
```
