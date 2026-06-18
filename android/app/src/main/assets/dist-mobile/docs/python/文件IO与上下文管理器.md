# 文件IO与上下文管理器

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 文件操作基础

**open() 函数：打开文件**
`open(<文件路径>, mode=<模式>, [encoding=<编码>])`

```python
# 打开模式
# r: 读取（默认）
# w: 写入（覆盖）
# a: 追加
# x: 独占创建
# b: 二进制模式
# t: 文本模式（默认）
# +: 读写模式

# 基本文件操作
file = open("example.txt", "r", encoding="utf-8")
content = file.read()
file.close()

# 使用 with 语句（推荐）
with open("example.txt", "r", encoding="utf-8") as file:
    content = file.read()
# 文件自动关闭

# 写入文件
with open("output.txt", "w", encoding="utf-8") as file:
    file.write("Hello, World!\n")
    file.write("This is a test.")

# 追加内容
with open("output.txt", "a", encoding="utf-8") as file:
    file.write("\nAppended line.")
```

---

## 读取文件

**读取方法：read、readline、readlines**
`<文件>.read([size])`
`<文件>.readline()`
`<文件>.readlines()`

```python
# 读取整个文件
with open("example.txt", "r", encoding="utf-8") as file:
    content = file.read()
    print(content)

# 读取指定字节数
with open("example.txt", "r", encoding="utf-8") as file:
    chunk = file.read(100)  # 读取 100 个字符
    print(chunk)

# 逐行读取
with open("example.txt", "r", encoding="utf-8") as file:
    for line in file:
        print(line.strip())

# 读取所有行到列表
with open("example.txt", "r", encoding="utf-8") as file:
    lines = file.readlines()
    print(lines)  # ['line1\n', 'line2\n', ...]

# 读取单行
with open("example.txt", "r", encoding="utf-8") as file:
    first_line = file.readline()
    second_line = file.readline()
```

---

## 写入文件

**写入方法：write、writelines**
`<文件>.write(<字符串>)`
`<文件>.writelines(<字符串列表>)`

```python
# 写入字符串
with open("output.txt", "w", encoding="utf-8") as file:
    file.write("Hello, World!\n")
    file.write("Python file I/O")

# 写入多行
lines = ["Line 1\n", "Line 2\n", "Line 3\n"]
with open("output.txt", "w", encoding="utf-8") as file:
    file.writelines(lines)

# 追加写入
with open("output.txt", "a", encoding="utf-8") as file:
    file.write("Appended line\n")

# 读写模式
with open("example.txt", "r+", encoding="utf-8") as file:
    content = file.read()
    file.write("\nNew content")
```

---

## 二进制文件操作

**二进制模式：处理图片、音频等**
`open(<文件路径>, "rb")` / `open(<文件路径>, "wb")`

```python
# 读取二进制文件
with open("image.png", "rb") as file:
    data = file.read()
    print(f"File size: {len(data)} bytes")

# 写入二进制文件
with open("copy.png", "wb") as file:
    file.write(data)

# 分块读取大文件
def read_in_chunks(filename, chunk_size=1024):
    """分块读取文件"""
    with open(filename, "rb") as file:
        while True:
            chunk = file.read(chunk_size)
            if not chunk:
                break
            yield chunk

for chunk in read_in_chunks("large_file.bin"):
    process(chunk)
```

---

## 文件指针操作

**文件指针：seek、tell**
`<文件>.seek(<偏移量>, [<起始位置>])`
`<文件>.tell()`

```python
# 起始位置:
# 0: 文件开头（默认）
# 1: 当前位置
# 2: 文件末尾

with open("example.txt", "r", encoding="utf-8") as file:
    # 获取当前位置
    print(file.tell())  # 0
    
    # 读取 10 个字符
    content = file.read(10)
    print(file.tell())  # 10
    
    # 移动到文件开头
    file.seek(0)
    print(file.tell())  # 0
    
    # 移动到文件末尾
    file.seek(0, 2)
    print(file.tell())  # 文件大小
    
    # 相对当前位置移动
    file.seek(0)
    file.read(5)
    file.seek(3, 1)  # 从当前位置向后移动 3 个字符
```

---

