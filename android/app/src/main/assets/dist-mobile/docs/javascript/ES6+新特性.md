# JavaScript ES6+ 新特性

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## let 与 const

**基本写法：let 声明**
`let <变量名> = <值>;`
```javascript
// 声明块级作用域变量
let count = 0;
```

---

**基本写法：const 声明**
`const <常量名> = <值>;`
```javascript
// 声明不可重新赋值的常量
const PI = 3.14159;
```

---

## 箭头函数

**基本写法：箭头函数**
`(<参数>) => <表达式>`
```javascript
// 箭头函数直接返回表达式
let square = x => x * x;
```

---

**基本写法：箭头函数带函数体**
`(<参数>) => { return <值>; }`
```javascript
// 箭头函数带函数体
let greet = (name) => {
    return "Hello, " + name;
};
```

---

## 模板字符串

**基本写法：模板字符串**
`` ` <文本> ` ``
```javascript
// 使用反引号创建字符串
let str = `Hello World`;
```

---

**基本写法：变量插值**
`` ` <文本> ${<变量>} ` ``
```javascript
// 在模板字符串中嵌入变量
let greeting = `Hello, ${name}!`;
```

---

**基本写法：多行字符串**
`` ` <行1> <行2> ` ``
```javascript
// 模板字符串支持多行
let text = `Line 1
Line 2`;
```

---

**基本写法：表达式插值**
`` ` <文本> ${<表达式>} ` ``
```javascript
// 在模板字符串中嵌入表达式
let result = `Sum: ${a + b}`;
```

---

## 解构赋值

**基本写法：数组解构**
`let [ <变量1>, <变量2> ] = <数组>;`
```javascript
// 解构数组元素
let [a, b] = [1, 2];
```

---

**基本写法：对象解构**
`let { <属性1>, <属性2> } = <对象>;`
```javascript
// 解构对象属性
let { name, age } = user;
```

---

**基本写法：默认值解构**
`let { <属性> = <默认值> } = <对象>;`
```javascript
// 解构时设置默认值
let { name = "Unknown" } = user;
```

---

**基本写法：重命名解构**
`let { <属性>: <新名> } = <对象>;`
```javascript
// 解构时重命名变量
let { name: userName } = user;
```

---

**基本写法：剩余元素解构**
`let [ <变量1>, ...<剩余> ] = <数组>;`
```javascript
// 解构剩余元素到数组
let [first, ...rest] = numbers;
```

---

## 展开运算符

**基本写法：数组展开**
`[...<数组1>, ...<数组2>]`
```javascript
// 合并数组
let combined = [...arr1, ...arr2];
```

---

**基本写法：对象展开**
`{ ...<对象1>, ...<对象2> }`
```javascript
// 合并对象
let merged = { ...obj1, ...obj2 };
```

---

**基本写法：函数参数展开**
`<函数>(...<数组>)`
```javascript
// 将数组展开为函数参数
Math.max(...numbers);
```

---

## 默认参数

**基本写法：默认参数**
`function <函数>(<参数> = <默认值>) { }`
```javascript
// 参数默认值
function greet(name = "Guest") {
}
```

---

## 剩余参数

**基本写法：剩余参数**
`function <函数>(...<参数名>) { }`
```javascript
// 收集剩余参数为数组
function sum(...numbers) {
}
```

---

## for-of 循环

**基本写法：for-of 遍历**
`for (let <元素> of <可迭代对象>) { }`
```javascript
// 遍历可迭代对象
for (let item of array) {
}
```

---

## Symbol

**基本写法：创建 Symbol**
`let <变量> = Symbol("<描述>");`
```javascript
// 创建唯一符号
let id = Symbol("id");
```

---

**基本写法：Symbol 作为属性键**
`{ [<Symbol>]: <值> }`
```javascript
// 使用 Symbol 作为对象属性键
let obj = { [id]: 123 };
```

---

## 类

**基本写法：类定义**
`class <类名> { }`
```javascript
// 定义类
class Person {
}
```

