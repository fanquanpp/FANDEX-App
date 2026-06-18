# 文件IO与文件系统

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 文件流基础

**文件写入**
`std::ofstream <out>("<filename>"); <out> << <data>;`
```cpp
#include <fstream>
std::ofstream outfile("example.txt");
if (outfile.is_open()) {
    outfile << "Hello, File!" << std::endl;
    outfile << "Number: " << 42 << std::endl;
    outfile.close();
}
```

---

**文件读取**
`std::ifstream <in>("<filename>"); while (std::getline(<in>, <line>)) { ... }`
```cpp
std::ifstream infile("example.txt");
if (infile.is_open()) {
    std::string line;
    while (std::getline(infile, line)) {
        std::cout << line << std::endl;
    }
    infile.close();
}
```

---

**文件读写**
`std::fstream <fs>("<filename>", <mode>);`
```cpp
#include <fstream>
std::fstream file("data.txt", std::ios::in | std::ios::out | std::ios::app);
if (file.is_open()) {
    file << "Append this line" << std::endl;
    file.seekg(0);    // 移动到文件开头
    std::string line;
    while (std::getline(file, line)) {
        std::cout << line << std::endl;
    }
    file.close();
}
```

---

## 文件打开模式

**打开模式标志**
`std::ios::<in|out|app|ate|trunc|binary>`
```cpp
std::ios::in       // 读模式
std::ios::out      // 写模式
std::ios::app      // 追加模式
std::ios::ate      // 打开后定位到末尾
std::ios::trunc    // 截断文件
std::ios::binary   // 二进制模式
// 组合使用
std::ofstream f1("file.txt", std::ios::out | std::ios::app);
std::ifstream f2("file.bin", std::ios::in | std::ios::binary);
```

---

## 文件状态检查

**状态检查函数**
`<fs>.is_open() | <fs>.good() | <fs>.eof() | <fs>.fail() | <fs>.bad()`
```cpp
std::ifstream file("data.txt");
if (!file.is_open()) {
    std::cerr << "Failed to open file" << std::endl;
    return;
}
while (!file.eof()) {
    std::string line;
    if (std::getline(file, line)) {
        std::cout << line << std::endl;
    }
}
if (file.fail() && !file.eof()) {
    std::cerr << "Read error occurred" << std::endl;
}
```

---

## 文件定位

**seekg 与 seekp**
`<fs>.seekg(<pos>, <origin>) | <fs>.seekp(<pos>, <origin>)`
```cpp
std::fstream file("data.txt", std::ios::in | std::ios::out);
// seekg：设置读取位置
file.seekg(0, std::ios::beg);    // 文件开头
file.seekg(0, std::ios::end);    // 文件末尾
file.seekg(10, std::ios::cur);   // 当前位置 +10
// seekp：设置写入位置
file.seekp(0, std::ios::beg);    // 文件开头
// tellg：获取读取位置
std::streampos pos = file.tellg();
// tellp：获取写入位置
std::streampos wpos = file.tellp();
```

---

## 二进制文件读写

**二进制写入**
`<out>.write(reinterpret_cast<const char*>(<ptr>), <size>)`
```cpp
struct Record {
    int id;
    char name[20];
    double score;
};
std::ofstream out("records.bin", std::ios::binary);
Record r = {1, "Alice", 95.5};
out.write(reinterpret_cast<const char*>(&r), sizeof(Record));
out.close();
```

---

**二进制读取**
`<in>.read(reinterpret_cast<char*>(<ptr>), <size>)`
```cpp
std::ifstream in("records.bin", std::ios::binary);
Record r;
in.read(reinterpret_cast<char*>(&r), sizeof(Record));
std::cout << r.id << " " << r.name << " " << r.score << std::endl;
in.close();
```

---

## 文件系统（C++17）

**路径操作**
`std::filesystem::path <p> = "<path>";`
```cpp
#include <filesystem>
namespace fs = std::filesystem;
// 创建路径对象
fs::path p = "dir/subdir/file.txt";
// 路径组件
std::cout << p.parent_path() << std::endl;    // "dir/subdir"
std::cout << p.filename() << std::endl;        // "file.txt"
std::cout << p.stem() << std::endl;            // "file"
std::cout << p.extension() << std::endl;       // ".txt"
// 路径拼接
fs::path dir = "mydir";
fs::path file = dir / "file.txt";    // "mydir/file.txt"
```

---

**目录操作**
`fs::create_directory(<path>) | fs::remove(<path>) | fs::exists(<path>)`
```cpp
namespace fs = std::filesystem;
// 创建目录
fs::create_directory("mydir");
fs::create_directories("a/b/c");    // 递归创建
// 检查是否存在
if (fs::exists("mydir")) {
    std::cout << "Directory exists" << std::endl;
}
// 删除文件或空目录
fs::remove("file.txt");
fs::remove_all("mydir");    // 递归删除
// 重命名
fs::rename("old.txt", "new.txt");
// 复制
fs::copy("source.txt", "dest.txt");
fs::copy("src_dir", "dest_dir", fs::copy_options::recursive);
```

---

**遍历目录**
`for (const auto& entry : fs::directory_iterator(<path>)) { ... }`
```cpp
namespace fs = std::filesystem;
// 遍历目录
for (const auto& entry : fs::directory_iterator(".")) {
    std::cout << entry.path() << std::endl;
}
// 递归遍历
for (const auto& entry : fs::recursive_directory_iterator(".")) {
    if (fs::is_regular_file(entry)) {
        std::cout << entry.path() << " (" << fs::file_size(entry) << " bytes)" << std::endl;
    }
}
```

---

**文件信息**
`fs::file_size(<path>) | fs::is_regular_file(<path>) | fs::is_directory(<path>)`
```cpp
namespace fs = std::filesystem;
fs::path p = "example.txt";
if (fs::exists(p)) {
    std::cout << "Size: " << fs::file_size(p) << " bytes" << std::endl;
    std::cout << "Is file: " << fs::is_regular_file(p) << std::endl;
    std::cout << "Is dir: " << fs::is_directory(p) << std::endl;
    // 获取最后修改时间
    auto ftime = fs::last_write_time(p);
    auto sctp = std::chrono::time_point_cast<std::chrono::system_clock::duration>(
        ftime - fs::file_time_type::clock::now() + std::chrono::system_clock::now());
    std::time_t cftime = std::chrono::system_clock::to_time_t(sctp);
    std::cout << "Modified: " << std::ctime(&cftime);
}
```

---

## 字符串流

**字符串流操作**
`std::stringstream <ss>; <ss> << <data>; std::string <str> = <ss>.str();`
```cpp
#include <sstream>
// 输出字符串流
std::ostringstream oss;
oss << "Name: " << "Alice" << ", Age: " << 25;
std::string result = oss.str();
// 输入字符串流
std::istringstream iss("10 3.14 hello");
int i;
double d;
std::string s;
iss >> i >> d >> s;
```
