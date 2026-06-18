# Kotlin 集合操作速查

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 基础操作

**filter / map / forEach**
`<collection>.[filter|map|forEach] { <lambda> }`
```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
// 过滤：只保留偶数
val evens = numbers.filter { it % 2 == 0 };  // [2, 4, 6, 8, 10]
// 映射：每个元素乘以2
val doubled = numbers.map { it * 2 };  // [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
// 链式调用：先过滤再映射
val result = numbers.filter { it % 2 == 0 }.map { it * it };  // [4, 16, 36, 64, 100]
// 排序
val sorted = numbers.sortedDescending();  // [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
// 求和
val sum = numbers.sum();  // 55
```

---

## 过滤操作

**filter / filterNot / filterNotNull**
`<collection>.[filter|filterNot|filterNotNull] { <predicate> }`
```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
// filter：保留满足条件的元素
val greaterThan5 = numbers.filter { it > 5 };  // [6, 7, 8, 9, 10]
// filterNot：保留不满足条件的元素
val notGreaterThan5 = numbers.filterNot { it > 5 };  // [1, 2, 3, 4, 5]
// filterNotNull：过滤掉 null
val mixed = listOf("a", null, "b", null, "c");
val nonNull = mixed.filterNotNull();  // [a, b, c]
```

**takeWhile / dropWhile**
`<collection>.[takeWhile|dropWhile] { <predicate> }`
```kotlin
// takeWhile：从开头取元素，直到不满足条件
val takeResult = numbers.takeWhile { it < 5 };  // [1, 2, 3, 4]
// dropWhile：从开头跳过元素，直到不满足条件
val dropResult = numbers.dropWhile { it < 5 };  // [5, 6, 7, 8, 9, 10]
```

**partition**
`val (<trueList>, <falseList>) = <collection>.partition { <predicate> }`
```kotlin
// partition：将集合分为满足条件和不满足条件的两部分
val (even, odd) = numbers.partition { it % 2 == 0 };
// 偶数: [2, 4, 6, 8, 10], 奇数: [1, 3, 5, 7, 9]
```

---

## 映射操作

**map / mapIndexed / mapNotNull**
`<collection>.[map|mapIndexed|mapNotNull] { <transform> }`
```kotlin
val names = listOf("alice", "bob", "charlie");
// map：转换每个元素
val upper = names.map { it.uppercase() };  // [ALICE, BOB, CHARLIE]
// mapIndexed：带索引的映射
val indexed = names.mapIndexed { index, name -> "$index: $name" };  // [0: alice, 1: bob, 2: charlie]
// mapNotNull：映射并过滤 null
val parsed = listOf("1", "abc", "3", "def").mapNotNull { it.toIntOrNull() };  // [1, 3]
```

**flatMap / flatten**
`<collection>.[flatMap|flatten] { <transform> }`
```kotlin
// flatMap：先映射再展平
val words = listOf("Hello World", "Kotlin is great");
val allWords = words.flatMap { it.split(" ") };  // [Hello, World, Kotlin, is, great]
// flatten：展平嵌套集合
val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5));
val flat = nested.flatten();  // [1, 2, 3, 4, 5]
```

---

## 排序操作

**sortedBy / sortedByDescending**
`<collection>.sortedBy[Descending] { <selector> }`
```kotlin
data class Person(val name: String, val age: Int);
val people = listOf(
    Person("Alice", 25),
    Person("Bob", 30),
    Person("Charlie", 20)
);
// sortedBy：按属性排序（升序）
val byAge = people.sortedBy { it.age };  // [Charlie(20), Alice(25), Bob(30)]
// sortedByDescending：按属性排序（降序）
val byAgeDesc = people.sortedByDescending { it.age };  // [Bob(30), Alice(25), Charlie(20)]
```

**sortedWith / reversed / shuffled**
`<collection>.[sortedWith|reversed|shuffled]`
```kotlin
// sortedWith：自定义排序
val custom = people.sortedWith(compareBy({ it.age }, { it.name }));
// reversed：反转顺序
val reversed = people.reversed();
// shuffled：随机打乱
val shuffled = people.shuffled();
```

---

## 分组和聚合

