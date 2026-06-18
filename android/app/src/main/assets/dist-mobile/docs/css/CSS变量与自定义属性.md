# CSS 变量与自定义属性

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 自定义属性声明

**声明自定义属性**：使用 -- 前缀定义变量
`--<变量名>: <值>;`
```css
/* 声明自定义属性 */
:root {
  --primary-color: #3498db;
  --secondary-color: #2ecc71;
  --font-size-base: 16px;
  --spacing-unit: 8px;
  --border-radius: 4px;
  --transition-speed: 0.3s;
}

/* 使用自定义属性 */
.button {
  background-color: var(--primary-color);
  font-size: var(--font-size-base);
  padding: var(--spacing-unit) calc(var(--spacing-unit) * 2);
  border-radius: var(--border-radius);
  transition: background-color var(--transition-speed) ease;
}
```

---

**命名规范**：语义化命名
`--<分类>-<名称>: <值>;`
```css
:root {
  /* 推荐：语义化命名 */
  --color-primary: #3498db;
  --color-secondary: #2ecc71;
  --color-text: #333;
  --color-danger: #e74c3c;
  --color-success: #2ecc71;

  /* 间距 */
  --spacing-small: 8px;
  --spacing-medium: 16px;
  --spacing-large: 24px;

  /* 字体 */
  --font-size-base: 1rem;
  --font-size-large: 1.25rem;
}
```

---

## var() 函数

**基本用法**：引用变量值
`var(--<变量名>)`
```css
.element {
  color: var(--color-text);
  background-color: var(--color-primary);
  padding: var(--spacing-medium);
}
```

---

**默认值**：变量未定义时使用默认值
`var(--<变量名>, <默认值>)`
```css
.element {
  /* 使用默认值 */
  color: var(--text-color, #333);
  font-size: var(--font-size, 16px);

  /* 默认值可以是另一个变量 */
  background: var(--bg-color, var(--default-bg, white));

  /* 默认值可以包含多个值 */
  margin: var(--margin, 10px 20px);
}
```

---

**calc() 中使用**：在计算中使用变量
`calc(var(--<变量>) * <数值>)`
```css
:root {
  --spacing: 8px;
}

.element {
  /* calc() 中使用变量 */
  padding: calc(var(--spacing) * 2);
  margin: calc(var(--spacing) * 3);
  width: calc(100% - var(--spacing) * 4);
}
```

---

## 作用域与层叠

**全局作用域**：在 :root 中定义
`:root { --<变量>: <值>; }`
```css
/* 全局作用域 */
:root {
  --main-color: #3498db;
  --padding: 16px;
}

/* 所有元素可访问 */
.element {
  color: var(--main-color);
  padding: var(--padding);
}
```

---

**局部作用域**：在特定选择器中定义
`<选择器> { --<变量>: <值>; }`
```css
/* 局部作用域 */
.card {
  --card-bg: white;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: var(--card-bg);
  box-shadow: var(--card-shadow);
}

/* 子元素继承父元素的自定义属性 */
.card-header {
  color: var(--main-color);
  padding: var(--padding);
}
```

---

**覆盖父级变量**：在子元素中重新定义
`<子选择器> { --<变量>: <新值>; }`
```css
:root {
  --theme-color: blue;
}

.container {
  --theme-color: green;
}

.container .element {
  color: var(--theme-color); /* green */
}

.element-outside {
  color: var(--theme-color); /* blue */
}

/* 优先级规则与普通 CSS 属性相同 */
#special {
  --theme-color: red;
}
```

---

## 动态主题系统

**亮色/暗色主题**：使用 data 属性切换
`[data-theme="dark"] { --<变量>: <值>; }`
```css
/* 亮色主题 */
:root {
  --color-bg-primary: #ffffff;
  --color-bg-secondary: #f5f5f5;
  --color-text-primary: #333333;
  --color-text-secondary: #666666;
  --color-border: #e0e0e0;
  --color-accent: #3498db;
  --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.08);
  --shadow-md: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 暗色主题 */
[data-theme='dark'] {
  --color-bg-primary: #1a1a2e;
  --color-bg-secondary: #16213e;
  --color-text-primary: #e0e0e0;
  --color-text-secondary: #a0a0a0;
  --color-border: #333355;
  --color-accent: #5dade2;
  --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.3);
  --shadow-md: 0 4px 12px rgba(0, 0, 0, 0.4);
}

/* 应用主题变量 */
body {
  background-color: var(--color-bg-primary);
  color: var(--color-text-primary);
  transition: background-color 0.3s ease, color 0.3s ease;
}
```

---

