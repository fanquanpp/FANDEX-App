# 枚举与typedef

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 枚举定义

**基本枚举：默认从 0 开始递增**
`enum <Name> { <MEM1>, <MEM2>, ... };`
```c
enum Color {
    RED,     // 默认值 0
    GREEN,   // 默认值 1
    BLUE     // 默认值 2
};
// 使用
enum Color favorite = GREEN;
printf("GREEN = %d\n", GREEN);    // 输出 1
```

---

**手动指定枚举值**
`enum <Name> { <MEM1> = <val>, <MEM2>, ... };`
```c
// 默认从0开始递增
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN };
// 手动指定值
enum HttpStatus {
    OK = 200,
    CREATED = 201,
    BAD_REQUEST = 400,
    NOT_FOUND = 404,
    INTERNAL_ERROR = 500
};
// 部分指定：未指定的值自动递增
enum Priority {
    LOW = 1,
    MEDIUM,     // 自动为 2
    HIGH,       // 自动为 3
    URGENT = 10,
    CRITICAL    // 自动为 11
};
// 可以有重复的值
enum Direction {
    UP = 1,
    DOWN = -1,
    LEFT = -2,
    RIGHT = 2
};
```

---

**枚举在 switch 中使用**
`switch (<enum_var>) { case <MEM>: ... break; ... }`
```c
enum Color favorite = GREEN;
switch (favorite) {
    case RED:   printf("红色\n"); break;
    case GREEN: printf("绿色\n"); break;
    case BLUE:  printf("蓝色\n"); break;
}
```

---

## 枚举与 typedef 结合

**使用 typedef 简化枚举类型名**
`typedef enum { <MEMBERS> } <Name>;`
```c
typedef enum {
    STATE_IDLE,
    STATE_RUNNING,
    STATE_PAUSED,
    STATE_STOPPED
} State;
// 使用时不需要 enum 前缀
State current_state = STATE_IDLE;
// 转换为字符串
const char *state_to_string(State s) {
    switch (s) {
        case STATE_IDLE:    return "空闲";
        case STATE_RUNNING: return "运行中";
        case STATE_PAUSED:  return "已暂停";
        case STATE_STOPPED: return "已停止";
        default:            return "未知";
    }
}
```

---

## typedef 使用

**为基本类型创建别名**
`typedef <existing_type> <new_name>;`
```c
typedef unsigned long ulong;
typedef unsigned char byte;
// 使用
ulong big_num = 123456789UL;
byte data[4] = {0x01, 0x02, 0x03, 0x04};
```

---

**为结构体创建别名**
`typedef struct { <members> } <Name>;`
```c
typedef struct {
    double x;
    double y;
} Point;
// 使用
Point p = {1.0, 2.0};
printf("点: (%.1f, %.1f)\n", p.x, p.y);
```

---

**为函数指针创建别名**
`typedef return_type (*<Name>)(<params>);`
```c
typedef int (*Comparator)(const void *, const void *);
int ascending(const void *a, const void *b) {
    return *(int *)a - *(int *)b;
}
// 使用函数指针作为参数
void sort_array(int *arr, int n, Comparator cmp) {
    qsort(arr, n, sizeof(int), cmp);
}
```

---

**为数组类型创建别名**
`typedef <type> <Name>[<size>];`
```c
typedef int IntArray[10];
typedef char Name[32];
// 使用
IntArray scores = {90, 85, 92, 78, 95, 88, 76, 91, 87, 83};
Name student = "张三";
```

---

## 枚举位标志

**使用枚举实现位标志**
`typedef enum { <FLAG1> = 1 << 0, <FLAG2> = 1 << 1, ... } <Name>;`
```c
typedef enum {
    PERM_READ    = 1 << 0,    // 1
    PERM_WRITE   = 1 << 1,    // 2
    PERM_EXECUTE = 1 << 2,    // 4
    PERM_DELETE  = 1 << 3     // 8
} Permission;
// 检查权限
int has_permission(int perms, Permission perm) {
    return (perms & perm) != 0;
}
// 添加权限
int add_permission(int perms, Permission perm) {
    return perms | perm;
}
// 移除权限
int remove_permission(int perms, Permission perm) {
    return perms & ~perm;
}
// 使用
int user_perms = PERM_READ | PERM_WRITE;
if (has_permission(user_perms, PERM_READ)) { /* 有读权限 */ }
```

---

## 可移植类型定义

**使用 typedef 定义平台无关类型**
`typedef <stdint_type> <short_name>;`
```c
#include <stdint.h>
typedef uint8_t  u8;
typedef uint16_t u16;
typedef uint32_t u32;
typedef uint64_t u64;
typedef int8_t  s8;
typedef int16_t s16;
typedef int32_t s32;
typedef int64_t s64;
// 使用
u8 byte_val = 255;
u32 counter = 1000000;
s64 timestamp = 1700000000LL;
```

---

## X-Macro 技巧

**自动生成枚举和字符串映射**
`#define <LIST> X(<item>) X(<item>) ... / typedef enum { #define X(name) <prefix>##name, <LIST> #undef X } <Name>;`
```c
// 定义枚举项列表（单一定义点）
#define FRUIT_LIST \
    X(APPLE)       \
    X(BANANA)      \
    X(CHERRY)      \
    X(DURIAN)      \
    X(ELDERBERRY)
// 生成枚举定义
typedef enum {
    #define X(name) FRUIT_##name,
    FRUIT_LIST
    #undef X
    FRUIT_COUNT    // 自动计算枚举项数量
} Fruit;
// 生成字符串数组
static const char *fruit_names[] = {
    #define X(name) #name,
    FRUIT_LIST
    #undef X
};
// 使用
const char *fruit_to_string(Fruit f) {
    if (f >= 0 && f < FRUIT_COUNT) {
        return fruit_names[f];
    }
    return "未知";
}
```
