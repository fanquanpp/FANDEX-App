
## 1. 核心装饰器

| 装饰器     | 说明               |
| ---------- | ------------------ |
| @Component | 声明自定义组件     |
| @Entry     | 标记页面入口       |
| @State     | 组件内状态（可变） |
| @Prop      | 父组件单向传递     |
| @Link      | 父子双向绑定       |
| @Provide   | 跨层级提供数据     |
| @Consume   | 跨层级消费数据     |
| @Watch     | 监听状态变化       |
| @Builder   | 轻量 UI 复用       |

## 2. @State

```typescript
@Entry
@Component
struct Counter {
  @State count: number = 0

  build() {
    Column() {
      Text(`Count: ${this.count}`)
      Button('+1')
        .onClick(() => this.count++)
    }
  }
}
```

## 3. @Prop 和 @Link

```typescript
// 父组件
@Entry
@Component
struct Parent {
  @State value: number = 0

  build() {
    Column() {
      Child({ count: this.value })       // @Prop 单向
      ChildLink({ count: $value })       // @Link 双向
    }
  }
}

// 子组件 - @Prop
@Component
struct Child {
  @Prop count: number  // 只读，父组件变化时更新

  build() { Text(`${this.count}`) }
}

// 子组件 - @Link
@Component
struct ChildLink {
  @Link count: number  // 可修改，双向同步

  build() {
    Button('+1').onClick(() => this.count++)
  }
}
```

## 4. @Builder

```typescript
@Builder function ItemView(text: string) {
  Row() {
    Text(text).fontSize(16)
  }.padding(10)
}

// 使用
build() {
  Column() {
    ItemView('Item 1')
    ItemView('Item 2')
  }
}
```
