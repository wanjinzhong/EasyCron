import { getJobStatus } from '@/api/listbox'

const listbox = {
  state: {
    jobStatus: []
  },

  mutations: {
    SET_JOB_STATUS: (state, jobStatus) => {
      state.jobStatus = jobStatus
    }
  },

  actions: {
    initListBox({ dispatch, commit }) {
      dispatch('getJobStatus')
    },
    getJobStatus({ commit }) {
      return new Promise((resolve, reject) => {
        getJobStatus().then(response => {
          const data = response.data.data
          for (var i in data) {
            let iconName = ''
            switch (data[i].code) {
              case 'RUNNING': iconName = 'yes'; break
              case 'STOPPED': iconName = 'stop'; break
              case 'CONFIG_ERROR': iconName = 'warning'; break
              case 'WAITING_CONFIG': iconName = 'waiting'; break
            }
            data[i].icon = iconName
          }
          commit('SET_JOB_STATUS', data)
          resolve()
        }).catch(error => {
          this.$notify.error({
            title: '错误',
            message: 'error.data.message'
          })
          reject(error)
        })
      })
    }
  }
}

export default listbox
