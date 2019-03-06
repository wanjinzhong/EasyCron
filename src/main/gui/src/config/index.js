export default {
  /**
   * @description 配置显示在浏览器标签的title
   */
  title: 'iView-admin',

  /**
   * @description api请求基础路径
   */
  baseUrl: {
    // dev: 'https://www.easy-mock.com/mock/5add9213ce4d0e69998a6f51/iview-admin/',
    dev: 'http://localhost:8089/easycron/public/api',
    pro: '/easycron/public/api'
  },
  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home',
  loginName: 'login',
  /**
   * @description 需要加载的插件
   */
  plugin: {
  }
}
