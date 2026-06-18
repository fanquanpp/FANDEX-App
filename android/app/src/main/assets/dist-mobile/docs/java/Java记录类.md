# Java 记录类

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 记录类定义

**基本记录类**：Java 14+，简化数据载体类
`public record <记录名>(<类型> <字段1>, <类型> <字段2>) { }`
```java
public record Point(int x, int y) { }

// 使用
Point p = new Point(10, 20);
int x = p.x();  // 访问字段
int y = p.y();
System.out.println(p);  // Point[x=10, y=20]
```

---

**多字段记录类**：包含多个字段
`public record <记录名>(<类型1> <字段1>, <类型2> <字段2>, ...) { }`
```java
public record User(String name, int age, String email) { }

// 使用
User user = new User("Alice", 25, "alice@example.com");
String name = user.name();
int age = user.age();
String email = user.email();
```

---

## 紧凑构造器

**紧凑构造器**：用于参数验证
`public <记录名> { if (<条件>) throw new <异常>(); }`
```java
public record Range(int start, int end) {
    public Range {
        if (start > end) {
            throw new IllegalArgumentException("Start must be less than or equal to end");
        }
    }
}

// 使用
Range range = new Range(1, 10);  // 正确
// Range invalid = new Range(10, 1);  // 抛出异常
```

---

## 自定义构造器

**规范构造器**：完整自定义构造器
`public <记录名>(<类型1> <字段1>, <类型2> <字段2>) { this.<字段1> = <值>; ... }`
```java
public record Person(String name, int age) {
    public Person(String name, int age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.name = name;
        this.age = age;
    }
}
```

---

**自定义规范构造器**：修改字段值
`public <记录名>(<类型1> <字段1>, <类型2> <字段2>) { this.<字段1> = <规范化值>; ... }`
```java
public record Email(String address) {
    public Email {
        address = address.toLowerCase().trim();  // 规范化邮箱地址
    }
}

// 使用
Email email = new Email("  ALICE@EXAMPLE.COM  ");
System.out.println(email.address());  // alice@example.com
```

---

## 添加方法

**实例方法**：在记录类中添加方法
`public <返回类型> <方法名>() { ... }`
```java
public record Rectangle(double width, double height) {
    public double area() {
        return width * height;
    }

    public double perimeter() {
        return 2 * (width + height);
    }

    public boolean isSquare() {
        return width == height;
    }
}

// 使用
Rectangle rect = new Rectangle(5.0, 3.0);
System.out.println(rect.area());       // 15.0
System.out.println(rect.perimeter());  // 16.0
System.out.println(rect.isSquare());  // false
```

---

**静态方法**：在记录类中添加静态方法
`public static <返回类型> <方法名>(<参数>) { ... }`
```java
public record Point(int x, int y) {
    public static Point origin() {
        return new Point(0, 0);
    }

    public static double distance(Point p1, Point p2) {
        int dx = p2.x() - p1.x();
        int dy = p2.y() - p1.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
}

// 使用
Point origin = Point.origin();
Point p1 = new Point(3, 4);
double dist = Point.distance(origin, p1);  // 5.0
```

---

## 实现接口

**记录类实现接口**：实现 Comparable 接口
`public record <记录名>(<字段>) implements <接口> { @Override ... }`
```java
public record Student(String name, int score) implements Comparable<Student> {
    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.score, other.score);
    }
}

// 使用
List<Student> students = Arrays.asList(
    new Student("Alice", 85),
    new Student("Bob", 92),
    new Student("Charlie", 78)
);
Collections.sort(students);
students.forEach(s -> System.out.println(s.name() + ": " + s.score()));
```

---

## 记录类与继承

**记录类不可继承**：记录类是 final 的
`public record <记录名>(<字段>) { }` | `// 不能被继承`
```java
public record Animal(String name) { }

// 错误：记录类不能被继承
// public record Dog(String name) extends Animal { }

// 正确：记录类可以实现接口
public record Dog(String name) extends Animal { }  // 错误
```

---

## 记录类常用方法

**自动生成的方法**：toString、equals、hashCode
`<记录对象>.toString()` | `<对象1>.equals(<对象2>)`
```java
public record Point(int x, int y) { }

Point p1 = new Point(1, 2);
Point p2 = new Point(1, 2);
Point p3 = new Point(3, 4);

// toString
System.out.println(p1.toString());  // Point[x=1, y=2]

// equals
System.out.println(p1.equals(p2));  // true
System.out.println(p1.equals(p3));  // false

// hashCode
System.out.println(p1.hashCode() == p2.hashCode());  // true
```

---

## 局部记录类

**局部记录类**：在方法内部定义
`record <记录名>(<字段>) { }`
```java
public void process() {
    // 局部记录类
    record Point(int x, int y) {
        double distanceFromOrigin() {
            return Math.sqrt(x * x + y * y);
        }
    }

    Point p = new Point(3, 4);
    System.out.println(p.distanceFromOrigin());  // 5.0
}
```

---

## 记录类与 Stream

**记录类与 Stream API**：作为数据载体
`<stream>.map(<记录构造器>)`
```java
public record User(String name, int age) { }

List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
List<User> users = names.stream()
    .map(name -> new User(name, name.length()))
    .collect(Collectors.toList());

users.forEach(u -> System.out.println(u.name() + ": " + u.age()));
```

---

## 记录类与 JSON

**记录类与 JSON 序列化**：使用 Jackson
`ObjectMapper <变量> = new ObjectMapper();`
```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public record User(String name, int age, String email) { }

// 对象转 JSON
ObjectMapper mapper = new ObjectMapper();
User user = new User("Alice", 25, "alice@example.com");
String json = mapper.writeValueAsString(user);
System.out.println(json);  // {"name":"Alice","age":25,"email":"alice@example.com"}

// JSON 转对象
String jsonInput = "{\"name\":\"Bob\",\"age\":30,\"email\":\"bob@example.com\"}";
User user2 = mapper.readValue(jsonInput, User.class);
System.out.println(user2.name());  // Bob
```
