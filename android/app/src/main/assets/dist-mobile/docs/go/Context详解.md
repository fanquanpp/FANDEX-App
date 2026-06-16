
## 1. 基本用法

```go
ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
defer cancel()

result, err := fetchWithTimeout(ctx, url)
```

## 2. 传播取消

```go
func handler(ctx context.Context) {
  go func() {
    select {
    case <-ctx.Done():
      log.Println("Cancelled:", ctx.Err())
    case <-time.After(10 * time.Second):
      log.Println("Done")
    }
  }()
}
```
