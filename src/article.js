/*
|--------------------------------------------------------------------------
| 文章处理
|--------------------------------------------------------------------------
*/

/* eslint-disable no-console */

import 'highlight.js/styles/atom-one-dark.css'
import hljs from 'highlight.js'
import Android from './android'

export default {

  // 将文章内容转换为HTML
  parseHtml: function (text) {
    var content = document.createElement("div")
    content.id = 'content_body'
    content.innerHTML = text
    // Markdown 语法的高亮代码
    var codes = content.querySelectorAll('pre code')
    for (var index in codes) {
      var block = codes[index]
      if (typeof block === 'object') {
        hljs.highlightBlock(block)
      }
    }

    // 将博客园高亮代码的转换为highlight高亮代码
    if (codes.length <= 0) {
      codes = content.querySelectorAll('pre');
      for (var i in codes) {
        var pre = codes[i]
        if (typeof pre === 'object') {
          var innerText = pre.innerText
          pre.innerHTML = '' // 清空
          var code = document.createElement('code')
          code.innerText = innerText
          pre.appendChild(code)
          console.log(code)
          hljs.highlightBlock(code)
        }
      }
    }

    // 检查是否需要渲染数学公式
    var mathjaxRegx = /math+/gi
    if (mathjaxRegx.test(text)) {
      this.initMathJaxScript()
    }

    // 图片点击处理
    this.bindImageEvent()

    return content.outerHTML
  },

  // 绑定图片事件
  bindImageEvent() {
    var $this = this
    var doc = document.getElementById('content')
    if (!doc) return
    doc.addEventListener('DOMNodeInserted', (e) => {
      e.target.querySelectorAll('img').forEach((img) => {
        img.onclick = function (item) {
          // 图片点击
          $this.handleImageClickEvent(item.target)
          return false
        }
      })
    })
  },

  // 处理图片点击事件
  handleImageClickEvent(e) {
    var src = e.src
    var urls = new Array()
    var images = document.querySelectorAll('img')
    for (var i in images) {
      var url = images[i].src
      if (url) urls.push(url)
    }
    var json = JSON.stringify(urls)
    console.log(src, json)
    Android.onImageClick(src, json)
  },

  // 初始化数学公式脚本
  initMathJaxScript() {
    console.log('初始化数学公式脚本')
    var script = document.createElement('script')
    script.src = 'https://cdn.bootcss.com/mathjax/2.7.6/MathJax.js?config=TeX-AMS-MML_HTMLorMML'
    script.addEventListener('load', function () {
      console.log('渲染数学公式')
      // 加载成功后，初始化脚本
      window.MathJax.Hub.Config({
        tex2jax: { inlineMath: [['$', '$'], ['\\(', '\\)']], processClass: 'math', processEscapes: true },
        TeX: {
          equationNumbers: { autoNumber: ['AMS'], useLabelIds: true },
          extensions: ['extpfeil.js', 'mediawiki-texvc.js'],
          Macros: { bm: "\\boldsymbol" }
        },
        'HTML-CSS': { linebreaks: { automatic: true } },
        SVG: { linebreaks: { automatic: true } }
      });
      // 渲染数学公式
      window.MathJax.Hub.Queue(["Typeset", window.MathJax.Hub, document.body])
    })
    document.body.appendChild(script)
  }
}