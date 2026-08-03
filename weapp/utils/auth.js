const { post } = require('./request')

/**
 * 微信登录：wx.login 拿 code，交给后端换 token
 * 后端契约：POST /auth/login  body: { code }  →  返回 data: token 字符串
 */
function login() {
  return new Promise((resolve, reject) => {
    wx.login({
      success: async (res) => {
        if (!res.code) {
          reject({ msg: '获取登录凭证失败' })
          return
        }
        try {
          const token = await post('/auth/login', { code: res.code })
          wx.setStorageSync('token', token)
          getApp().globalData.token = token
          resolve(token)
        } catch (e) {
          reject(e)
        }
      },
      fail: (err) => reject(err)
    })
  })
}

module.exports = { login }
