# STL容器与迭代器

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 顺序容器

**vector：动态数组**
`std::vector<<Type>> <vec>;`
```cpp
#include <vector>
std::vector<int> v1;                    // 空 vector
std::vector<int> v2(10);                // 10 个元素，默认值 0
std::vector<int> v3(10, 5);             // 10 个元素，值为 5
std::vector<int> v4 = {1, 2, 3, 4, 5};  // 列表初始化
// 添加元素
v1.push_back(10);
v1.emplace_back(20);
// 访问元素
std::cout << v4[0] << std::endl;        // 1
std::cout << v4.at(1) << std::endl;     // 2（带边界检查）
std::cout << v4.front() << std::endl;   // 1
std::cout << v4.back() << std::endl;    // 5
// 大小
std::cout << v4.size() << std::endl;    // 5
```

---

**list：双向链表**
`std::list<<Type>> <lst>;`
```cpp
#include <list>
std::list<int> lst = {1, 2, 3};
// 头尾操作
lst.push_front(0);
lst.push_back(4);
// 插入与删除
auto it = lst.begin();
std::advance(it, 2);
lst.insert(it, 10);
lst.remove(10);    // 删除所有值为 10 的元素
// 大小
std::cout << lst.size() << std::endl;
```

---

**deque：双端队列**
`std::deque<<Type>> <dq>;`
```cpp
#include <deque>
std::deque<int> dq = {1, 2, 3};
dq.push_front(0);
dq.push_back(4);
// dq: {0, 1, 2, 3, 4}
dq.pop_front();
dq.pop_back();
// dq: {1, 2, 3}
```

---

**array：固定大小数组（C++11）**
`std::array<<Type>, <Size>> <arr>;`
```cpp
#include <array>
std::array<int, 5> arr = {1, 2, 3, 4, 5};
// 访问
std::cout << arr[0] << std::endl;       // 1
std::cout << arr.at(1) << std::endl;    // 2
std::cout << arr.size() << std::endl;   // 5
// 遍历
for (const auto& elem : arr) {
    std::cout << elem << " ";
}
```

---

**forward_list：单向链表（C++11）**
`std::forward_list<<Type>> <flst>;`
```cpp
#include <forward_list>
std::forward_list<int> flst = {1, 2, 3};
flst.push_front(0);
flst.push_front(-1);
// flst: {-1, 0, 1, 2, 3}
flst.pop_front();
// flst: {0, 1, 2, 3}
```

---

## 关联容器

**map：有序键值对**
`std::map<<KeyType>, <ValueType>> <m>;`
```cpp
#include <map>
std::map<std::string, int> ages;
// 插入
ages["Alice"] = 25;
ages["Bob"] = 30;
ages.insert({"Charlie", 35});
// 访问
std::cout << ages["Alice"] << std::endl;    // 25
// 查找
if (ages.find("Bob") != ages.end()) {
    std::cout << "Found Bob" << std::endl;
}
// 遍历
for (const auto& [name, age] : ages) {
    std::cout << name << ": " << age << std::endl;
}
```

---

**set：有序集合**
`std::set<<Type>> <s>;`
```cpp
#include <set>
std::set<int> s = {3, 1, 4, 1, 5, 9, 2, 6};
// 自动排序去重
// s: {1, 2, 3, 4, 5, 6, 9}
s.insert(7);
s.erase(4);
// 查找
if (s.count(5)) {
    std::cout << "5 exists" << std::endl;
}
// 遍历
for (int n : s) {
    std::cout << n << " ";
}
```

---

**unordered_map：哈希表（C++11）**
`std::unordered_map<<KeyType>, <ValueType>> <m>;`
```cpp
#include <unordered_map>
std::unordered_map<std::string, int> hash_map;
hash_map["one"] = 1;
hash_map["two"] = 2;
hash_map["three"] = 3;
// 查找
auto it = hash_map.find("two");
if (it != hash_map.end()) {
    std::cout << it->first << ": " << it->second << std::endl;
}
```

---

**unordered_set：哈希集合（C++11）**
`std::unordered_set<<Type>> <s>;`
```cpp
#include <unordered_set>
std::unordered_set<int> uset = {3, 1, 4, 1, 5, 9};
// 自动去重
// uset: {1, 3, 4, 5, 9}（无序）
uset.insert(7);
if (uset.count(4)) {
    std::cout << "4 exists" << std::endl;
}
```

---

**multimap 与 multiset：允许重复键**
`std::multimap<<KeyType>, <ValueType>> <mm>;`
```cpp
#include <map>
#include <set>
std::multimap<std::string, int> mm;
mm.insert({"Alice", 90});
mm.insert({"Alice", 85});
mm.insert({"Bob", 95});
// 查找所有 Alice 的记录
auto range = mm.equal_range("Alice");
for (auto it = range.first; it != range.second; ++it) {
    std::cout << it->first << ": " << it->second << std::endl;
}
```

---

## 容器适配器

**stack：栈**
`std::stack<<Type>> <stk>;`
```cpp
#include <stack>
std::stack<int> stk;
stk.push(1);
stk.push(2);
stk.push(3);
std::cout << stk.top() << std::endl;    // 3
stk.pop();
std::cout << stk.top() << std::endl;    // 2
std::cout << stk.size() << std::endl;   // 2
```

---

**queue：队列**
`std::queue<<Type>> <q>;`
```cpp
#include <queue>
std::queue<int> q;
q.push(1);
q.push(2);
q.push(3);
std::cout << q.front() << std::endl;    // 1
std::cout << q.back() << std::endl;     // 3
q.pop();
std::cout << q.front() << std::endl;    // 2
```