**groupBy**
`<collection>.groupBy { <keySelector> }`
```kotlin
data class Student(val name: String, val grade: String, val score: Int);
val students = listOf(
    Student("Alice", "A", 90),
    Student("Bob", "B", 80),
    Student("Charlie", "A", 95),
    Student("David", "B", 75),
    Student("Eve", "A", 88)
);
// groupBy：按属性分组
val byGrade = students.groupBy { it.grade };
// A组: [Alice, Charlie, Eve], B组: [Bob, David]
```

**groupingBy + aggregate**
`<collection>.groupingBy { <key> }.<aggregate>()`
```kotlin
// groupingBy + aggregate：更灵活的分组聚合
val avgScoreByGrade = students.groupingBy { it.grade }.average();
// A组平均分: 91.0
// count：计数
val countByGrade = students.groupingBy { it.grade }.eachCount();  // {A=3, B=2}
```

**聚合函数**
`<collection>.[max|min|maxOrNull|average|sum]()`
```kotlin
val scores = students.map { it.score };
scores.max();          // 最高分
scores.min();          // 最低分
scores.average();      // 平均分
scores.sum();          // 总分
scores.maxOrNull();    // 安全版本，空集合返回 null
```

---

## 查找操作

**find / findLast**
`<collection>.[find|findLast] { <predicate> }`
```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
// find：找到第一个满足条件的元素
val firstEven = numbers.find { it % 2 == 0 };  // 2
// findLast：找到最后一个满足条件的元素
val lastEven = numbers.findLast { it % 2 == 0 };  // 10
```

**first / last / firstOrNull / lastOrNull**
`<collection>.[first|last|firstOrNull|lastOrNull]([<predicate>])`
```kotlin
// first / last：获取第一个/最后一个元素
numbers.first();   // 1
numbers.last();    // 10
// firstOrNull / lastOrNull：安全版本
val empty = emptyList<Int>();
empty.firstOrNull();  // null（不会抛异常）
```

**binarySearch / contains**
`<collection>.[binarySearch|contains](<value>)`
```kotlin
// binarySearch：二分查找（集合必须已排序）
val sorted = numbers.sorted();
val index = sorted.binarySearch(5);  // 5的索引
// contains / in：检查是否包含
5 in numbers;           // true
numbers.contains(100);  // false
```

---

## Map 操作

**遍历 / mapKeys / mapValues**
`<map>.[forEach|mapKeys|mapValues] { <transform> }`
```kotlin
val scores = mapOf("Alice" to 90, "Bob" to 80, "Charlie" to 95);
// 遍历
scores.forEach { (name, score) ->
    println("$name: $score");
};
// mapKeys / mapValues：转换键或值
val upperKeys = scores.mapKeys { it.key.uppercase() };  // {ALICE=90, BOB=80, CHARLIE=95}
val graded = scores.mapValues { if (it.value >= 90) "A" else "B" };  // {Alice=A, Bob=B, Charlie=A}
```

**filterKeys / filterValues / getOrDefault**
`<map>.[filterKeys|filterValues|getOrDefault|getOrElse] { <predicate> }`
```kotlin
// filterKeys / filterValues：过滤
val highScores = scores.filterValues { it >= 90 };  // {Alice=90, Charlie=95}
// getOrDefault / getOrElse
scores.getOrDefault("David", 0);   // 0
scores.getOrElse("David") { 0 };    // 0
// toList：转为键值对列表
val pairs = scores.toList();  // [(Alice, 90), (Bob, 80), (Charlie, 95)]
```

---

## Sequence 懒序列

**asSequence**
`<collection>.asSequence().<op>().<op>().toList()`
```kotlin
val numbers = (1..100).toList();
// 普通集合操作：每一步都创建新集合
val listResult = numbers
    .filter { it % 2 == 0 }   // 创建中间集合
    .map { it * it }           // 又创建中间集合
    .take(5);                   // 再创建中间集合
// Sequence：懒执行，不创建中间集合
val seqResult = numbers.asSequence()
    .filter { it % 2 == 0 }   // 不执行
    .map { it * it }           // 不执行
    .take(5)                   // 不执行
    .toList();                  // 到这里才执行，且每个元素走完整个管道
// [4, 16, 36, 64, 100]
```

