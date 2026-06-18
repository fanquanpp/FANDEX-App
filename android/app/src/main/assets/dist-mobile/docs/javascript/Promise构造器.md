# JavaScript Promise 构造器

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## Promise 创建

**基本创建**：使用 new Promise
`const <变量名> = new Promise((<resolve>, <reject>) => { ... });`
```javascript
const promise = new Promise((resolve, reject) => {
  // 异步操作
  const success = true;
  if (success) {
    resolve('操作成功');
  } else {
    reject('操作失败');
  }
});
```

---

**带参数的 Promise**：传递参数给执行器
`new Promise((<resolve>, <reject>) => { <使用参数> })`
```javascript
function createPromise(value, shouldResolve) {
  return new Promise((resolve, reject) => {
    if (shouldResolve) {
      resolve(value);
    } else {
      reject(new Error(`Failed with value: ${value}`));
    }
  });
}
```

---

## Promise 状态

**pending 状态**：初始状态
`new Promise(() => {})`
```javascript
const pendingPromise = new Promise(() => {
  // 不调用 resolve 或 reject，Promise 永远处于 pending 状态
});
console.log(pendingPromise);  // Promise { <pending> }
```

---

**fulfilled 状态**：操作成功
`Promise.resolve(<值>)`
```javascript
const fulfilledPromise = Promise.resolve('成功');
console.log(fulfilledPromise);  // Promise { '成功' }
```

---

**rejected 状态**：操作失败
`Promise.reject(<原因>)`
```javascript
const rejectedPromise = Promise.reject('失败');
console.log(rejectedPromise);  // Promise { <rejected> '失败' }
```

---

## resolve 调用

**resolve 基本用法**：传递成功值
`resolve(<值>)`
```javascript
const promise = new Promise((resolve) => {
  setTimeout(() => {
    resolve({ name: '张三', age: 30 });
  }, 1000);
});
promise.then((data) => {
  console.log(data);  // { name: '张三', age: 30 }
});
```

---

**resolve 另一个 Promise**：Promise 链式传递
`resolve(<另一个Promise>)`
```javascript
const promise1 = Promise.resolve('第一个');
const promise2 = new Promise((resolve) => {
  resolve(promise1);  // resolve 一个 Promise
});
promise2.then((result) => {
  console.log(result);  // '第一个'
});
```

---

**resolve thenable 对象**：转换 thenable 为 Promise
`resolve(<thenable对象>)`
```javascript
const thenable = {
  then(resolve, reject) {
    resolve('thenable 结果');
  },
};
const promise = Promise.resolve(thenable);
promise.then((result) => {
  console.log(result);  // 'thenable 结果'
});
```

---

## reject 调用

**reject 基本用法**：传递错误原因
`reject(<原因>)`
```javascript
const promise = new Promise((resolve, reject) => {
  setTimeout(() => {
    reject(new Error('操作失败'));
  }, 1000);
});
promise.catch((error) => {
  console.error(error.message);  // '操作失败'
});
```

---

**reject 传递错误对象**：推荐使用 Error 对象
`reject(new Error(<消息>))`
```javascript
function validateAge(age) {
  return new Promise((resolve, reject) => {
    if (age < 0) {
      reject(new Error('年龄不能为负数'));
    } else if (age > 150) {
      reject(new Error('年龄不能超过 150'));
    } else {
      resolve(age);
    }
  });
}
```

---

## 执行器函数

**执行器同步执行**：Promise 构造器立即执行
`new Promise((resolve, reject) => { <同步代码> })`
```javascript
console.log('1. 开始');
const promise = new Promise((resolve) => {
  console.log('2. 执行器同步执行');
  resolve('完成');
});
console.log('3. Promise 创建完成');
promise.then((result) => {
  console.log('4. ' + result);
});
// 输出顺序: 1 -> 2 -> 3 -> 4
```

---

