import { getSettings, saveSettings } from '@/api/setting'

const setting = {
  state: {},
  mutations: {},

  actions: {
    getSettings({ commit }) {
      return new Promise((resolve, reject) => {
        getSettings().then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    saveSettings({ commit }, data) {
      return new Promise((resolve, reject) => {
        saveSettings(data).then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default setting
