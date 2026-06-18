# 类型注解与mypy

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 基本类型注解

**变量类型注解**
`<变量>: <类型> = <值>`

```python
# 基本类型注解
age: int = 30
name: str = "Alice"
height: float = 5.9
is_active: bool = True

# 容器类型注解（Python 3.9+）
numbers: list[int] = [1, 2, 3]
person: dict[str, int] = {"Alice": 30, "Bob": 25}
unique_numbers: set[int] = {1, 2, 3}
coordinates: tuple[int, int] = (10, 20)

# 旧版本兼容（Python 3.8 及以下）
from typing import List, Dict, Set, Tuple
numbers: List[int] = [1, 2, 3]
person: Dict[str, int] = {"Alice": 30}
unique_numbers: Set[int] = {1, 2, 3}
coordinates: Tuple[int, int] = (10, 20)
```

---

## 函数类型注解

**函数参数和返回值注解**
`def <函数>(<参数>: <类型>) -> <返回类型>: ...`

```python
# 基本函数注解
def greet(name: str) -> str:
    return f"Hello, {name}!"

def add(a: int, b: int) -> int:
    return a + b

def divide(a: float, b: float) -> float:
    return a / b

# 带默认值
def greet(name: str, greeting: str = "Hello") -> str:
    return f"{greeting}, {name}!"

# 可变参数
def sum_all(*args: int) -> int:
    return sum(args)

def print_info(**kwargs: str) -> None:
    for key, value in kwargs.items():
        print(f"{key}: {value}")

# 多种返回类型
from typing import Union, Optional

def find_user(user_id: int) -> Union[dict, None]:
    if user_id == 1:
        return {"name": "Alice"}
    return None

# Optional 简写
def get_user(user_id: int) -> Optional[dict]:
    if user_id == 1:
        return {"name": "Alice"}
    return None
```

---

## typing 模块

**常用类型**
`from typing import <类型>`

```python
from typing import (
    List, Dict, Set, Tuple,  # 容器类型
    Union, Optional,  # 联合类型
    Any,  # 任意类型
    Callable,  # 可调用类型
    Iterable, Iterator,  # 迭代器
    Generator,  # 生成器
    TypeVar, Generic,  # 泛型
    Protocol,  # 协议
    Literal,  # 字面量类型
    Type,  # 类型本身
    ClassVar,  # 类变量
    Final,  # 常量
)

# Any 类型
def process(data: Any) -> Any:
    return data

# Union 类型
def process_value(value: Union[int, str, float]) -> str:
    return str(value)

# Optional 类型（等价于 Union[T, None]）
def find(value: int) -> Optional[int]:
    if value > 0:
        return value
    return None

# Callable 类型
def apply(func: Callable[[int, int], int], a: int, b: int) -> int:
    return func(a, b)

# Iterable 和 Iterator
def process_items(items: Iterable[int]) -> list[int]:
    return [item * 2 for item in items]

# Generator 类型
def counter() -> Generator[int, None, None]:
    count = 0
    while True:
        yield count
        count += 1

# Literal 类型
def set_mode(mode: Literal["read", "write", "append"]) -> None:
    print(f"Mode: {mode}")

# Type 类型
def create_instance(cls: Type[int]) -> int:
    return cls()
```

---

## 泛型

**TypeVar：类型变量**
`from typing import TypeVar`
`<T> = TypeVar("<T>")`

```python
from typing import TypeVar, List, Generic

# 基本泛型
T = TypeVar("T")

def first(items: List[T]) -> T:
    return items[0]

# 使用泛型函数
print(first([1, 2, 3]))        # 返回 int
print(first(["a", "b", "c"]))  # 返回 str

# 泛型类
class Stack(Generic[T]):
    def __init__(self) -> None:
        self.items: List[T] = []
    
    def push(self, item: T) -> None:
        self.items.append(item)
    
    def pop(self) -> T:
        return self.items.pop()
    
    def is_empty(self) -> bool:
        return len(self.items) == 0

# 使用泛型类
int_stack: Stack[int] = Stack()
int_stack.push(1)
int_stack.push(2)
print(int_stack.pop())  # 2

str_stack: Stack[str] = Stack()
str_stack.push("hello")
print(str_stack.pop())  # hello

# 受限泛型
T = TypeVar("T", int, float)

def add(a: T, b: T) -> T:
    return a + b

print(add(1, 2))        # 3
print(add(1.5, 2.5))    # 4.0
# add("a", "b")  # 类型错误
```

