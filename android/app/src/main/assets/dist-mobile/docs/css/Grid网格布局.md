# CSS Grid 网格布局

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 网格容器创建

**块级网格容器**：创建块级 grid 容器
`display: grid;`
```css
/* 块级网格容器 */
.grid-container {
  display: grid;
}
```

---

**内联网格容器**：创建内联 grid 容器
`display: inline-grid;`
```css
/* 内联网格容器 */
.inline-grid-container {
  display: inline-grid;
}
```

---

## 容器属性

**grid-template-columns**：定义列轨道
`grid-template-columns: <值> <值> ...;`
```css
/* 使用固定值 */
.container { display: grid; grid-template-columns: 100px 200px 100px; }

/* 使用百分比 */
.container { display: grid; grid-template-columns: 25% 50% 25%; }

/* 使用分数单位 */
.container { display: grid; grid-template-columns: 1fr 2fr 1fr; }

/* 使用混合单位 */
.container { display: grid; grid-template-columns: 100px 1fr 2fr; }

/* 使用重复函数 */
.container { display: grid; grid-template-columns: repeat(3, 1fr); }

/* 使用自动填充 */
.container { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); }

/* 使用自动适应 */
.container { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); }
```

---

**grid-template-rows**：定义行轨道
`grid-template-rows: <值> <值> ...;`
```css
/* 使用固定值 */
.container { display: grid; grid-template-rows: 50px 100px 50px; }

/* 使用百分比 */
.container { display: grid; grid-template-rows: 20% 60% 20%; }

/* 使用分数单位 */
.container { display: grid; grid-template-rows: 1fr 2fr 1fr; }

/* 使用混合单位 */
.container { display: grid; grid-template-rows: 50px 1fr 50px; }
```

---

**grid-template-areas**：定义命名的网格区域
`grid-template-areas: '<区域名> <区域名> <区域名>' '<区域名> <区域名> <区域名>';`
```css
/* 定义网格区域 */
.container {
  display: grid;
  grid-template-areas:
    'header header header'
    'sidebar main main'
    'footer footer footer';
  grid-template-columns: 200px 1fr 1fr;
  grid-template-rows: 60px 1fr 60px;
}
.header { grid-area: header; }
.sidebar { grid-area: sidebar; }
.main { grid-area: main; }
.footer { grid-area: footer; }
```

---

**grid-template 复合属性**：columns、rows、areas 的简写
`grid-template: '<区域> <区域>' <行高> / <列宽>;`
```css
/* 复合属性 */
.container {
  display: grid;
  grid-template:
    'header header header' 60px
    'sidebar main main' 1fr
    'footer footer footer' 60px
    / 200px 1fr 1fr;
}
```

---

**gap**：网格间距
`gap: <行间距> <列间距>;`
```css
/* 设置行和列间距 */
.container {
  display: grid;
  gap: 20px;
}

/* 设置不同的行和列间距 */
.container {
  display: grid;
  gap: 10px 20px;
}

/* 单独设置 */
.container { display: grid; row-gap: 20px; column-gap: 20px; }
```

---

**justify-items**：项目在列轴上的对齐
`justify-items: stretch | start | end | center;`
```css
/* 项目在列轴上居中对齐 */
.container { display: grid; justify-items: center; }

/* 项目拉伸以填充单元格（默认） */
.container { display: grid; justify-items: stretch; }

/* 项目对齐到起始边缘 */
.container { display: grid; justify-items: start; }
```

---

**align-items**：项目在行轴上的对齐
`align-items: stretch | start | end | center | baseline;`
```css
/* 项目在行轴上居中对齐 */
.container { display: grid; align-items: center; }

/* 项目拉伸以填充单元格（默认） */
.container { display: grid; align-items: stretch; }

/* 基线对齐 */
.container { display: grid; align-items: baseline; }
```

---

**place-items**：align-items 和 justify-items 的复合属性
`place-items: <align> <justify>;`
```css
/* 项目在单元格中居中对齐 */
.container { display: grid; place-items: center; }

/* 不同的行轴和列轴对齐方式 */
.container { display: grid; place-items: start end; }
```

---

**justify-content**：网格在列轴上的对齐
`justify-content: stretch | start | end | center | space-between | space-around | space-evenly;`
```css
/* 网格在列轴上居中对齐 */
.container { display: grid; justify-content: center; }

/* 网格之间均匀分布 */
.container { display: grid; justify-content: space-between; }
```