---

**基本写法：构造方法**
`constructor(<参数>) { }`
```javascript
// 类的构造方法
class Person {
    constructor(name) {
        this.name = name;
    }
}
```

---

**基本写法：类方法**
`<方法名>() { }`
```javascript
// 定义类方法
class Person {
    greet() {
    }
}
```

---

**基本写法：类继承**
`class <子类> extends <父类> { }`
```javascript
// 类继承
class Student extends Person {
}
```

---

**基本写法：super 调用**
`super.<方法>()`
```javascript
// 调用父类方法
class Student extends Person {
    greet() {
        super.greet();
    }
}
```

---

**基本写法：静态方法**
`static <方法名>() { }`
```javascript
// 定义静态方法
class Person {
    static create() {
    }
}
```

---

## Promise

**基本写法：创建 Promise**
`new Promise((<resolve>, <reject>) => { })`
```javascript
// 创建 Promise 对象
let p = new Promise((resolve, reject) => {
});
```

---

**基本写法：async-await**
`async function <函数>() { await <Promise>; }`
```javascript
// 使用 async-await 处理异步
async function fetchData() {
    let data = await promise;
}
```

---

## Map 与 Set

**基本写法：创建 Map**
`let <变量> = new Map();`
```javascript
// 创建 Map 对象
let map = new Map();
```

---

**基本写法：Map 设置**
`<map>.set(<键>, <值>);`
```javascript
// 设置 Map 键值对
map.set("name", "Alice");
```

---

**基本写法：Map 获取**
`<map>.get(<键>);`
```javascript
// 获取 Map 值
let name = map.get("name");
```

---

**基本写法：创建 Set**
`let <变量> = new Set();`
```javascript
// 创建 Set 对象
let set = new Set();
```

---

**基本写法：Set 添加**
`<set>.add(<值>);`
```javascript
// 向 Set 添加值
set.add(1);
```

---

## 模块化

**基本写法：命名导出**
`export <声明>`
```javascript
// 导出变量
export let name = "Alice";
```

---

**基本写法：默认导出**
`export default <表达式>`
```javascript
// 默认导出
export default function() {
}
```

---

**基本写法：导入**
`import { <标识符> } from "<模块>";`
```javascript
// 导入模块
import { name } from "./module.js";
```

---

## 可选链与空值合并

**基本写法：可选链**
`<对象>?.<属性>`
```javascript
// 安全访问嵌套属性
let name = user?.name;
```

---

**基本写法：可选链方法**
`<对象>?.<方法>()`
```javascript
// 安全调用方法
let result = obj?.method();
```

---

**基本写法：空值合并**
`<值1> ?? <值2>`
```javascript
// 左侧为 null 或 undefined 时返回右侧
let value = a ?? b;
```

---

## 其他特性

**基本写法：BigInt**
`<数字>n`
```javascript
// 创建大整数
let big = 9007199254740991n;
```

---

**基本写法：globalThis**
`globalThis`
```javascript
// 访问全局对象
globalThis.variable = 10;
```

---

**基本写法：数值分隔符**
`<数字>_<数字>`
```javascript
// 使用下划线分隔数字提高可读性
let num = 1_000_000;
```

---

**基本写法：Array.flat**
`<数组>.flat(<深度>)`
```javascript
// 展平嵌套数组
let flat = [1, [2, [3]]].flat(Infinity);
```

---

**基本写法：Object.fromEntries**
`Object.fromEntries(<键值对数组>)`
```javascript
// 将键值对数组转换为对象
let obj = Object.fromEntries([["a", 1], ["b", 2]]);
```

---

**基本写法：String.trimStart**
`<字符串>.trimStart()`
```javascript
// 去除字符串开头空白
let trimmed = " hello".trimStart();
```

---

**基本写法：String.trimEnd**
`<字符串>.trimEnd()`
```javascript
// 去除字符串结尾空白
let trimmed = "hello ".trimEnd();
```
