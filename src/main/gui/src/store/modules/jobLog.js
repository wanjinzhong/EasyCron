import { getJobLogs, getJobLogDetails } from '@/api/jobLog'

const jobLog = {
  state: {

  },

  mutations: {

  },

  actions: {
    getJobLogs({ commit }, param) {
      return new Promise((resolve, reject) => {
        getJobLogs(param).then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getJobLogDetails({ commit }, jobId) {
      return new Promise((resolve, reject) => {
        getJobLogDetails(jobId).then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default jobLog