**执行器异步操作**：在异步回调中调用 resolve
`new Promise((resolve, reject) => { setTimeout(() => resolve(<值>), <延迟>); })`
```javascript
function delay(value, ms) {
  return new Promise((resolve) => {
    setTimeout(() => resolve(value), ms);
  });
}
delay('延迟结果', 1000).then((result) => {
  console.log(result);  // 1秒后输出: 延迟结果
});
```

---

## Promise 包装回调函数

**Promise 包装 setTimeout**：将回调转为 Promise
`new Promise((resolve) => setTimeout(resolve, <延迟>))`
```javascript
function delay(ms) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
}
delay(1000).then(() => {
  console.log('1秒后执行');
});
```

---

**Promise 包装事件监听**：将事件转为 Promise
`new Promise((resolve) => { <元素>.addEventListener(<事件>, resolve, { once: true }); })`
```javascript
function waitForEvent(element, eventName) {
  return new Promise((resolve) => {
    element.addEventListener(eventName, resolve, { once: true });
  });
}
// 使用
const button = document.querySelector('button');
waitForEvent(button, 'click').then((event) => {
  console.log('按钮被点击', event);
});
```

---

**Promise 包装 XMLHttpRequest**：将 XHR 转为 Promise
`new Promise((resolve, reject) => { <XHR操作> })`
```javascript
function fetchJSON(url) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url);
    xhr.onload = function () {
      if (xhr.status === 200) {
        try {
          resolve(JSON.parse(xhr.responseText));
        } catch (e) {
          reject(e);
        }
      } else {
        reject(new Error(xhr.statusText));
      }
    };
    xhr.onerror = function () {
      reject(new Error('网络错误'));
    };
    xhr.send();
  });
}
```

---

## Promise.withResolvers

**传统写法**：手动提取 resolve/reject
`let resolve, reject; const promise = new Promise((res, rej) => { resolve = res; reject = rej; });`
```javascript
let resolve, reject;
const promise = new Promise((res, rej) => {
  resolve = res;
  reject = rej;
});
// 在外部使用
resolve('done');
```

---

**withResolvers 写法**：ES2024+，一步到位
`const { promise, resolve, reject } = Promise.withResolvers();`
```javascript
const { promise, resolve, reject } = Promise.withResolvers();
// resolve 和 reject 可以在 Promise 外部使用
setTimeout(() => resolve('done'), 1000);
const result = await promise;  // 'done'
```

---

**事件包装**：使用 withResolvers
`const { promise, resolve } = Promise.withResolvers();`
```javascript
function waitForEvent(element, eventName) {
  const { promise, resolve } = Promise.withResolvers();
  element.addEventListener(eventName, resolve, { once: true });
  return promise;
}
// 使用
const click = await waitForEvent(button, 'click');
```

---

**超时控制**：使用 withResolvers
`const { promise: timeoutPromise, resolve } = Promise.withResolvers();`
```javascript
function withTimeout(promise, ms) {
  const { promise: timeoutPromise, resolve } = Promise.withResolvers();
  const timer = setTimeout(() => resolve('timeout'), ms);
  return Promise.race([promise.finally(() => clearTimeout(timer)), timeoutPromise]);
}
```

---

**一次性信号**：使用 withResolvers 实现信号
`class Signal { constructor() { const { promise, resolve } = Promise.withResolvers(); ... } }`
```javascript
class Signal {
  #promise;
  #resolve;
  constructor() {
    const { promise, resolve } = Promise.withResolvers();
    this.#promise = promise;
    this.#resolve = resolve;
  }
  wait() {
    return this.#promise;
  }
  emit(value) {
    this.#resolve(value);
  }
}
const ready = new Signal();
ready.emit('data');
const data = await ready.wait();  // 'data'
```

---

## Promise 缓存

**缓存 Promise**：避免重复请求
`const cache = new Map();`
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

## 可取消的异步操作

**可取消任务**：使用 withResolvers
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
