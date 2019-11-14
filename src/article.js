/*
|--------------------------------------------------------------------------
| 文章处理
|--------------------------------------------------------------------------
*/

/* eslint-disable no-console */

// import 'highlight.js/styles/atom-one-dark.css'
// import 'highlight.js/styles/atom-one-light.css'
import 'highlight.js/styles/github.css'
import hljs from 'highlight.js'
import Android from './android'

var log = function (msg) {
  console.log("[RAE] " + msg)
}

window.scrollToLink = function (e) {
  log(e)
}

let HtmlParser = function (text) {
  let that = this
  this.document = document.createElement("div")
  this.document.id = 'content-body'
  this.document.innerHTML = text
  var timer;


  // 转换为 HTML
  this.toHtml = function () {
    // 延时渲染
    that.delayedRender()
    // 渲染代码块
    var document = that.renderCodeBlock(that.document)
    document = that.fixLink(document)
    return document.outerHTML
  }

  // 渲染 Markdown代码语法
  this.renderCodeBlock = function (doc) {
    let codes = doc.querySelectorAll('pre')
    if (codes.length <= 0) return doc
    for (var index in codes) {
      var block = codes[index]
      if (typeof block !== 'object') continue
      var code = block.querySelectorAll('code')
      if (code.length > 0) {
        // Markdown 语法
        hljs.highlightBlock(code[0])
        continue
      }

      // 默认的代码块
      var innerText = block.innerText
      block.innerHTML = '' // 清空
      code = document.createElement('code')
      code.innerText = innerText
      block.appendChild(code)
      hljs.highlightBlock(code)
    }
    return doc
  }

  // 延时渲染
  this.delayedRender = function () {
    timer = setTimeout(() => {
      var doc = window.document.getElementById('content-body')
      if (doc != null) {
        clearInterval(timer)
        timer = null
        that.fixImageSize(doc)
        that.renderMathJax(doc)
        that.bindImageClickEvent(doc)
      }
    }, 1);
  }

  // 渲染数学公式
  this.renderMathJax = function (doc) {
    var mathjaxRegx = /math+/gi
    if (!mathjaxRegx.test(doc.innerHTML)) return
    log('准备渲染数学公式脚本')
    var script = document.createElement('script')
    script.src = 'https://cdn.bootcss.com/mathjax/2.7.6/MathJax.js?config=TeX-AMS-MML_HTMLorMML'
    script.addEventListener('load', function () {
      log('渲染数学公式')
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

  // 绑定图片点击事件
  this.bindImageClickEvent = function (doc) {
    var images = doc.querySelectorAll('img')
    var handleImageClickEvent = function (e, images) {
      var src = e.src
      var urls = new Array()
      for (var i in images) {
        var url = images[i].src
        if (url) urls.push(url)
      }
      var json = JSON.stringify(urls)
      Android.onImageClick(src, json)
    }
    for (var index in images) {
      var img = images[index]
      if (typeof img !== 'object') continue
      img.onclick = function (item) {
        // 图片点击
        handleImageClickEvent(item.target, images)
        return false
      }
    }
  }

  // 图片渲染
  this.fixImageSize = function (doc) {
    var images = doc.querySelectorAll('img')
    for (var index in images) {
      var item = images[index]
      if (typeof item !== 'object') continue
      if (item.src.indexOf('.gif') > 0) {
        item.style.width = 'auto'
        item.style.height = 'auto'
      }
    }
  }

  // 适配锚点链接
  this.fixLink = function (doc) {
    // var links = doc.querySelectorAll('a')
    // for (var index in links) {
    //   var item = links[index]
    //   if (typeof item !== 'object') continue
    //   var href = item.getAttribute('href')
    //   if (href.indexOf('#') == 0) {
    //     item.href = 'javascript:scrollToLink();'
    //   }
    // }
    return doc
  }
}

export default {
  // 将文章内容转换为HTML
  parseHtml: function (text) {
    return new HtmlParser(text).toHtml()
  }
}