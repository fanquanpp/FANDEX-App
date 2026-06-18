# 条件类型与infer

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 条件类型基础

**条件类型：根据类型关系选择不同类型**
`type <类型><T> = T extends U ? X : Y`

```typescript
// 基本条件类型
type IsString<T> = T extends string ? true : false;
type IsNumber<T> = T extends number ? true : false;
// 使用条件类型
type A = IsString<string>; // true
type B = IsString<number>; // false
type C = IsNumber<number>; // true
type D = IsNumber<string>; // false
```

---

## 分布式条件类型

**分布式条件类型：对联合类型自动分发**
`type <类型><T> = T extends U ? X : Y`

```typescript
// 分布式条件类型
type ToArray<T> = T extends any ? T[] : never;
// 使用联合类型
type A = ToArray<string | number>; // string[] | number[]
// 非分布式条件类型（使用方括号阻止分发）
type ToArrayNonDist<T> = [T] extends [any] ? T[] : never;
type B = ToArrayNonDist<string | number>; // (string | number)[]
```

---

## infer 关键字

**infer：在条件类型中推断类型**
`type <类型><T> = T extends Array<infer U> ? U : T`

```typescript
// 提取数组元素类型
type UnpackArray<T> = T extends Array<infer U> ? U : T;
type A = UnpackArray<string[]>; // string
type B = UnpackArray<number[]>; // number
type C = UnpackArray<string>; // string
```

---

**提取函数返回类型**
`type <类型><T> = T extends (...args: any[]) => infer R ? R : any`

```typescript
// 提取函数返回类型
type ReturnType<T> = T extends (...args: any[]) => infer R ? R : any;
type R1 = ReturnType<() => string>; // string
type R2 = ReturnType<() => number>; // number
type R3 = ReturnType<(x: number) => boolean>; // boolean
```

---

**提取函数参数类型**
`type <类型><T> = T extends (...args: infer P) => any ? P : never`

```typescript
// 提取函数参数类型
type Parameters<T> = T extends (...args: infer P) => any ? P : never;
type P1 = Parameters<(a: string) => void>; // [string]
type P2 = Parameters<(a: string, b: number) => void>; // [string, number]
// 提取第一个参数类型
type FirstParameter<T> = T extends (first: infer F, ...rest: any[]) => any ? F : never;
type F = FirstParameter<(name: string, age: number) => void>; // string
```

---

**提取 Promise 值类型**
`type <类型><T> = T extends Promise<infer U> ? U : T`

```typescript
// 提取 Promise 值类型
type Awaited<T> = T extends Promise<infer U> ? U : T;
type A = Awaited<Promise<string>>; // string
type B = Awaited<Promise<number>>; // number
```

---

**提取构造函数实例类型**
`type <类型><T> = T extends new (...args: any[]) => infer R ? R : any`

```typescript
// 提取构造函数实例类型
type InstanceType<T> = T extends new (...args: any[]) => infer R ? R : any;
class MyClass {
  name: string = 'MyClass';
}
type I = InstanceType<typeof MyClass>; // MyClass
const instance: I = new MyClass();
console.log(instance.name); // MyClass
```

---

## 内置条件类型

**Exclude：从联合类型中排除**
`type <类型> = Exclude<T, U>`

```typescript
// Exclude：从联合类型中排除
type T1 = Exclude<'a' | 'b' | 'c', 'a'>; // 'b' | 'c'
type T2 = Exclude<string | number | boolean, string>; // number | boolean
type T3 = Exclude<number | string | (() => void), Function>; // number | string
```

---

**Extract：从联合类型中提取**
`type <类型> = Extract<T, U>`

```typescript
// Extract：从联合类型中提取
type T1 = Extract<'a' | 'b' | 'c', 'a' | 'b'>; // 'a' | 'b'
type T2 = Extract<string | number | boolean, string | number>; // string | number
```

---

**NonNullable：排除 null 和 undefined**
`type <类型> = NonNullable<T>`

