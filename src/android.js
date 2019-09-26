/*
|--------------------------------------------------------------------------
| Android 交互接口
|--------------------------------------------------------------------------
*/

export default {
  isApp() {
    return typeof window.app === 'object'
      && typeof window.app.getBlog == 'function'
  },

  // 获取博客
  getBlog() {
    if (!this.isApp()) return null
    var text = window.app.getBlog()
    return text ? JSON.parse(text) : null
  },

  onImageClick(src, images) {
    if (!this.isApp()) return
    window.app.onImageClick(src, images)
  }
}


