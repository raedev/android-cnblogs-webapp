<template>
  <div id="comment"
       class="comment">
    <div class="title">评论</div>
    <van-list v-model="loading"
              :finished="finished"
              :error="loadError"
              loading-text="加载评论中"
              finished-text="评论加载完毕"
              error-text="评论加载失败，请重试"
              v-if="comments.length>0"
              @load="onLoad">
      <van-cell v-for="(item, key) in comments"
                :key="key">
        <van-row>
          <van-col span="3">
            <van-image round
                       class="avatar"
                       width="2.4rem"
                       height="2.4rem"
                       @click="handleAvatarClick(item.author)"
                       :src="item.author.avatar">
              <template v-slot:loading>
                <van-loading type="spinner"
                             size="12" />
              </template>
            </van-image>
          </van-col>
          <van-col span="21"
                   class="anthor-box">
            <div class="name">
              {{item.author.name}}
              <span class="date">{{ item.date }}</span>
            </div>

            <div class="content"
                 @click="handleItemClick(item)">{{ item.content }}</div>

            <!-- 引用的回复 -->
            <div class="content quote"
                 v-if="item.quote">
              <div class="quote-author">回复：{{ item.quote.author.name }}</div>
              <div class="quote-content">{{ item.quote.content }}</div>
            </div>
          </van-col>
        </van-row>
      </van-cell>
    </van-list>

    <!-- 评论为空 -->
    <div class="comment-empty"
         v-else
         @click="handleItemClick(null)">
      <van-image height="140"
                 fit="contain"
                 :src="imageEmptyCommentPath" />
      <i class="empty-text">{{ loading ? '加载精彩评论中，请稍后..' : errorMsg }}</i>
    </div>

  </div>
</template>

<script>
import imageEmptyComment from '@/assets/images/empty_comment.png'
import Vue from 'vue';
import { Row, Cell, Col, List, Image, Icon } from 'vant';
import Android from '../android';
Vue.use(Row).use(Col).use(Cell).use(List).use(Image).use(Icon);

export default {
  name: 'ArticleComment',
  data() {
    return {
      pageIndex: 1, // 当前页
      loading: false,
      loadError: false,
      finished: false,
      lockLoaidng: false, // 锁定加载状态
      imageEmptyCommentPath: imageEmptyComment,
      errorMsg: '暂无评论，客官请座',
      comments: []
    }
  },
  computed: {
    // 计算属性的 getter
    isEmpty: function () {
      return !this.comments || this.comments.length <= 0
    }
  },
  created() {
    // 注册对象
    this.regAndroidObject()
    this.onLoad()
  },
  methods: {
    regAndroidObject() {
      var webApp = window.webapp || {}
      let $that = this


      // 加载评论失败
      webApp.onLoadCommentError = function (msg) {
        $that.loadError = true
        $that.lockLoaidng = false
        $that.loading = false
        $that.errorMsg = msg
      }

      // 加载评论成功
      webApp.onLoadComments = function () {
        $that.pageIndex++
        $that.loading = false
        var data = Android.getComments()
        if (!data || data.length <= 0) {
          $that.finished = true
          return
        }
        $that.lockLoaidng = false
        data.map((item) => {
          $that.comments.push(item)
        })
      }
    },
    // 加载数据
    onLoad() {
      if (this.lockLoaidng) return
      this.lockLoaidng = true
      this.loading = true
      Android.onLoadCommentClick(this.pageIndex)
    },

    // 评论点击
    handleItemClick(item) {
      Android.onCommentClick(item ? JSON.stringify(item) : null)
    },

    // 头像点击
    handleAvatarClick(author) {
      Android.onAuthorHomeClick(author)
      return false
    }
  }
}
</script>

<style lang="less" scoped>
.comment {
  padding-left: 1.2rem;
  padding-right: 1.2rem;
  padding-bottom: 2rem;
  scroll-behavior: smooth;
  .title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 18px;
    margin-top: 18px;
  }
  .van-cell {
    padding-left: 0px;
    padding-right: 0px;
    padding-bottom: 20px;
    padding-top: 20px;
    .anthor-box {
      padding-left: 8px;
      color: #8a9aa9;
      line-height: 1.2;
    }
    .name {
      color: #8a9aa9;
      font-size: 13px;
    }
    .date {
      color: #8a9aa9;
      font-size: 11px;
      margin-left: 6px;
      // margin-top: 4px;
    }
    .content {
      margin-top: 8px;
      font-size: 16px;
      line-height: 1.5 !important;
      color: #333;
    }
    .quote {
      border-left: 3px solid #efeff0;
      padding-left: 12px;
      color: #8a9aa9;
    }
  }

  .van-cell:not(:last-child)::after {
    left: 46px;
  }
  .comment-empty {
    text-align: center;
    color: #999999;
    font-size: 12px;
    padding-top: 1rem;
    padding-bottom: 1rem;

    .empty-text {
      display: block;
      margin-top: 12px;
    }
  }
}
</style>

