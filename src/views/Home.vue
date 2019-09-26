<template>
  <div class="article">
    <!-- 标题 -->
    <h1 class="title">{{ title }}</h1>

    <!-- 作者 -->
    <van-row
      type="flex"
      class="author"
    >
      <van-col span="4">
        <van-image
          round
          width="2rem"
          height="2rem"
          src="https://piccdn.igetget.com/img/201807/19/201807191556587359037523.jpg@100w_100h"
        />
      </van-col>
      <van-col span="16">
        <p>罗振宇</p>
      </van-col>
      <van-col span="4">
        <span class="time">09-16</span>
      </van-col>
    </van-row>

    <!-- 内容 -->
    <div
      id="content"
      class="content"
      v-html="content"
    >
      <van-loading
        size="42px"
        vertical
      >
        正在加载文章
      </van-loading>

    </div>
  </div>
</template>

<script>

import Vue from 'vue';
import axios from 'axios';
import { Row, Col, Loading, Image } from 'vant';
import Article from '../article'
import Android from '../android'
Vue.use(Row).use(Col).use(Loading).use(Image);


export default {
  name: 'home',
  data() {
    return {
      title: '第809期 | 为什么会有大颠覆时代？',
      content: null,
    }
  },
  mounted() {
    // 加载博客内容
    var blog = Android.getBlog()
    this.loadData()
    if (blog) {
      try {
        // alert('加载博客')
        this.content = Article.parseHtml(this.blog.content)
      } catch (e) {
        this.content = '加载文章错误：' + e
      }
    }
  },
  methods: {
    loadData() {
      var blogId = '11532708' // 11532708
      const service = axios.create({
        timeout: 15000
      })
      service({
        url: 'http://192.168.2.189:8085/test/blog?id=' + blogId,
        method: 'GET'
      }).then(response => {
        this.content = Article.parseHtml(response.data.content)
      })
    }
  }
}
</script>

<style>
.van-loading {
  margin-top: 240px;
}
</style>
