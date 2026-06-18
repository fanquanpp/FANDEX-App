
## 概述

Python 的类型系统（Type System）是从 Python 3.5 开始逐步引入的静态类型注解体系。Python 本身仍然是动态类型语言，但通过类型注解（Type Hints）和类型检查工具（如 mypy、pyright），开发者可以在编码阶段发现类型错误，提升代码的可靠性和可维护性。

类型注解不会影响运行时行为，它是一种"渐进式类型"（Gradual Typing）方案：你可以选择性地为代码添加类型，未注解的部分仍然保持动态类型。

## 基础概念

### 类型注解语法

| 语法 | 含义 | 示例 |
| ---- | ---- | ---- |
| `: 类型` | 参数类型注解 | `def f(x: int) -> str` |
| `-> 类型` | 返回值类型注解 | `def f() -> None` |
| `变量: 类型` | 变量类型注解 | `name: str = "hello"` |

### 内置类型注解

```python
# 基本类型
name: str = "Python"
version: float = 3.12
is_active: bool = True
count: int = 0

# 容器类型（Python 3.9+ 可直接使用内置类型）
names: list[str] = ["Alice", "Bob"]
scores: dict[str, int] = {"Alice": 95, "Bob": 87}
coords: tuple[float, float] = (3.14, 2.71)
unique_ids: set[int] = {1, 2, 3}

# 函数类型注解
def greet(name: str, times: int = 1) -> str:
    return f"Hello, {name}! " * times

# 可选类型
from typing import Optional
nickname: Optional[str] = None  # 等价于 str | None（Python 3.10+）
```

### 联合类型

```python
# Python 3.10+ 使用 | 运算符
def process(value: int | str | None) -> str:
    if value is None:
        return "空值"
    return str(value)

# Python 3.9 及以下使用 Union
from typing import Union
def process_legacy(value: Union[int, str, None]) -> str:
    if value is None:
        return "空值"
    return str(value)
```

## 进阶用法

### 泛型（Generics）

泛型允许编写适用于多种类型的函数和类，同时保持类型安全。

```python
from typing import TypeVar, Generic

# 定义类型变量
T = TypeVar("T")
K = TypeVar("K")
V = TypeVar("V")

# 泛型函数
def first(items: list[T]) -> T:
    return items[0]

# 泛型类
class Stack(Generic[T]):
    def __init__(self) -> None:
        self._items: list[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> T:
        return self._items.pop()

# 使用
stack: Stack[int] = Stack()
stack.push(42)
value: int = stack.pop()
```

### Protocol（结构化类型）

Protocol 是 Python 的结构化子类型（Structural Subtyping），类似于 Go 的接口：只要实现了所需方法，就满足类型约束，无需显式继承。

```python
from typing import Protocol

# 定义协议
class Drawable(Protocol):
    def draw(self) -> str: ...

class Circle:
    def draw(self) -> str:
        return "绘制圆形"

class Square:
    def draw(self) -> str:
        return "绘制正方形"

# Circle 和 Square 无需继承 Drawable，只要实现了 draw 方法即可
def render(shape: Drawable) -> None:
    print(shape.draw())

render(Circle())  # 合法
render(Square())  # 合法
```

### TypeGuard 与类型收窄

TypeGuard 用于自定义类型收窄逻辑，帮助类型检查器在条件分支中推断更精确的类型。

```python
from typing import TypeGuard

def is_str_list(val: list[object]) -> TypeGuard[list[str]]:
    return all(isinstance(x, str) for x in val)

def process(items: list[object]) -> None:
    if is_str_list(items):
        # 此分支中 items 被收窄为 list[str]
        print(items[0].upper())
```

### 重载（Overload）

当一个函数有多种调用签名时，使用 `@overload` 提供精确的类型信息。

```python
from typing import overload

@overload
def scale(value: int, factor: int) -> int: ...
@overload
def scale(value: float, factor: float) -> float: ...
@overload
def scale(value: str, factor: int) -> str: ...

def scale(value: int | float | str, factor: int | float) -> int | float | str:
    if isinstance(value, str):
        return value * int(factor)
    return value * factor

# 类型检查器能正确推断返回类型
a: int = scale(3, 2)        # 返回 int
b: float = scale(2.5, 1.5)  # 返回 float
c: str = scale("Hi", 3)     # 返回 str
```

### TypedDict

TypedDict 为字典提供键值类型约束，适用于 JSON 数据等场景。

```python
from typing import TypedDict

class UserInfo(TypedDict):
    name: str
    age: int
    email: str

# 类型检查器会验证键和值的类型
user: UserInfo = {
    "name": "Alice",
    "age": 30,
    "email": "alice@example.com"
}

# 部分键可选
class PartialUser(TypedDict, total=False):
    name: str
    age: int
    email: str
```

## 实践指南

### 类型检查工具配置

推荐使用 mypy 或 pyright 进行类型检查：

```bash
# 安装 mypy
pip install mypy

# 检查项目
mypy src/ --strict

# 安装 pyright（更快）
pip install pyright
pyright src/
```

`pyproject.toml` 配置示例：

```toml
[tool.mypy]
python_version = "3.12"
strict = true
warn_return_any = true
disallow_untyped_defs = true

[tool.pyright]
pythonVersion = "3.12"
typeCheckingMode = "strict"
```

### 常见陷阱

| 陷阱 | 错误写法 | 正确写法 |
| ---- | -------- | -------- |
| 可变默认参数类型 | `def f(x: list[int] = [])` | `def f(x: list[int] \| None = None)` |
| 类属性与实例属性混淆 | `class C: x: list[int]` | `class C: def __init__(self): self.x: list[int] = []` |
| Any 滥用 | `def f(x: Any) -> Any` | 尽量使用具体类型或泛型 |
| 忽略 None 检查 | `def f(x: str \| None): return x.upper()` | 先 `if x is not None:` 再操作 |

### 渐进式采用策略

1. 新代码：全部添加类型注解
2. 核心模块：优先添加类型注解，配合 `--strict` 检查
3. 旧代码：逐步添加，从公共 API 开始
4. 第三方库：安装 `types-xxx` 存根包（如 `types-requests`）

## 最佳实践

- 优先使用 Python 3.10+ 语法（`X | Y` 替代 `Union`，`list[X]` 替代 `List[X]`）
- 使用 `Protocol` 替代抽象基类，保持灵活性
- 使用 `TypedDict` 描述 JSON 结构，避免 `Dict[str, Any]`
- 避免使用 `Any`，必要时用 `object` 替代并做类型收窄
- 在 CI 中集成 mypy/pyright 检查，防止类型回归
- 使用 `cast()` 仅在类型检查器无法推断时，不要滥用

## 参考资料

- [PEP 484](https://peps.python.org/pep-0484/) - 类型注解
- [PEP 544](https://peps.python.org/pep-0544/) - Protocol
- [PEP 604](https://peps.python.org/pep-0604/) - 联合类型语法 `X | Y`
- [mypy 官方文档](https://mypy.readthedocs.io/)
- [pyright 官方文档](https://github.com/microsoft/pyright)
