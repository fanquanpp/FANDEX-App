
## 概述

Go 1.18 引入了泛型（Generics），这是 Go 语言自诞生以来最重要的特性之一。泛型允许编写与类型无关的代码，通过类型参数（Type Parameters）实现代码复用，同时保持类型安全。Go 的泛型设计遵循"少即是多"的理念，没有 C++ 模板那样复杂的元编程能力，但足以解决日常开发中的代码重复问题。

## 基础概念

### 类型参数语法

```go
// 泛型函数：类型参数放在方括号中
func Print[T any](value T) {
    fmt.Println(value)
}

// 调用时可以省略类型参数（编译器自动推断）
Print(42)          // T 推断为 int
Print("hello")     // T 推断为 string
Print[int](42)     // 显式指定类型参数
```

### 类型约束

类型约束限制类型参数可以接受哪些类型，使用 `interface` 定义。

| 内置约束 | 含义 |
| -------- | ---- |
| `any` | 任意类型（`interface{}` 的别名） |
| `comparable` | 支持 `==` 和 `!=` 的类型 |
| `signed` | 有符号整数 |
| `unsigned` | 无符号整数 |
| `integer` | 所有整数 |
| `float` | 浮点数 |
| `number` | 所有数值 |
| `ordered` | 支持 `<` `>` `<=` `>=` 的类型 |

```go
import "golang.org/x/exp/constraints"

// 使用 comparable 约束：要求类型可比较
func Contains[T comparable](slice []T, target T) bool {
    for _, v := range slice {
        if v == target {
            return true
        }
    }
    return false
}

// 使用 constraints.Ordered 约束：要求类型可排序
func Max[T constraints.Ordered](a, b T) T {
    if a > b {
        return a
    }
    return b
}
```

### 自定义类型约束

```go
// 方式一：接口约束
type Stringer interface {
    String() string
}

func PrintAll[T Stringer](items []T) {
    for _, item := range items {
        fmt.Println(item.String())
    }
}

// 方式二：联合类型约束（类型集合）
type Number interface {
    int | int8 | int16 | int32 | int64 |
    float32 | float64
}

func Sum[T Number](numbers []T) T {
    var total T
    for _, n := range numbers {
        total += n
    }
    return total
}

// 方式三：底层类型约束
type Integer interface {
    ~int | ~int8 | ~int16 | ~int32 | ~int64
}

// ~ 表示底层类型为 int 的自定义类型也满足约束
type MyInt int // MyInt 的底层类型是 int，满足 Integer 约束
```

## 泛型实战

### 泛型切片工具

```go
// 过滤
func Filter[T any](slice []T, predicate func(T) bool) []T {
    result := make([]T, 0, len(slice))
    for _, v := range slice {
        if predicate(v) {
            result = append(result, v)
        }
    }
    return result
}

// 映射
func Map[T, U any](slice []T, transform func(T) U) []U {
    result := make([]U, len(slice))
    for i, v := range slice {
        result[i] = transform(v)
    }
    return result
}

// 归约
func Reduce[T, U any](slice []T, initial U, reducer func(U, T) U) U {
    result := initial
    for _, v := range slice {
        result = reducer(result, v)
    }
    return result
}

// 使用示例
nums := []int{1, 2, 3, 4, 5}
evens := Filter(nums, func(n int) bool { return n%2 == 0 })
doubled := Map(nums, func(n int) int { return n * 2 })
sum := Reduce(nums, 0, func(acc, n int) int { return acc + n })
```

### 泛型栈

```go
type Stack[T any] struct {
    items []T
}

func NewStack[T any]() *Stack[T] {
    return &Stack[T]{items: make([]T, 0)}
}

func (s *Stack[T]) Push(item T) {
    s.items = append(s.items, item)
}

func (s *Stack[T]) Pop() (T, bool) {
    if len(s.items) == 0 {
        var zero T
        return zero, false
    }
    top := s.items[len(s.items)-1]
    s.items = s.items[:len(s.items)-1]
    return top, true
}

func (s *Stack[T]) Peek() (T, bool) {
    if len(s.items) == 0 {
        var zero T
        return zero, false
    }
    return s.items[len(s.items)-1], true
}

func (s *Stack[T]) Len() int {
    return len(s.items)
}
```

### 泛型键值缓存

```go
type Cache[K comparable, V any] struct {
    data map[K]V
    mu   sync.RWMutex
}

func NewCache[K comparable, V any]() *Cache[K, V] {
    return &Cache[K, V]{
        data: make(map[K]V),
    }
}

func (c *Cache[K, V]) Get(key K) (V, bool) {
    c.mu.RLock()
    defer c.mu.RUnlock()
    val, ok := c.data[key]
    return val, ok
}

func (c *Cache[K, V]) Set(key K, value V) {
    c.mu.Lock()
    defer c.mu.Unlock()
    c.data[key] = value
}

func (c *Cache[K, V]) Delete(key K) {
    c.mu.Lock()
    defer c.mu.Unlock()
    delete(c.data, key)
}
```

## 类型收窄

类型收窄（Type Narrowing）在泛型中通过类型断言实现，但应谨慎使用。

```go
// 不推荐：过度使用类型断言
func Process[T any](value T) string {
    switch v := any(value).(type) {
    case int:
        return fmt.Sprintf("整数: %d", v)
    case string:
        return fmt.Sprintf("字符串: %s", v)
    default:
        return fmt.Sprintf("其他: %v", v)
    }
}

// 推荐：通过约束限制类型范围，避免运行时类型判断
type Describable interface {
    Describe() string
}

func Describe[T Describable](value T) string {
    return value.Describe()
}
```

## 最佳实践

- 泛型用于算法和数据结构，不要用于业务逻辑
- 优先使用接口，只有当接口无法满足时才使用泛型
- 类型约束尽量窄，避免使用 `any` 作为万能约束
- 不要为了使用泛型而使用泛型，简单场景用具体类型更清晰
- 泛型函数的代码量不应超过非泛型版本节省的重复代码量
- 使用 `~T` 语法保护底层类型约束，避免自定义类型被排除

## 常见陷阱

| 陷阱 | 说明 | 解决方案 |
| ---- | ---- | -------- |
| 方法不支持额外类型参数 | Go 泛型方法不能引入新的类型参数 | 改用包级泛型函数 |
| 泛型与接口混淆 | 泛型是编译时多态，接口是运行时多态 | 需要运行时多态用接口，编译时类型安全用泛型 |
| 约束过宽 | `any` 约束导致无法使用任何操作 | 使用具体约束（如 `comparable`、`Number`） |
| 忽略底层类型 | `int` 约束不匹配 `type MyInt int` | 使用 `~int` 语法 |

## 参考资料

- [Go 泛型提案](https://go.dev/doc/design/generics-proposal)
- [Go 泛型教程](https://go.dev/doc/tutorial/generics)
- [Go 1.18 Release Notes](https://go.dev/doc/go1.18)