---

**priority_queue：优先队列**
`std::priority_queue<<Type>> <pq>;`
```cpp
#include <queue>
// 默认最大堆
std::priority_queue<int> pq;
pq.push(3);
pq.push(1);
pq.push(4);
pq.push(1);
pq.push(5);
std::cout << pq.top() << std::endl;    // 5（最大值）
pq.pop();
std::cout << pq.top() << std::endl;    // 4
// 最小堆
std::priority_queue<int, std::vector<int>, std::greater<int>> min_pq;
min_pq.push(3);
min_pq.push(1);
min_pq.push(4);
std::cout << min_pq.top() << std::endl;    // 1（最小值）
```

---

## 迭代器

**迭代器类型**
`<container>::iterator | <container>::const_iterator | <container>::reverse_iterator`
```cpp
std::vector<int> v = {1, 2, 3, 4, 5};
// 正向迭代器
for (std::vector<int>::iterator it = v.begin(); it != v.end(); ++it) {
    std::cout << *it << " ";
}
// const 迭代器
for (std::vector<int>::const_iterator it = v.cbegin(); it != v.cend(); ++it) {
    std::cout << *it << " ";
}
// 反向迭代器
for (std::vector<int>::reverse_iterator it = v.rbegin(); it != v.rend(); ++it) {
    std::cout << *it << " ";    // 5 4 3 2 1
}
```

---

**迭代器辅助函数**
`std::advance(<it>, <n>) | std::distance(<first>, <last>) | std::next(<it>[, <n>]) | std::prev(<it>[, <n>])`
```cpp
#include <iterator>
std::vector<int> v = {1, 2, 3, 4, 5};
auto it = v.begin();
// 前进
std::advance(it, 2);
std::cout << *it << std::endl;    // 3
// 距离
auto it2 = v.begin();
std::cout << std::distance(it2, it) << std::endl;    // 2
// next/prev
auto next_it = std::next(it);    // 指向 4
auto prev_it = std::prev(it);   // 指向 2
```

---

**插入迭代器**
`std::back_inserter(<container>) | std::front_inserter(<container>) | std::inserter(<container>, <it>)`
```cpp
#include <iterator>
std::vector<int> src = {1, 2, 3};
std::vector<int> dest;
// back_inserter：在末尾插入
std::copy(src.begin(), src.end(), std::back_inserter(dest));
// dest: {1, 2, 3}
std::list<int> lst;
// front_inserter：在开头插入
std::copy(src.begin(), src.end(), std::front_inserter(lst));
// lst: {3, 2, 1}
```

---

## 容器通用操作

**大小与容量**
`<c>.size() | <c>.empty() | <c>.clear()`
```cpp
std::vector<int> v = {1, 2, 3};
std::cout << v.size() << std::endl;    // 3
std::cout << v.empty() << std::endl;   // false
v.clear();
std::cout << v.empty() << std::endl;   // true
```

---

**交换**
`<c1>.swap(<c2>)`
```cpp
std::vector<int> v1 = {1, 2, 3};
std::vector<int> v2 = {4, 5, 6};
v1.swap(v2);
// v1: {4, 5, 6}, v2: {1, 2, 3}
```

---

## 算法

**排序算法**
`std::sort(<begin>, <end>[, <comp>])`
```cpp
#include <algorithm>
std::vector<int> v = {5, 2, 8, 1, 9, 3};
// 升序
std::sort(v.begin(), v.end());
// 降序
std::sort(v.begin(), v.end(), std::greater<int>());
// 自定义比较
std::sort(v.begin(), v.end(), [](int a, int b) {
    return a % 10 < b % 10;    // 按个位数排序
});
```

---

**查找算法**
`std::find(<begin>, <end>, <value>) | std::find_if(<begin>, <end>, <pred>)`
```cpp
std::vector<int> v = {1, 2, 3, 4, 5};
// 查找值
auto it = std::find(v.begin(), v.end(), 3);
if (it != v.end()) {
    std::cout << "Found at: " << std::distance(v.begin(), it) << std::endl;
}
// 条件查找
auto it2 = std::find_if(v.begin(), v.end(), [](int n) { return n > 3; });
```

---

**计数与累加**
`std::count(<begin>, <end>, <value>) | std::accumulate(<begin>, <end>, <init>)`
```cpp
#include <numeric>
std::vector<int> v = {1, 2, 3, 4, 5};
// 计数
int count = std::count(v.begin(), v.end(), 3);    // 1
// 累加
int sum = std::accumulate(v.begin(), v.end(), 0);    // 15
```

---

**复制与转换**
`std::copy(<begin>, <end>, <out>) | std::transform(<begin>, <end>, <out>, <func>)`
```cpp
std::vector<int> src = {1, 2, 3, 4, 5};
std::vector<int> dest(src.size());
// 复制
std::copy(src.begin(), src.end(), dest.begin());
// 转换
std::transform(src.begin(), src.end(), dest.begin(), [](int n) {
    return n * n;    // 平方
});
// dest: {1, 4, 9, 16, 25}
```

---

**删除元素**
`std::remove(<begin>, <end>, <value>) | std::remove_if(<begin>, <end>, <pred>)`
```cpp
std::vector<int> v = {1, 2, 3, 4, 5, 3};
// 删除所有值为 3 的元素
auto new_end = std::remove(v.begin(), v.end(), 3);
v.erase(new_end, v.end());
// v: {1, 2, 4, 5}
// 条件删除
v.erase(std::remove_if(v.begin(), v.end(), [](int n) { return n % 2 == 0; }), v.end());
```
