
## 1. BeautifulSoup

```python
from bs4 import BeautifulSoup
import requests

response = requests.get('https://example.com')
soup = BeautifulSoup(response.text, 'html.parser')
titles = [h2.text for h2 in soup.find_all('h2')]
```

## 2. Scrapy

```python
class QuotesSpider(scrapy.Spider):
  name = 'quotes'
  start_urls = ['https://quotes.toscrape.com']

  def parse(self, response):
    for quote in response.css('div.quote'):
      yield {'text': quote.css('span::text').get()}
```