```typescript
// NonNullable：排除 null 和 undefined
type T1 = NonNullable<string | null>; // string
type T2 = NonNullable<number | null | undefined>; // number
type T3 = NonNullable<string | null | undefined | number>; // string | number
```

---

**ReturnType：获取函数返回类型**
`type <类型> = ReturnType<T>`

```typescript
// ReturnType：获取函数返回类型
type T1 = ReturnType<() => string>; // string
type T2 = ReturnType<() => number>; // number
type T3 = ReturnType<typeof JSON.parse>; // any
```

---

**Parameters：获取函数参数类型**
`type <类型> = Parameters<T>`

```typescript
// Parameters：获取函数参数类型
type T1 = Parameters<(a: string) => void>; // [string]
type T2 = Parameters<(a: string, b: number) => void>; // [string, number]
type T3 = Parameters<typeof Math.max>; // number[]
```

---

**InstanceType：获取构造函数实例类型**
`type <类型> = InstanceType<T>`

```typescript
// InstanceType：获取构造函数实例类型
class Person {
  name: string;
  constructor(name: string) {
    this.name = name;
  }
}
type T1 = InstanceType<typeof Person>; // Person
const person: T1 = new Person('Alice');
```

---

## 递归条件类型

**递归条件类型：处理嵌套结构**
`type <类型><T> = T extends Array<infer U> ? <递归调用> : T`

```typescript
// 递归展平数组类型
type DeepFlatten<T> = T extends Array<infer U> ? DeepFlatten<U> : T;
type A = DeepFlatten<number[][][]>; // number
// 递归提取 Promise 类型
type DeepAwaited<T> = T extends Promise<infer U> ? DeepAwaited<U> : T;
type B = DeepAwaited<Promise<Promise<string>>>; // string
```

---

## 条件类型与映射类型

**条件类型与映射类型组合**
`type <类型><T> = { [K in keyof T]: T[K] extends <约束> ? <类型1> : <类型2> }`

```typescript
// 条件类型与映射类型组合
type FunctionProperties<T> = {
  [K in keyof T]: T[K] extends Function ? K : never;
};
type NonFunctionProperties<T> = {
  [K in keyof T]: T[K] extends Function ? never : K;
};
// 使用
interface Example {
  name: string;
  age: number;
  greet: () => void;
  farewell: () => string;
}
type Functions = FunctionProperties<Example>[keyof Example]; // 'greet' | 'farewell'
type NonFunctions = NonFunctionProperties<Example>[keyof Example]; // 'name' | 'age'
```

---

## 条件类型的应用

**根据条件选择不同类型**
`type <类型><T> = T extends <约束> ? <类型1> : <类型2>`

```typescript
// 根据条件选择不同类型
type ResultType<T> = T extends string
  ? { type: 'string'; value: T }
  : T extends number
  ? { type: 'number'; value: T }
  : T extends boolean
  ? { type: 'boolean'; value: T }
  : { type: 'unknown'; value: T };
// 使用
type R1 = ResultType<string>; // { type: 'string'; value: string }
type R2 = ResultType<number>; // { type: 'number'; value: number }
type R3 = ResultType<boolean>; // { type: 'boolean'; value: boolean }
```

---

## 自定义条件类型工具

**自定义条件类型工具**
`type <类型><T> = ...`

```typescript
// 自定义条件类型工具
// 检查类型是否为 never
type IsNever<T> = [T] extends [never] ? true : false;
type A = IsNever<never>; // true
type B = IsNever<string>; // false
// 检查类型是否为 any
type IsAny<T> = 0 extends 1 & T ? true : false;
type C = IsAny<any>; // true
type D = IsAny<string>; // false
// 检查类型是否为 unknown
type IsUnknown<T> = unknown extends T ? (T extends object ? false : true) : false;
type E = IsUnknown<unknown>; // true
type F = IsUnknown<string>; // false
```
