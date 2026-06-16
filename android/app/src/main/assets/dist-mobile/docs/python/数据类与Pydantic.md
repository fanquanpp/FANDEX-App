
## 1. dataclass

```python
from dataclasses import dataclass, field

@dataclass
class User:
  name: str
  age: int
  tags: list[str] = field(default_factory=list)
```

## 2. Pydantic

```python
from pydantic import BaseModel, EmailStr

class UserCreate(BaseModel):
  name: str
  email: EmailStr
  age: int = Field(ge=0, le=150)

user = UserCreate(name="Alice", email="a@b.com", age=25)
user.model_dump_json()
```
