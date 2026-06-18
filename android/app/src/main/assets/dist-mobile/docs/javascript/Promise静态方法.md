# JavaScript Promise 静态方法

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## Promise.all

**全部成功**：等待所有 Promise 完成
`Promise.all([<promise1>, <promise2>, ...])`
```javascript
const [users, posts, comments] = await Promise.all([
  fetchUsers(),
  fetchPosts(),
  fetchComments(),
]);
// 所有 Promise 成功 -> 返回结果数组
// 任一 Promise 失败 -> 立即拒绝，返回第一个错误
```

---

**错误处理**：捕获第一个失败
`try { await Promise.all([...]) } catch (<错误>) { <处理> }`
```javascript
try {
  const results = await Promise.all([
    fetch('/api/a'),
    fetch('/api/b'),
    fetch('/api/c'),
  ]);
} catch (error) {
  // 只能捕获第一个失败，其他请求的结果丢失
  console.error(error);
}
```

---

## Promise.allSettled

**全部完成**：等待所有 Promise 完成（无论成功或失败）
`Promise.allSettled([<promise1>, <promise2>, ...])`
```javascript
const results = await Promise.allSettled([
  fetch('/api/a'),
  fetch('/api/b'),
  fetch('/api/c'),
]);
results.forEach((result) => {
  if (result.status === 'fulfilled') {
    console.log('成功:', result.value);
  } else {
    console.log('失败:', result.reason);
  }
});
```

---

**结果格式**：成功和失败的对象结构
`{ status: 'fulfilled', value: <值> }` | `{ status: 'rejected', reason: <原因> }`
```javascript
// 成功结果
{ status: 'fulfilled', value: any }

// 失败结果
{ status: 'rejected', reason: any }
```

---

**分离成功和失败**：使用工具函数
`function partitionResults(<结果数组>) { ... }`
```javascript
function partitionResults(results) {
  const fulfilled = results.filter((r) => r.status === 'fulfilled').map((r) => r.value);
  const rejected = results.filter((r) => r.status === 'rejected').map((r) => r.reason);
  return { fulfilled, rejected };
}

// 批量操作，收集所有结果
async function batchDelete(ids) {
  const results = await Promise.allSettled(
    ids.map((id) => fetch(`/api/items/${id}`, { method: 'DELETE' }))
  );
  const { fulfilled, rejected } = partitionResults(results);
  console.log(`成功: ${fulfilled.length}, 失败: ${rejected.length}`);
  return { fulfilled, rejected };
}
```

---

## Promise.any

**任一成功**：返回第一个成功的 Promise
`Promise.any([<promise1>, <promise2>, ...])`
```javascript
// 多源竞速，取最快成功的
const fastest = await Promise.any([
  fetchFromCDN1('/api/data'),
  fetchFromCDN2('/api/data'),
  fetchFromCDN3('/api/data'),
]);
// 任一 Promise 成功 -> 立即返回该结果
// 所有 Promise 失败 -> 抛出 AggregateError
```

---

**AggregateError**：所有 Promise 失败时抛出
`catch (<aggregateError>) { <处理> }`
```javascript
try {
  const result = await Promise.any([
    Promise.reject(new Error('CDN1 failed')),
    Promise.reject(new Error('CDN2 failed')),
    Promise.reject(new Error('CDN3 failed')),
  ]);
} catch (aggregateError) {
  console.log(aggregateError instanceof AggregateError);  // true
  console.log(aggregateError.errors);  // [Error, Error, Error]
  aggregateError.errors.forEach((err) => console.error(err.message));
}
```

---

**与 Promise.race 的区别**：成功条件不同
`Promise.race([...])` | `Promise.any([...])`
```javascript
// race: 第一个拒绝就拒绝
const r1 = await Promise.race([
  Promise.reject(new Error('fast fail')),
  new Promise((resolve) => setTimeout(() => resolve('slow success'), 100)),
]);
// 抛出 Error: fast fail

// any: 忽略拒绝，等第一个成功
const r2 = await Promise.any([
  Promise.reject(new Error('fast fail')),
  new Promise((resolve) => setTimeout(() => resolve('slow success'), 100)),
]);
// 'slow success'
```

---

## Promise.race

**第一个完成**：返回第一个完成的 Promise（无论成功或失败）
`Promise.race([<promise1>, <promise2>, ...])`
```javascript
const promise1 = new Promise((resolve) => setTimeout(() => resolve('第一个'), 1000));
const promise2 = new Promise((resolve) => setTimeout(() => resolve('第二个'), 500));
Promise.race([promise1, promise2]).then((result) => {
  console.log('第一个完成的 Promise:', result);  // '第二个'
});
```

