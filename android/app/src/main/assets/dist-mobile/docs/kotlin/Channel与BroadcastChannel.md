
## 1. Channel

```kotlin
val channel = Channel<Int>()

launch { channel.send(1) }
val value = channel.receive()  // 1
```

### 1.1 容量类型

| 类型       | 说明               |
| ---------- | ------------------ |
| RENDEZVOUS | 无缓冲（默认）     |
| UNLIMITED  | 无限缓冲           |
| BUFFERED   | 固定缓冲（默认64） |
| CONFLATED  | 新值覆盖旧值       |

```kotlin
Channel<Int>(capacity = Channel.BUFFERED)
Channel<Int>(capacity = Channel.CONFLATED)
```

## 2. Channel 操作

```kotlin
// 遍历接收
for (item in channel) {
    println(item)
}

// produce 创建生产者
val producer = produce {
    repeat(10) { send(it) }
}

// consumeEach 消费
channel.consumeEach { println(it) }
```

## 3. BroadcastChannel（已废弃）

使用 SharedFlow 替代：

```kotlin
// 旧方式（已废弃）
// val broadcast = BroadcastChannel<Int>(1)

// 新方式
val broadcast = MutableSharedFlow<Int>(
    replay = 0,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
```
