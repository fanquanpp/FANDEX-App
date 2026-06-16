
## 1. 创建异步任务

```java
// 无返回值
CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
    // 异步任务
});

// 有返回值
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
    return "result";
});
```

## 2. 串行编排

```java
f2.thenApply(result -> result.toUpperCase())     // 转换
  .thenAccept(r -> System.out.println(r))        // 消费
  .thenRun(() -> System.out.println("done"));    // 执行
```

## 3. 组合编排

```java
// 两个任务都完成后合并
CompletableFuture<String> name = CompletableFuture.supplyAsync(() -> "Alice");
CompletableFuture<Integer> age = CompletableFuture.supplyAsync(() -> 30);

name.thenCombine(age, (n, a) -> n + " is " + a);

// 任一完成
CompletableFuture.anyOf(f1, f2, f3);

// 全部完成
CompletableFuture.allOf(f1, f2, f3);
```

## 4. 异常处理

```java
f2.exceptionally(ex -> "default")
  .handle((result, ex) -> ex != null ? "error" : result)
  .whenComplete((result, ex) -> { /* 最终处理 */ });
```