---

## 数据转换管道

**链式数据转换**
`<list>.filter { <pred> }.map { <transform> }.sortedBy { <sel> }.distinctBy { <sel> }`
```kotlin
data class RawUser(val name: String, val age: String, val email: String?);
data class ValidUser(val name: String, val age: Int, val email: String);
fun processUsers(rawUsers: List<RawUser>): List<ValidUser> {
    return rawUsers
        .filter { it.email != null }           // 过滤掉没有邮箱的
        .map {                                  // 转换数据
            ValidUser(
                name = it.name.trim(),
                age = it.age.toIntOrNull() ?: 0,
                email = it.email!!
            );
        }
        .filter { it.age >= 18 }               // 过滤掉未成年
        .sortedBy { it.name }                   // 按名字排序
        .distinctBy { it.email };                // 按邮箱去重
};
```

---

## 频率统计

**groupingBy + eachCount**
`<collection>.groupingBy { <key> }.eachCount()`
```kotlin
val text = "hello world kotlin programming";
// 统计每个字符出现的次数
val charFreq = text.groupingBy { it }.eachCount();
// 统计每个单词出现的次数
val wordFreq = text.split(" ").groupingBy { it }.eachCount();
// 找出出现次数最多的元素
val words = listOf("a", "b", "a", "c", "a", "b");
val mostCommon = words.groupingBy { it }.eachCount()
    .maxByOrNull { it.value };  // a=3
```

---

## 集合的交并差

**intersect / union / subtract**
`<setA> [intersect|union|subtract] <setB>`
```kotlin
val a = setOf(1, 2, 3, 4, 5);
val b = setOf(4, 5, 6, 7, 8);
// 交集
val intersect = a intersect b;  // {4, 5}
// 并集
val union = a union b;  // {1, 2, 3, 4, 5, 6, 7, 8}
// 差集
val subtract = a subtract b;  // {1, 2, 3}
```

---

## 自定义聚合

**fold / reduce**
`<collection>.[fold|reduce] { <acc>, <item> -> <body> }`
```kotlin
val numbers = listOf(1, 2, 3, 4, 5);
// fold：带初始值的累积
val sum = numbers.fold(0) { acc, num -> acc + num };  // 15
// 用 fold 构建字符串
val result = numbers.fold("Numbers:") { acc, num -> "$acc $num" };  // Numbers: 1 2 3 4 5
// reduce：不带初始值的累积（集合不能为空）
val product = numbers.reduce { acc, num -> acc * num };  // 120
```

---

## 窗口和分块

**chunked / windowed / zip / unzip**
`<collection>.[chunked|windowed|zip|unzip]`
```kotlin
val numbers = (1..10).toList();
// chunked：按大小分块
val chunks = numbers.chunked(3);  // [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10]]
// windowed：滑动窗口
val windows = numbers.windowed(3, step = 2);  // [[1, 2, 3], [3, 4, 5], [5, 6, 7], [7, 8, 9], [9, 10]]
// zip：合并两个集合
val names = listOf("Alice", "Bob", "Charlie");
val ages = listOf(25, 30, 20);
val pairs = names.zip(ages);  // [(Alice, 25), (Bob, 30), (Charlie, 20)]
// unzip：拆分
val (names2, ages2) = pairs.unzip();
```

---

## 关联操作

**associateBy / associateWith / associate**
`<collection>.[associateBy|associateWith|associate] { <transform> }`
```kotlin
data class Product(val id: Int, val name: String, val price: Double);
val products = listOf(
    Product(1, "手机", 2999.0),
    Product(2, "电脑", 5999.0),
    Product(3, "耳机", 299.0)
);
// associateBy：按某个属性建立 Map
val byId = products.associateBy { it.id };
byId[2];  // Product(id=2, name=电脑, price=5999.0)
// associateWith：用元素本身作为键
val priceMap = products.associateWith { it.price };
// associate：自定义键值
val namePriceMap = products.associate { it.name to it.price };  // {手机=2999.0, 电脑=5999.0, 耳机=299.0}
```
