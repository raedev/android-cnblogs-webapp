<template>
  <div v-if="!isDataReady"
       class="container">
    <van-loading class="article-loading"
                 size="32px"
                 vertical
                 color="#1989fa">
      <div class="loading-text">精彩即将呈现..</div>
    </van-loading>
  </div>

  <div v-else
       class="container">
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
                     :src="model.author.avatar" />
        </van-col>
        <van-col span="12"
                 @click="handleAuthorHome">
          <p class="author-title">{{ model.author.name }}</p>
          <p class="date">{{ model.viewCount }} 次阅读 {{ model.date }}</p>
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
             v-html="model.content"></div>

        <!-- 点赞区 -->
        <van-button round
                    :icon="isLike ? 'smile' : 'good-job'"
                    class="recommend"
                    color="#ffd100"
                    :loading="likeLoading"
                    :disabled="isDataReady && isLike"
                    @click="handleLikeClick">
          {{ isLike ? "感谢推荐" : "推荐" }}
        </van-button>

        <i class="tips"
           v-if="!isLike">文章写得还不错，给个鼓励吧</i>
      </div>
    </div>

    <div id="scrollSpace"
         class="space"></div>

    <!-- 上一篇 下一篇 -->
    <div class="diagg"
         v-if="nextArticles.length > 0">
      <div class="title">下一篇文章</div>
      <div v-for="(item, key) in nextArticles"
           :key="key"
           class="article-next"
           @click="handleArticleClick(item)">
        <div class="article-next-title">{{ item.title }}</div>
        <div class="article-next-date">{{ item.date }}</div>
      </div>
    </div>

    <div class="space"></div>
    <!-- 推荐区 -->
    <div class="diagg"
         v-if="nextArticles.length > 0">
      <div class="title">相关文章</div>
      <div v-for="(item, key) in recommendArticles"
           :key="key"
           class="article-next"
           @click="handleArticleClick(item)">
        <div class="article-next-title">{{ item.title }}</div>
        <div class="article-next-date">{{ item.date }}</div>
      </div>
    </div>

    <div class="space"></div>

    <!-- 评论区 -->
    <comment />
  </div>
</template>

<script>
import Vue from "vue";
import { Row, Col, Loading, Image, Button, Icon } from "vant";
import Comment from "../components/AritcleComment";
import Article from "../article";
import Android from "../android";
import { log } from 'util';
Vue.use(Row)
  .use(Col)
  .use(Loading)
  .use(Image)
  .use(Button)
  .use(Icon);

export default {
  name: "home",
  components: { Comment },
  data() {
    return {
      isDataReady: false, // 第一次数据是否创建好了
      isLike: false, // 是否已推荐
      likeLoading: false, // 点赞加载状态
      model: {
        title: "",
        content: null,
        author: {}
      },
      nextArticles: [], // 下一篇文章
      recommendArticles: [] // 推荐文章
    };
  },
  mounted() {
    this.initAndroidObject();
  },
  created() {
    if (Android.isApp()) {
      this.loadData();
    }

  },
  methods: {
    /*
    |------------------------------------
    | Android 调用WEB接口
    |------------------------------------
    */
    initAndroidObject() {
      let $that = this;
      var webApp = window.webapp || {};
      // 加载数据
      webApp.onLoadData = function () {
        // 避免多次回调
        if (!$that.isDataReady) {
          $that.loadData();
        }
      };

      // 加载下一篇文章数据
      webApp.onLoadNextArticles = () => {
        $that.loadNextArticles();
      };

      // 滚动到评论位置
      webApp.scrollToComment = function () {
        var comment = document.querySelector("#comment");
        window.scrollTo(0, comment.offsetTop);
      };

      // 滚动到头部
      webApp.scrollToTop = () => {
        var title = document.querySelector("#title");
        window.scrollTo(0, title.offsetTop);
      };

      // 设置字体大小
      webApp.setFontSize = function () { };
      // 点赞成功
      webApp.onLikeFinish = function () {
        $that.likeLoading = false;
        $that.isLike = true;
      };

      webApp.onLikeError = () => {
        $that.likeLoading = false;
      };
    },
    loadData() {
      // 加载博客
      try {
        var article = Android.getArticle();
        if (article != null) {
          document.title = article.title;
          this.isLike = article.isLike;
          article.content = Article.parseHtml(article.content);
          this.model = article
          log('解析文章内容完成..')
        }
        this.loadNextArticles();
      } catch (e) {
        this.model.content = '加载文章错误:' + e
      }
      this.isDataReady = true // 第一次数据准备好了
    },
    // 加载下一篇文章
    loadNextArticles() {
      var articles = Android.getNextArticle();
      for (var index in articles) {
        var article = articles[index];
        if (article.date) {
          this.nextArticles.push(article);
        } else {
          this.recommendArticles.push(article);
        }
      }
    },
    // 进入主页
    handleAuthorHome() {
      Android.onAuthorHomeClick(this.model.author);
    },

    // 文章点击
    handleArticleClick(item) {
      Android.onArticleClick(item);
    },

    // 点赞
    handleLikeClick() {
      this.likeLoading = true;
      Android.handleLikeClick(true);
    }
  }
};
</script>

<style scoped>
.container {
  padding-bottom: 40px;
}
.article-loading {
  margin-top: 240px;
}
.loading-text {
  margin-top: 24px;
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
