
## 1. JUnit 5

```java
import org.junit.jupiter.api.*;

class UserServiceTest {
  @Test
  @DisplayName("Should create user")
  void createUser() {
    assertThrows(IllegalArgumentException.class, () -> new User(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"alice", "bob"})
  void validNames(String name) {
    assertNotNull(new User(name));
  }
}
```

## 2. Mockito

```java
@ExtendWith(MockitoExtension.class)
class Test {
  @Mock UserRepository repo;
  @InjectMocks UserService service;

  @Test
  void test() {
    when(repo.findById(1L)).thenReturn(Optional.of(user));
    User found = service.getUser(1L);
    assertEquals("Alice", found.getName());
  }
}
```
