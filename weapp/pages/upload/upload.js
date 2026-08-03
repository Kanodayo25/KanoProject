const { collectionName } = require('../../config')

/**
 * 上传页：多选文件 → 逐个上传入库（对应 POST /document/upload）
 * multipart: file(MultipartFile) + formData { fileName, collectionName }
 * → data: { documentId, fileName, collectionName, chunkCount }
 */
const SUPPORTED = ['txt', 'pdf', 'doc', 'docx', 'md', 'html', 'rtf', 'xml', 'csv']

function extOf(name) {
  const i = name.lastIndexOf('.')
  return i < 0 ? '' : name.slice(i + 1).toLowerCase()
}

Page({
  data: {
    files: [],
    collectionName: collectionName,
    uploading: false,
    doneCount: 0,
    allDone: false
  },

  chooseFiles() {
    if (this.data.uploading) return
    wx.chooseMessageFile({
      count: 9,
      type: 'file',
      extension: SUPPORTED,
      success: (res) => {
        const files = res.tempFiles.map((f) => ({
          fileName: f.name,
          fileSize: (f.size / 1024).toFixed(1) + ' KB',
          filePath: f.path,
          status: 'pending',
          statusText: '待上传',
          progress: 0,
          uploading: false,
          result: null
        }))
        this.setData({ files, doneCount: 0, allDone: false })
      }
    })
  },

  onCollectionInput(e) {
    this.setData({ collectionName: e.detail.value })
  },

  doUpload() {
    if (this.data.uploading || !this.data.files.length) return
    this.setData({ uploading: true, doneCount: 0, allDone: false })

    const { baseUrl } = require('../../config')
    const token = wx.getStorageSync('token') || ''
    const collection = this.data.collectionName || collectionName
    const files = this.data.files
    let done = 0

    const uploadOne = (index) => {
      if (index >= files.length) {
        this.setData({ uploading: false, allDone: true })
        return
      }
      const item = files[index]
      this.setData({
        [`files[${index}].status`]: 'uploading',
        [`files[${index}].statusText`]: '上传中',
        [`files[${index}].uploading`]: true,
        [`files[${index}].progress`]: 0
      })

      const task = wx.uploadFile({
        url: baseUrl + '/document/upload',
        filePath: item.filePath,
        name: 'file',
        header: { 'token': token },
        formData: { fileName: item.fileName, collectionName: collection },
        success: (res) => {
          let body = {}
          try {
            body = JSON.parse(res.data || '{}')
          } catch (e) {}
          if (body.success) {
            this.setData({
              [`files[${index}].result`]: body.data,
              [`files[${index}].status`]: 'success',
              [`files[${index}].statusText`]: '成功'
            })
          } else {
            this.setData({
              [`files[${index}].status`]: 'fail',
              [`files[${index}].statusText`]: body.msg || '失败'
            })
          }
        },
        fail: () => this.setData({
          [`files[${index}].status`]: 'fail',
          [`files[${index}].statusText`]: '失败'
        }),
        complete: () => {
          this.setData({ [`files[${index}].uploading`]: false })
          done++
          this.setData({ doneCount: done })
          uploadOne(index + 1)
        }
      })
      task.onProgressUpdate((p) => {
        this.setData({ [`files[${index}].progress`]: p.progress })
      })
    }

    uploadOne(0)
  },

  goChat() {
    wx.switchTab({ url: '/pages/chat/chat' })
  }
})
