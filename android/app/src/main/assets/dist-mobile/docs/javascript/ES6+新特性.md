# JavaScript ES6+ 新特性

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 解构赋值

**数组解构**：从数组提取值
`const [<变量1>, <变量2>] = <数组>;`
```javascript
const [a, b, c] = [1, 2, 3];
console.log(a, b, c);  // 1 2 3

// 跳过元素
const [first, , third] = [1, 2, 3];
console.log(first, third);  // 1 3

// 默认值
const [x, y, z = 10] = [1, 2];
console.log(x, y, z);  // 1 2 10

// 交换变量
let m = 1, n = 2;
[m, n] = [n, m];
console.log(m, n);  // 2 1

// 剩余元素
const [head, ...tail] = [1, 2, 3, 4, 5];
console.log(head, tail);  // 1 [2, 3, 4, 5]
```

---

**对象解构**：从对象提取属性
`const { <属性1>, <属性2> } = <对象>;`
```javascript
const { name, age } = { name: 'Alice', age: 25 };
console.log(name, age);  // Alice 25

// 重命名
const { name: userName, age: userAge } = { name: 'Alice', age: 25 };
console.log(userName, userAge);  // Alice 25

// 默认值
const { x = 1, y = 2 } = { x: 10 };
console.log(x, y);  // 10 2

// 嵌套解构
const user = {
  profile: { name: 'Alice', address: { city: 'Beijing' } },
};
const { profile: { name, address: { city } } } = user;
console.log(name, city);  // Alice Beijing

// 剩余属性
const { a: pa, ...rest } = { a: 1, b: 2, c: 3 };
console.log(pa, rest);  // 1 { b: 2, c: 3 }
```

---

**函数参数解构**：解构函数参数
`function <函数名>({ <属性1>, <属性2> = <默认值> }) { ... }`
```javascript
function greet({ name, age = 0, greeting = 'Hello' }) {
  console.log(`${greeting}, ${name}! You are ${age} years old.`);
}
greet({ name: 'Alice', age: 25 });  // Hello, Alice! You are 25 years old.
```

---

## 展开运算符

**数组展开**：展开数组元素
`[...<数组1>, ...<数组2>]`
```javascript
// 合并数组
const merged = [...[1, 2, 3], ...[4, 5, 6]];
console.log(merged);  // [1, 2, 3, 4, 5, 6]

// 复制数组
const original = [1, 2, 3];
const copy = [...original];

// 数组插入
const result = [1, 2, ...[3, 4], 5, 6];
console.log(result);  // [1, 2, 3, 4, 5, 6]

// Math 方法
console.log(Math.max(...[5, 2, 8, 1, 9]));  // 9
```

---

**对象展开**：展开对象属性
`{ ...<对象1>, ...<对象2> }`
```javascript
// 合并对象（后者覆盖前者）
const defaults = { theme: 'light', fontSize: 14, lang: 'zh' };
const userPrefs = { theme: 'dark', fontSize: 16 };
const config = { ...defaults, ...userPrefs };
console.log(config);  // { theme: 'dark', fontSize: 16, lang: 'zh' }

// 添加/覆盖属性
const user = { name: 'Alice', age: 25 };
const updated = { ...user, age: 26, email: 'alice@example.com' };

// 移除属性
const { removed, ...withoutRemoved } = { a: 1, removed: 2, b: 3 };
console.log(withoutRemoved);  // { a: 1, b: 3 }
```

---

## Symbol

**Symbol 创建**：创建唯一标识符
`const <变量名> = Symbol([<描述>]);`
```javascript
const sym1 = Symbol();
const sym2 = Symbol('description');
console.log(Symbol('foo') === Symbol('foo'));  // false，每个 Symbol 唯一

// 作为对象属性键
const id = Symbol('id');
const user = { name: 'Alice', [id]: 12345 };
console.log(user[id]);  // 12345

// Symbol 属性不出现在常规遍历中
console.log(Object.keys(user));  // ['name']
console.log(Object.getOwnPropertySymbols(user));  // [Symbol(id)]
```

