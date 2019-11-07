/*
|--------------------------------------------------------------------------
| Android 交互接口
|--------------------------------------------------------------------------
*/

export default {
  isApp() {
    return typeof window.cnblogsApp === 'object'
  },

  // 获取博客
  getArticle() {
    if (!this.isApp()) return null
    var text = window.cnblogsApp.getArticle()
    return text ? JSON.parse(text) : null
  },

  // 获取下一篇文章
  getNextArticle() {
    if (!this.isApp()) return null
    var text = window.cnblogsApp.getNextArticle()
    return text ? JSON.parse(text) : null
  },

  // 获取博客评论
  getComments() {
    if (!this.isApp()) return null
    var text = window.cnblogsApp.getArticleComment()
    return text ? JSON.parse(text) : null
  },

  // 加载评论
  onLoadCommentClick(page) {
    if (!this.isApp()) return
    window.cnblogsApp.onLoadCommentClick(page)
  },

  // 图片点击
  onImageClick(src, images) {
    if (!this.isApp()) return
    window.cnblogsApp.onImageClick(src, images)
  },

  // 图片点击
  onArticleClick(item) {
    if (!this.isApp()) return
    window.cnblogsApp.onArticleClick(JSON.stringify(item))
  },

  // 主页点击
  onAuthorHomeClick(author) {
    if (!this.isApp()) return
    window.cnblogsApp.onAuthorHomeClick(JSON.stringify(author))
  },

  // 点赞
  handleLikeClick(isLike) {
    if (!this.isApp()) return
    window.cnblogsApp.onLikeClick(isLike + '')
  },

  // 点击评论
  onCommentClick(item) {
    if (!this.isApp()) return
    window.cnblogsApp.onCommentClick(item)
  },

  // 关闭当前窗口
  close() {
    if (!this.isApp()) return
    window.cnblogsApp.close()
  }
}
