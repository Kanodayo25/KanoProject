// 后端 HTTP 接口基地址
// 注意：正式环境小程序要求 https，且需在「微信公众平台 → 开发 → 开发设置 → 服务器域名」配置 request/uploadFile 合法域名
const baseUrl = 'https://your-domain.com/api'

// 默认知识库集合名（与后端 application.properties 的 milvus.collection.default 保持一致）
const collectionName = 'default_base'

module.exports = {
  baseUrl,
  collectionName
}
