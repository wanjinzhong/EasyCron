import { getPlugins } from '@/api/plugin'
import defaultPluginPic from '@/assets/default_plugin.png'
import { Notification } from 'element-ui'

const plugin = {
  state: {
    plugins: []
  },

  mutations: {
    SET_PLUGINS: (state, plugins) => {
      state.plugins = plugins
    }
  },

  actions: {
    getPlugins({ commit }) {
      return new Promise((resolve, reject) => {
        getPlugins().then(response => {
          const data = response.data.data
          for (var i in data) {
            if (data[i].picture === null) {
              data[i].picture = defaultPluginPic
            } else {
              data[i].picture = process.env.BASE_API + '/resource/' + data[i].picture
            }
          }
          commit('SET_PLUGINS', data)
          resolve()
        }).catch(error => {
          Notification.error({
            title: '错误',
            message: error.message
          })
          reject(error)
        })
      })
    }
  }
}

export default plugin
