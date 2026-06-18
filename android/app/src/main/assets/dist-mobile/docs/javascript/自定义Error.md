# JavaScript 自定义 Error

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 自定义错误类

**基础自定义错误**：继承 Error 类
`class <错误类> extends Error { constructor(<消息>) { super(<消息>); this.name = <名称>; } }`
```javascript
class AppError extends Error {
  constructor(message, options = {}) {
    super(message);
    this.name = this.constructor.name;
    this.code = options.code || 'UNKNOWN_ERROR';
    this.statusCode = options.statusCode || 500;
    this.details = options.details || null;
    Object.setPrototypeOf(this, new.target.prototype);
  }
}

// 使用
const error = new AppError('操作失败', {
  code: 'OPERATION_FAILED',
  statusCode: 500,
});
console.log(error.message);      // '操作失败'
console.log(error.code);         // 'OPERATION_FAILED'
console.log(error.statusCode);   // 500
```

---

**错误类型层次**：构建错误类型体系
`class <子错误> extends <父错误> { constructor(<消息>, <选项>) { super(<消息>, { code: '...', ...<选项> }); } }`
```javascript
class NetworkError extends AppError {
  constructor(message, options = {}) {
    super(message, { code: 'NETWORK_ERROR', ...options });
  }
}

class TimeoutError extends NetworkError {
  constructor(message, options = {}) {
    super(message, { code: 'TIMEOUT_ERROR', ...options });
  }
}

class ValidationError extends AppError {
  constructor(message, options = {}) {
    super(message, { code: 'VALIDATION_ERROR', ...options });
    this.fields = options.fields || [];
  }
}

// instanceof 正常工作
const err = new TimeoutError('请求超时');
console.log(err instanceof TimeoutError);  // true
console.log(err instanceof NetworkError);  // true
console.log(err instanceof AppError);      // true
console.log(err instanceof Error);          // true
```

---

## 错误链（Error Cause）

**ES2022 cause 属性**：传递原始错误
`new Error(<消息>, { cause: <原始错误> })`
```javascript
try {
  JSON.parse('invalid');
} catch (e) {
  throw new AppError('数据解析失败', { cause: e });
}

// 获取原始错误
try {
  // 可能失败的代码
} catch (error) {
  if (error.cause) {
    console.error('原始错误:', error.cause);
  }
}
```

---

**错误链遍历**：获取完整错误链
`class <错误类> extends Error { get chain() { ... } get rootCause() { ... } }`
```javascript
class ChainedError extends Error {
  constructor(message, options = {}) {
    super(message, options);
    this.name = this.constructor.name;
  }

  get chain() {
    const errors = [this];
    let current = this.cause;
    while (current) {
      errors.push(current);
      current = current.cause;
    }
    return errors;
  }

  get rootCause() {
    let current = this.cause;
    while (current?.cause) {
      current = current.cause;
    }
    return current;
  }
}

// 使用
try {
  try {
    JSON.parse('invalid');
  } catch (e) {
    throw new ChainedError('解析失败', { cause: e });
  }
} catch (e) {
  console.log(e.chain);       // [ChainedError, SyntaxError]
  console.log(e.rootCause);   // SyntaxError
}
```

---

## 聚合错误（AggregateError）

**创建 AggregateError**：聚合多个错误
`new AggregateError(<错误数组>[, <消息>])`
```javascript
const errors = [new Error('错误 A'), new Error('错误 B')];
const aggregate = new AggregateError(errors, '多个错误发生');
console.log(aggregate.errors.length);  // 2
console.log(aggregate.message);        // '多个错误发生'
```

---

**Promise.any 失败**：产生 AggregateError
`Promise.any([<rejected promises>]).catch(<aggregateError>)`
```javascript
Promise.any([
  Promise.reject(new Error('A 失败')),
  Promise.reject(new Error('B 失败')),
  Promise.reject(new Error('C 失败')),
]).catch((err) => {
  console.log(err instanceof AggregateError);  // true
  console.log(err.errors);  // [Error: A 失败, Error: B 失败, Error: C 失败]
  err.errors.forEach((e) => console.error(e.message));
});
```

---

## 错误处理模式

**try-catch 捕获**：根据错误类型处理
`try { ... } catch (<错误>) { if (<错误> instanceof <类型>) { ... } }`
```javascript
try {
  // 可能抛出多种错误的代码
  const data = await fetchData();
} catch (error) {
  if (error instanceof ValidationError) {
    console.error('验证错误:', error.fields);
  } else if (error instanceof NetworkError) {
    console.error('网络错误:', error.message);
  } else if (error instanceof TimeoutError) {
    console.error('超时错误，请重试');
  } else {
    console.error('未知错误:', error);
  }
}
```

