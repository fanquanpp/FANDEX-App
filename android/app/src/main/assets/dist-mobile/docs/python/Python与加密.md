
## 1. 加密

```python
from cryptography.fernet import Fernet

key = Fernet.generate_key()
cipher = Fernet(key)
encrypted = cipher.encrypt(b"secret message")
decrypted = cipher.decrypt(encrypted)
```

## 2. 哈希

```python
import hashlib
hash = hashlib.sha256(data.encode()).hexdigest()

import bcrypt
hashed = bcrypt.hashpw(password.encode(), bcrypt.gensalt())
bcrypt.checkpw(input.encode(), hashed)
```
