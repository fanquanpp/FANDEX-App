# Lambda表达式

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## Lambda 基础

**基本 Lambda 语法**
`[<capture>](<params>) -> <return_type> { <body> }`
```cpp
// 无捕获、无参数的 Lambda
auto hello = []() { std::cout << "Hello, Lambda!" << std::endl; };
hello();
// 带参数的 Lambda
auto add = [](int a, int b) { return a + b; };
std::cout << add(3, 4) << std::endl;    // 7
// 显式指定返回类型
auto divide = [](double a, double b) -> double {
    if (b == 0) return 0;
    return a / b;
};
```

---

**Lambda 立即调用**
`[<capture>](<params>) { ... }();`
```cpp
// 立即调用的 Lambda
[]() {
    std::cout << "Executed immediately" << std::endl;
}();
// 带参数
[](int x) {
    std::cout << "x = " << x << std::endl;
}(42);
```

---

## 捕获方式

**值捕获**
`[<var>]`
```cpp
int x = 10;
int y = 20;
// 值捕获：捕获 x 的副本
auto lambda = [x]() {
    std::cout << "x = " << x << std::endl;    // 10
};
x = 100;
lambda();    // 仍输出 10（捕获的是副本）
```

---

**引用捕获**
`[&<var>]`
```cpp
int x = 10;
// 引用捕获：捕获 x 的引用
auto lambda = [&x]() {
    x = 100;    // 修改外部变量
};
lambda();
std::cout << x << std::endl;    // 100
```

---

**混合捕获**
`[<var1>, &<var2>]`
```cpp
int x = 10;
int y = 20;
int z = 30;
// x 值捕获，y 引用捕获，z 值捕获
auto lambda = [x, &y, z]() {
    y = x + y + z;    // 10 + 20 + 30 = 60
};
lambda();
std::cout << y << std::endl;    // 60
```

---

**全部捕获**
`[=]` 或 `[&]`
```cpp
int x = 10, y = 20, z = 30;
// 全部值捕获
auto val_lambda = [=]() {
    std::cout << x << " " << y << " " << z << std::endl;
};
// 全部引用捕获
auto ref_lambda = [&]() {
    x = 1; y = 2; z = 3;
};
ref_lambda();
std::cout << x << " " << y << " " << z << std::endl;    // 1 2 3
```

---

**this 捕获**
`[this]` 或 `[*this]`
```cpp
class Counter {
    int count_ = 0;
public:
    auto getIncrementor() {
        // 捕获 this
        return [this]() { return ++count_; };
    }
    auto getIncrementorCopy() {
        // C++17：拷贝 *this
        return [*this]() mutable { return ++count_; };
    }
};
```

---

## mutable Lambda

**mutable 关键字**
`[<capture>](<params>) mutable { <body> }`
```cpp
int x = 10;
// mutable 允许修改值捕获的变量
auto counter = [x]() mutable {
    return ++x;    // 修改的是副本
};
std::cout << counter() << std::endl;    // 11
std::cout << counter() << std::endl;    // 12
std::cout << x << std::endl;           // 10（原变量未变）
```

---

## Lambda 与 STL

**for_each**
`std::for_each(<begin>, <end>, <lambda>)`
```cpp
std::vector<int> nums = {1, 2, 3, 4, 5};
std::for_each(nums.begin(), nums.end(), [](int n) {
    std::cout << n << " ";
});
```

---

**transform**
`std::transform(<begin>, <end>, <out>, <lambda>)`
```cpp
std::vector<int> nums = {1, 2, 3, 4, 5};
std::vector<int> squared(nums.size());
std::transform(nums.begin(), nums.end(), squared.begin(), [](int n) {
    return n * n;
});
// squared: {1, 4, 9, 16, 25}
```

---

**sort 自定义比较**
`std::sort(<begin>, <end>, <lambda>)`
```cpp
std::vector<int> nums = {5, 2, 8, 1, 9, 3};
// 升序排序
std::sort(nums.begin(), nums.end(), [](int a, int b) {
    return a < b;
});
// 降序排序
std::sort(nums.begin(), nums.end(), [](int a, int b) {
    return a > b;
});
```

---

**find_if**
`std::find_if(<begin>, <end>, <lambda>)`
```cpp
std::vector<int> nums = {1, 2, 3, 4, 5};
// 查找第一个大于 3 的元素
auto it = std::find_if(nums.begin(), nums.end(), [](int n) {
    return n > 3;
});
if (it != nums.end()) {
    std::cout << "Found: " << *it << std::endl;    // 4
}
```

---

**remove_if**
`std::remove_if(<begin>, <end>, <lambda>)`
```cpp
std::vector<int> nums = {1, 2, 3, 4, 5};
// 移除所有偶数
auto new_end = std::remove_if(nums.begin(), nums.end(), [](int n) {
    return n % 2 == 0;
});
nums.erase(new_end, nums.end());
// nums: {1, 3, 5}
```

---

## Lambda 存储与传递

**存储 Lambda**
`auto <var> = <lambda>;` 或 `std::function<<signature>> <var> = <lambda>;`
```cpp
#include <functional>
// 使用 auto
auto add = [](int a, int b) { return a + b; };
// 使用 std::function
std::function<int(int, int)> subtract = [](int a, int b) { return a - b; };
// 调用
std::cout << add(5, 3) << std::endl;       // 8
std::cout << subtract(5, 3) << std::endl;  // 2
```

---

**Lambda 作为函数参数**
`void <func>(std::function<<signature>> <callback>) { ... }`
```cpp
#include <functional>
void process(int value, std::function<void(int)> callback) {
    callback(value);
}
// 使用
int multiplier = 10;
process(5, [multiplier](int x) {
    std::cout << x * multiplier << std::endl;    // 50
});
```

---

**模板参数传递 Lambda**
`template <typename Func> void <func>(Func <callback>) { ... }`
```cpp
template <typename Func>
void process(int value, Func callback) {
    callback(value);
}
// 使用
process(5, [](int x) { std::cout << x << std::endl; });
```

---

## 泛型 Lambda（C++14）

**auto 参数**
`[<capture>](auto <param>) { <body> }`
```cpp
// C++14：泛型 Lambda
auto print = [](auto value) {
    std::cout << value << std::endl;
};
print(42);
print(3.14);
print("Hello");
// 多参数泛型
auto max = [](auto a, auto b) {
    return a > b ? a : b;
};
std::cout << max(10, 20) << std::endl;        // 20
std::cout << max(3.14, 2.71) << std::endl;    // 3.14
```

---

## Lambda 捕获初始化（C++14）

**捕获初始化**
`[<var> = <expr>]`
```cpp
// C++14：捕获初始化
auto lambda = [x = 10, y = 20]() {
    std::cout << x << " " << y << std::endl;
};
lambda();    // 10 20
// 移动捕获
auto ptr = std::make_unique<int>(42);
auto move_lambda = [p = std::move(ptr)]() {
    std::cout << *p << std::endl;
};
move_lambda();    // 42
```