---

**全局 Symbol**：使用 Symbol.for
`Symbol.for(<键>)` | `Symbol.keyFor(<symbol>)`
```javascript
const globalSym1 = Symbol.for('app.id');
const globalSym2 = Symbol.for('app.id');
console.log(globalSym1 === globalSym2);  // true
console.log(Symbol.keyFor(globalSym1));  // "app.id"
```

---

**内置 Symbol**：自定义迭代行为
`[Symbol.iterator]() { ... }`
```javascript
const range = {
  from: 1,
  to: 5,
  [Symbol.iterator]() {
    let current = this.from;
    const last = this.to;
    return {
      next() {
        return current <= last
          ? { value: current++, done: false }
          : { done: true };
      },
    };
  },
};
for (const num of range) {
  console.log(num);  // 1, 2, 3, 4, 5
}
```

---

**Symbol.toPrimitive**：自定义类型转换
`[Symbol.toPrimitive](<hint>) { ... }`
```javascript
const temperature = {
  celsius: 25,
  [Symbol.toPrimitive](hint) {
    if (hint === 'number') return this.celsius;
    if (hint === 'string') return `${this.celsius}°C`;
    return this.celsius;
  },
};
console.log(+temperature);    // 25（number hint）
console.log(`${temperature}`);  // "25°C"（string hint）
console.log(temperature + 5);  // 30（default hint）
```

---

## Proxy 代理

**基本代理**：拦截对象操作
`new Proxy(<目标对象>, <处理器>)`
```javascript
const target = { name: 'Alice', age: 25 };
const handler = {
  get(obj, prop) {
    console.log(`读取属性: ${prop}`);
    return prop in obj ? obj[prop] : '属性不存在';
  },
  set(obj, prop, value) {
    console.log(`设置属性: ${prop} = ${value}`);
    if (prop === 'age' && (typeof value !== 'number' || value < 0)) {
      throw new TypeError('年龄必须是非负数');
    }
    obj[prop] = value;
    return true;
  },
};
const proxy = new Proxy(target, handler);
console.log(proxy.name);  // 读取属性: name → "Alice"
proxy.age = 30;           // 设置属性: age = 30
```

---

**验证代理**：属性验证
`new Proxy({}, { set(obj, prop, value) { ... } })`
```javascript
function validatedObject(schema) {
  return new Proxy({}, {
    set(obj, prop, value) {
      if (schema[prop]) {
        const { type, validator } = schema[prop];
        if (type && typeof value !== type) {
          throw new TypeError(`${prop} must be ${type}`);
        }
        if (validator && !validator(value)) {
          throw new TypeError(`${prop} validation failed`);
        }
      }
      obj[prop] = value;
      return true;
    },
  });
}
const user = validatedObject({
  name: { type: 'string' },
  age: { type: 'number', validator: (v) => v >= 0 && v <= 150 },
});
user.name = 'Alice';
user.age = 25;
```

---

**只读代理**：禁止修改
`new Proxy(<对象>, { set() { throw new Error(...); } })`
```javascript
function readOnly(obj) {
  return new Proxy(obj, {
    set() { throw new Error('只读对象，不可修改'); },
    deleteProperty() { throw new Error('只读对象，不可删除'); },
  });
}
const config = readOnly({ version: '1.0', debug: false });
// config.version = '2.0';  // Error: 只读对象
```

---

**缓存代理**：缓存函数结果
`new Proxy(<函数>, { apply(target, thisArg, args) { ... } })`
```javascript
function createCache(fn) {
  const cache = new Map();
  return new Proxy(fn, {
    apply(target, thisArg, args) {
      const key = JSON.stringify(args);
      if (cache.has(key)) {
        console.log('缓存命中');
        return cache.get(key);
      }
      const result = Reflect.apply(target, thisArg, args);
      cache.set(key, result);
      return result;
    },
  });
}
const expensiveCalc = createCache((n) => n * n);
expensiveCalc(5);  // 计算中... → 25
expensiveCalc(5);  // 缓存命中 → 25
```