**系统偏好检测**：跟随系统主题
`@media (prefers-color-scheme: dark) { :root:not([data-theme]) { <样式> } }`
```css
/* 系统偏好检测 */
@media (prefers-color-scheme: dark) {
  :root:not([data-theme]) {
    --color-bg-primary: #1a1a2e;
    --color-text-primary: #e0e0e0;
    --color-accent: #5dade2;
  }
}
```

---

**品牌主题系统**：多品牌切换
`[data-brand="<品牌>"] { --<变量>: <值>; }`
```css
:root {
  --brand-primary: #3498db;
  --brand-secondary: #2ecc71;
  --brand-gradient: linear-gradient(135deg, var(--brand-primary), var(--brand-secondary));
}

[data-brand='brand-a'] {
  --brand-primary: #e74c3c;
  --brand-secondary: #f39c12;
}

[data-brand='brand-b'] {
  --brand-primary: #9b59b6;
  --brand-secondary: #1abc9c;
}

.brand-button {
  background: var(--brand-gradient);
  color: white;
}
```

---

## 响应式变量

**断点变量**：在媒体查询中修改变量
`@media (min-width: <宽度>) { :root { --<变量>: <值>; } }`
```css
:root {
  --container-padding: 16px;
  --section-spacing: 32px;
}

@media (min-width: 768px) {
  :root {
    --container-padding: 24px;
    --section-spacing: 48px;
  }
}

@media (min-width: 1024px) {
  :root {
    --container-padding: 32px;
    --section-spacing: 64px;
  }
}

.container {
  padding: 0 var(--container-padding);
}

section {
  margin-bottom: var(--section-spacing);
}
```

---

**流式排版变量**：使用 clamp 实现流式字体
`--<变量>: clamp(<最小值>, <理想值>, <最大值>);`
```css
:root {
  --font-size-base: clamp(1rem, 0.875rem + 0.5vw, 1.25rem);
  --font-size-sm: clamp(0.875rem, 0.75rem + 0.5vw, 1rem);
  --font-size-lg: clamp(1.25rem, 1rem + 1vw, 1.75rem);
  --font-size-xl: clamp(1.75rem, 1.25rem + 2vw, 3rem);
}

body {
  font-size: var(--font-size-base);
}

h1 {
  font-size: var(--font-size-xl);
}

small {
  font-size: var(--font-size-sm);
}
```

---

## JavaScript 操作

**读写自定义属性**：使用 getComputedStyle 和 setProperty
`getComputedStyle(<元素>).getPropertyValue('--<变量>') | <元素>.style.setProperty('--<变量>', <值>)`
```javascript
// 读取自定义属性
const root = document.documentElement;
const primaryColor = getComputedStyle(root).getPropertyValue('--color-primary');

// 设置自定义属性
root.style.setProperty('--color-primary', '#e74c3c');

// 在特定元素上设置
const card = document.querySelector('.card');
card.style.setProperty('--card-bg', '#f0f0f0');

// 移除自定义属性
card.style.removeProperty('--card-bg');
```

---

**动态样式更新**：基于用户交互更新变量
`document.documentElement.style.setProperty('--<变量>', <值>)`
```javascript
// 根据用户输入动态更新主题色
function updateAccentColor(hex) {
  document.documentElement.style.setProperty('--color-accent', hex);
}

// 基于滚动位置更新变量
window.addEventListener('scroll', () => {
  const scrollPercent = window.scrollY / (document.body.scrollHeight - window.innerHeight);
  document.documentElement.style.setProperty('--scroll-progress', scrollPercent);
});

// 鼠标位置跟踪
document.addEventListener('mousemove', (e) => {
  const x = e.clientX / window.innerWidth;
  const y = e.clientY / window.innerHeight;
  document.documentElement.style.setProperty('--mouse-x', x);
  document.documentElement.style.setProperty('--mouse-y', y);
});
```

---

## 常见问题处理

**变量未定义回退**：提供默认值
`var(--<变量>, <默认值>)`
```css
/* 提供默认值 */
.element {
  color: var(--undefined-var, #333);
}

/* 使用 @supports 检测 */
@supports (--css: variables) {
  .element {
    color: var(--text-color);
  }
}
@supports not (--css: variables) {
  .element {
    color: #333;
  }
}
```

---

**避免循环依赖**：单向依赖
`--<变量A>: var(--<变量B>);`
```css
/* 错误：循环引用 */
:root {
  --a: var(--b);
  --b: var(--a); /* 无限循环 */
}

/* 正确：单向依赖 */
:root {
  --a: blue;
  --b: var(--a);
}
```

---

**变量与单位**：变量值包含单位
`--<变量>: <数值><单位>;`
```css
/* 推荐：变量直接包含单位 */
:root {
  --size: 20px;
}
.element {
  width: var(--size);
}

/* 无单位变量需配合 calc */
:root {
  --size: 20;
}
.element {
  width: calc(var(--size) * 1px);
}
```