---

**align-content**：网格在行轴上的对齐
`align-content: stretch | start | end | center | space-between | space-around;`
```css
/* 网格在行轴上居中对齐 */
.container { display: grid; align-content: center; }

/* 网格之间均匀分布 */
.container { display: grid; align-content: space-between; }
```

---

**grid-auto-columns / grid-auto-rows**：自动生成的轨道大小
`grid-auto-columns: <值>; | grid-auto-rows: <值>;`
```css
/* 自动生成的列轨道大小 */
.container { display: grid; grid-auto-columns: 100px; }

/* 自动生成的行轨道大小 */
.container { display: grid; grid-auto-rows: 100px; }
```

---

**grid-auto-flow**：自动放置项目的方式
`grid-auto-flow: row | column | dense | row dense | column dense;`
```css
/* 按列填充 */
.container { display: grid; grid-auto-flow: column; }

/* 按行填充并尝试填充空白区域 */
.container { display: grid; grid-auto-flow: row dense; }
```

---

## 项目属性

**grid-column-start / grid-column-end**：定义项目的列线
`grid-column-start: <数值>; grid-column-end: <数值>;`
```css
/* 项目从第 1 列线开始 */
.item { grid-column-start: 1; }

/* 项目到第 3 列线结束 */
.item { grid-column-end: 3; }

/* 跨越 2 列 */
.item { grid-column-end: span 2; }
```

---

**grid-column**：grid-column-start 和 grid-column-end 的复合属性
`grid-column: <起始> / <结束>;`
```css
/* 从第 1 列线到第 3 列线 */
.item { grid-column: 1 / 3; }

/* 从第 2 列线开始，跨越 2 列 */
.item { grid-column: 2 / span 2; }
```

---

**grid-row**：grid-row-start 和 grid-row-end 的复合属性
`grid-row: <起始> / <结束>;`
```css
/* 从第 1 行线到第 3 行线 */
.item { grid-row: 1 / 3; }

/* 从第 2 行线开始，跨越 2 行 */
.item { grid-row: 2 / span 2; }
```

---

**grid-area**：定义项目的区域
`grid-area: <名称> | <行起始> / <列起始> / <行结束> / <列结束>;`
```css
/* 使用命名的区域 */
.item { grid-area: header; }

/* 使用行/列的起始和结束线 */
.item { grid-area: 1 / 1 / 3 / 4; }
```

---

**justify-self / align-self**：单个项目的对齐
`justify-self: <值>; | align-self: <值>;`
```css
/* 单个项目在列轴上居中对齐 */
.item { justify-self: center; }

/* 单个项目在行轴上居中对齐 */
.item { align-self: center; }

/* 复合属性 */
.item { place-self: center; }
.item { place-self: start end; }
```

---

## 响应式 Grid

**媒体查询调整**：响应式网格
`@media (max-width: <宽度>) { <选择器> { grid-template-columns: <值>; } }`
```css
/* 基础布局 */
.container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .container { grid-template-columns: repeat(3, 1fr); }
}
@media (max-width: 992px) {
  .container { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 768px) {
  .container { grid-template-columns: 1fr; }
}
```

---

## 实战布局

**网站布局**：使用 grid-template-areas
`grid-template-areas: '<区域>';`
```css
.page {
  display: grid;
  grid-template-areas:
    'header header header'
    'sidebar main main'
    'footer footer footer';
  grid-template-columns: 200px 1fr 1fr;
  grid-template-rows: 60px 1fr 60px;
  height: 100vh;
  gap: 10px;
}
.header { grid-area: header; }
.sidebar { grid-area: sidebar; }
.main { grid-area: main; }
.footer { grid-area: footer; }

/* 移动端调整 */
@media (max-width: 768px) {
  .page {
    grid-template-areas:
      'header'
      'main'
      'sidebar'
      'footer';
    grid-template-columns: 1fr;
  }
}
```

---

**卡片网格**：自适应卡片布局
`grid-template-columns: repeat(auto-fill, minmax(<最小宽度>, 1fr));`
```css
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}
.card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}
```

---

**仪表盘布局**：跨列项目
`grid-column: span <数值>;`
```css
.dashboard {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(2, 200px);
  gap: 20px;
}
.dashboard__widget--large {
  grid-column: span 2;
}
```
