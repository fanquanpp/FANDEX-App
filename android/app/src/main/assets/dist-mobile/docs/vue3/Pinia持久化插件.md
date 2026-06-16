
## 1. 安装与配置

```bash
npm install pinia-plugin-persistedstate
```

```javascript
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
```

## 2. 基本用法

```javascript
export const useUserStore = defineStore('user', {
  state: () => ({
    name: '',
    token: '',
  }),
  persist: true, // 启用持久化
});
```

## 3. 高级配置

```javascript
export const useUserStore = defineStore('user', {
  state: () => ({
    name: '',
    token: '',
    preferences: { theme: 'light' },
  }),
  persist: {
    key: 'my-user-store', // 存储键名
    storage: sessionStorage, // 存储方式
    pick: ['token', 'preferences'], // 只持久化部分字段
    omit: ['name'], // 排除部分字段
    beforeHydrate: (ctx) => {
      // 恢复前处理
      console.log('about to hydrate', ctx);
    },
    afterHydrate: (ctx) => {
      // 恢复后处理
      console.log('hydrated', ctx);
    },
  },
});
```

## 4. 自定义存储

```javascript
persist: {
  storage: {
    getItem: (key) => {
      return cookies.get(key);
    },
    setItem: (key, value) => {
      cookies.set(key, value, { expires: 7 });
    },
    removeItem: (key) => {
      cookies.remove(key);
    }
  }
}
```

## 5. Setup Store 语法

```javascript
export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref('');
    const name = ref('');

    return { token, name };
  },
  {
    persist: {
      pick: ['token'],
    },
  }
);
```
