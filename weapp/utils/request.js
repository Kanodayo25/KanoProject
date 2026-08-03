const { baseUrl } = require('../config')

/**
 * wx.request 统一封装
 * 后端返回体约定：{ success, code, msg, data }（对应 com.kano.project.common.model.Result）
 * 鉴权约定：请求头携带 token
 */
function request(path, method = 'GET', data = {}) {
  return new Promise((resolve, reject) => {
    const token = getApp().globalData.token || wx.getStorageSync('token') || ''
    wx.request({
      url: baseUrl + path,
      method,
      data,
      header: {
        'content-type': 'application/json',
        'token': token
      },
      success: (res) => {
        const body = res.data || {}
        if (res.statusCode === 200 && body.success) {
          resolve(body.data)
        } else if (res.statusCode === 401) {
          wx.removeStorageSync('token')
          wx.showToast({ title: '登录已过期，请重新登录', icon: 'none' })
          setTimeout(() => wx.reLaunch({ url: '/pages/login/login' }), 600)
          reject(body)
        } else {
          reject(body)
        }
      },
      fail: (err) => reject(err)
    })
  })
}

function get(path, data) {
  return request(path, 'GET', data)
}

function post(path, data) {
  return request(path, 'POST', data)
}

module.exports = { request, get, post }
