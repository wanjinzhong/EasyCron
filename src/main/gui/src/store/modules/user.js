import { loginByEmail, logout, getUserInfo } from '@/api/login'
import { getUsersAsUserView, getUsersAsRoleView, disableUser, enableUser, addUser, updateUserName, getValCode } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    user: '',
    id: 0,
    status: '',
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    roleBos: [],
    usersAsUserView: [],
    usersAsRoleView: [],
    addUser: {}
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_ROLE_BOS: (state, roleBos) => {
      state.roleBos = roleBos
      const roles = []
      for (const i in roleBos) {
        roles.push(roleBos[i].code)
      }
      state.roles = roles
    },
    SET_USERS_AS_USER_VIEW: (state, users) => {
      state.usersAsUserView = users
    },
    SET_USERS_AS_ROLE_VIEW: (state, roles) => {
      state.usersAsRoleView = roles
    },
    SET_ADD_USER: (state, user) => {
      state.addUser = user
    }
  },

  actions: {
    // 用户名登录
    loginByEmail({ commit }, userInfo) {
      const email = userInfo.email.trim()
      return new Promise((resolve, reject) => {
        loginByEmail(email, userInfo.password).then(response => {
          const data = response.data.data
          commit('SET_TOKEN', data.token)
          setToken(response.data.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then(response => {
          if (!response.data) {
            reject('Verification failed, please login again.')
          }
          const data = response.data.data
          commit('SET_ID', data.id)
          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLE_BOS', data.roles)
          } else {
            reject('getInfo: roles must be a non-null array!')
          }

          commit('SET_NAME', data.name)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit, dispatch }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data
          commit('SET_ROLE_BOS', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          dispatch('GenerateRoutes', data) // 动态修改权限后 重绘侧边菜单
          resolve()
        })
      })
    },

    getUsersAsUserView({ commit }) {
      return new Promise(resolve => {
        getUsersAsUserView().then(res => {
          const data = res.data.data
          commit('SET_USERS_AS_USER_VIEW', data)
          resolve()
        })
      })
    },

    getUsersAsRoleView({ commit }) {
      return new Promise(resolve => {
        getUsersAsRoleView().then(res => {
          const data = res.data.data
          commit('SET_USERS_AS_ROLE_VIEW', data)
          resolve()
        })
      })
    },

    disableUser({ commit }, userId) {
      return new Promise(resolve => {
        disableUser(userId).then(res => {
          resolve()
        })
      })
    },

    enableUser({ commit }, userId) {
      return new Promise(resolve => {
        enableUser(userId).then(res => {
          resolve()
        })
      })
    },

    addUser({ commit }, user) {
      return new Promise(resolve => {
        addUser(user).then(res => {
          resolve(res)
        })
      })
    },

    updateUserName({ commit }, data) {
      return new Promise(resolve => {
        updateUserName(data).then(res => {
          resolve(res)
        })
      })
    },
    getValCode({ commit }, userId) {
      return new Promise(resolve => {
        getValCode(userId).then(res => {
          resolve(res)
        })
      })
    }
  }
}

export default user
