# Kotlin 序列化速查

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 插件与依赖

**build.gradle.kts 配置**
`plugins { kotlin("plugin.serialization") } dependencies { implementation("...") }`
```kotlin
plugins {
    kotlin("jvm") version "2.0.0";
    // 必须添加序列化编译器插件
    kotlin("plugin.serialization") version "2.0.0";
};
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3");
};
```

---

## 基本序列化

**@Serializable 标记数据类**
`@Serializable data class <Name>(val <prop>: <Type>, ...)`
```kotlin
import kotlinx.serialization.*;
import kotlinx.serialization.json.*;
// 用 @Serializable 标记数据类
@Serializable
data class User(val name: String, val age: Int);
fun main() {
    // 序列化：对象 -> JSON 字符串
    val user = User("Alice", 25);
    val jsonString = Json.encodeToString(user);
    println(jsonString);  // {"name":"Alice","age":25}
    // 反序列化：JSON 字符串 -> 对象
    val decoded = Json.decodeFromString<User>("""{"name":"Bob","age":30}""");
    println(decoded);  // User(name=Bob, age=30)
};
```

---

## 配置 Json 对象

**自定义配置**
`val <json> = Json { <options> }`
```kotlin
// 创建自定义配置的 Json 实例
val json = Json {
    ignoreUnknownKeys = true;    // 忽略 JSON 中有但类中没有的字段
    prettyPrint = true;          // 格式化输出，方便阅读
    isLenient = true;            // 宽松模式，允许非标准 JSON
    encodeDefaults = true;       // 编码默认值
    coerceInputValues = true;    // 将无效值强制转为默认值
};
// 使用自定义配置
val user = json.decodeFromString<User>("""{"name":"Alice","age":25,"extra":"ignored"}""");
// extra 字段会被忽略，因为 ignoreUnknownKeys = true
```

---

## 可选字段与默认值

**默认值与可空字段**
`@Serializable data class <Name>(val <prop>: <Type> = <default>, val <prop2>: <Type>? = null)`
```kotlin
@Serializable
data class Profile(
    val name: String,
    val age: Int = 0,              // 默认值，JSON 中可以省略
    val email: String? = null,     // 可空类型，可以传 null 或省略
    val role: String = "user"      // 默认角色
);
fun main() {
    // JSON 中省略了 age 和 email，会使用默认值
    val profile = Json.decodeFromString<Profile>("""{"name":"Alice"}""");
    println(profile);  // Profile(name=Alice, age=0, email=null, role=user)
    // 序列化时，默认值默认不会输出（节省空间）
    val json = Json { encodeDefaults = true; };
    println(json.encodeToString(profile));
    // 加了 encodeDefaults 后会输出所有字段
};
```

---

## 嵌套对象和列表

**嵌套对象**
`@Serializable data class <Name>(val <prop>: <OtherClass>, val <list>: List<<Type>>)`
```kotlin
@Serializable
data class Address(val city: String, val street: String);
@Serializable
data class Person(
    val name: String,
    val address: Address,          // 嵌套对象
    val hobbies: List<String>      // 列表
);
fun main() {
    val person = Person(
        name = "Alice",
        address = Address("Beijing", "Chaoyang Road"),
        hobbies = listOf("Reading", "Coding")
    );
    val jsonString = Json.encodeToString(person);
    println(jsonString);
    // {"name":"Alice","address":{"city":"Beijing","street":"Chaoyang Road"},"hobbies":["Reading","Coding"]}
    val decoded = Json.decodeFromString<Person>(jsonString);
    println(decoded.address.city);  // Beijing
};
```

---

## 自定义字段名

**@SerialName 映射字段名**
`@SerialName("<json_name>") val <prop>: <Type>`
```kotlin
@Serializable
data class ApiResponse(
    @SerialName("status_code")
    val statusCode: Int,           // JSON 中是 status_code，Kotlin 中是 statusCode
    @SerialName("user_name")
    val userName: String
);
fun main() {
    val json = """{"status_code":200,"user_name":"Alice"}""";
    val response = Json.decodeFromString<ApiResponse>(json);
    println(response.statusCode);  // 200
    println(response.userName);    // Alice
};
```

---

## 枚举和密封类的序列化

**枚举序列化**
`@Serializable enum class <Name> { <VALUES> }`
```kotlin
@Serializable
enum class Status { ACTIVE, INACTIVE, SUSPENDED };
@Serializable
data class Account(val name: String, val status: Status);
fun main() {
    val account = Account("Alice", Status.ACTIVE);
    val json = Json.encodeToString(account);
    println(json);  // {"name":"Alice","status":"ACTIVE"}
};
```

**密封类的多态序列化**
`@Serializable sealed class <Name> { @Serializable @SerialName(<name>) data class <Sub>(...): <Name>() }`
```kotlin
@Serializable
sealed class Message {
    abstract val id: String;
    @Serializable
    @SerialName("text")
    data class Text(override val id: String, val content: String) : Message();
    @Serializable
    @SerialName("image")
    data class Image(override val id: String, val url: String) : Message();
};
fun main() {
    val messages: List<Message> = listOf(
        Message.Text("1", "Hello"),
        Message.Image("2", "https://example.com/img.png")
    );
    val json = Json.encodeToString(messages);
    // 会自动添加 type 字段区分子类
};
```

