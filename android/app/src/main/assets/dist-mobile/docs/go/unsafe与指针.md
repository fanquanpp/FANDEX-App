# Go unsafe 与指针

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 指针类型

**指针声明**：获取变量地址
`var <变量> *<类型>` / `&<变量>`

```go
// 指针声明和取地址
var x int = 42;
var p *int = &x;
fmt.Println(*p); // 42
```

**空指针**：nil 指针
`var <变量> *<类型>`

```go
// nil 指针
var p *int;
if p == nil {
    fmt.Println("nil pointer");
}
```

**new 返回指针**：分配零值内存
`new(<类型>)`

```go
// new 分配并返回指针
p := new(int);
*p = 100;
```

---

## unsafe.Pointer

**Pointer 类型**：通用指针类型
`unsafe.Pointer(&<变量>)`

```go
// 转换为 unsafe.Pointer
var x int = 42;
p := unsafe.Pointer(&x);
```

**三种指针转换**：普通指针、uintptr、unsafe.Pointer
`(*<类型>)(unsafe.Pointer(<指针>))`

```go
// 指针类型转换
var x int64 = 42;
var y float64 = *(*float64)(unsafe.Pointer(&x));
```

**Pointer 与 uintptr 转换**：uintptr 可进行算术运算
`uintptr(unsafe.Pointer(&<变量>))`

```go
// 转换为 uintptr 进行地址运算
var x int = 42;
addr := uintptr(unsafe.Pointer(&x));
```

---

## unsafe.Sizeof

**获取大小**：变量占用的字节数
`unsafe.Sizeof(<变量>)`

```go
// 获取类型大小
fmt.Println(unsafe.Sizeof(int(0)));     // 8
fmt.Println(unsafe.Sizeof("hello"));    // 16
fmt.Println(unsafe.Sizeof(true));       // 1
```

**结构体大小**：包含 padding
`unsafe.Sizeof(<结构体>{})`

```go
// 结构体大小（含 padding）
type User struct {
    Age  int8;   // 1 + 7 padding
    Name string; // 16
}
fmt.Println(unsafe.Sizeof(User{})); // 24
```

---

## unsafe.Offsetof

**字段偏移量**：字段在结构体中的偏移
`unsafe.Offsetof(<结构体>.<字段>)`

```go
// 获取字段偏移量
type User struct {
    ID   int;
    Name string;
    Age  int;
}
fmt.Println(unsafe.Offsetof(User{}.ID));   // 0
fmt.Println(unsafe.Offsetof(User{}.Name)); // 8
fmt.Println(unsafe.Offsetof(User{}.Age));  // 24
```

**通过偏移访问字段**：指针运算
`(*<类型>)(unsafe.Pointer(<基址> + <偏移>))`

```go
// 通过偏移量访问字段
u := User{ID: 1, Name: "Alice", Age: 30};
base := unsafe.Pointer(&u);
idPtr := (*int)(unsafe.Pointer(base));
fmt.Println(*idPtr); // 1
```

---

## unsafe.Alignof

**对齐系数**：类型的对齐要求
`unsafe.Alignof(<变量>)`

```go
// 获取类型对齐系数
fmt.Println(unsafe.Alignof(int(0)));    // 8
fmt.Println(unsafe.Alignof(int8(0)));   // 1
fmt.Println(unsafe.Alignof(int32(0))); // 4
```

**结构体对齐**：最大字段的对齐系数
`unsafe.Alignof(<结构体>{})`

```go
// 结构体对齐系数
type S struct {
    A int8;   // align 1
    B int64;  // align 8
}
fmt.Println(unsafe.Alignof(S{})); // 8
```

---

## 指针运算

**uintptr 加法**：地址偏移
`uintptr + unsafe.Sizeof(<类型>) * <索引>`

```go
// 指针算术运算
arr := [5]int{10, 20, 30, 40, 50};
base := uintptr(unsafe.Pointer(&arr[0]));
elem := (*int)(unsafe.Pointer(base + unsafe.Sizeof(int(0))*2));
fmt.Println(*elem); // 30
```