---

**错误转换**：分层错误处理
`catch (<技术错误>) { throw new <业务错误>(<消息>, { cause: <技术错误> }); }`
```javascript
// 数据访问层
class UserRepository {
  async findById(id) {
    try {
      const response = await fetch(`/api/users/${id}`);
      return await response.json();
    } catch (error) {
      throw new NetworkError('获取用户失败', { cause: error });
    }
  }
}

// 业务逻辑层
class UserService {
  async getUser(id) {
    try {
      const user = await this.userRepo.findById(id);
      if (!user) {
        throw new ValidationError('用户不存在', { fields: ['id'] });
      }
      return user;
    } catch (error) {
      if (error instanceof NetworkError) {
        throw new AppError('服务暂时不可用', {
          code: 'SERVICE_UNAVAILABLE',
          statusCode: 503,
          cause: error,
        });
      }
      throw error;
    }
  }
}
```

---

## 错误工厂

**错误工厂函数**：统一创建错误
`function createError(<类型>, <消息>, <选项>) { ... }`
```javascript
const ErrorFactory = {
  validation(message, fields = []) {
    return new ValidationError(message, { fields });
  },

  network(message, statusCode = 500) {
    return new NetworkError(message, { statusCode });
  },

  timeout(message, timeout) {
    return new TimeoutError(message, { timeout });
  },

  notFound(resource) {
    return new AppError(`${resource} 不存在`, {
      code: 'NOT_FOUND',
      statusCode: 404,
    });
  },
};

// 使用
throw ErrorFactory.validation('邮箱格式不正确', ['email']);
throw ErrorFactory.notFound('用户');
```

---

## 错误断言

**断言函数**：条件检查
`function assert(<条件>, <错误>) { if (!<条件>) throw <错误>; }`
```javascript
function assert(condition, error) {
  if (!condition) {
    throw error;
  }
}

function assertValidUser(user) {
  assert(user, ErrorFactory.notFound('用户'));
  assert(user.name, ErrorFactory.validation('用户名不能为空', ['name']));
  assert(user.age >= 0, ErrorFactory.validation('年龄不能为负数', ['age']));
}

// 使用
try {
  assertValidUser({ name: '', age: -1 });
} catch (error) {
  if (error instanceof ValidationError) {
    console.error('验证失败:', error.fields);
  }
}
```

---

## 错误日志

**错误日志记录**：统一记录错误
`function logError(<错误>) { ... }`
```javascript
function logError(error) {
  const errorInfo = {
    name: error.name,
    message: error.message,
    code: error.code,
    statusCode: error.statusCode,
    stack: error.stack,
    cause: error.cause ? {
      name: error.cause.name,
      message: error.cause.message,
    } : null,
    timestamp: new Date().toISOString(),
  };
  console.error(JSON.stringify(errorInfo, null, 2));
  // 发送到错误监控系统
}

// 使用
try {
  // 可能出错的代码
} catch (error) {
  logError(error);
  throw error;  // 重新抛出
}
```

---

## 错误边界

**异步错误边界**：捕获异步错误
`async function withErrorBoundary(<异步函数>) { try { ... } catch (<错误>) { ... } }`
```javascript
async function withErrorBoundary(asyncFn, fallback = null) {
  try {
    return await asyncFn();
  } catch (error) {
    logError(error);
    if (error instanceof ValidationError) {
      // 验证错误可以恢复
      return fallback;
    }
    if (error instanceof NetworkError) {
      // 网络错误可以重试
      return fallback;
    }
    // 其他错误向上抛出
    throw error;
  }
}

// 使用
const data = await withErrorBoundary(
  () => fetchData(),
  { default: true }
);
```

---

## 错误序列化

**错误转 JSON**：序列化错误信息
`function errorToJSON(<错误>) { ... }`
```javascript
function errorToJSON(error) {
  return {
    name: error.name,
    message: error.message,
    code: error.code,
    statusCode: error.statusCode,
    details: error.details,
    fields: error.fields,
    cause: error.cause ? errorToJSON(error.cause) : null,
  };
}

// 使用
const error = new ValidationError('验证失败', {
  fields: ['email', 'password'],
});
console.log(JSON.stringify(errorToJSON(error)));
```
