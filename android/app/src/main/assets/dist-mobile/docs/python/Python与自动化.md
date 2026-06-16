
## 1. 文件自动化

```python
import shutil
from pathlib import Path

# 批量重命名
for f in Path('photos').glob('*.jpg'):
  new_name = f"IMG_{f.stat().st_mtime_ns}.jpg"
  f.rename(f.parent / new_name)
```

## 2. 定时任务

```python
from apscheduler.schedulers.asyncio import AsyncIOScheduler

scheduler = AsyncIOScheduler()
scheduler.add_job(cleanup, 'cron', hour=2)
scheduler.start()
```
