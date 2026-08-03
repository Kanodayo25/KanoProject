const { login } = require('../../utils/auth')

Page({
  data: {
    loading: false
  },

  onLoad() {
    // 已登录则直接进入对话页
    if (wx.getStorageSync('token')) {
      wx.switchTab({ url: '/pages/chat/chat' })
    }
  },

  handleLogin() {
    if (this.data.loading) return
    this.setData({ loading: true })
    login()
      .then(() => {
        // 初始化会话记忆 id（RAG 多轮对话用；后端接口为 Long，必须用数字）
        if (!wx.getStorageSync('memoryId')) {
          const memoryId = Date.now()
          wx.setStorageSync('memoryId', memoryId)
          getApp().globalData.memoryId = memoryId
        }
        wx.switchTab({ url: '/pages/chat/chat' })
      })
      .catch((e) => {
        wx.showToast({ title: (e && e.msg) || '登录失败，请重试', icon: 'none' })
      })
      .finally(() => this.setData({ loading: false }))
  }
})
