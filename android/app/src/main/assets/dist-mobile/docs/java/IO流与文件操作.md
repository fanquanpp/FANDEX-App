# Java IO 流与文件操作

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 字节流

**FileInputStream**：读取字节数据
`FileInputStream <变量名> = new FileInputStream(<文件路径>);`
```java
import java.io.FileInputStream;
import java.io.IOException;

try (FileInputStream fis = new FileInputStream("input.txt")) {
    int data;
    while ((data = fis.read()) != -1) {  // 逐字节读取
        System.out.print((char) data);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**FileOutputStream**：写入字节数据
`FileOutputStream <变量名> = new FileOutputStream(<文件路径>);`
```java
import java.io.FileOutputStream;
import java.io.IOException;

try (FileOutputStream fos = new FileOutputStream("output.txt")) {
    String content = "Hello, World!";
    fos.write(content.getBytes());  // 写入字节数组
    fos.write(65);                   // 写入单个字节
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**BufferedInputStream**：带缓冲的字节输入流
`BufferedInputStream <变量名> = new BufferedInputStream(new FileInputStream(<路径>));`
```java
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("input.txt"))) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        System.out.println(new String(buffer, 0, bytesRead));
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**BufferedOutputStream**：带缓冲的字节输出流
`BufferedOutputStream <变量名> = new BufferedOutputStream(new FileOutputStream(<路径>));`
```java
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("output.txt"))) {
    String content = "Hello, World!";
    bos.write(content.getBytes());
    bos.flush();  // 刷新缓冲区
} catch (IOException e) {
    e.printStackTrace();
}
```

---

## 字符流

**FileReader**：读取字符数据
`FileReader <变量名> = new FileReader(<文件路径>);`
```java
import java.io.FileReader;
import java.io.IOException;

try (FileReader fr = new FileReader("input.txt")) {
    int data;
    while ((data = fr.read()) != -1) {
        System.out.print((char) data);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**FileWriter**：写入字符数据
`FileWriter <变量名> = new FileWriter(<文件路径>);`
```java
import java.io.FileWriter;
import java.io.IOException;

try (FileWriter fw = new FileWriter("output.txt")) {
    fw.write("Hello, World!");
    fw.write("\n");
    fw.write("Java IO");
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**BufferedReader**：带缓冲的字符输入流
`BufferedReader <变量名> = new BufferedReader(new FileReader(<路径>));`
```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {  // 逐行读取
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**BufferedWriter**：带缓冲的字符输出流
`BufferedWriter <变量名> = new BufferedWriter(new FileWriter(<路径>));`
```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    bw.write("First line");
    bw.newLine();  // 写入换行符
    bw.write("Second line");
} catch (IOException e) {
    e.printStackTrace();
}
```

---

## 文件操作

**File 类**：表示文件或目录
`File <变量名> = new File(<路径>);`
```java
import java.io.File;

File file = new File("test.txt");
boolean exists = file.exists();          // 文件是否存在
boolean created = file.createNewFile();  // 创建新文件
boolean deleted = file.delete();          // 删除文件
String name = file.getName();             // 获取文件名
String path = file.getPath();            // 获取路径
long length = file.length();             // 获取文件大小
boolean isFile = file.isFile();           // 是否为文件
boolean isDir = file.isDirectory();       // 是否为目录
```

---

**目录操作**：创建、列出、删除
`File <目录> = new File(<路径>);`
```java
File dir = new File("mydir");
boolean created = dir.mkdir();    // 创建目录
boolean created2 = dir.mkdirs(); // 创建多级目录

// 列出目录内容
File dir2 = new File(".");
String[] files = dir2.list();         // 列出文件名
File[] files2 = dir2.listFiles();     // 列出 File 对象

// 遍历目录
for (File f : files2) {
    if (f.isFile()) {
        System.out.println("File: " + f.getName());
    } else if (f.isDirectory()) {
        System.out.println("Dir: " + f.getName());
    }
}
```

---

## NIO Files 类

**Files 写入**：写入文件
`Files.write(<路径>, <字节>);`
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

Path path = Paths.get("output.txt");

// 写入字节数组
Files.write(path, "Hello, World!".getBytes());

// 写入行列表
List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
Files.write(path, lines);
```

---

**Files 读取**：读取文件
`List<String> <变量> = Files.readAllLines(<路径>);`
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

Path path = Paths.get("input.txt");

// 读取所有行
List<String> lines = Files.readAllLines(path);
for (String line : lines) {
    System.out.println(line);
}

// 读取所有字节
byte[] bytes = Files.readAllBytes(path);
String content = new String(bytes);
```

---

**Files 复制**：复制文件
`Files.copy(<源路径>, <目标路径>);`
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

Path source = Paths.get("source.txt");
Path target = Paths.get("target.txt");

// 复制文件
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
```

---

**Files 移动和重命名**：移动文件
`Files.move(<源路径>, <目标路径>);`
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

Path source = Paths.get("old.txt");
Path target = Paths.get("new.txt");

// 移动或重命名
Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
```

---

**Files 删除**：删除文件
`Files.delete(<路径>);`
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

Path path = Paths.get("file.txt");

// 删除文件
Files.delete(path);

// 如果存在则删除
Files.deleteIfExists(path);
```

---

## 对象序列化

**序列化**：将对象写入文件
`ObjectOutputStream <变量名> = new ObjectOutputStream(new FileOutputStream(<路径>));`
```java
import java.io.*;

// 实现 Serializable 接口
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private transient String password;  // transient 表示不序列化

    public User(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}

// 序列化
User user = new User("Alice", 25, "secret");
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.dat"))) {
    oos.writeObject(user);
} catch (IOException e) {
    e.printStackTrace();
}
```

---

**反序列化**：从文件读取对象
`ObjectInputStream <变量名> = new ObjectInputStream(new FileInputStream(<路径>));`
```java
import java.io.*;

// 反序列化
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.dat"))) {
    User user = (User) ois.readObject();
    System.out.println(user.getName());  // Alice
    System.out.println(user.getAge());   // 25
    // password 为 null，因为被 transient 修饰
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();
}
```

---

## Scanner 读取

**Scanner 读取文件**：逐行读取
`Scanner <变量名> = new Scanner(new File(<路径>));`
```java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

try (Scanner scanner = new Scanner(new File("input.txt"))) {
    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        System.out.println(line);
    }
} catch (FileNotFoundException e) {
    e.printStackTrace();
}
```

---

## PrintWriter

**PrintWriter**：格式化输出
`PrintWriter <变量名> = new PrintWriter(<文件路径>);`
```java
import java.io.PrintWriter;
import java.io.IOException;

try (PrintWriter pw = new PrintWriter("output.txt")) {
    pw.println("Hello, World!");
    pw.printf("Name: %s, Age: %d%n", "Alice", 25);
    pw.write("Direct write");
} catch (IOException e) {
    e.printStackTrace();
}
```
