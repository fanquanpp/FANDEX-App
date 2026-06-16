
## 1. PyTorch

```python
import torch

model = torch.nn.Sequential(
  torch.nn.Linear(784, 256),
  torch.nn.ReLU(),
  torch.nn.Linear(256, 10)
)

optimizer = torch.optim.Adam(model.parameters())
loss_fn = torch.nn.CrossEntropyLoss()

for epoch in range(10):
  output = model(inputs)
  loss = loss_fn(output, labels)
  optimizer.zero_grad()
  loss.backward()
  optimizer.step()
```