## CSV 文件处理

**csv 模块：读写 CSV 文件**
`import csv`
`csv.reader(<文件>)` / `csv.writer(<文件>)`

```python
import csv

# 读取 CSV 文件
with open("data.csv", "r", encoding="utf-8", newline="") as file:
    reader = csv.reader(file)
    for row in reader:
        print(row)  # ['Name', 'Age', 'City']

# 使用 DictReader
with open("data.csv", "r", encoding="utf-8", newline="") as file:
    reader = csv.DictReader(file)
    for row in reader:
        print(row["Name"], row["Age"])

# 写入 CSV 文件
data = [
    ["Name", "Age", "City"],
    ["Alice", "30", "New York"],
    ["Bob", "25", "London"]
]

with open("output.csv", "w", encoding="utf-8", newline="") as file:
    writer = csv.writer(file)
    writer.writerows(data)

# 使用 DictWriter
data = [
    {"Name": "Alice", "Age": 30, "City": "New York"},
    {"Name": "Bob", "Age": 25, "City": "London"}
]

with open("output.csv", "w", encoding="utf-8", newline="") as file:
    fieldnames = ["Name", "Age", "City"]
    writer = csv.DictWriter(file, fieldnames=fieldnames)
    writer.writeheader()
    writer.writerows(data)
```

---

## JSON 文件处理

**json 模块：读写 JSON 文件**
`import json`
`json.load(<文件>)` / `json.dump(<对象>, <文件>)`

```python
import json

# 读取 JSON 文件
with open("data.json", "r", encoding="utf-8") as file:
    data = json.load(file)
    print(data)

# 写入 JSON 文件
data = {
    "name": "Alice",
    "age": 30,
    "hobbies": ["reading", "swimming"],
    "address": {
        "city": "New York",
        "country": "USA"
    }
}

with open("output.json", "w", encoding="utf-8") as file:
    json.dump(data, file, indent=4, ensure_ascii=False)

# JSON 字符串转换
json_str = json.dumps(data, indent=4)
parsed_data = json.loads(json_str)
```

---

## pickle 序列化

**pickle 模块：Python 对象序列化**
`import pickle`
`pickle.dump(<对象>, <文件>)` / `pickle.load(<文件>)`

```python
import pickle

# 序列化对象到文件
data = {
    "name": "Alice",
    "scores": [85, 92, 78],
    "nested": {"a": 1, "b": [2, 3]}
}

with open("data.pkl", "wb") as file:
    pickle.dump(data, file)

# 从文件反序列化
with open("data.pkl", "rb") as file:
    loaded_data = pickle.load(file)
    print(loaded_data)

# 序列化到字符串
data_bytes = pickle.dumps(data)
restored_data = pickle.loads(data_bytes)
```

---

## os 和 pathlib 路径操作

**os.path 模块：路径操作**
`import os`
`os.path.<方法>(<路径>)`

```python
import os

# 路径操作
path = "/home/user/documents/file.txt"

# 获取文件名
print(os.path.basename(path))  # file.txt

# 获取目录名
print(os.path.dirname(path))  # /home/user/documents

# 分离扩展名
print(os.path.splitext(path))  # ('/home/user/documents/file', '.txt')

# 拼接路径
new_path = os.path.join("/home", "user", "documents", "file.txt")

# 检查路径存在
print(os.path.exists(path))  # True/False

# 检查是否为文件
print(os.path.isfile(path))

# 检查是否为目录
print(os.path.isdir(path))

# 获取文件大小
print(os.path.getsize(path))

# 获取绝对路径
print(os.path.abspath("file.txt"))

# 目录操作
os.mkdir("new_directory")  # 创建目录
os.makedirs("path/to/dir", exist_ok=True)  # 递归创建目录
os.rmdir("new_directory")  # 删除空目录
os.remove("file.txt")  # 删除文件

# 列出目录内容
print(os.listdir("."))
```

---

**pathlib 模块：面向对象的路径操作**
`from pathlib import Path`
`Path(<路径>)`

