import Vue from 'vue'
import App from './App.vue'
import router from './router'

Vue.config.productionTip = false
// 注册Android回调
window.webapp = window.webapp || {}
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