---

## 手动构建和解析 JSON

**手动构建 JSON 对象**
`buildJsonObject { put(<key>, <value>) }`
```kotlin
import kotlinx.serialization.json.*;
fun main() {
    // 手动构建 JSON 对象
    val jsonObject = buildJsonObject {
        put("name", "Alice");
        put("age", 25);
        put("isStudent", true);
        put("hobbies", buildJsonArray {
            add("Reading");
            add("Coding");
        });
        put("address", buildJsonObject {
            put("city", "Beijing");
        });
    };
    println(jsonObject.toString());
};
```

**手动解析 JSON**
`Json.parseToJsonElement(<jsonString>).jsonObject["<key>"]`
```kotlin
// 手动解析 JSON
val jsonElement = Json.parseToJsonElement("""{"name":"Bob","scores":[90,85,92]}""");
// 读取字段
val name = jsonElement.jsonObject["name"]?.jsonPrimitive?.content;  // "Bob"
val scores = jsonElement.jsonObject["scores"]?.jsonArray
    ?.map { it.jsonPrimitive.int };  // [90, 85, 92]
println("Name: $name, Scores: $scores");
```

---

## 封装网络请求的响应格式

**泛型响应封装**
`@Serializable data class <Result><T>(val <code>: Int, val <message>: String, val <data>: T? = null)`
```kotlin
@Serializable
data class ApiResult<T>(
    val code: Int,
    val message: String,
    val data: T? = null
);
@Serializable
data class Article(val id: Int, val title: String, val content: String);
fun main() {
    val json = """{"code":200,"message":"success","data":{"id":1,"title":"Hello","content":"World"}}""";
    val result = Json.decodeFromString<ApiResult<Article>>(json);
    result.data?.let { article ->
        println("文章: ${article.title} - ${article.content}");
    };
};
```

---

## 本地数据持久化

**保存设置到文件**
`fun <name>(<settings>: <Type>, <filePath>: String) { File(<filePath>).writeText(<json>.encodeToString(<settings>)) }`
```kotlin
import java.io.File;
@Serializable
data class AppSettings(
    val theme: String = "light",
    val fontSize: Int = 14,
    val recentFiles: List<String> = emptyList()
);
// 保存设置到文件
fun saveSettings(settings: AppSettings, filePath: String) {
    val json = Json { prettyPrint = true; };
    File(filePath).writeText(json.encodeToString(settings));
};
// 从文件读取设置
fun loadSettings(filePath: String): AppSettings {
    val text = File(filePath).readText();
    return Json.decodeFromString(text);
};
```

---

## 自定义序列化器

**KSerializer 自定义序列化器**
`object <Serializer> : KSerializer<<Type>> { override val descriptor / serialize / deserialize }`
```kotlin
import kotlinx.serialization.*;
import kotlinx.serialization.descriptors.*;
import kotlinx.serialization.encoding.*;
// 为 Date 类自定义序列化器
object DateSerializer : KSerializer<java.util.Date> {
    // 描述符：序列化后的类型是 LONG
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG);
    // 反序列化：从时间戳还原 Date
    override fun deserialize(decoder: Decoder): java.util.Date {
        return java.util.Date(decoder.decodeLong());
    };
    // 序列化：将 Date 转为时间戳
    override fun serialize(encoder: Encoder, value: java.util.Date) {
        encoder.encodeLong(value.time);
    };
};
// 使用自定义序列化器
@Serializable
data class Event(
    val title: String,
    @Serializable(with = DateSerializer::class)
    val date: java.util.Date
);
```

---

## 多格式支持

**Protobuf 支持**
`ProtoBuf.encodeToByteArray(<obj>) | ProtoBuf.decodeFromByteArray<<Type>>(<bytes>)`
```kotlin
// Protobuf 支持（需要添加对应依赖）
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.6.3");
};
import kotlinx.serialization.protobuf.*;
@Serializable
data class ProtoMessage(val id: Int, val text: String);
fun main() {
    val message = ProtoMessage(1, "Hello");
    // 序列化为 Protobuf 二进制格式
    val bytes = ProtoBuf.encodeToByteArray(message);
    // 从二进制还原
    val decoded = ProtoBuf.decodeFromByteArray<ProtoMessage>(bytes);
};
```

---

## 泛型序列化

**泛型类的序列化**
`@Serializable data class <Box><T>(val value: T)`
```kotlin
// 泛型类的序列化
@Serializable
data class Box<T>(val value: T);
fun main() {
    // 需要显式指定泛型类型
    val box = Box(User("Alice", 25));
    val json = Json.encodeToString(Box.serializer(User.serializer()), box);
    // 反序列化时也需要指定
    val decoded = Json.decodeFromString(Box.serializer(User.serializer()), json);
};
```