```python
from pathlib import Path

# 创建 Path 对象
path = Path("/home/user/documents/file.txt")
current_dir = Path.cwd()
home_dir = Path.home()

# 路径属性
print(path.name)        # file.txt
print(path.stem)        # file
print(path.suffix)      # .txt
print(path.parent)      # /home/user/documents
print(path.parts)       # ('/', 'home', 'user', 'documents', 'file.txt')

# 路径操作
new_path = Path("/home") / "user" / "documents" / "file.txt"

# 检查路径
print(path.exists())    # True/False
print(path.is_file())   # True/False
print(path.is_dir())    # True/False

# 文件操作
print(path.stat().st_size)  # 文件大小

# 遍历目录
for file_path in Path(".").iterdir():
    print(file_path)

# 递归查找文件
for py_file in Path(".").rglob("*.py"):
    print(py_file)

# 创建和删除
Path("new_dir").mkdir(parents=True, exist_ok=True)
Path("file.txt").touch()
Path("file.txt").unlink()
```

---

## 上下文管理器

**with 语句：自动管理资源**
`with <上下文管理器> [as <变量>]: <语句>`

```python
# 文件操作
with open("example.txt", "r") as file:
    content = file.read()
# 文件自动关闭

# 多个上下文管理器
with open("input.txt", "r") as infile, open("output.txt", "w") as outfile:
    content = infile.read()
    outfile.write(content)

# 数据库连接
with database_connection() as conn:
    conn.execute("SELECT * FROM users")

# 锁
from threading import Lock
lock = Lock()
with lock:
    # 临界区代码
    pass
```

---

**自定义上下文管理器：实现 __enter__ 和 __exit__**
`class <类>: def __enter__(self): ...; def __exit__(self, ...): ...`

```python
class FileManager:
    def __init__(self, filename, mode):
        self.filename = filename
        self.mode = mode
        self.file = None
    
    def __enter__(self):
        print(f"Opening file: {self.filename}")
        self.file = open(self.filename, self.mode)
        return self.file
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.file:
            self.file.close()
        print(f"File closed: {self.filename}")
        if exc_type:
            print(f"Exception occurred: {exc_val}")
        return False  # 不抑制异常

# 使用自定义上下文管理器
with FileManager("example.txt", "w") as f:
    f.write("Hello, World!")

# 计时器上下文管理器
import time

class Timer:
    def __enter__(self):
        self.start = time.time()
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        self.end = time.time()
        print(f"Execution time: {self.end - self.start:.4f} seconds")

with Timer():
    time.sleep(1)
```

---

**contextlib 模块：使用生成器创建上下文管理器**
`from contextlib import contextmanager`
`@contextmanager def <函数>(): ...`

```python
from contextlib import contextmanager

@contextmanager
def open_file(filename, mode):
    """文件上下文管理器"""
    f = open(filename, mode)
    try:
        yield f
    finally:
        f.close()

with open_file("example.txt", "w") as f:
    f.write("Hello, World!")

@contextmanager
def timer():
    """计时器上下文管理器"""
    import time
    start = time.time()
    try:
        yield
    finally:
        end = time.time()
        print(f"Execution time: {end - start:.4f} seconds")

with timer():
    time.sleep(1)

@contextmanager
def change_directory(path):
    """切换目录上下文管理器"""
    import os
    old_dir = os.getcwd()
    os.chdir(path)
    try:
        yield
    finally:
        os.chdir(old_dir)
```

---

## 临时文件

**tempfile 模块：创建临时文件**
`import tempfile`
`tempfile.NamedTemporaryFile()`
`tempfile.mkdtemp()`

```python
import tempfile
import os

# 创建临时文件
with tempfile.NamedTemporaryFile(mode="w", delete=False, suffix=".txt") as f:
    f.write("Temporary content")
    temp_filename = f.name
    print(f"Temporary file created: {temp_filename}")

# 使用后手动删除
os.unlink(temp_filename)

# 创建临时目录
with tempfile.TemporaryDirectory() as temp_dir:
    print(f"Temporary directory: {temp_dir}")
    # 在临时目录中工作
    temp_file = os.path.join(temp_dir, "temp.txt")
    with open(temp_file, "w") as f:
        f.write("Content")
# 临时目录自动删除

# 获取临时文件路径
temp_path = tempfile.gettempdir()
print(f"System temp directory: {temp_path}")
```
