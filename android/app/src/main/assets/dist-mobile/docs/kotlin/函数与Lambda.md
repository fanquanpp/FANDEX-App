# Kotlin 函数与 Lambda 速查

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 函数定义

**标准函数**
`fun <name>(<params>): <ReturnType> { <body> }`
```kotlin
// 标准函数
fun add(a: Int, b: Int): Int {
    return a + b;
};
```

**表达式函数体**
`fun <name>(<params>): <ReturnType> = <expr>`
```kotlin
// 表达式函数体
fun add(a: Int, b: Int): Int = a + b;
// 返回类型推断
fun add(a: Int, b: Int) = a + b;  // 推断为 Int
```

**无返回值函数**
`fun <name>(<params>): Unit { <body> }`
```kotlin
// 无返回值（Unit）
fun greet(name: String): Unit {
    println("Hello, $name!");
};
// Unit 可省略
fun greet(name: String) {
    println("Hello, $name!");
};
```

**默认参数**
`fun <name>(<param1>, <param2>: <Type> = <default>): <ReturnType>`
```kotlin
fun connect(host: String, port: Int = 8080, timeout: Int = 5000): String {
    return "$host:$port (timeout: ${timeout}ms)";
};
connect("localhost");                    // localhost:8080 (timeout: 5000ms)
connect("localhost", 3306);              // localhost:3306 (timeout: 5000ms)
connect("localhost", 3306, 10000);       // localhost:3306 (timeout: 10000ms)
```

**命名参数**
`<name>(<arg> = <value>)`
```kotlin
fun createUser(name: String, age: Int = 0, email: String = "", active: Boolean = true) {
    // ...
};
// 使用命名参数跳过中间参数
createUser("Alice", email = "alice@example.com");
createUser(name = "Bob", active = false);
```

**可变参数**
`fun <name>(vararg <params>: <Type>): <ReturnType>`
```kotlin
fun sum(vararg numbers: Int): Int {
    return numbers.sum();
};
sum(1, 2, 3);          // 6
sum(1, 2, 3, 4, 5);    // 15
// 展开数组
val array = intArrayOf(1, 2, 3);
sum(*array);            // 使用 * 展开运算符
```

**尾递归函数**
`tailrec fun <name>(<params>): <ReturnType>`
```kotlin
tailrec fun factorial(n: Long, acc: Long = 1): Long {
    return if (n <= 1) acc else factorial(n - 1, acc * n);
};
factorial(20);  // 2432902008176640000
```

---

## 扩展函数

**为类添加扩展函数**
`fun <ReceiverType>.<name>(<params>): <ReturnType>`
```kotlin
// 为 String 添加扩展函数
fun String.addExclamation(): String = this + "!";
println("Hello".addExclamation());  // Hello!
// 为 Int 添加扩展
fun Int.isEven(): Boolean = this % 2 == 0;
println(4.isEven());  // true
println(3.isEven());  // false
// 为 List 添加扩展
fun <T> List<T>.second(): T = this[1];
val list = listOf("a", "b", "c");
println(list.second());  // b
```

**可空接收者扩展**
`fun <ReceiverType>?.<name>(<params>): <ReturnType>`
```kotlin
// 可空接收者 — 在函数内部处理 null
fun String?.isNullOrEmpty(): Boolean = this == null || this.isEmpty();
val s: String? = null;
s.isNullOrEmpty();  // true
```

**扩展属性**
`val <ReceiverType>.<name>: <Type> get() = <expr>`
```kotlin
val String.halfLength: Int
    get() = this.length / 2;
println("Kotlin".halfLength);  // 3
var StringBuilder.lastChar: Char
    get() = this[this.length - 1]
    set(value) { this.append(value); };
```

**扩展函数静态解析**
`fun <BaseType>.<name>() = <expr>`
```kotlin
open class Animal;
class Dog : Animal();
fun Animal.sound() = "Generic sound";
fun Dog.sound() = "Woof";
fun makeSound(animal: Animal) {
    println(animal.sound());  // 静态解析，调用 Animal.sound()
};
val dog: Dog = Dog();
val animal: Animal = dog;
makeSound(dog);     // "Generic sound"（不是 "Woof"）
dog.sound();        // "Woof"
animal.sound();     // "Generic sound"
```

---

## Lambda 表达式

**完整语法**
`val <name>: (<ParamTypes>) -> <ReturnType> = { <params> -> <body> }`
```kotlin
// 完整语法
val sum: (Int, Int) -> Int = { a: Int, b: Int -> a + b };
// 类型推断
val sum = { a: Int, b: Int -> a + b };
// 指定类型省略参数类型
val sum: (Int, Int) -> Int = { a, b -> a + b };
```

**it 隐式参数**
`{ <body using it> }`
```kotlin
// 当 Lambda 只有一个参数时，可用 it 代替
val square: (Int) -> Int = { it * it };
val isEven: (Int) -> Boolean = { it % 2 == 0 };
val numbers = listOf(1, 2, 3, 4, 5);
numbers.filter { it > 3 };      // [4, 5]
numbers.map { it * 2 };          // [2, 4, 6, 8, 10]
```

