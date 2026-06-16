
## 1. Channel 结构

```go
type hchan struct {
  qcount   uint
  dataqsiz uint
  buf      unsafe.Pointer
  sendx    uint
  recvx    uint
  sendq    waitq
  recvq    waitq
  lock     mutex
}
```

## 2. 发送与接收

- 无缓冲：发送方阻塞直到接收方就绪
- 有缓冲：缓冲区满时发送方阻塞
- 关闭 channel：接收方获取零值

## 3. select 实现

随机选择一个就绪的 case 执行。