---

## Promise.resolve

**返回已解决的 Promise**：快速创建成功 Promise
`Promise.resolve(<值>)`
```javascript
const promise = Promise.resolve('成功');
promise.then((result) => {
  console.log(result);  // '成功'
});
```

---

**resolve thenable**：转换 thenable 对象
`Promise.resolve(<thenable>)`
```javascript
const thenable = {
  then(resolve) {
    resolve('thenable 结果');
  },
};
Promise.resolve(thenable).then((result) => {
  console.log(result);  // 'thenable 结果'
});
```

---

## Promise.reject

**返回已拒绝的 Promise**：快速创建失败 Promise
`Promise.reject(<原因>)`
```javascript
const promise = Promise.reject('失败');
promise.catch((error) => {
  console.log(error);  // '失败'
});
```

---

## Promise.withResolvers

**withResolvers 用法**：ES2024+，一步到位
`const { promise, resolve, reject } = Promise.withResolvers();`
```javascript
const { promise, resolve, reject } = Promise.withResolvers();
// resolve 和 reject 可以在 Promise 外部使用
setTimeout(() => resolve('done'), 1000);
const result = await promise;  // 'done'
```

---

**缓存 Promise**：使用 withResolvers
`const { promise, resolve, reject } = Promise.withResolvers();`
```javascript
function createCachedFetcher() {
  const cache = new Map();
  return function fetchWithCache(url) {
    if (cache.has(url)) {
      return cache.get(url).promise;
    }
    const { promise, resolve, reject } = Promise.withResolvers();
    cache.set(url, { promise });
    fetch(url)
      .then((res) => res.json())
      .then(resolve)
      .catch(reject);
    return promise;
  };
}
```

---

**事件转 Promise**：使用 withResolvers
`const { promise, resolve } = Promise.withResolvers();`
```javascript
function waitForEvent(emitter, eventName) {
  const { promise, resolve } = Promise.withResolvers();
  emitter.once(eventName, resolve);
  setTimeout(() => resolve(null), 5000);  // 添加超时
  return promise;
}
// 使用
const data = await waitForEvent(socket, 'message');
```

---

**可取消的异步操作**：使用 withResolvers
`function createCancellableTask(<异步函数>) { ... }`
```javascript
function createCancellableTask(asyncFn) {
  const { promise, resolve, reject } = Promise.withResolvers();
  let cancelled = false;
  asyncFn()
    .then((result) => {
      if (!cancelled) resolve(result);
    })
    .catch((error) => {
      if (!cancelled) reject(error);
    });
  return {
    promise,
    cancel() {
      cancelled = true;
      reject(new Error('Task cancelled'));
    },
  };
}
```

---

## 组合使用

**带超时的请求**：使用 Promise.race
`Promise.race([<promise>, <超时promise>])`
```javascript
function fetchWithTimeout(url, timeout = 5000) {
  return Promise.race([
    fetch(url),
    new Promise((_, reject) =>
      setTimeout(() => reject(new Error('Timeout')), timeout)
    ),
  ]);
}
```

---

**批量重试**：失败后重试
`async function fetchWithRetry(<URL>, <重试次数>) { ... }`
```javascript
async function fetchWithRetry(url, retries = 3) {
  for (let i = 0; i < retries; i++) {
    try {
      return await fetch(url);
    } catch (error) {
      if (i === retries - 1) throw error;
      await new Promise((r) => setTimeout(r, 1000 * Math.pow(2, i)));
    }
  }
}
```

---

## 方法对比

**方法对比表**：不同静态方法的差异
| 方法 | 成功条件 | 失败条件 | 返回值 |
|------|----------|----------|--------|
| `Promise.all` | 全部成功 | 任一失败 | 结果数组 |
| `Promise.allSettled` | 永远成功 | 不会失败 | 状态对象数组 |
| `Promise.race` | 第一个完成 | 第一个完成（可能拒绝） | 第一个结果 |
| `Promise.any` | 第一个成功 | 全部失败 | 第一个成功值 |
| `Promise.withResolvers` | 手动控制 | 手动控制 | { promise, resolve, reject } |
```javascript
// Promise.all - 全部成功才成功
Promise.all([p1, p2, p3]).then(([r1, r2, r3]) => { ... });

// Promise.allSettled - 等待全部完成
Promise.allSettled([p1, p2, p3]).then((results) => { ... });

// Promise.race - 第一个完成
Promise.race([p1, p2, p3]).then((first) => { ... });

// Promise.any - 第一个成功
Promise.any([p1, p2, p3]).then((first) => { ... });
```
