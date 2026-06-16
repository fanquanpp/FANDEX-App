
## 1. 定义 Vue Web Component

```javascript
import { defineCustomElement } from 'vue';

const MyVueElement = defineCustomElement({
  props: { message: String },
  template: '<span>{{ message }}</span>',
  styles: [
    `
    span { color: red; }
  `,
  ],
});

customElements.define('my-vue-element', MyVueElement);
```

## 2. 事件

```javascript
const MyElement = defineCustomElement({
  emits: ['change'],
  template: `
    <button @click="$emit('change', value)">
      Click
    </button>
  `,
});
```

## 3. 在 Vue 中使用 Web Components

```javascript
// vite.config.js
export default {
  compilerOptions: {
    isCustomElement: (tag) => tag.startsWith('my-'),
  },
};
```

```vue
<template>
  <my-custom-element :data="myData" @change="onChange"></my-custom-element>
</template>
```

## 4. Shadow DOM 与样式

```javascript
// Vue Web Components 使用 Shadow DOM
// 样式隔离，不受外部 CSS 影响
// 可以使用 CSS 自定义属性穿透
```
