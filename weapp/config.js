// 后端 HTTP 接口基地址（controller 端口 8099，接口路径以 / 开头，如 /ai/chat/rag）
// 本地开发：微信开发者工具用 http://localhost:8099，需勾选「不校验合法域名」
// 真机/正式：必须是 https，且在小程序后台「服务器域名」配置 request/uploadFile 合法域名
const baseUrl = 'http://localhost:8099'

// 默认知识库集合名（与后端 application.properties 的 milvus.collection.default 保持一致）
const collectionName = 'default_base'

// 对话页图片上传按钮（🖼️）可见的 openid 白名单
// 不在名单内的用户看不到上传按钮；需先登录拿到 openid（GET /auth/openid）才会判断
const openidWhitelist = [
  // 'oXXXXXXXXXXXXXX' // 填入自己的 openid
]

module.exports = {
  baseUrl,
  collectionName,
  openidWhitelist
}
