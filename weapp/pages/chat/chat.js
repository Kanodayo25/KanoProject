const { post, get } = require('../../utils/request')
const { collectionName, baseUrl, openidWhitelist } = require('../../config')

/**
 * 上传图片 → 返回可访问 URL（POST /troubleInfo/upload，后端已有接口，返回 COS URL）
 */
function uploadImage(filePath) {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token') || ''
    wx.uploadFile({
      url: baseUrl + '/troubleInfo/upload',
      filePath,
      name: 'file',
      header: { 'token': token },
      success: (res) => {
        let body = {}
        try {
          body = JSON.parse(res.data || '{}')
        } catch (e) {}
        if (body.success) resolve(body.data)
        else reject(body)
      },
      fail: reject
    })
  })
}

/**
 * 对话页：知识库问答（RAG），可附带图片
 * 接口：POST /ai/chat/rag  { memoryId, message, collectionName, topK, imageUrl }
 */
Page({
  data: {
    collectionName: collectionName,
    topKOptions: [3, 5, 10],
    topKIndex: 1, // 默认 5
    messages: [],
    draft: '',
    thinking: false,
    sending: false,
    scrollTo: '',
    imagePath: '', // 待发送的图片本地路径
    showAttach: false // 图片上传按钮是否可见（默认隐藏，仅 openid 白名单内显示）
  },

  onShow() {
    this.resolveOpenid()
  },

  // 解析当前用户 openid，命中白名单才开放图片上传按钮
  resolveOpenid() {
    const openid = wx.getStorageSync('openid')
    if (openid) {
      this.applyWhitelist(openid)
      return
    }
    // 无本地 openid：已登录则向后端取一次并缓存；未登录保持隐藏
    const token = wx.getStorageSync('token')
    if (!token) return
    get('/auth/openid')
      .then((oid) => {
        wx.setStorageSync('openid', oid)
        this.applyWhitelist(oid)
      })
      .catch(() => {})
  },

  applyWhitelist(openid) {
    const showAttach = !!openid && openidWhitelist.indexOf(openid) !== -1
    this.setData({ showAttach })
  },

  onCollectionInput(e) {
    this.setData({ collectionName: e.detail.value })
  },

  onTopKChange(e) {
    this.setData({ topKIndex: Number(e.detail.value) })
  },

  onInput(e) {
    this.setData({ draft: e.detail.value })
  },

  clearChat() {
    // 清空界面并换新的 memoryId（后端旧记忆 2 小时自动过期）
    const memoryId = Date.now()
    wx.setStorageSync('memoryId', memoryId)
    getApp().globalData.memoryId = memoryId
    this.setData({ messages: [], scrollTo: '', imagePath: '' })
    wx.showToast({ title: '已清空对话', icon: 'none' })
  },

  chooseImage() {
    if (this.data.sending || this.data.thinking) return
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      success: (res) => {
        this.setData({ imagePath: res.tempFiles[0].tempFilePath })
      }
    })
  },

  removeImage() {
    this.setData({ imagePath: '' })
  },

  onSend() {
    const content = (this.data.draft || '').trim()
    if (!content || this.data.sending || this.data.thinking) return

    const imagePath = this.data.imagePath
    const memoryId = Number(wx.getStorageSync('memoryId')) || Date.now()

    // 无图直接发；有图先上传拿 URL 再发
    const sendText = (imageUrl) => {
      this.setData({
        messages: [...this.data.messages, { role: 'user', content, image: imagePath || '' }],
        draft: '',
        imagePath: '',
        thinking: true,
        sending: false,
        scrollTo: 'bottom'
      })

      post('/ai/chat/rag', {
        memoryId,
        message: content,
        collectionName: this.data.collectionName,
        topK: this.data.topKOptions[this.data.topKIndex],
        imageUrl: imageUrl || ''
      })
        .then((data) => {
          this.setData({
            messages: [...this.data.messages, { role: 'ai', content: data || '（无返回）' }],
            scrollTo: 'bottom'
          })
        })
        .catch((e) => {
          wx.showToast({ title: (e && e.msg) || '请求失败', icon: 'none' })
          this.setData({
            messages: [...this.data.messages, { role: 'ai', content: '请求失败，请稍后重试' }],
            scrollTo: 'bottom'
          })
        })
        .finally(() => this.setData({ thinking: false, sending: false }))
    }

    if (imagePath) {
      // 图片上传中先禁用发送，成功后发消息；失败保持输入内容不变
      this.setData({ sending: true })
      uploadImage(imagePath)
        .then(sendText)
        .catch((e) => {
          wx.showToast({ title: (e && e.msg) || '图片上传失败', icon: 'none' })
          this.setData({ sending: false })
        })
    } else {
      sendText('')
    }
  }
})
