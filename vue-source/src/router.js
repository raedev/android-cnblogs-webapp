import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Article',
      component: () => import('./views/Article.vue')
    },
    // {
    //   // 过渡页，让用户视觉过渡更加自然
    //   path: '/',
    //   name: 'Launcher',
    //   component: () => import('./views/Launcher.vue')
    // }

  ]
})
