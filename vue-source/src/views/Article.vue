<template>
  <div class="container">
    <!-- 文章区 -->
    <div class="article">
      <!-- 标题 -->
      <h1 id="title"
          class="title">{{ model.title }}</h1>
      <!-- 作者 -->
      <van-row type="flex"
               class="author">
        <van-col span="4">
          <van-image round
                     @click="handleAuthorHome"
                     width="2.4rem"
                     height="2.4rem"
                     :src="model.avatar" />
        </van-col>
        <van-col span="12"
                 @click="handleAuthorHome">
          <p class="author-title">{{ model.author }}</p>
          <p class="date">{{ model.date }}</p>
        </van-col>
        <van-col span="8"
                 style="text-align: right">
          <van-button class="author-button"
                      type="default"
                      size="small"
                      round
                      @click="handleAuthorHome">进入主页
            <van-icon name="arrow"
                      size="9" />
          </van-button>
        </van-col>
      </van-row>

      <!-- 分割线 -->
      <div class="line"></div>

      <!-- 内容区 -->
      <div class="content">

        <!-- 内容实体 -->
        <div id="content"
             v-html="model.content">
        </div>

        <!-- 点赞区 -->
        <van-button round
                    icon="good-job"
                    class="recommend"
                    color="#ffd100"
                    :loading="likeLoading"
                    :disabled="isLike"
                    @click="handleLikeClick">
          {{ isLike ? '感谢推荐':'推荐' }}
        </van-button>

        <i class="tips">文章写得还不错，给个鼓励吧</i>

      </div>

    </div>

    <div class="space"></div>
    <!-- 上一篇 下一篇 -->
    <div class="diagg"
         v-if="nextArticles.length>0">
      <div class="title">推荐阅读</div>
      <div v-for="(item,key) in nextArticles"
           :key="key"
           class="article-next"
           @click="handleArticleClick(item)">
        <div class="article-next-title">{{ item.name }} </div>
        <div class="article-next-date">{{ item.date }}</div>
      </div>
    </div>

    <div class="space"></div>
    <!-- 推荐区 -->
    <div class="diagg"
         v-if="nextArticles.length>0">
      <div class="title">猜你喜欢</div>
      <div v-for="(item,key) in recommendArticles"
           :key="key"
           class="article-next"
           @click="handleArticleClick(item)">
        <div class="article-next-title">{{ item.name }} </div>
        <div class="article-next-date">{{ item.date }}</div>
      </div>
    </div>

    <div class="space"></div>

    <!-- 评论区 -->
    <comment />

  </div>
</template>

<script>

import Vue from 'vue';
import axios from 'axios';
import { Row, Col, Loading, Image, Button, Icon } from 'vant';
import Comment from '../components/AritcleComment'
import Article from '../article'
import Android from '../android'
Vue.use(Row).use(Col).use(Loading).use(Image).use(Button).use(Icon);


export default {
  name: 'home',
  components: { Comment },
  data() {
    return {
      isLike: false, // 是否已推荐
      likeAnmin: true, // 点赞动画
      likeLoading: false, // 点赞加载状态
      model: {
        title: '博客园',
        content: null
      },
      nextArticles: [], // 下一篇文章
      recommendArticles: [] // 推荐文章
    }
  },
  mounted() {
    // 初始化
    this.initAndroidObject()
    // 加载数据
    if (Android.isApp())
      this.loadData()
    else
      this.loadTestData()
  },
  methods: {

    /*
    |------------------------------------
    | Android 调用WEB接口
    |------------------------------------
    */
    initAndroidObject() {
      let $that = this
      var webApp = window.webapp || {}
      // 加载数据
      webApp.onLoadData = function () {
        this.loadData()
      }
      // 设置字体大小
      webApp.setFontSize = function () {

      }
      // 点赞成功
      webApp.onLikeFinish = function () {
        $that.likeLoading = false
        $that.isLike = true
      }
    },
    loadData() {
      // 加载博客
      this.model = Android.getBlog()
      if (this.model != null) {
        document.title = this.model.title
        this.model.content = Article.parseHtml(this.model.content)
      }
      // 加载上一篇文章
      var articles = Android.getNextArticle()
      for (var index in articles) {
        var article = articles[index]
        if (article.date) {
          this.nextArticles.push(article)
        } else {
          this.recommendArticles.push(article)
        }
      }
    },
    // 测试数据
    loadTestData() {
      var blogId = '11558268' // 11532708 11558268
      const service = axios.create({
        timeout: 15000
      })
      service({
        url: 'http://192.168.10.145:8085/test/blog?id=' + blogId,
        method: 'GET'
      }).then(response => {
        this.model.title = '第809期 | 为什么会有大颠覆时代？'
        this.model.date = '12分钟前'
        this.model.author = '千与千寻'
        this.model.avatar = 'https://piccdn.igetget.com/img/201807/19/201807191556587359037523.jpg@100w_100h'
        this.model.content = Article.parseHtml(response.data.content)
      })
    },
    // 进入主页
    handleAuthorHome() {
      Android.onAuthorHomeClick(this.model.blogApp);
    },

    // 文章点击
    handleArticleClick(item) {
      Android.onArticleClick(item)
    },

    // 点赞
    handleLikeClick() {
      this.likeLoading = true
      Android.handleLikeClick()
    }
  }
}
</script>

<style  scoped>
.article-loading {
  margin-top: 240px;
}
.author-button {
  color: #b1b1b1;
  padding-left: 14px;
  padding-right: 14px;
}
.article .recommend {
  color: black !important;
  margin: 0 auto;
  padding-left: 60px;
  padding-right: 60px;
  margin-top: 40px;
  display: block;
  font-weight: bold;
}
.article .tips {
  text-align: center;
  font-size: 11px;
  color: #b3b3b3;
  display: block;
  margin-top: 12px;
}

.diagg {
  padding: 1.2rem;
  font-size: 16px;
}
.diagg .title {
  font-weight: bold;
}
.diagg .article-next {
  /* margin: 1rem 0 1rem 0.2rem; */
  margin-top: 1.2rem;
  background: #fff;
  box-shadow: 0 0.32rem 0.64rem 0 rgba(0, 0, 0, 0.04);
  padding: 1.01333rem 0.8rem 0.8rem;
}
.diagg .article-next .article-next-date {
  color: #999999;
  font-size: 12px;
  margin-top: 8px;
}
.space {
  background: #f5f5f5;
  height: 12px;
}
.vue-star {
  width: 300px;
  height: 300px;
}
</style>
