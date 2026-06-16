
## 1. 工具链

```bash
# Ruff — linter + formatter
ruff check .
ruff format .

# mypy — 类型检查
mypy --strict .

# pre-commit
pre-commit run --all-files
```

## 2. pyproject.toml 配置

```toml
[tool.ruff]
line-length = 88
select = ["E", "F", "I", "N", "UP"]

[tool.mypy]
strict = true
```
