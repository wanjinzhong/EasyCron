import { getRoles, deleteUserRole, addUserRole } from '@/api/role'

const role = {
  state: {
    allRoles: []
  },

  mutations: {
    SET_ALL_ROLES: (state, roles) => {
      state.allRoles = roles
    }
  },

  actions: {
    getRoles({ commit }) {
      return new Promise((resolve, reject) => {
        getRoles().then(response => {
          const data = response.data.data
          commit('SET_ALL_ROLES', data)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    deleteUserRole({ commit }, { userId, roleId }) {
      return new Promise((resolve, reject) => {
        deleteUserRole(userId, roleId).then(res => {
          resolve()
        })
      })
    },
    addUserRole({ commit }, { userId, roleId }) {
      return new Promise((resolve, reject) => {
        addUserRole(userId, roleId).then(res => {
          resolve()
        })
      })
    }
  }
}

export default role
