
## 1. Flow（冷流）

```kotlin
val numbers = flow {
    for (i in 1..5) {
        emit(i)
        delay(100)
    }
}

// 每个收集者独立执行
numbers.collect { println(it) }  // 1,2,3,4,5
numbers.collect { println(it) }  // 1,2,3,4,5（重新执行）
```

## 2. SharedFlow（热流）

```kotlin
val sharedFlow = MutableSharedFlow<String>()

// 发射
sharedFlow.emit("event")

// 多个收集者共享
sharedFlow.collect { println("A: $it") }
sharedFlow.collect { println("B: $it") }
```

配置：

```kotlin
MutableSharedFlow<String>(
    replay = 0,        // 新订阅者重放数量
    extraBufferCapacity = 64,
    onBufferOverflow = BufferOverflow.SUSPEND
)
```

## 3. StateFlow

```kotlin
val state = MutableStateFlow(0)  // 必须有初始值

state.value = 1  // 更新值
state.collect { println(it) }  // 总是能获取最新值
```

## 4. 对比

| 特性     | Flow     | SharedFlow  | StateFlow            |
| -------- | -------- | ----------- | -------------------- |
| 冷/热    | 冷       | 热          | 热                   |
| 多收集者 | 各自执行 | 共享        | 共享                 |
| 初始值   | 无       | 无          | 必须                 |
| 值缓存   | 无       | replay 配置 | 最新值               |
| 去重     | 无       | 无          | 自动（值相同不发射） |
