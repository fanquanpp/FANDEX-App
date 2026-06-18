
## 概述

密封类（Sealed Classes）是 Java 17 正式引入的特性，允许开发者精确控制类的继承层次。密封类通过 `sealed` 关键字声明，并用 `permits` 子句明确列出允许继承的子类。与 `final`（完全禁止继承）和 `abstract`（任意继承）不同，密封类提供了一种中间方案：只允许特定的类继承。

密封类与模式匹配（Pattern Matching）配合使用，可以在编译时保证 `switch` 语句覆盖所有子类，消除遗漏分支的风险。

## 基础概念

### 声明语法

```java
// 密封类声明：sealed + permits
public sealed class Shape permits Circle, Rectangle, Triangle {
    // 密封类体
}

// 允许的子类必须是 final、sealed 或 non-sealed
public final class Circle extends Shape {
    private final double radius;
    public Circle(double radius) { this.radius = radius; }
    public double radius() { return radius; }
}

public final class Rectangle extends Shape {
    private final double width;
    private final double height;
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    public double width() { return width; }
    public double height() { return height; }
}

public final class Triangle extends Shape {
    private final double base;
    private final double height;
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    public double base() { return base; }
    public double height() { return height; }
}
```

### 子类修饰符规则

| 修饰符 | 含义 | 继承性 |
| ------ | ---- | ------ |
| `final` | 不可再被继承 | 完全封闭 |
| `sealed` | 继续密封，需指定 permits | 有限开放 |
| `non-sealed` | 回到开放继承 | 任意类可继承 |

```java
public sealed class Expr permits Const, UnaryOp, BinaryOp {
    // 密封表达式基类
}

public final class Const extends Expr {
    public final double value;
    public Const(double value) { this.value = value; }
}

public sealed class UnaryOp extends Expr permits Neg, Abs {
    public final Expr operand;
    public UnaryOp(Expr operand) { this.operand = operand; }
}

public final class Neg extends UnaryOp {
    public Neg(Expr operand) { super(operand); }
}

public final class Abs extends UnaryOp {
    public Abs(Expr operand) { super(operand); }
}

// non-sealed：开放继承，编译器不再追踪子类
public non-sealed class BinaryOp extends Expr {
    public final Expr left;
    public final Expr right;
    public final String operator;
    public BinaryOp(Expr left, String operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
```

### 密封接口

```java
public sealed interface JSON permits JSONObject, JSONArray, JSONPrimitive {
    String toJSONString();
}

public final class JSONObject implements JSON {
    private final Map<String, JSON> entries = new LinkedHashMap<>();
    public void put(String key, JSON value) { entries.put(key, value); }
    @Override
    public String toJSONString() {
        // 生成 JSON 对象字符串
        return entries.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":" + e.getValue().toJSONString())
            .collect(Collectors.joining(",", "{", "}"));
    }
}

public final class JSONArray implements JSON {
    private final List<JSON> items = new ArrayList<>();
    public void add(JSON value) { items.add(value); }
    @Override
    public String toJSONString() {
        return items.stream()
            .map(JSON::toJSONString)
            .collect(Collectors.joining(",", "[", "]"));
    }
}

public sealed interface JSONPrimitive extends JSON permits JSONString, JSONNumber, JSONBoolean, JSONNull {}

public record JSONString(String value) implements JSONPrimitive {
    @Override public String toJSONString() { return "\"" + value + "\""; }
}
public record JSONNumber(double value) implements JSONPrimitive {
    @Override public String toJSONString() { return String.valueOf(value); }
}
public record JSONBoolean(boolean value) implements JSONPrimitive {
    @Override public String toJSONString() { return String.valueOf(value); }
}
public record JSONNull() implements JSONPrimitive {
    @Override public String toJSONString() { return "null"; }
}
```

## 模式匹配

### instanceof 模式匹配（Java 16+）

```java
public double area(Shape shape) {
    if (shape instanceof Circle c) {
        return Math.PI * c.radius() * c.radius();
    } else if (shape instanceof Rectangle r) {
        return r.width() * r.height();
    } else if (shape instanceof Triangle t) {
        return 0.5 * t.base() * t.height();
    } else {
        // 密封类保证此处不可达，但 Java 16 的 instanceof 不支持穷举检查
        throw new IllegalArgumentException("未知形状");
    }
}
```

### switch 模式匹配（Java 21+）