**Add 函数**：Go 1.17+ 指针加法
`unsafe.Add(<指针>, <偏移>)`

```go
// Go 1.17+ 使用 unsafe.Add
arr := [5]int{10, 20, 30, 40, 50};
ptr := unsafe.Pointer(&arr[0]);
elem := (*int)(unsafe.Add(ptr, unsafe.Sizeof(int(0))*2));
fmt.Println(*elem); // 30
```

---

## Slice 操作

**SliceHeader 结构**：切片的内部表示
`reflect.SliceHeader`

```go
// 切片内部结构
type SliceHeader struct {
    Data uintptr;  // 指向底层数组
    Len  int;      // 长度
    Cap  int;      // 容量
}
```

**通过 unsafe 修改切片**：直接操作底层
`(*reflect.SliceHeader)(unsafe.Pointer(&<切片>))`

```go
// 通过 unsafe 修改切片长度
s := make([]int, 0, 10);
hdr := (*reflect.SliceHeader)(unsafe.Pointer(&s));
hdr.Len = 5; // 直接修改长度
```

---

## String 操作

**StringHeader 结构**：字符串的内部表示
`reflect.StringHeader`

```go
// 字符串内部结构
type StringHeader struct {
    Data uintptr;  // 指向字节数组
    Len  int;      // 长度
}
```

**String 与 []byte 零拷贝**：共享底层数据
`*(*string)(unsafe.Pointer(&<字节切片>))`

```go
// []byte 转 string 零拷贝（不安全）
b := []byte{'h', 'e', 'l', 'l', 'o'};
s := *(*string)(unsafe.Pointer(&b));
fmt.Println(s); // hello
```

**StringHeader 转换**：直接构造字符串
`reflect.StringHeader{ Data: <地址>, Len: <长度> }`

```go
// 通过 StringHeader 构造字符串
data := [...]byte{'h', 'i'};
hdr := reflect.StringHeader{
    Data: uintptr(unsafe.Pointer(&data[0])),
    Len:  2,
};
s := *(*string)(unsafe.Pointer(&hdr));
```

---

## unsafe.Pointer 转换规则

**安全转换**：*T1 -> *T2（相同大小）
`(*<T2>)(unsafe.Pointer(<*T1>))`

```go
// int32 与 float32 互转（相同大小）
var i int32 = 42;
f := *(*float32)(unsafe.Pointer(&i));
```

**uintptr 转换**：用于地址运算
`uintptr(unsafe.Pointer(&<变量>))`

```go
// 转换为 uintptr 进行地址运算
addr := uintptr(unsafe.Pointer(&x));
```

**unsafe.Pointer 转回**：还原为具体类型指针
`(*<类型>)(unsafe.Pointer(<uintptr>))`

```go
// uintptr 转回 Pointer
ptr := unsafe.Pointer(addr);
p := (*int)(ptr);
```

---

## Slice 转换

**[]T1 转 []T2**：不同类型切片转换
`*[]<T2>(unsafe.Pointer(&<切片>))`

```go
// []int 转 []int64（不安全）
src := []int{1, 2, 3, 4};
dst := *(*[]int64)(unsafe.Pointer(&src));
```

---

## 实际应用

**高性能字符串拼接**：避免拷贝
`strings.Builder + unsafe`

```go
// 使用 unsafe 优化字符串操作
func concat(a, b string) string {
    var builder strings.Builder;
    builder.Grow(len(a) + len(b));
    builder.WriteString(a);
    builder.WriteString(b);
    return builder.String();
}
```

**直接内存访问**：绕过类型系统
`(*[<大小>]<类型>)(unsafe.Pointer(&<变量>))`

```go
// 将结构体转为字节数组
type Data struct {
    A int32;
    B int32;
}
d := Data{A: 1, B: 2};
bytes := *(*[8]byte)(unsafe.Pointer(&d));
```