**Lambda 与集合操作**
`<collection>.<func> { <predicate or transform> }`
```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
// filter — 过滤
val evens = numbers.filter { it % 2 == 0 };
// map — 映射
val squares = numbers.map { it * it };
// forEach — 遍历
numbers.forEach { println(it); };
// fold — 累积
val sum = numbers.fold(0) { acc, num -> acc + num };
// groupBy — 分组
val grouped = numbers.groupBy { if (it % 2 == 0) "even" else "odd" };
// sortedBy — 排序
val sorted = numbers.sortedByDescending { it };
```

---

## 高阶函数

**函数作为参数**
`fun <name>(<param>: (<Type>) -> <ReturnType>): <ReturnType>`
```kotlin
fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>();
    for (item in this) {
        if (predicate(item)) result.add(item);
    };
    return result;
};
numbers.customFilter { it > 5 };
```

**函数作为返回值**
`fun <name>(<params>): (<Type>) -> <ReturnType>`
```kotlin
fun multiplier(factor: Int): (Int) -> Int = { number -> number * factor };
val double = multiplier(2);
val triple = multiplier(3);
double(5);   // 10
triple(5);   // 15
```

**函数类型**
`val <name>: (<ParamTypes>) -> <ReturnType> = { <body> }`
```kotlin
// 函数类型语法
val f1: () -> Unit = { println("No params"); };
val f2: (Int) -> String = { "Number: $it" };
val f3: (Int, String) -> Boolean = { num, str -> num == str.length };
// 带接收者的函数类型
val f4: String.(Int) -> String = { this.repeat(it) };
"Ha".f4(3);        // "HaHaHa"
// 函数类型实例化
val f5 = fun(a: Int, b: Int): Int = a + b;  // 匿名函数
```

**常见高阶函数模式**
`<obj>.<scopeFunc> { <body> }`
```kotlin
// also — 执行附加操作，返回原对象
val user = User("Alice", 25).also {
    println("Created: $it");
};
// apply — 配置对象，返回原对象
val builder = StringBuilder().apply {
    append("Hello");
    append(", ");
    append("Kotlin");
};
// let — 转换对象，返回 Lambda 结果
val length = "Kotlin".let {
    println("Processing: $it");
    it.length;
};
// run — 执行代码块，返回结果
val result = "Kotlin".run {
    println("Processing: $this");
    length;
};
// with — 非扩展版本的 run
val greeting = with(StringBuilder()) {
    append("Hello");
    append(", Kotlin");
    toString();
};
```

---

## 内联函数

**inline 关键字**
`inline fun <name>(<params>): <ReturnType>`
```kotlin
inline fun <T> measureTime(block: () -> T): T {
    val start = System.currentTimeMillis();
    val result = block();
    val end = System.currentTimeMillis();
    println("Execution time: ${end - start}ms");
    return result;
};
```

**noinline 与 crossinline**
`inline fun <name>(<param1>: () -> Unit, noinline <param2>: () -> Unit)`
```kotlin
// noinline — 禁止内联特定参数
inline fun process(
    inlineBlock: () -> Unit,      // 被内联
    noinline notInlined: () -> Unit  // 不被内联
) {
    inlineBlock();
    notInlined();
};
// crossinline — 允许内联但禁止非局部返回
inline fun runInThread(crossinline action: () -> Unit) {
    Thread { action(); }.start();
    // action 中不能使用 return 退出外层函数
};
```

**非局部返回**
`<collection>.forEach { if (<cond>) return; <body> }`
```kotlin
fun processElements(elements: List<Int>) {
    elements.forEach {
        if (it == 0) return;  // 非局部返回，退出 processElements
        println(it);
    };
    println("Done");  // 如果遇到 0，这行不会执行
};
```

---

## SAM 转换

**Java SAM 接口转换**
`<obj>.setListener { <param> -> <body> }`
```kotlin
// Java 接口
// public interface OnClickListener {
//     void onClick(View v);
// }
// Kotlin 中使用 SAM 转换
button.setOnClickListener { view ->
    println("Clicked: $view");
};
```

**Kotlin 函数式接口**
`fun interface <Name> { fun <method>(<params>): <ReturnType> }`
```kotlin
// fun interface — Kotlin 1.4+ 支持
fun interface Producer<T> {
    fun produce(): T;
};
val producer = Producer { "Hello" };
producer.produce();  // "Hello"
// 多类型参数函数式接口
fun interface Transformer<T, R> {
    fun transform(input: T): R;
};
val toLength: Transformer<String, Int> = Transformer { it.length };
toLength.transform("Kotlin");  // 6
```

---

## 作用域函数对比

**作用域函数选择**
`<obj>.[let|run|apply|also] { <body> } | with(<obj>) { <body> }`
```kotlin
// 典型使用场景
val person = Person().apply {
    name = "Alice";
    age = 25;
}.also {
    println("Created person: $it");
};
val nameLength = person.let {
    it.name.length;
};
```
