# constexpr与编译期计算

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## constexpr 变量

**constexpr 变量声明**
`constexpr <type> <var> = <constant_expr>;`
```cpp
// 编译期常量
constexpr int MAX_SIZE = 1024;
constexpr double PI = 3.14159265358979;
constexpr const char* VERSION = "1.0.0";
// 用于数组大小
int buffer[MAX_SIZE];
// 用于模板参数
template <int N> struct Array { int data[N]; };
Array<MAX_SIZE> arr;
```

---

## constexpr 函数

**constexpr 函数定义**
`constexpr <return_type> <func>(<params>) { ... }`
```cpp
// C++11：constexpr 函数只能有 return 语句
constexpr int factorial(int n) {
    return n <= 1 ? 1 : n * factorial(n - 1);
}
// C++14：允许更多语句
constexpr int sum(int n) {
    int result = 0;
    for (int i = 1; i <= n; ++i) {
        result += i;
    }
    return result;
}
// 使用
constexpr int fact5 = factorial(5);    // 120，编译期计算
constexpr int sum10 = sum(10);         // 55，编译期计算
int n = 5;
int result = factorial(n);             // 运行期计算
```

---

**constexpr 递归函数**
`constexpr <return_type> <func>(<params>) { return <base_case> ? <val> : <recursive_call>; }`
```cpp
// 编译期递归计算斐波那契
constexpr int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}
// 使用
constexpr int fib10 = fibonacci(10);    // 55
```

---

## constexpr 与 const

**constexpr 与 const 对比**
`constexpr <type> <var> = <expr>; // 编译期 | const <type> <var> = <expr>; // 运行期或编译期`
```cpp
// constexpr：一定是编译期常量
constexpr int x = 10;
// const：可能是编译期或运行期常量
const int y = 20;           // 编译期
int n = 30;
const int z = n;            // 运行期
// constexpr 可以隐式转换为 const
const int a = x;            // 正确
// const 不能隐式转换为 constexpr
// constexpr int b = z;    // 错误
```

---

## constexpr 类

**constexpr 构造函数**
`constexpr <ClassName>(<params>) : <member>(<value>), ... {}`
```cpp
class Point {
    double x_, y_;
public:
    // constexpr 构造函数
    constexpr Point(double x = 0, double y = 0) : x_(x), y_(y) {}
    // constexpr 成员函数
    constexpr double x() const { return x_; }
    constexpr double y() const { return y_; }
    constexpr double distance_to(const Point& other) const {
        double dx = x_ - other.x_;
        double dy = y_ - other.y_;
        return dx * dx + dy * dy;    // 返回距离平方
    }
};
// 编译期构造
constexpr Point p1(0, 0);
constexpr Point p2(3, 4);
constexpr double dist_sq = p1.distance_to(p2);    // 25
```

---

**constexpr 成员函数**
`constexpr <return_type> <func>() const { ... }`
```cpp
class Vector {
    double x_, y_, z_;
public:
    constexpr Vector(double x, double y, double z) : x_(x), y_(y), z_(z) {}
    constexpr double dot(const Vector& other) const {
        return x_ * other.x_ + y_ * other.y_ + z_ * other.z_;
    }
    constexpr Vector cross(const Vector& other) const {
        return Vector(
            y_ * other.z_ - z_ * other.y_,
            z_ * other.x_ - x_ * other.z_,
            x_ * other.y_ - y_ * other.x_
        );
    }
};
```

---

## constexpr if（C++17）

**编译期条件判断**
`if constexpr (<condition>) { ... } else { ... }`
```cpp
template <typename T>
void process(T value) {
    if constexpr (std::is_integral_v<T>) {
        std::cout << "Integral: " << value << std::endl;
    } else if constexpr (std::is_floating_point_v<T>) {
        std::cout << "Floating: " << value << std::endl;
    } else {
        std::cout << "Other type" << std::endl;
    }
}
// 使用
process(42);        // 输出 Integral: 42
process(3.14);      // 输出 Floating: 3.14
process("hello");   // 输出 Other type
```

---

## consteval（C++20）

**consteval 函数**
`consteval <return_type> <func>(<params>) { ... }`
```cpp
// C++20：consteval 强制编译期执行
consteval int compile_time_square(int x) {
    return x * x;
}
// 必须在编译期调用
constexpr int result = compile_time_square(5);    // 25
// int n = 5;
// int r = compile_time_square(n);    // 错误：n 不是编译期常量
```

---

## constinit（C++20）

**constinit 变量**
`constinit <type> <var> = <constant_expr>;`
```cpp
// C++20：constinit 确保编译期初始化
constinit int global_var = 42;
// 与 constexpr 的区别
// constexpr：既是编译期常量，又是只读
// constinit：编译期初始化，但可修改
global_var = 100;    // 正确：可以修改
```

---

## 编译期计算示例

**编译期字符串操作**
`constexpr <type> <func>(const char* <str>) { ... }`
```cpp
// 编译期计算字符串长度
constexpr size_t str_len(const char* str) {
    size_t len = 0;
    while (str[len] != '\0') {
        ++len;
    }
    return len;
}
// 使用
constexpr const char* msg = "Hello";
constexpr size_t len = str_len(msg);    // 5
static_assert(len == 5, "Length must be 5");
```

---

**编译期数组求和**
`constexpr <type> <func>(const std::array<<Type>, <Size>>& <arr>) { ... }`
```cpp
#include <array>
template <typename T, size_t N>
constexpr T array_sum(const std::array<T, N>& arr) {
    T sum = T();
    for (size_t i = 0; i < N; ++i) {
        sum += arr[i];
    }
    return sum;
}
// 使用
constexpr std::array<int, 5> arr = {1, 2, 3, 4, 5};
constexpr int sum = array_sum(arr);    // 15
```

---

## static_assert

**编译期断言**
`static_assert(<constant_expr>, "<message>");`
```cpp
// 编译期检查
static_assert(sizeof(int) == 4, "int must be 4 bytes");
static_assert(sizeof(void*) == 8, "64-bit system required");
// 模板参数检查
template <int N>
struct Buffer {
    static_assert(N > 0, "Buffer size must be positive");
    int data[N];
};
// 使用 constexpr 变量
constexpr int SIZE = 10;
static_assert(SIZE > 0, "SIZE must be positive");
```

---

## 类型 traits（编译期）

**类型检查**
`std::is_integral_v<T> | std::is_floating_point_v<T> | std::is_pointer_v<T>`
```cpp
#include <type_traits>
// 类型检查
static_assert(std::is_integral_v<int>, "int is integral");
static_assert(std::is_floating_point_v<double>, "double is floating point");
static_assert(std::is_pointer_v<int*>, "int* is pointer");
// 条件编译
template <typename T>
void process(T value) {
    if constexpr (std::is_integral_v<T>) {
        std::cout << "Integer: " << value << std::endl;
    }
}
```

---

**类型转换**
`std::remove_const_t<T> | std::add_const_t<T> | std::remove_reference_t<T>`
```cpp
#include <type_traits>
// 移除 const
using NonConst = std::remove_const_t<const int>;    // int
// 添加 const
using Const = std::add_const_t<int>;                // const int
// 移除引用
using NoRef = std::remove_reference_t<int&>;        // int
// 获取底层类型
using Decay = std::decay_t<int[5>;                 // int*
```