---

## Protocol（协议）

**Protocol：结构子类型**
`from typing import Protocol`
`class <协议>(Protocol): def <方法>(self) -> ...: ...`

```python
from typing import Protocol

class Drawable(Protocol):
    def draw(self) -> None:
        ...

class Movable(Protocol):
    def move(self, x: int, y: int) -> None:
        ...

# 实现协议（隐式）
class Circle:
    def draw(self) -> None:
        print("Drawing circle")
    
    def move(self, x: int, y: int) -> None:
        print(f"Moving circle to ({x}, {y})")

class Square:
    def draw(self) -> None:
        print("Drawing square")

def render(obj: Drawable) -> None:
    obj.draw()

def animate(obj: Movable) -> None:
    obj.move(10, 20)

circle = Circle()
square = Square()

render(circle)  # Drawing circle
render(square)  # Drawing square
animate(circle)  # Moving circle to (10, 20)
# animate(square)  # 类型错误：Square 没有 move 方法
```

---

## TypedDict

**TypedDict：类型化字典**
`from typing import TypedDict`
`class <类型>(TypedDict): <字段>: <类型>`

```python
from typing import TypedDict

class Person(TypedDict):
    name: str
    age: int
    city: str

# 使用
person: Person = {
    "name": "Alice",
    "age": 30,
    "city": "New York"
}

# 函数参数
def print_person(p: Person) -> None:
    print(f"{p['name']}, {p['age']}, {p['city']}")

print_person(person)

# 必需和可选字段
from typing import TypedDict, NotRequired

class User(TypedDict):
    name: str
    age: int
    email: NotRequired[str]

user1: User = {"name": "Alice", "age": 30}
user2: User = {"name": "Bob", "age": 25, "email": "bob@example.com"}
```

---

## Final 和 ClassVar

**Final：常量声明**
`from typing import Final`
`<变量>: Final[<类型>] = <值>`

```python
from typing import Final, ClassVar

# Final 常量
MAX_SIZE: Final[int] = 100
PI: Final[float] = 3.14159265359
APP_NAME: Final[str] = "MyApp"

# MAX_SIZE = 200  # 类型错误：不能修改 Final 变量

# ClassVar 类变量
class Config:
    DATABASE_URL: ClassVar[str] = "postgresql://localhost/db"
    DEBUG: ClassVar[bool] = False
    MAX_CONNECTIONS: ClassVar[int] = 10
    
    def __init__(self, name: str) -> None:
        self.name = name  # 实例变量

print(Config.DATABASE_URL)
print(Config.DEBUG)
```

---

## mypy 类型检查

**安装和基本使用**
`pip install mypy`
`mypy <文件>`

```python
# example.py
def add(a: int, b: int) -> int:
    return a + b

def greet(name: str) -> str:
    return f"Hello, {name}!"

# 正确调用
result = add(1, 2)
message = greet("Alice")

# 类型错误（mypy 会报错）
# result = add("1", "2")  # error: Argument 1 to "add" has incompatible type "str"
# message = greet(123)    # error: Argument 1 to "greet" has incompatible type "int"
```

---

**mypy 配置文件**
`mypy.ini` / `setup.cfg` / `pyproject.toml`

