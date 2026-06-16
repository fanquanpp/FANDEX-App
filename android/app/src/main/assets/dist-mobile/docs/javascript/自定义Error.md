
## 1. 自定义错误类

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

// 错误类型层次
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
console.log(err instanceof TimeoutError); // true
console.log(err instanceof NetworkError); // true
console.log(err instanceof AppError); // true
```

## 2. 错误链（Error Cause）

```javascript
// ES2022 内置 cause 支持
try {
  JSON.parse('invalid');
} catch (e) {
  throw new AppError('数据解析失败', { cause: e });
}

// 获取完整错误链
class ChainedError extends Error {
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
    while (current?.cause) current = current.cause;
    return current;
  }
}
```

## 3. 聚合错误（AggregateError）

```javascript
const errors = [new Error('A'), new Error('B')];
const aggregate = new AggregateError(errors, '多个错误');
console.log(aggregate.errors.length); // 2

// Promise.any 失败时产生 AggregateError
Promise.any([Promise.reject(new Error('A')), Promise.reject(new Error('B'))]).catch((err) => {
  console.log(err instanceof AggregateError); // true
});
```
