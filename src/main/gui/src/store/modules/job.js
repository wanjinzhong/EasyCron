import { getJobs, getConfig, saveConfig, createNewJob, deleteJob, startJob, stopJob } from '@/api/job'
import defaultPluginPic from '@/assets/default_plugin.png'

const job = {
  state: {
    keyword: '',
    searchStatus: [],
    searchPlugins: [],
    jobs: [],
    page: 1,
    size: 10,
    totalCount: 0,
    totalPages: 0,
    configId: 0,
    configs: []
  },

  mutations: {
    SET_JOBES: (state, jobs) => {
      state.jobs = jobs
    },
    SET_PAGE: (state, page) => {
      state.page = page
    },
    SET_SIZE: (state, size) => {
      state.size = size
    },
    SET_TOTAL_COUNT: (state, totalCount) => {
      state.totalCount = totalCount
    },
    SET_TOTAL_PAGES: (state, totalPages) => {
      state.totalPages = totalPages
    },
    SET_KEY_WORD: (state, keyword) => {
      state.keyword = keyword
    },
    SET_SEARCH_STATUS: (state, searchStatus) => {
      state.searchStatus = searchStatus
    },
    SET_SEARCH_PLUGINS: (state, searchPlugins) => {
      state.searchPlugins = searchPlugins
    },
    SET_CONFIG_ID: (state, id) => {
      state.configId = id
    },
    SET_CONFIGS: (state, configs) => {
      state.configs = configs
    }
  },

  actions: {
    createNewJob({ commit }, data) {
      return new Promise((resolve, reject) => {
        createNewJob(data).then(response => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    getJobs({ commit, state }) {
      const data = {
        'keyword': state.keyword,
        'status': state.searchStatus,
        'plugins': state.searchPlugins,
        'page': state.page,
        'size': state.size
      }
      return new Promise((resolve, reject) => {
        getJobs(data).then(response => {
          const data = response.data.data
          for (var i in data.data) {
            if (data.data[i].pluginPicId === null) {
              data.data[i].pluginPicId = defaultPluginPic
            } else {
              data.data[i].pluginPicId = process.env.BASE_API + '/resource/' + data.data[i].pluginPicId
            }
          }
          commit('SET_JOBES', data.data)
          commit('SET_PAGE', data.page)
          commit('SET_SIZE', data.size)
          commit('SET_TOTAL_COUNT', data.totalCount)
          commit('SET_TOTAL_PAGES', data.totalPages)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    getConfig({ commit }, id) {
      return new Promise((resolve, reject) => {
        getConfig(id).then(response => {
          const data = response.data.data
          commit('SET_CONFIGS', data)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    saveConfig({ commit }, data) {
      return new Promise((resolve, reject) => {
        saveConfig(data.id, data.data).then(response => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    deleteJob({ commit }, id) {
      return new Promise((resolve, reject) => {
        deleteJob(id).then(response => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    startJob({ commit }, id) {
      return new Promise((resolve, reject) => {
        startJob(id).then(res => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    stopJob({ commit }, id) {
      return new Promise((resolve, reject) => {
        stopJob(id).then(res => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default job
