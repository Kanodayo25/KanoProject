# 公务员考试助手 · 微信小程序

对接 KanoProject 后端（Dubbo + LangChain4j + Milvus RAG 知识库）的小程序前端骨架。

## 目录结构

```
weapp/
├── app.js / app.json / app.wxss        小程序入口与全局配置
├── project.config.json                 开发者工具项目配置（appid 当前为游客，需替换）
├── config.js                           后端接口基地址 + 默认知识库集合名
├── sitemap.json
├── utils/
│   ├── request.js                      wx.request 封装（统一 Result 解析 / token / 401 处理）
│   └── auth.js                          wx.login → code 换 token
└── pages/
    ├── login/                          登录页
    ├── chat/                           对话页（知识库问答 RAG，支持集合 / topK 选择与图片附件）
    └── upload/                         文档上传页（多选文件逐个上传，含进度与结果）
```

## 使用步骤

1. 微信开发者工具 → 导入项目 → 选择本 `weapp` 目录。
2. `project.config.json` 中把 `appid` 换成你自己的小程序 AppID（当前是游客模式 `touristappid`）。
3. 修改 `config.js` 的 `baseUrl` 为后端 HTTPS 地址。
4. 在微信公众平台后台配置「服务器域名」（request / uploadFile 合法域名，需 HTTPS）。

## 后端接口契约

| 接口 | 方法 | 入参 | 返回 data |
|---|---|---|---|
| `/auth/login` | POST | `{ code }` | `token` 字符串 |
| `/ai/chat/rag` | POST | `{ memoryId, message, collectionName, topK }`（JSON body） | 回答文本 |
| `/ai/chat` | POST | `?memoryId=&message=`（@RequestParam） | 回答文本 |
| `/document/upload` | POST | `multipart`: `file`(MultipartFile) + `fileName` + `collectionName` | `{ documentId, fileName, collectionName, chunkCount }` |

统一返回体：`{ success, code, msg, data }`（对应后端 `com.kano.project.common.model.Result`）。
鉴权：请求头携带 `token`；401 时小程序自动回登录页。

## 说明

- 对话页为知识库问答（RAG）单模式；topK 可选 3/5/10，默认 5，与后端 `doc.search.topk` 默认一致。
- 会话记忆：启动时生成数字 `memoryId`（后端接口为 Long）存本地，多轮对话共用（对应后端 Redis 记忆）。
- 上传仅支持文本类文件，与后端 `SupportedFileTypes` 白名单一致。
- **对话附带图片**：小程序先调后端已有接口 `POST /troubleInfo/upload`（multipart `file`）上传图片拿到 URL，随 RAG 请求带 `imageUrl`；后端已在 RAG 链路消费该字段（有图时用 `UserMessage.from(TextContent, ImageContent)` 组装消息交给模型）。
