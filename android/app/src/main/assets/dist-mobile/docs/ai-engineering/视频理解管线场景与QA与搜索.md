
## 问题

长视频QA是2026年规模下带宽最饥渴的多模态问题。Gemini 2.5 Pro可以原生读取2小时视频，但将100小时视频摄入可查询语料库仍需要场景级索引。生产形态结合场景分割（TransNetV2或PySceneDetect）、每场景VLM字幕（Gemini 2.5、Qwen3-VL-Max或Molmo 2）、转录对齐（Whisper-v3-turbo带词时间戳）和并排存储字幕、帧嵌入和转录的多向量索引。查询管线以(start, end)时间戳加帧预览回答。

基准公开（ActivityNet-QA、NeXT-GA）加你自己的100查询自定义集。计数和动作类型问题上的幻觉是已知困难失败类；顶点项目显式测量它。

## 核心架构

### 摄取管线

1. **场景分割。** TransNetV2或PySceneDetect切分视频为场景。
2. **每场景字幕。** VLM为每个场景生成文本描述。
3. **转录对齐。** Whisper-v3-turbo带词级时间戳。
4. **多向量索引。** 字幕嵌入+帧嵌入+转录并排索引。

### 查询管线

1. **检索。** 混合搜索找到相关场景。
2. **回答生成。** VLM基于检索场景+查询生成回答。
3. **时间定位。** 返回(start, end)时间戳+帧预览。

## 关键术语

| 术语                    | 常见说法     | 实际含义                     |
| ----------------------- | ------------ | ---------------------------- |
| Scene segmentation      | "场景分割"   | 将视频切分为语义连贯的场景   |
| Temporal grounding      | "时间定位"   | 定位视频中特定事件的时间范围 |
| Multi-vector index      | "多向量索引" | 并排索引多种模态的嵌入       |
| Transcription alignment | "转录对齐"   | 将语音转录与视频时间戳对齐   |

## 延伸阅读

- Twelve Labs — Marengo+Pegasus视频理解
- VideoDB — 视频CRUD API
- AI2 Molmo 2 — 开放VLM
- TimeLens-100K — 时间定位基准