```ini
# mypy.ini
[mypy]
python_version = 3.11
warn_return_any = True
warn_unused_configs = True
disallow_untyped_defs = True
disallow_incomplete_defs = True
check_untyped_defs = True
disallow_untyped_decorators = True
no_implicit_optional = True
warn_redundant_casts = True
warn_unused_ignores = True
warn_no_return = True
warn_return_any = True
no_implicit_reexport = True
strict_equality = True

# 针对特定模块的配置
[mypy-my_module.*]
disallow_untyped_defs = False

# 忽略特定模块
[mypy-some_module.*]
ignore_missing_imports = True
```

---

**mypy 严格模式**
`mypy --strict <文件>`

```python
# 严格模式要求所有函数都有类型注解
# 不允许 Any 类型
# 不允许未类型化的装饰器

# 严格模式示例
from typing import List

def process(items: List[int]) -> List[int]:
    result: List[int] = []
    for item in items:
        result.append(item * 2)
    return result

# 类型忽略注释
def some_function() -> int:
    value = "not an int"  # type: ignore
    return value  # type: ignore
```

---

## 类型别名

**类型别名**
`<别名> = <类型>`

```python
from typing import Union, List, Tuple

# 简单类型别名
Vector = List[float]
Matrix = List[Vector]

# 联合类型别名
JSONValue = Union[str, int, float, bool, None, List["JSONValue"], dict[str, "JSONValue"]]

# 使用类型别名
def dot_product(a: Vector, b: Vector) -> float:
    return sum(x * y for x, y in zip(a, b))

def transpose(matrix: Matrix) -> Matrix:
    return [list(row) for row in zip(*matrix)]

# Python 3.12+ 类型别名语法
# type Vector = List[float]
# type Matrix = List[Vector]
```

---

## 函数重载

**@overload：函数重载**
`from typing import overload`
`@overload def <函数>(...): ...`

```python
from typing import overload, Union

@overload
def process(value: int) -> int: ...

@overload
def process(value: str) -> str: ...

@overload
def process(value: list) -> list: ...

def process(value):
    """实际实现"""
    if isinstance(value, int):
        return value * 2
    elif isinstance(value, str):
        return value.upper()
    elif isinstance(value, list):
        return [item * 2 for item in value]
    raise TypeError("Unsupported type")

# 使用
print(process(5))        # 10
print(process("hello"))  # HELLO
print(process([1, 2, 3]))  # [2, 4, 6]
```

---

## NewType

**NewType：创建新类型**
`from typing import NewType`
`<类型> = NewType("<名称>", <基础类型>)`

```python
from typing import NewType

# 创建新类型
UserId = NewType("UserId", int)
Username = NewType("Username", str)
Email = NewType("Email", str)

# 使用
def get_user(user_id: UserId) -> dict:
    return {"id": user_id, "name": "Alice"}

def send_email(to: Email, subject: str) -> None:
    print(f"Sending email to {to}: {subject}")

# 创建新类型实例
user_id = UserId(123)
email = Email("alice@example.com")

get_user(user_id)
send_email(email, "Hello")

# get_user(123)  # 类型错误：需要 UserId 类型
# get_user(email)  # 类型错误
```

---

## 运行时类型检查

**typing.get_type_hints()：获取类型提示**
`from typing import get_type_hints`
`get_type_hints(<函数>)`

```python
from typing import get_type_hints

def greet(name: str, age: int) -> str:
    return f"Hello, {name}! You are {age} years old."

# 获取类型提示
hints = get_type_hints(greet)
print(hints)  # {'name': <class 'str'>, 'age': <class 'int'>, 'return': <class 'str'>}

# 运行时类型检查
def validate_type(value, expected_type):
    """运行时类型检查"""
    if not isinstance(value, expected_type):
        raise TypeError(f"Expected {expected_type}, got {type(value)}")
    return value

# 使用 pydantic 进行运行时类型检查
from pydantic import BaseModel

class User(BaseModel):
    name: str
    age: int
    email: str

user = User(name="Alice", age=30, email="alice@example.com")
print(user.name)  # Alice
# user = User(name="Alice", age="thirty")  # ValidationError
```