```java
public double area(Shape shape) {
    // switch 模式匹配 + 穷举检查
    // 编译器验证所有密封子类均已覆盖，无需 default 分支
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> 0.5 * t.base() * t.height();
    };
}

// 带守卫条件（Guard Patterns）
public String describe(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 10 -> "大圆";
        case Circle c -> "小圆";
        case Rectangle r when r.width() == r.height() -> "正方形";
        case Rectangle r -> "长方形";
        case Triangle t -> "三角形";
    };
}
```

### Record 与密封类配合

Record 是密封类的最佳搭档，用于定义不可变的数据载体。

```java
// 用密封类 + Record 建立类型安全的 AST
public sealed interface ASTNode {
    record Literal(double value) implements ASTNode {}
    record Variable(String name) implements ASTNode {}
    record BinaryExpr(ASTNode left, String op, ASTNode right) implements ASTNode {}
    record UnaryExpr(String op, ASTNode operand) implements ASTNode {}
    record FunctionCall(String name, List<ASTNode> args) implements ASTNode {}
}

// 求值器：switch 穷举所有节点类型
public double evaluate(ASTNode node, Map<String, Double> env) {
    return switch (node) {
        case ASTNode.Literal l -> l.value();
        case ASTNode.Variable v -> env.getOrDefault(v.name(), 0.0);
        case ASTNode.BinaryExpr b -> switch (b.op()) {
            case "+" -> evaluate(b.left(), env) + evaluate(b.right(), env);
            case "-" -> evaluate(b.left(), env) - evaluate(b.right(), env);
            case "*" -> evaluate(b.left(), env) * evaluate(b.right(), env);
            case "/" -> evaluate(b.left(), env) / evaluate(b.right(), env);
            default -> throw new IllegalArgumentException("未知运算符: " + b.op());
        };
        case ASTNode.UnaryExpr u -> switch (u.op()) {
            case "-" -> -evaluate(u.operand(), env);
            case "+" -> evaluate(u.operand(), env);
            default -> throw new IllegalArgumentException("未知运算符: " + u.op());
        };
        case ASTNode.FunctionCall f -> {
            // 函数调用求值逻辑
            double[] args = f.args().stream()
                .mapToDouble(arg -> evaluate(arg, env))
                .toArray();
            yield applyFunction(f.name(), args);
        }
    };
}
```

## 实践指南

### 密封类 vs 枚举

| 维度 | 枚举 | 密封类 |
| ---- | ---- | ------ |
| 实例数量 | 固定且有限 | 固定类型，但实例不限 |
| 携带数据 | 通过字段，所有实例结构相同 | 每个子类可携带不同数据 |
| 继承 | 不可继承 | 受控继承 |
| 适用场景 | 固定常量集合 | 固定类型集合，各类型数据不同 |

### 何时使用密封类

- 表达式树 / AST 节点类型
- 状态机状态定义
- 结果类型（成功 / 失败，各携带不同数据）
- 消息协议中的消息类型
- 领域模型中的值对象

### 与 Spring Boot 集成

```java
// API 响应密封接口
public sealed interface ApiResponse permits SuccessResponse, ErrorResponse {
    int status();
}

public record SuccessResponse<T>(int status, T data) implements ApiResponse {
    public SuccessResponse(T data) { this(200, data); }
}

public record ErrorResponse(int status, String message, String code) implements ApiResponse {
    public ErrorResponse(String message, String code) { this(400, message, code); }
}

// Controller 使用
@RestController
public class UserController {
    @GetMapping("/users/{id}")
    public ApiResponse getUser(@PathVariable Long id) {
        return userService.findById(id)
            .<ApiResponse>map(user -> new SuccessResponse<>(user))
            .orElseGet(() -> new ErrorResponse("用户不存在", "USER_NOT_FOUND"));
    }
}
```

## 最佳实践

- 密封类与 Record 配合使用，简洁且类型安全
- 优先使用 `final` 子类，只在需要继续扩展时使用 `sealed`
- 谨慎使用 `non-sealed`，它会打破密封保证
- 在 switch 中利用穷举检查，避免遗漏分支
- 密封类的子类应与密封类在同一包或同一模块中
- 使用密封类替代继承层次过深的抽象类体系

## 参考资料

- [JEP 409](https://openjdk.org/jeps/409) - Sealed Classes
- [JEP 441](https://openjdk.org/jeps/441) - Pattern Matching for switch
- [JEP 395](https://openjdk.org/jeps/395) - Records
- [Java 17 语言特性](https://openjdk.org/projects/jdk/17/)
