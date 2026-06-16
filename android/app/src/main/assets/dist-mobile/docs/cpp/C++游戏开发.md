
## 1. 游戏循环

```cpp
class Game {
  bool running_ = true;
public:
  void run() {
    auto last = std::chrono::high_resolution_clock::now();
    while (running_) {
      auto now = std::chrono::high_resolution_clock::now();
      float dt = std::chrono::duration<float>(now - last).count();
      last = now;
      processInput();
      update(dt);
      render();
    }
  }
};
```
