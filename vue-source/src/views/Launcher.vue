<template>
  <div class="container">

    <van-loading class="article-loading"
                 size="42px"
                 vertical
                 color="#1989fa">
      <div class="loading-text">文章加载中，请稍后...</div>
    </van-loading>

  </div>
</template>

<script>

import Vue from 'vue';
import { Loading } from 'vant';
Vue.use(Loading);


export default {
  name: 'Launcher',
  data() {
    return {
    }
  },
  created() {
    setTimeout(() => {
      // 自动跳转
      this.$router.push('/article')
    }, 1);
  }
}
</script>

<style  scoped>
.article-loading {
  margin-top: 240px;
}
.loading-text {
  padding-top: 24px;
}
</style>
