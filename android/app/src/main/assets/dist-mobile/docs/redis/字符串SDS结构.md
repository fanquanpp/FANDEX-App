# 字符串 SDS 结构

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 结构定义

**sdshdr 结构定义：定义不同长度级别的 SDS 头部**
`struct __attribute__((packed)) sdshdr<N> { uint<N>_t len; uint<N>_t alloc; unsigned char flags; char buf[]; }`
```c
// Redis 5.0+ sdshdr 结构（按长度分5种）
struct __attribute__((packed)) sdshdr8 {
    uint8_t  len;         // 已使用长度（不含\0）
    uint8_t  alloc;       // 总分配容量（不含\0和header）
    unsigned char flags;   // 类型标识：SDS_TYPE_8
    char     buf[];       // 实际数据（柔性数组）
};

struct __attribute__((packed)) sdshdr16 {
    uint16_t len;
    uint16_t alloc;
    unsigned char flags;
    char buf[];
};

// 同理 sdshdr32, sdshdr64
```

---

## 内存布局

**SDS 内存布局：展示 sdshdr8 的字节排列**
`[len][alloc][flags][buf[]][\0]`
```c
// SDS 内存布局 (sdshdr8):
// ┌──────┬───────┬───────┬──────────────────────────┬────┐
// │ len  │ alloc │ flags │          buf[]           │ \0 │
// │  5   │  10   │  s8   │ 'H','e','l','l','o',... │ 0  │
// └──────┴───────┴───────┴──────────────────────────┴────┘
//   1字节  1字节   1字节         11字节(alloc+1)
// len = 5:   已使用5字节
// alloc = 10: 总容量10字节（不含header和\0）
// 剩余空间 = alloc - len = 5字节
```

---

## 预分配策略

**空间预分配规则：扩容时额外分配空间**
`if (newlen < SDS_MAX_PREALLOC) newlen *= 2; else newlen += SDS_MAX_PREALLOC;`
```c
// 规则1: 修改后 len < 1MB
//   预分配: alloc = len（翻倍分配）
//   示例: len=10 → 修改后 len=15 → alloc=30
//
// 规则2: 修改后 len >= 1MB
//   预分配: alloc = len + 1MB（固定追加1MB）
//   示例: len=3MB → 修改后 len=5MB → alloc=6MB
```

**sdsMakeRoomFor 源码：扩容核心函数**
`sds sdsMakeRoomFor(sds s, size_t addlen)`
```c
// sds.c - sdsMakeRoomFor
sds sdsMakeRoomFor(sds s, size_t addlen) {
    size_t free = sdsavail(s);  // 剩余空间
    if (free >= addlen) return s;  // 空间足够，直接返回

    size_t len = sdslen(s);
    size_t newlen = len + addlen;

    // 预分配策略
    if (newlen < SDS_MAX_PREALLOC)  // SDS_MAX_PREALLOC = 1MB
        newlen *= 2;               // 翻倍
    else
        newlen += SDS_MAX_PREALLOC; // +1MB

    return sds_realloc(s, newlen);
}
```

---

## 惰性删除

**sdstrim 源码：缩短字符串不立即释放内存**
`sds sdstrim(sds s, const char *cset)`
```c
// sds.c - sdstrim
sds sdstrim(sds s, const char *cset) {
    // ... 删除首尾匹配字符
    // 不调用 realloc 释放内存
    // 仅更新 len 和 free
    sdssetlen(s, newlen);  // 更新 len
    return s;
}
```

**惰性删除示例：仅更新 len 不释放内存**
`sdstrim(s, "ld")`
```c
// SDS: "hello world" (len=11, alloc=11)
//
// 执行: sdstrim(s, "ld")  → 删除首尾的 'l' 和 'd'
// 结果: "hello wor" (len=9, alloc=11)
//       剩余空间 = 2 字节，未释放
//
// 执行: sdsRemoveFreeSpace(s)  → 显式释放
// 结果: "hello wor" (len=9, alloc=9)
```

**真正释放时机：触发内存回收的条件**
`sdsRemoveFreeSpace(s) | 键被删除 | 内存淘汰 | 对象重写`
```c
// 1. 显式调用 sdsRemoveFreeSpace
// 2. 键被删除时，整个 SDS 被释放
// 3. Redis 内存淘汰时
// 4. 使用 SDS 的对象被重写时
```

---

## 二进制安全

**sdsnewlen 创建：存储包含 \0 的二进制数据**
`sds sdsnewlen(const void *init, size_t initlen)`
```c
// SDS 可以存储任意二进制数据
sds s = sdsnewlen("hello\0world", 11);  // len=11
printf("%zu", sdslen(s));  // 输出: 11

// buf 中: 'h','e','l','l','o','\0','w','o','r','l','d','\0'
//          ↑ 数据中的\0           ↑ 结尾的\0（兼容C函数）
```

**兼容 C 字符串函数：buf 末尾保留 \0**
`sdsnew("hello")`
```c
// SDS 的 buf 末尾始终保留 \0，可以直接使用 C 字符串函数
sds s = sdsnew("hello");
printf("%s", s);  // 直接传给 printf，兼容 C 字符串
strcmp(s, "hello");  // 可以使用 strcmp
```

---

## 类型选择

**sdsReqType 源码：根据字符串长度选择 SDS 类型**
`static inline char sdsReqType(size_t string_size)`
```c
// sds.c - sdsReqType
static inline char sdsReqType(size_t string_size) {
    if (string_size < 1 << 5)  return SDS_TYPE_5;
    if (string_size < 1 << 8)  return SDS_TYPE_8;
    if (string_size < 1 << 16) return SDS_TYPE_16;
    if (string_size < 1ll << 32) return SDS_TYPE_32;
    return SDS_TYPE_64;
}
```