---

## 可选链与空值合并

**可选链操作符**：安全访问嵌套属性
`<对象>?.<属性>`
```javascript
const user = { profile: { address: { city: 'Beijing' } } };

// 传统写法
const city1 = user && user.profile && user.profile.address && user.profile.address.city;

// 可选链
const city2 = user?.profile?.address?.city;  // "Beijing"
const zip = user?.profile?.address?.zip;      // undefined（不会报错）

// 函数调用
const obj = { method: () => 'result' };
console.log(obj.method?.());    // "result"
console.log(obj.nonExist?.());  // undefined

// 数组访问
const arr = null;
console.log(arr?.[0]);  // undefined
```

---

**空值合并操作符**：仅在 null/undefined 时使用默认值
`<值1> ?? <值2>`
```javascript
const value1 = null ?? 'default';       // "default"
const value2 = undefined ?? 'default'; // "default"
const value3 = 0 ?? 'default';          // 0（不是 null/undefined）
const value4 = '' ?? 'default';        // ""（不是 null/undefined）
const value5 = false ?? 'default';     // false

// 对比 ||
const value6 = 0 || 'default';    // "default"（0 是 falsy）
const value7 = '' || 'default';   // "default"（空字符串是 falsy）

// 配置合并
function createConfig(options) {
  return {
    timeout: options.timeout ?? 5000,  // 0 是有效值
    retries: options.retries ?? 3,
    debug: options.debug ?? false,      // false 是有效值
    prefix: options.prefix ?? 'app',    // '' 是有效值
  };
}
```

---

## 逻辑赋值运算符

**逻辑赋值**：ES2021+，运算并赋值
`<变量> <&&=|| ||=|??=> <值>`
```javascript
let a = null;
a ??= 'default';  // a = a ?? 'default' → "default"

let b = 0;
b ||= 10;  // b = b || 10 → 10

let c = { x: 1 };
c &&= c.x;  // c = c && c.x → 1
```

---

## Object 新方法

**Object.keys/values/entries**：获取对象键值
`Object.keys(<对象>)` | `Object.values(<对象>)` | `Object.entries(<对象>)`
```javascript
const obj = { a: 1, b: 2, c: 3 };
console.log(Object.keys(obj));     // ['a', 'b', 'c']
console.log(Object.values(obj)); // [1, 2, 3]
console.log(Object.entries(obj)); // [['a',1], ['b',2], ['c',3]]
```

---

**Object.fromEntries**：从键值对数组创建对象
`Object.fromEntries(<键值对数组>)`
```javascript
const entries = [['a', 1], ['b', 2]];
const newObj = Object.fromEntries(entries);
console.log(newObj);  // { a: 1, b: 2 }

// 对象过滤
const filtered = Object.fromEntries(
  Object.entries(obj).filter(([key, value]) => value > 1)
);
console.log(filtered);  // { b: 2, c: 3 }
```

---

**Object.assign vs 展开运算符**：合并对象
`Object.assign(<目标>, <源>)` | `{ ...<对象> }`
```javascript
const target = { a: 1 };
const source = { b: 2 };
Object.assign(target, source);  // 修改 target
const merged = { ...target, ...source };  // 创建新对象
```

---

## Array 新方法

**Array.from**：从可迭代对象创建数组
`Array.from(<可迭代对象>[, <映射函数>])`
```javascript
const set = new Set([1, 2, 3]);
const arr = Array.from(set);
console.log(arr);  // [1, 2, 3]

// 带映射函数
const doubled = Array.from([1, 2, 3], (x) => x * 2);
console.log(doubled);  // [2, 4, 6]
```

