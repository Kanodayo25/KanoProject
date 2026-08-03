App({
  globalData: {
    token: '',
    memoryId: ''
  },

  onLaunch() {
    // 恢复登录态
    const token = wx.getStorageSync('token')
    if (token) this.globalData.token = token

    // 暂不启用登录界面，启动时直接初始化会话记忆 id（后端接口为 Long，用数字）
    let memoryId = wx.getStorageSync('memoryId')
    if (!memoryId) {
      memoryId = Date.now()
      wx.setStorageSync('memoryId', memoryId)
    }
    this.globalData.memoryId = memoryId
  }
})
