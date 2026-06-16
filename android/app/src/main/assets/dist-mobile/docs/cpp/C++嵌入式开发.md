
## 1. 嵌入式 C++ 限制

```cpp
// 避免异常（增加代码体积）
// -fno-exceptions

// 避免RTTI
// -fno-rtti

// 使用 constexpr 代替运行时计算
constexpr int BUFFER_SIZE = 1024;
alignas(16) uint8_t buffer[BUFFER_SIZE];

// 静态分配
class ObjectPool {
  std::array<Object, MAX_OBJECTS> pool_;
  std::bitset<MAX_OBJECTS> used_;
};
```