---

**Array.of**：创建数组
`Array.of(<元素1>, <元素2>, ...)`
```javascript
const arr = Array.of(1, 2, 3);
console.log(arr);  // [1, 2, 3]

// 对比 Array 构造函数
console.log(Array(3));     // [empty x 3]
console.log(Array.of(3));  // [3]
```

---

**flat / flatMap**：扁平化数组
`<数组>.flat([<深度>])` | `<数组>.flatMap(<回调>)`
```javascript
const nested = [1, [2, [3, [4]]]];
console.log(nested.flat());          // [1, 2, [3, [4]]]
console.log(nested.flat(2));        // [1, 2, 3, [4]]
console.log(nested.flat(Infinity)); // [1, 2, 3, 4]

// flatMap
const sentences = ['Hello World', 'Good Morning'];
const words = sentences.flatMap((s) => s.split(' '));
console.log(words);  // ['Hello', 'World', 'Good', 'Morning']
```

---

**at**：支持负索引访问
`<数组>.at(<索引>)`
```javascript
const arr = [1, 2, 3, 4, 5];
console.log(arr.at(-1));  // 5（最后一个元素）
console.log(arr.at(-2));  // 4
```

---

**findLast / findLastIndex**：从末尾查找
`<数组>.findLast(<回调>)` | `<数组>.findLastIndex(<回调>)`
```javascript
const nums = [1, 2, 3, 4, 3, 5];
console.log(nums.findLast((n) => n === 3));        // 3
console.log(nums.findLastIndex((n) => n === 3));  // 4
```

---

**toSorted / toReversed / toSpliced**：不修改原数组
`<数组>.toSorted()` | `<数组>.toReversed()`
```javascript
const original = [3, 1, 4, 1, 5];
const sorted = original.toSorted();
console.log(sorted);    // [1, 1, 3, 4, 5]
console.log(original);  // [3, 1, 4, 1, 5]（不变）
```

---

## String 新方法

**trimStart / trimEnd**：去除首尾空白
`<字符串>.trimStart()` | `<字符串>.trimEnd()`
```javascript
const str = '  hello  ';
console.log(str.trimStart());  // "hello  "
console.log(str.trimEnd());    // "  hello"
```

---

**replaceAll**：替换所有匹配
`<字符串>.replaceAll(<模式>, <替换>)`
```javascript
const text = 'aaa';
console.log(text.replaceAll('a', 'b'));  // "bbb"
```

---

**at**：支持负索引访问
`<字符串>.at(<索引>)`
```javascript
const str = 'Hello';
console.log(str.at(-1));  // 'o'
```

---

**includes / startsWith / endsWith**：字符串包含检查
`<字符串>.includes(<子串>)` | `<字符串>.startsWith(<前缀>)` | `<字符串>.endsWith(<后缀>)`
```javascript
console.log('Hello World'.includes('World'));    // true
console.log('Hello World'.startsWith('Hello'));   // true
console.log('Hello World'.endsWith('World'));    // true
```

---

## 深拷贝

**structuredClone**：ES2022+，深拷贝
`structuredClone(<对象>)`
```javascript
const original = { a: 1, b: { c: 2 } };
const deepCopy = structuredClone(original);
deepCopy.b.c = 99;
console.log(original.b.c);  // 2（原对象不受影响）

// 对比展开运算符（浅拷贝）
const shallowCopy = { ...original };
shallowCopy.b.c = 99;
console.log(original.b.c);  // 99（原对象受影响）
```

---

**逐层展开**：手动深拷贝
`{ ...<对象>, <嵌套属性>: { ...<对象>.<嵌套属性> } }`
```javascript
const obj = { a: 1, b: { c: 2 } };
const deepSpread = { ...obj, b: { ...obj.b } };
deepSpread.b.c = 99;
console.log(obj.b.c);  // 2（原对象不受影响）
```
