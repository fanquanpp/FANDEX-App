# 文件IO操作

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 文件打开与关闭

**fopen：打开文件**
`FILE *fopen(const char *<path>, const char *<mode>);`
```c
#include <stdio.h>
FILE *fp = fopen("data.txt", "r");
if (fp == NULL) {
    perror("Failed to open file");
    return 1;
}
```

---

**文件打开模式**
`"<r|w|a|r+|w+|a+>[b]>"`
```c
"r"   // 只读，文件必须存在
"w"   // 只写，清空或创建
"a"   // 追加，在末尾写入
"r+"  // 读写，文件必须存在
"w+"  // 读写，清空或创建
"a+"  // 读写，在末尾写入
"rb"  // 二进制只读
"wb"  // 二进制只写
```

---

**fclose：关闭文件**
`int fclose(FILE *<fp>);`
```c
fclose(fp);    // 关闭文件并刷新缓冲区
```

---

## 文本文件读写

**fgetc：读取单个字符**
`int fgetc(FILE *<fp>);`
```c
int ch;
while ((ch = fgetc(fp)) != EOF) {
    putchar(ch);
}
```

---

**fputc：写入单个字符**
`int fputc(int <ch>, FILE *<fp>);`
```c
fputc('A', fp);    // 写入字符 A
```

---

**fgets：读取一行字符串**
`char *fgets(char *<str>, int <size>, FILE *<fp>);`
```c
char buffer[256];
while (fgets(buffer, sizeof(buffer), fp) != NULL) {
    printf("%s", buffer);
}
```

---

**fputs：写入字符串**
`int fputs(const char *<str>, FILE *<fp>);`
```c
fputs("Hello, World!\n", fp);
```

---

**fprintf：格式化写入**
`int fprintf(FILE *<fp>, const char *<format>, ...);`
```c
fprintf(fp, "Name: %s, Age: %d\n", "Alice", 25);
fprintf(fp, "Pi: %.2f\n", 3.14159);
```

---

**fscanf：格式化读取**
`int fscanf(FILE *<fp>, const char *<format>, ...);`
```c
char name[50];
int age;
fscanf(fp, "%49s %d", name, &age);
printf("Name: %s, Age: %d\n", name, age);
```

---

## 二进制文件读写

**fwrite：写入二进制数据**
`size_t fwrite(const void *<ptr>, size_t <size>, size_t <count>, FILE *<fp>);`
```c
int numbers[] = {1, 2, 3, 4, 5};
fwrite(numbers, sizeof(int), 5, fp);
// 写入结构体
typedef struct { int id; char name[20]; } Record;
Record r = {1, "Alice"};
fwrite(&r, sizeof(Record), 1, fp);
```

---

**fread：读取二进制数据**
`size_t fread(void *<ptr>, size_t <size>, size_t <count>, FILE *<fp>);`
```c
int numbers[5];
fread(numbers, sizeof(int), 5, fp);
// 读取结构体
Record r;
fread(&r, sizeof(Record), 1, fp);
```

---

## 文件定位

**fseek：移动文件指针**
`int fseek(FILE *<fp>, long <offset>, int <origin>);`
```c
fseek(fp, 0, SEEK_SET);    // 移动到文件开头
fseek(fp, 0, SEEK_END);    // 移动到文件末尾
fseek(fp, 10, SEEK_CUR);   // 从当前位置向前移动 10 字节
fseek(fp, -5, SEEK_END);   // 从文件末尾向后移动 5 字节
```

---

**ftell：获取当前位置**
`long ftell(FILE *<fp>);`
```c
long pos = ftell(fp);
printf("Current position: %ld\n", pos);
```

---

**rewind：重置到文件开头**
`void rewind(FILE *<fp>);`
```c
rewind(fp);    // 等同于 fseek(fp, 0, SEEK_SET)
```

---

**fgetpos / fsetpos：获取/设置位置**
`int fgetpos(FILE *<fp>, fpos_t *<pos>); / int fsetpos(FILE *<fp>, const fpos_t *<pos>);`
```c
fpos_t pos;
fgetpos(fp, &pos);    // 保存当前位置
// ... 读取操作 ...
fsetpos(fp, &pos);    // 恢复到保存的位置
```

---

## 文件状态检查

**feof：检查是否到达文件末尾**
`int feof(FILE *<fp>);`
```c
while (!feof(fp)) {
    int ch = fgetc(fp);
    if (ch == EOF) break;
    putchar(ch);
}
```

---

**ferror：检查文件错误**
`int ferror(FILE *<fp>);`
```c
if (ferror(fp)) {
    printf("File error occurred!\n");
    clearerr(fp);    // 清除错误标志
}
```

---

## 标准流

**标准输入输出流**
`stdin | stdout | stderr`
```c
// 从标准输入读取
char buffer[100];
fgets(buffer, sizeof(buffer), stdin);
// 输出到标准输出
fprintf(stdout, "Output: %s\n", buffer);
// 输出到标准错误
fprintf(stderr, "Error: %s\n", "Something went wrong");
```

---

## 文件缓冲

**setvbuf：设置缓冲区**
`int setvbuf(FILE *<fp>, char *<buf>, int <mode>, size_t <size>);`
```c
// 无缓冲
setvbuf(fp, NULL, _IONBF, 0);
// 行缓冲
setvbuf(fp, NULL, _IOLBF, 1024);
// 全缓冲
setvbuf(fp, NULL, _IOFBF, 4096);
```

---

**fflush：刷新缓冲区**
`int fflush(FILE *<fp>);`
```c
fflush(fp);      // 刷新文件缓冲区
fflush(stdout);  // 刷新标准输出
```

---

## 临时文件

**tmpfile：创建临时文件**
`FILE *tmpfile(void);`
```c
FILE *tmp = tmpfile();
if (tmp != NULL) {
    fprintf(tmp, "Temporary data\n");
    rewind(tmp);
    char buf[100];
    fgets(buf, sizeof(buf), tmp);
    printf("%s", buf);
    fclose(tmp);    // 关闭后自动删除
}
```

---

**tmpnam：生成临时文件名**
`char *tmpnam(char *<str>);`
```c
char filename[L_tmpnam];
tmpnam(filename);
printf("Temp file: %s\n", filename);
```

---

## 文件删除与重命名

**remove：删除文件**
`int remove(const char *<path>);`
```c
if (remove("temp.txt") == 0) {
    printf("File deleted\n");
} else {
    perror("Delete failed");
}
```

---

**rename：重命名文件**
`int rename(const char *<old>, const char *<new>);`
```c
if (rename("old.txt", "new.txt") == 0) {
    printf("File renamed\n");
} else {
    perror("Rename failed");
}
```
