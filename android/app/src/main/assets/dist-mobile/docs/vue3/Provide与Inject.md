
## 1. 基本用法

```javascript
// 父组件 - provide
import { provide, ref } from 'vue';

const theme = ref('dark');
provide('theme', theme);
provide('toggleTheme', () => (theme.value = theme.value === 'dark' ? 'light' : 'dark'));

// 子组件 - inject
import { inject } from 'vue';

const theme = inject('theme', 'light'); // 第二个参数是默认值
const toggleTheme = inject('toggleTheme');
```

## 2. 类型安全的 Provide/Inject

```typescript
import type { InjectionKey, Ref } from 'vue';

// 定义注入键
export const ThemeKey: InjectionKey<Ref<string>> = Symbol('theme');
export const ToggleThemeKey: InjectionKey<() => void> = Symbol('toggleTheme');

// provide
provide(ThemeKey, theme);
provide(ToggleThemeKey, toggleTheme);

// inject — 自动推断类型
const theme = inject(ThemeKey); // Ref<string> | undefined
```

## 3. 使用 Symbol 避免冲突

```typescript
// keys.ts
export const UserKey: InjectionKey<User> = Symbol('user');
export const ConfigKey: InjectionKey<AppConfig> = Symbol('config');
```

## 4. 响应式注入

```javascript
// 提供响应式数据
const state = reactive({ count: 0 });
provide('state', state);

// 只读注入
provide('readonlyState', readonly(state));

// 提供修改方法
provide('increment', () => state.count++);
```

## 5. 默认值

```javascript
// 静态默认值
const theme = inject('theme', 'light');

// 工厂函数默认值
const config = inject('config', () => createDefaultConfig(), true);
```
