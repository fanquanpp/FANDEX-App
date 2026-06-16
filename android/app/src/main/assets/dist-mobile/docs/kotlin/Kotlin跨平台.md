
## 1. Kotlin Multiplatform

```kotlin
// 共享代码
expect fun getPlatformName(): String

// 平台实现
// androidMain
actual fun getPlatformName() = "Android"

// iosMain
actual fun getPlatformName() = "iOS"

// jsMain
actual fun getPlatformName() = "JavaScript"
```

## 2. Kotlin/JS

```kotlin
// 调用 JavaScript API
external fun alert(message: String)

// 使用 DOM
document.getElementById("app")?.innerHTML = "<h1>Hello</h1>"
```

## 3. Kotlin/Native

```kotlin
// 直接编译为原生二进制
// 无需 JVM
// 适用于 iOS、嵌入式、WASM

// 内存管理：自动引用计数（ARC）
```

## 4. 共享模块策略

```
shared/
  commonMain/     # 所有平台共享
  androidMain/    # Android 特定
  iosMain/        # iOS 特定
  jsMain/         # JS 特定
  desktopMain/    # JVM Desktop 特定
```
