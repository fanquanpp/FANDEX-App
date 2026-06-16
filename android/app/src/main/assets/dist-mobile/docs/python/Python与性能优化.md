
## 1. 性能分析

```python
# cProfile
python -m cProfile -s time my_script.py

# line_profiler
@profile
def slow_function():
  ...

# memory_profiler
@profile
def memory_heavy():
  ...
```

## 2. 优化技巧

- 使用 `__slots__` 减少内存
- 使用生成器代替列表
- 使用 NumPy 向量化
- 使用 Cython 加速
- 使用 `functools.lru_cache`
