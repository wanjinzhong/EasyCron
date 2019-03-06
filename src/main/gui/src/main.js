// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import iView from 'iview'
import i18n from '@/locale'
import config from '@/config'
import installPlugin from '@/plugin'
import './index.less'
import '@/assets/icons/iconfont.css'
import TreeTable from 'tree-table-vue'
import VOrgTree from 'v-org-tree'
import 'v-org-tree/dist/v-org-tree.css'
import axios from 'axios'

Vue.use(iView, {
  i18n: (key, value) => i18n.t(key, value)
})
Vue.use(TreeTable)
Vue.use(VOrgTree)
/**
 * @description 注册admin内置插件
 */
installPlugin(Vue)
/**
 * @description 生产环境关掉提示
 */
Vue.config.productionTip = false
/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config
let apiUrl = ''
// 根据 process.env.HOST 的值判断当前是什么环境
// 命令：npm run build -- test ，process.env.HOST就设置为：'test'
let HOST = process.env.HOST
HOST = HOST === 'prod' ? '' : HOST + '-'
if (HOST === 'prod') {
  apiUrl = '/easycron/public/api'
} else {
  apiUrl = 'http://localhost:8089/easycron/public/api'
}
axios.defaults.baseURL = apiUrl
axios.defaults.withCredentials = true
axios.interceptors.response.use(function (response) {
  return response
}, function (error) {
  if (error.config.url.indexOf('api/login') === -1 &&
    error.response.data.code === 401) {
    window.localStorage.removeItem('USER')
    router.push('/login')
  } else if (error.response.data.code !== 500) {
    Vue.prototype.$Notice.error({
      title: 'Error',
      desc: error.response.data.message
    })
  }
  return Promise.reject(error)
})
Vue.prototype.axios = axios

// axios.defaults.baseURL = apiUrl
// axios.defaults.withCredentials = true
// Vue.prototype.axios = axios
// axios.interceptors.response.use(function (response) {
//   return response
// }, function (error) {
//   if (error.config.url.indexOf('login') !== -1 &&
//     error.response.data.status === 401) {
//     Vue.prototype.$Message.error(error.response.data.message)
//   } else if (error.response.data.status === 500) {
//     Vue.prototype.$Notice.error({
//       title: 'Error',
//       desc: error.response.data.message
//     })
//   } else {
//     var path = router.currentRoute.fullPath
//     path = path.replace('/login?origin=', '')
//     router.push('/login?origin=' + path)
//   }
//   return Promise.reject(error)
// })

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  store,
  render: h => h(App)
})
